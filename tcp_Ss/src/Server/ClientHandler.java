package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class ClientHandler implements Runnable {

    private static final String NAME = "name";
    private static final String INCERCARI = "incercari";

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    int nrGenerat;
    private List<String> clienti = new ArrayList<>();
    private Map<String, Object> castigatori;
    private List<Character> centrate;
    private List<Character> necentrate;

    public ClientHandler(Socket socket, int random, Map<String, Object> castigatori) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream());
        nrGenerat = random;
        this.castigatori = castigatori;
    }

    @Override
    public void run() {
        try {
            String name = null;
            if (!socket.isClosed()) {
                name = reader.readLine();
                System.out.println(name);
                clienti.add(name);
                writer.println("User adaugat");
                writer.flush();
            }
            int nrIncercari = 0;
            while (!socket.isClosed()) {
                try {
                    String numar = reader.readLine();
                    this.centrate = new ArrayList<>();
                    this.necentrate = new ArrayList<>();
                    verificare(nrGenerat, numar, centrate, necentrate);
                    nrIncercari++;
                    if (castigatori.size() == 2) {
                        writer.println("Castigator este clientul = " + castigatori.get(NAME) + " cu nr de incercari = " + castigatori.get(INCERCARI));
                        writer.flush();
                        break;
                    } else if (centrate.size() != 4) {
                        writer.println("centrate = " + centrate.size() + " necentrate = " + necentrate.size());
                        writer.flush();
                    } else {
                        castigatori.put(NAME, name);
                        castigatori.put(INCERCARI, nrIncercari);
                        writer.println("Castigator este clientul = " + name + " cu nr de incercari = " + nrIncercari);
                        writer.flush();
                        break;
                    }
                } catch (Exception e) {
                    writer.println(e.getMessage());
                    writer.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void verificare(int targetNumber, String userGuess, List<Character> centrate, List<Character> necentrate) {
        String a = Integer.toString(targetNumber);
        for (int i = 0; i < 4; i++) {
            if (a.charAt(i) == userGuess.charAt(i))
                centrate.add(a.charAt(i));
            else
                necentrate.add(a.charAt(i));
        }
    }
}
