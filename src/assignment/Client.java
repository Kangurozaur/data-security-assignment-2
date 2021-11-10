package assignment;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {

    static double g = 5.0;
    static double publicKey = 23.0;
    static double privateKey = 4.0;
    static double symmetricKey;
    public static void main(String[] args) throws NotBoundException, MalformedURLException, RemoteException {
        PrinterService service = (PrinterService) Naming.lookup("rmi://localhost:5099/printer");

        double clientPartialKey = ((Math.pow(g, privateKey)) % publicKey);
        double serverPartialKey = service.getServerPartialKey(publicKey, g, clientPartialKey);
        symmetricKey = ((Math.pow(serverPartialKey, privateKey)) % publicKey);

        System.out.println(symmetricKey);

        // testing access rights of bob
        String accessToken = service.getAccessToken("bob", "pass");
        System.out.println(service.start("bob", accessToken));
        System.out.println(service.readConfig("ink", "bob", accessToken));
        System.out.println(service.setConfig("ink", "200", "bob", accessToken));
        System.out.println(service.status("HP LaserJet 9000", "bob", accessToken));
        System.out.println(service.restart("bob", accessToken));
        System.out.println(service.queue("HP LaserJet 9000", "bob", accessToken));
        System.out.println(service.topQueue("HP LaserJet 9000", 3, "bob", accessToken));
        System.out.println(service.print("helloWorld.pdf","HP LaserJet 9000", "bob", accessToken));
        System.out.println(service.stop("bob", accessToken));
    }
}
