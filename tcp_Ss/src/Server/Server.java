package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements AutoCloseable{

    private ServerSocket serverSocket;
    private volatile Map<String, Object> castigatori;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        int random = new Random().nextInt(9000) + 1000;
        castigatori = new HashMap<>();
        System.out.println("Serverul a generat nr: " + random);
        ExecutorService executorService = Executors.newFixedThreadPool(10 * Runtime.getRuntime().availableProcessors());
        executorService.execute(() -> {
            while (!serverSocket.isClosed()) {
                try {
                    executorService.submit(new ClientHandler(serverSocket.accept(), random, castigatori));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void close() throws Exception {
        serverSocket.close();
    }

}

