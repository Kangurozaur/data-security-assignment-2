package com.company;

import com.company.passwordstorage.PasswordStorage;

import java.nio.charset.Charset;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class PrinterServant extends UnicastRemoteObject implements PrinterService {

    double privateKey;
    double symmetricKey;
    Map<String, String> accessTokens;

    AccessControl ac;

    public PrinterServant(double privateKey) throws  RemoteException {
        super();
        this.privateKey = privateKey;
        this.accessTokens = new HashMap<String,String>();

        // Load Access Control List
        this.ac = new AccessControl();
        try {
            this.ac.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        String[] addedRightsUser2 = {"queue"};
//        ac.addRights("user2", addedRightsUser2)
//        ac.removeUser("alice");
//        String[] newAliceRights = {"start", "stop"};
//        ac.addUser("newAlice", newAliceRights);
    }

    @Override
    public String print(String filename, String printer, String username, String accessToken) throws RemoteException {
        var methodName = "print";
        if (!this.ac.verify(username, methodName)){
            return "From server: User: " + username + " unauthorized to perform " + methodName + " operation.";
        }
        if (this.accessTokens.containsKey(username) && this.accessTokens.get(username).equals(accessToken)) {
            return "From server: Print " + filename + " on printer " + printer;
        } else {
            return "From server: [Unauthorized] Print " + filename + " on printer " + printer + " by user " + username;
        }
    }

    @Override
    public String queue(String printer, String username, String accessToken) throws RemoteException {
        var methodName = "queue";
        if (!this.ac.verify(username, methodName)){
            return "From server: User: " + username + " unauthorized to perform " + methodName + " operation.";
        }
        if (this.accessTokens.containsKey(username) && this.accessTokens.get(username).equals(accessToken)) {
            return "From server: Queue for printer " + printer;
        } else {
            return "From server: [Unauthorized] Queue for printer " + printer + " by user " + username;
        }
    }

    @Override
    public String topQueue(String printer, int job, String username, String accessToken) throws RemoteException {
        var methodName = "topQueue";
        if (!this.ac.verify(username, methodName)){
            return "From server: User: " + username + " unauthorized to perform " + methodName + " operation.";
        }
        if (this.accessTokens.containsKey(username) && this.accessTokens.get(username).equals(accessToken)) {
            return "From server: Moved job " + job + " to the top of the queue for printer " + printer;
        } else {
            return "From server: [Unauthorized] Move job " + job + " to the top of the queue for printer " + printer + " by user " + username;
        }
    }

    @Override
    public String start(String username, String accessToken) throws RemoteException {
        var methodName = "start";
        if (!this.ac.verify(username, methodName)){
            return "From server: User: " + username + " unauthorized to perform " + methodName + " operation.";
        }
        if (this.accessTokens.containsKey(username) && this.accessTokens.get(username).equals(accessToken)) {
            return "From server: Starting";
        } else {
            return "From server: [Unauthorized] Starting" + " by user " + username;
        }
    }

    @Override
    public String stop(String username, String accessToken) throws RemoteException {
        var methodName = "stop";
        if (!this.ac.verify(username, methodName)){
            return "From server: User: " + username + " unauthorized to perform " + methodName + " operation.";
        }
        if (this.accessTokens.containsKey(username) && this.accessTokens.get(username).equals(accessToken)) {
            return "From server: Stopping";
        } else {
            return "From server: [Unauthorized] Stopping" + " by user " + username;
        }
    }

    @Override
    public String restart(String username, String accessToken) throws RemoteException {
        var methodName = "restart";
        if (!this.ac.verify(username, methodName)){
            return "From server: User: " + username + " unauthorized to perform " + methodName + " operation.";
        }
        if (this.accessTokens.containsKey(username) && this.accessTokens.get(username).equals(accessToken)) {
            return "From server: Restarting";
        } else {
            return "From server: [Unauthorized] Restarting" + " by user " + username;
        }
    }

    @Override
    public String status(String printer, String username, String accessToken) throws RemoteException {
        var methodName = "status";
        if (!this.ac.verify(username, methodName)){
            return "From server: User: " + username + " unauthorized to perform " + methodName + " operation.";
        }
        if (this.accessTokens.containsKey(username) && this.accessTokens.get(username).equals(accessToken)) {
            return "From server: Status of printer " + printer + " is OK";
        } else {
            return "From server: [Unauthorized] Status of printer " + printer + " by user " + username;
        }
    }

    @Override
    public String readConfig(String parameter, String username, String accessToken) throws RemoteException {
        var methodName = "readConfig";
        if (!this.ac.verify(username, methodName)){
            return "From server: User: " + username + " unauthorized to perform " + methodName + " operation.";
        }
        if (this.accessTokens.containsKey(username) && this.accessTokens.get(username).equals(accessToken)) {
            return "From server: Reading config for parameter: " + parameter;
        } else {
            return "From server: [Unauthorized] Reading config for parameter: " + parameter + " by user " + username;
        }
    }

    @Override
    public String setConfig(String parameter, String value, String username, String accessToken) throws RemoteException {
        var methodName = "setConfig";
        if (!this.ac.verify(username, methodName)){
            return "From server: User: " + username + " unauthorized to perform " + methodName + " operation.";
        }
        if (this.accessTokens.containsKey(username) && this.accessTokens.get(username).equals(accessToken)) {
            return "From server: Setting config for parameter " + parameter + " to value " + value;
        } else {
            return "From server: [Unauthorized] Setting config for parameter " + parameter + " to value " + value + " by user " + username;
        }

    }

    @Override
    public double getServerPartialKey(double p, double g, double clientPartialKey) throws RemoteException {
        double serverPartialKey = ((Math.pow(g, privateKey)) % p);
        this.symmetricKey = ((Math.pow(clientPartialKey, privateKey)) % p);
        return serverPartialKey;
    }

    @Override
    public String getAccessToken(String username, String password) throws RemoteException {
        try {
            PasswordStorage ps = new PasswordStorage();
            ps.load();
            if (ps.verify(username, password)) {
                byte[] array = new byte[7]; // length is bounded by 7
                new Random().nextBytes(array);
                String generatedToken = new String(array, Charset.forName("UTF-8"));
                this.accessTokens.put(username, generatedToken);
                return generatedToken;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
