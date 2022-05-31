package Client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ProgramC {

    public static void main(String[] args) {
        int port = Integer.parseInt(ResourceBundle.getBundle("settings").getString("port"));
        String hostname = ResourceBundle.getBundle("settings").getString("host");
       try (Socket socket = new Socket(hostname, port)) {
            System.out.println("Connected to server");
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            System.out.println("Introduceti numele de client: ");
            try(Scanner scanner = new Scanner(System.in)) {
                String name = scanner.nextLine();
                writer.println(name);
                writer.flush();

                while(true) {
                    String response = reader.readLine();
                    System.out.println(response);
                    if (response.contains("Castigator")) {
                        System.out.println("Ai castigat!");
                        System.exit(0);
                    } else {
                        System.out.println("Introdu un numar: ");
                        String command = scanner.nextLine();
                        writer.println(command);
                        writer.flush();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            System.exit(0);
        }
    }

}
