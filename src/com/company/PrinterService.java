package com.company;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PrinterService extends Remote {
    public String print( String filename, String printer, String username, String accessToken) throws RemoteException;
    public String queue( String printer, String username, String accessToken) throws RemoteException;
    public String topQueue( String printer, int job, String username, String accessToken) throws RemoteException;
    public String start(String username, String accessToken) throws RemoteException;
    public String stop(String username, String accessToken) throws RemoteException;
    public String restart(String username, String accessToken) throws RemoteException;
    public String status( String printer, String username, String accessToken) throws RemoteException;
    public String readConfig( String parameter, String username, String accessToken) throws RemoteException;
    public String setConfig( String parameter, String value, String username, String accessToken) throws RemoteException;
    public double getServerPartialKey(double p, double g, double clientPartialKey) throws RemoteException;
    public String getAccessToken(String username, String password) throws RemoteException;
}
