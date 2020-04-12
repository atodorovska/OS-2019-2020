package mk.ukim.finki.os.networking.tcp.server;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// Pretstavuva obrabotuvach na edno baranje
// Za sekoe korisnichko baranje kje imame po eden ovakov Worker
// Go chita baranjeto, i vrakja do korisnikot shto pratil
public class Worker extends Thread {

    private static int id = 0;

    // Socket-ot preku koj kje primame i prakjame komandi do korisnikot
    private Socket client;

    private int workerId;

    // Socketot kje ni go dade Server-ot koga kje go kreira Worker-ot
    public Worker(Socket socket) {
        this.client = socket;
        this.workerId = Worker.id++;
    }

    @Override
    public void run() {

        try {

            // Od ovde kje gi chitame komandite na korisnikot
            BufferedReader br = new BufferedReader(new InputStreamReader(this.client.getInputStream()));

            // Koga sakame neshto da pratime do korisnikot,
            // kje go koristime ovoj objekt
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(this.client.getOutputStream()));


            // Znaeme deka sekogash prvata linija e: VERB URI
            Request request = new Request(br.readLine().split("\\s+"));
            System.err.println("Worker[" + this.workerId + "] <<< " + request.verb + " " + request.uri);

            // Ostanatite linii se od tipot:  NAME: VALUE
            String line = null;

            // Chitame se dodeka ne zavrshi baranjeto
            // zavrshuva so eden prazen red
            while (!(line = br.readLine()).equals("")) {
                System.err.println("Worker[" + this.workerId + "] <<< " + line);
                String[] parts = line.split(":\\s+", 2);
                request.headers.put(parts[0], parts[1]); // parts[0] = NAME, parts[1] = VALUE
            }

            if (request.verb.equals("POST") && request.headers.get("Content-Length") != null) {
                StringBuilder sb = new StringBuilder();
                // Ovoj header kje ni kazhuva kolku bajti ima vo teloto
                int length = Integer.parseInt(request.headers.get("Content-Length").trim());
                while (length-- > 0)
                    sb.append((char)br.read());

                request.body = sb.toString();
                System.out.println("BODY: " + request.body);
            }


            // Vo ovoj moment, go prochitavme celoto baranje od korisnikot
            // i istoto go imame vo `request` promenlivata

            String clientName = Optional
                    .ofNullable(request.headers.get("USER"))
                    // Ova e standarden Header vo HTTP za tipot na klient shto pobaruva
                    // Voobichaeno toa e vashiot prebaruvach (Firefox, Chrome, ...)
                    .orElse(request.headers.get("User-Agent"));

            // Vrakjame na korisnikot
            bw.write("HTTP/1.1 200 OK\n\n"); // za Chrome da ne se buni
            bw.write("Hello, " + clientName + "!\n");
            bw.write("You requested to " + request.verb + " the resource: " + request.uri + "\n");
            if (request.verb.equals("POST") && request.body != null)
                bw.write("You sent me: " + request.body + "\n");
            bw.write("\n"); // So eden prazen red kje mu kazheme na korisnikot deka zavrshivme so odgovorot
            bw.flush(); // Mora da napravime flush, za da go ispratime baferiraniot tekst

            this.client.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Ednostaven primer na HTTP baranje
    public static class Request {
        public String verb; // Definira akcija: dali zemam ili prikachuvame neshto na server
        public String uri; // Go identifikva resursot shto sakame da go zememe/prikachime
        public String version; // Verzija na protokolot
        public Map<String, String> headers; // Niza na header-i, kako dopolnitelni informacii
        public String body; // Teloto na baranjeto, dokolku saka neshto da prati korisnikot

        public Request(String[] line) {
            this.verb = line[0];
            this.uri = String.join(" ", Arrays.copyOfRange(line,1, line.length - 1));
            this.version = line[line.length - 1];
            this.headers = new HashMap<>();
        }
        // Baranjeto e vo sledniot format:
        // VERB URI
        // HeaderName1: HeaderValue1
        // HeaderName2: HeaderValue2
        // ...
        //
    }
}
