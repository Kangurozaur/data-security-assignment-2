package com.company;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PrinterServant extends UnicastRemoteObject implements PrinterService {

    public PrinterServant() throws  RemoteException {
        super();
    }

    @Override
    public String print(String filename, String printer) throws RemoteException {
        return "From server: " + filename + " on printer " + printer;
    }
}
