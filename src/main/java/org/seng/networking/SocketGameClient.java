package org.seng.networking;

import java.io.*;
import java.net.Socket;

// this class is the actual game client that connects to the game server
// it sends/receives messages over a socket and talks to the server
public class SocketGameClient {
    private Socket socket;               // this is the connection to the server
    private BufferedReader in;          // this reads messages from server
    private BufferedWriter out;         // this sends messages to server

    // when we make a client, we pass in the IP/host and port to connect to
    public SocketGameClient(String host, int port) throws IOException {
        this.socket = new Socket(host, port); // this connects to the game server
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));   // input from server
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // output to server
    }

    // this is used to send a message to the server (like "READY" or a move)
    public void sendMessage(String message) throws IOException {
        out.write(message);     // this writes it
        out.newLine();          // this adds a newline so server can read it
        out.flush();            // this sends it right away
    }

    // this is used to receive messages sent by the server (or opponent)
    public String receiveMessage() throws IOException {
        return in.readLine(); // blocks until a message is received
    }

    // this is used to clean up and close everything when you're done
    public void close() throws IOException {
        socket.close();
        in.close();
        out.close();
    }

    // this is just for testing — lets you try running the client manually
    public static void main(String[] args) {
        try {
            // connect to the server running locally on port 12345
            SocketGameClient client = new SocketGameClient("10.13.180.57", 12345);

            // example sending/receiving
            client.sendMessage("Hello from client!");
            String response = client.receiveMessage();
            System.out.println("Server says: " + response);

            client.close(); // clean up when done
        } catch (IOException e) {
            e.printStackTrace(); // print what went wrong
        }
    }
}
