package com.company;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class PrinterClient {

    public static void main(String[] args) throws NotBoundException, MalformedURLException, RemoteException {
        PrinterService service = (PrinterService) Naming.lookup("rmi://localhost:5099/printer");
        System.out.println("~~~~~~~~" + service.print("filename", "some printer"));
    }
}
