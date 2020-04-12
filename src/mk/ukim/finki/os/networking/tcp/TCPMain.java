package mk.ukim.finki.os.networking.tcp;

import mk.ukim.finki.os.networking.tcp.client.Client;
import mk.ukim.finki.os.networking.tcp.server.Server;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;


// Ovaa programa simulira poednostavena HTTP client-server komunikacija
// Imame eden Server koj shto kreva Worker-i za da gi chitaat baranjata i da prakjaat odgovori
// Generirame 10 klienti koi shto prakjaat sample baranje
// Na kraj, kreirame edno nashe baranje preku tastatura
// Pomegju sekoj klient i worker imame full-duplex stream komunikacija
// vo sekoj moment klientot ili worker-ot mozhe da pratat shto sakaat
public class TCPMain {

    public static void main(String[] args) throws InterruptedException, IOException {
        Server server = new Server();
        server.start();

        // Kje spieme 1 sekunda, za da se osigurame
        // deka serverot ima vreme da se inicijalizira
        Thread.sleep(1000);

        // Kreirame 10 korisnici koi kje pratat po edno baranje
        for (int i = 0; i < 10; ++i) {
            Client client = new Client();
            client.start();
        }

        // Kje pochekame da zavrshat site klienti
        Thread.sleep(1000);

        System.out.println("\nGET/POST a new movie from/to the server:");
        System.out.println("========================================\n");

        // Kje kreirame i edno nashe baranje
        // da vidime dali serverot kje ni vrati

        Scanner key = new Scanner(System.in);
        String verb, resource, user;

        System.out.print("Enter action: ");
        verb = key.nextLine();

        System.out.print("Enter movie: ");
        resource = key.nextLine();

        System.out.print("What is your name: ");
        user = key.nextLine();

        // Kreirame socket za komunikacija so serverot
        Socket socket = new Socket(InetAddress.getLocalHost(), 8000);

        // Gi zemame dvata stream-a
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        // Go prakjame nasheto baranje do serverot
        bw.write(verb + " " + resource + "\n");
        bw.write("USER: " + user + "\n");
        bw.write("\n");
        bw.flush();

        // System.err kasneshe so printanje, pa ispagjashe deka odgovorot se dobiva pred baranjeto
        // Ova go reshava toa
        Thread.sleep(100);

        String line = null;

        // Kje chekame da ni vrati serverot
        // I go printame baranjeto
        while (!(line = br.readLine()).equals(""))
            System.out.println("ME: <<< " + line);

        socket.close();

        System.out.println("\n========================================\n");

        System.out.println("We are done! :)");
    }
}
