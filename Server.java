import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Scanner scanner;

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server is listening on port " + port);

            waitForConnection();
            setupStreams();

            // Start a separate thread to handle incoming messages from the client
            new Thread(this::receiveMessages).start();

            // Use the main thread to send messages to the client
            sendMessages();
        } 
        catch (IOException e) {
            e.printStackTrace();
        } 
        finally {
            stop();
        }
    }

    private void waitForConnection() throws IOException {
        System.out.println("Waiting for a client to connect...");
        clientSocket = serverSocket.accept();
        System.out.println("Client connected: " + clientSocket.getInetAddress());
    }

    private void setupStreams() throws IOException {
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        scanner = new Scanner(System.in);
    }

    private void receiveMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Client: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessages() {
        try {
            while (true) {
                System.out.print("Server: ");
                String message = scanner.nextLine();
                out.println(message);
            }
        } 
        finally {
            scanner.close();
        }
    }

    public void stop() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
            if (serverSocket != null) serverSocket.close();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start(6666);
    }
}
