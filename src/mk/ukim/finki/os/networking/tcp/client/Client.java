package mk.ukim.finki.os.networking.tcp.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

// Definira eden korisnik
public class Client extends Thread {

    private static int id = 0;

    @Override
    public void run() {
        Socket socket;
        int clientId = Client.id++;

        try {
            socket = new Socket(InetAddress.getByName("localhost"), 8000);

            // Od ovde kje gi chitame komandite na serverot
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Preku ovoj objekt kje go pratime baranjeto do serverot
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Go prakjame nasheto baranje do serverot
            bw.write((clientId % 2 == 0 ? "GET" : "POST") + " /Movies/" + clientId + "\n");
            bw.write("USER: Sasho Najdov\n");
            bw.write("\n"); // so eden prazen red kje mu kazheme na serverot deka zavrshime so baranjeto

            // Mora da povikame flush, bidekji koristime BufferedReader,
            // i site write-ovi do sega bea baferirani
            bw.flush();

            String line = null;

            // Ovde kje blokirame se dodeka serverot nema nishto za chitanje
            // Shtom serverot ni prati cela linija, kje ja isprintame ovde
            while (!(line = br.readLine()).equals(""))
                System.out.println("Client[" + clientId + "] <<< " + line);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
