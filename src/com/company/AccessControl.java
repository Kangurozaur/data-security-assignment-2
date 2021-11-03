package com.company;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
                methods.add(decoded[i]);
            }
            _accessControlMap.put(decoded[0], methods);
        }
        reader.close();
    }

    public Map<String, Set<String>> getAccessControlMap() {
        return _accessControlMap;
    }
}