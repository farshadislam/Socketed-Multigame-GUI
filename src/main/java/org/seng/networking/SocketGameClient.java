package org.seng.networking;

import java.io.*;
import java.net.Socket;

// This is the client that connects to the game server
public class SocketGameClient {
    private Socket socket;
    private BufferedReader in; // reads from server
    private BufferedWriter out; // sends to server

    // When we make a client, we provide the host and port to connect to
    public SocketGameClient(String host, int port) throws IOException {
        this.socket = new Socket(host, port); // connect to the server
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    // This is usedto send messages to the server
    public void sendMessage(String message) throws IOException {
        out.write(message);
        out.newLine();
        out.flush();
    }

    // This is used to get messages sent back from the server (like from opponent)
    public String receiveMessage() throws IOException {
        return in.readLine();
    }

    // This is to properly close everything once done
    public void close() throws IOException {
        socket.close();
        in.close();
        out.close();
    }
    public static void main(String[] args) {
        try {
            SocketGameClient client = new SocketGameClient("localhost", 12345); // IP (host) just represents the ip address of the host, port should be the same number for both the client and the server so that both connect on the same thing
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
