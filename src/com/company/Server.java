package com.company;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    static double privateKey = 3.0;
    public static void main(String[] args) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(5099);
        registry.rebind("printer", new PrinterServant(privateKey));
    }
}
