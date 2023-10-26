import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientSimple {

	public static void main(String[] args) throws IOException {

		final String ADRESSE_IP_SERVEUR = "127.0.0.1";
		final int PORT_ECOUTE_SERVEUR = 1234;

		Scanner clavier = new Scanner(System.in);

		Socket socket = null;
		BufferedInputStream bis;
		BufferedReader br = null;
		PrintWriter pw = null;
		String message;
		String reponseRecu;

		try {
			//Connexion au serveur :
			socket = new Socket(ADRESSE_IP_SERVEUR,PORT_ECOUTE_SERVEUR);

			System.out.println("Connexion établie avec le serveur...");

			//Création des flux d'entrée/sortie pour la communication avec le serveur :
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(socket.getOutputStream());

			//Envoi des messages au serveur :
			System.out.print("Saisissez votre message (FIN pour terminer) : ");
			message = clavier.nextLine();
			pw.println(message);
			pw.flush();
			while (!"FIN".equals(message)) {
				//Attente et lecture de la réponse du serveur :
				reponseRecu = br.readLine();
				System.out.println("Réponse du serveur : "+reponseRecu);

				//Envoi du message suivant :
				System.out.print("Saisissez votre message (FIN pour terminer) : ");
				message = clavier.nextLine();
				pw.println(message);
				pw.flush();
			}
		}  finally {
			//Fermeture des flux et des sockets :
			if (br!=null)
				br.close();
			if (pw!=null)
				pw.close();
			if (socket!=null)
				socket.close();
		}
		System.out.println("Bye Bye!");
	}
}