package com.company;

import com.company.passwordstorage.FileCorruptedException;
import com.company.passwordstorage.PasswordStorage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {

    static double g = 5.0;
    static double publicKey = 23.0;
    static double privateKey = 4.0;
    static double symmetricKey;
    public static void main(String[] args) throws NotBoundException, MalformedURLException, RemoteException {
        PrinterService service = (PrinterService) Naming.lookup("rmi://localhost:5099/printer");

        double clientPartialKey = ((Math.pow(g, privateKey)) % publicKey);
        double serverPartialKey = service.getServerPartialKey(publicKey, g, clientPartialKey);
        symmetricKey = ((Math.pow(serverPartialKey, privateKey)) % publicKey);

        System.out.println(symmetricKey);

        // Load Passwords
        try {
            PasswordStorage ps = new PasswordStorage();
            ps.load();
            ps.add("user1", "pass1");
            ps.add("user2", "pass2");
            ps.add("user3", "pass3");
            System.out.println(ps.verify("user1", "pass1"));
            ps.store();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Load Access Control List
        AccessControl ac = new AccessControl();
        try {
            ac.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        var accessControlMap = ac.getAccessControlMap();

        System.out.println(accessControlMap.toString());

        String accessToken = service.getAccessToken("user2", "pass2");
        System.out.println(service.start("user2", accessToken));
        System.out.println(service.start("user2", "foobar"));
        System.out.println(service.readConfig("ink", "user2", accessToken));
        System.out.println(service.readConfig("ink", "user2", "foobar"));
        System.out.println(service.setConfig("ink", "200", "user2", accessToken));
        System.out.println(service.setConfig("ink", "200", "user2", "foobar"));
        System.out.println(service.status("HP LaserJet 9000", "user2", accessToken));
        System.out.println(service.status("HP LaserJet 9000", "user2", "foobar"));
        System.out.println(service.restart("user2", accessToken));
        System.out.println(service.restart("user2", "foobar"));
        System.out.println(service.queue("HP LaserJet 9000", "user2", accessToken));
        System.out.println(service.queue("HP LaserJet 9000", "user2", "foobar"));
        System.out.println(service.topQueue("HP LaserJet 9000", 3, "user2", accessToken));
        System.out.println(service.topQueue("HP LaserJet 9000", 3, "user2", "foobar"));
        System.out.println(service.print("helloWorld.pdf","HP LaserJet 9000", "user2", accessToken));
        System.out.println(service.print("helloWorld.pdf","HP LaserJet 9000", "user2", "foobar"));
        System.out.println(service.stop("user2", accessToken));
        System.out.println(service.stop("user2", "foobar"));


    }
}
