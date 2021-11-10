package assignment;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PrinterService extends Remote {
    String print(String filename, String printer, String username, String accessToken) throws RemoteException;
    String queue(String printer, String username, String accessToken) throws RemoteException;
    String topQueue(String printer, int job, String username, String accessToken) throws RemoteException;
    String start(String username, String accessToken) throws RemoteException;
    String stop(String username, String accessToken) throws RemoteException;
    String restart(String username, String accessToken) throws RemoteException;
    String status(String printer, String username, String accessToken) throws RemoteException;
    String readConfig(String parameter, String username, String accessToken) throws RemoteException;
    String setConfig(String parameter, String value, String username, String accessToken) throws RemoteException;
    double getServerPartialKey(double p, double g, double clientPartialKey) throws RemoteException;
    String getAccessToken(String username, String password) throws RemoteException;
}
