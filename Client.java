import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Scanner scanner;

    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            scanner = new Scanner(System.in);
            System.out.println("Connected to the server");

            // Start a separate thread to handle incoming messages from the server
            new Thread(this::receiveMessages).start();

            // Use the main thread to send messages to the server
            sendMessages();
        } 
        catch (IOException e) {
            e.printStackTrace();
        } 
        finally {
            stopConnection();
        }
    }

    private void receiveMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Server: " + message);
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessages() {
        try {
            while (true) {
                System.out.print("Client: ");
                String message = scanner.nextLine();
                out.println(message);
            }
        } 
        finally {
            scanner.close();
        }
    }

    public void stopConnection() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
            System.out.println("Connection closed");
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.startConnection("localhost", 6666);
    }
}