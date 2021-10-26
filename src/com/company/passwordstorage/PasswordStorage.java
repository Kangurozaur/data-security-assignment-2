package com.company.passwordstorage;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class PasswordStorage {

    final static String FILE_NAME = Paths.get("passwords.csv").toAbsolutePath().toString();
    private Map<String, String> _passwordMap;

    public PasswordStorage () throws FileNotFoundException {
        _passwordMap = new HashMap<>();
    }

    public void load () throws IOException, FileCorruptedException {
        var reader = new BufferedReader(new FileReader(FILE_NAME));
        String line;
        while ((line = reader.readLine()) != null) {
            var decoded = line.split(",");
            if (decoded.length != 2) throw new FileCorruptedException();
            _passwordMap.put(decoded[0], decoded[1]);
        }
        reader.close();
    }

    public void store () throws IOException {
        var writer = new BufferedWriter(new FileWriter(FILE_NAME));
        for (var entry: _passwordMap.entrySet()) {
            var message = entry.getKey() + "," + entry.getValue();
            writer.write(message, 0, message.length());
            writer.newLine();
        }
        writer.close();
    }

    public boolean verify (String username, String password) throws Exception {
        String stored_password = _passwordMap.get(username);
        if (stored_password == null) {
            throw new Exception("Entry for user " + username + " not found");
        }
        return stored_password.equals(hashPassword(username, password));
    }

    public void add (String username, String password) throws NoSuchAlgorithmException {
        _passwordMap.put(username, hashPassword(username, password));
    }

    private String hashPassword (String username, String password) throws NoSuchAlgorithmException {
        var md = MessageDigest.getInstance("SHA-512");
        var withSalt = username + password;
        var digest = md.digest(withSalt.getBytes());
        return new BigInteger(1, digest).toString(16);
    }
}
