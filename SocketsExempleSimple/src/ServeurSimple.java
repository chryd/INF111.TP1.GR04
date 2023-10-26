import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurSimple {

    public static void main(String[] args) throws IOException {
        final int PORT_ECOUTE_SERVEUR = 1234;
        ServerSocket serverSocket=null;
        Socket socket=null;
        BufferedReader br = null;
        PrintWriter pw=null;
        String messageRecu, messageInverse;

        try {
            serverSocket = new ServerSocket(PORT_ECOUTE_SERVEUR);

            // Attente d'une connexion :
            System.out.println("SERVEUR : Attente de connexion...");
            socket = serverSocket.accept();
            System.out.println("SERVEUR : Connexion acceptee.");

            //Création des flux d'entrée/sortie pour la communication avec le client :
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(socket.getOutputStream());

            //Lecture des messages provenant du client :
            messageRecu = br.readLine();
            while (!"FIN".equals(messageRecu)) {
                System.out.println("SERVEUR: recue : " + messageRecu);

                //Création de la réponse au client (messageRecu inversé) :
                messageInverse = new StringBuilder(messageRecu).reverse().toString();

                //Envoi de la réponse au client :
                pw.println(messageInverse);
                pw.flush();

                //Attente et lecture du message suivant :
                messageRecu = br.readLine();
            }
        } finally {
            //Fermeture des flux et des sockets :
            if (br!=null)
                br.close();
            if (pw!=null)
                pw.close();
            if (socket!=null)
                socket.close();
            if (serverSocket!=null)
                serverSocket.close();
        }

        System.out.println("Bye Bye!");
    }
}