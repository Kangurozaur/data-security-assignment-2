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

        // test different user
        testServices("Bob", "password", service) ;
//        testServices("Cecilia", "password", service) ;
//        testServices("George", "password", service) ;
//        testServices("Ida", "password", service) ;
//        testServices("David", "password", service) ;
//        testServices("Fred", "password", service) ;
//        testServices("Erica", "password", service) ;
//        testServices("Henry", "password", service) ;
    }

    private static void testServices(String userName, String password, PrinterService service) throws RemoteException {
        String accessToken = service.getAccessToken(userName, password);
        System.out.println(service.start(userName, accessToken));
        System.out.println(service.readConfig("ink", userName, accessToken));
        System.out.println(service.setConfig("ink", "200", userName, accessToken));
        System.out.println(service.status("HP LaserJet 9000", userName, accessToken));
        System.out.println(service.restart(userName, accessToken));
        System.out.println(service.queue("HP LaserJet 9000", userName, accessToken));
        System.out.println(service.topQueue("HP LaserJet 9000", 3, userName, accessToken));
        System.out.println(service.print("helloWorld.pdf","HP LaserJet 9000", userName, accessToken));
        System.out.println(service.stop(userName, accessToken));
    }
}
