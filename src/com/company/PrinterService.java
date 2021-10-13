package com.company;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PrinterService extends Remote {

    public String print( String filename, String printer) throws RemoteException;

}
