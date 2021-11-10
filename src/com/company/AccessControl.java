package com.company;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import com.company.passwordstorage.FileCorruptedException;

public class AccessControl {
    final static String FILE_NAME = Paths.get("access_control_list.csv").toAbsolutePath().toString();
    private Map<String, Set<String>> _accessControlMap;

    public AccessControl () {
        this._accessControlMap = new HashMap<String, Set<String>>();
    }

    public void load() throws FileCorruptedException, IOException{
        var reader = new BufferedReader(new FileReader(FILE_NAME));
        String line;
        while ((line = reader.readLine()) != null) {
            var decoded = line.split(",");
            if (decoded.length < 2) throw new FileCorruptedException();
            var methods = new HashSet<String>();
            for (int i = 1; i < decoded.length; i++) {
                methods.add(decoded[i].trim());
            }
            _accessControlMap.put(decoded[0], methods);
        }
        reader.close();
    }

    public Map<String, Set<String>> getAccessControlMap() {
        return _accessControlMap;
    }

    public boolean verify(String username, String methodName) {
        try {
            return this._accessControlMap.get(username).contains(methodName);
        } catch (Exception e){
            return false;
        }
    }

    public void store () throws IOException {
        var writer = new BufferedWriter(new FileWriter(FILE_NAME));
        for (var entry: _accessControlMap.entrySet()) {
            var message = entry.getKey() ;
            for(String method: entry.getValue()) {
                message += ", " + method;
            }
            writer.write(message, 0, message.length());
            writer.newLine();
        }
        writer.close();
    }

    public void addRights(String userName, String[] methods) {
        if(!this._accessControlMap.containsKey(userName)) {
            System.out.println("User " + userName + " does not exist");
            return;
        }
        Set rights = new HashSet<String>(this._accessControlMap.get(userName));
        rights.addAll(Arrays.asList(methods));
        this._accessControlMap.replace(userName, rights);
        try {
            store();
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    public void removeRights(String userName, String[] methods) {
        if(!this._accessControlMap.containsKey(userName)) {
            System.out.println("User " + userName + " does not exist");
            return;
        }
        Set rights = new HashSet<String>(this._accessControlMap.get(userName));
        rights.removeAll(Arrays.asList(methods));
        this._accessControlMap.replace(userName, rights);
        try {
            store();
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    public void removeUser(String userName) {
        if(!this._accessControlMap.containsKey(userName)) {
            System.out.println("User " + userName + " does not exist");
            return;
        }
        this._accessControlMap.remove(userName);
        try {
            store();
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    public void addUser(String userName, String[] methods) {
        if(this._accessControlMap.containsKey(userName)) {
            System.out.println("User " + userName + " already exists");
            return;
        }
        Set rights = new HashSet<>(Arrays.asList(methods));
        this._accessControlMap.put(userName, rights);
        try {
            store();
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }

}