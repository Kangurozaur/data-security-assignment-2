package assignment;

import assignment.passwordstorage.PasswordStorage;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Collections;

public class Server {
    static double privateKey = 3.0;
    public static void main(String[] args) throws RemoteException {

        try {
            PasswordStorage ps = new PasswordStorage();
            ps.load();
            ps.addUser("alice", "pass", Collections.singletonList("manager"));
            ps.addUser("bob", "pass", Collections.singletonList("technician"));
            ps.addUser("cecilia", "pass", Collections.singletonList("poweruser"));
            ps.addUser("david", "pass", Collections.singletonList("user"));
            ps.addUser("erica", "pass", Collections.singletonList("user"));
            ps.addUser("fred", "pass", Collections.singletonList("user"));
            ps.addUser("george", "pass", Collections.singletonList("user"));
            // testing the changing actions
//            ps.removeUser("bob");
//            ps.addRole("george", "technician");
//            ps.addUser("henry", "pass", Collections.singletonList("user"));
//            ps.addUser("ida", "pass", Collections.singletonList("poweruser"));
//            ps.addRole("george", "manager");
//            System.out.println("assert Ida = Cecilia: " + ps.getUserByUsername("ida").getRoles().containsAll(ps.getUserByUsername("cecilia").getRoles()));
            ps.store();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Registry registry = LocateRegistry.createRegistry(5099);
        registry.rebind("printer", new PrinterServant(privateKey));
    }
}
