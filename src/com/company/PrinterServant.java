package com.company;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PrinterServant extends UnicastRemoteObject implements PrinterService {


    public PrinterServant() throws  RemoteException {
        super();
    }

    @Override
    public String print(String filename, String printer) throws RemoteException {
        return "From server: Print " + filename + " on printer " + printer;
    }

    @Override
    public String queue(String printer) throws RemoteException {
        return "From server: Queue for printer " + printer;
    }

    @Override
    public String topQueue(String printer, int job) throws RemoteException {
        return "From server: Moved job " + job + " to the top of the queue for printer " + printer;
    }

    @Override
    public String start() throws RemoteException {
        return "From server: Starting";
    }

    @Override
    public String stop() throws RemoteException {
        return "From server: Stopping";
    }

    @Override
    public String restart() throws RemoteException {
        return "From server: Restarting";
    }

    @Override
    public String status(String printer) throws RemoteException {
        return "From server: Status of printer " + printer + " is OK";
    }

    @Override
    public String readConfig(String parameter) throws RemoteException {
        return "From server: Reading config for parameter: " + parameter;
    }

    @Override
    public String setConfig(String parameter, String value) throws RemoteException {
        return "From server: Setting config for parameter " + parameter + " to value " + value;
    }

}
