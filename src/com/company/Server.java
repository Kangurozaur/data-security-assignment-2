package com.company;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    static double privateKey = 3.0;
    public static void main(String[] args) throws RemoteException {
        try {
            AccessControl ac = new AccessControl();

            ac.addUser("David", "password", new String[]{"print", "queue"});
            ac.addUser("Erica", "password", new String[]{"print", "queue"});
            ac.addUser("Fred", "password", new String[]{"print", "queue"});
            ac.addUser("George", "password", new String[]{"print", "queue"});
            ac.addUser("Bob", "password", new String[]{"start", "stop", "restart", "status", "readConfig", "setConfig"});
            ac.addUser("Cecilia", "password", new String[]{"print", "restart", "queue", "topQueue"});

            // test case: Bob leaves company and George took over Bob's rights
//            ac.removeUser("Bob");
//            ac.addRights("George", new String[]{"start", "stop", "restart", "status", "readConfig", "setConfig"});

            // test case: hire Henry and Ida
//            ac.addUser("Henry", "password", new String[]{"print", "queue"});
//            ac.addUser("Ida", "password", new String[]{"print", "restart", "queue", "topQueue"});

            Registry registry = LocateRegistry.createRegistry(5099);
            registry.rebind("printer", new PrinterServant(privateKey, ac));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
