package com.company;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {

    public static void main(String[] args) throws NotBoundException, MalformedURLException, RemoteException {
        PrinterService service = (PrinterService) Naming.lookup("rmi://localhost:5099/printer");
        
        System.out.println(service.start());
        System.out.println(service.readConfig("ink"));
        System.out.println(service.setConfig("ink", "200"));
        System.out.println(service.status("HP LaserJet 9000"));
        System.out.println(service.restart());
        System.out.println(service.queue("HP LaserJet 9000"));
        System.out.println(service.topQueue("HP LaserJet 9000", 3));
        System.out.println(service.print("helloWorld.pdf","HP LaserJet 9000"));
        System.out.println(service.stop());
        
    }
}
