package org.seng.networking;

import java.io.*;
import java.net.Socket;

// this class acts as the game client that connects to the server using sockets
// it sends messages to, and receives messages from, the server
public class SocketGameClient {

    // this is our active connection to the server
    private Socket socket;
    // this reader lets us capture messages coming from the server
    private BufferedReader in;
    // this writer lets us send out messages to the server
    private BufferedWriter out;

    public SocketGameClient(String host, int port) throws IOException {
        // this connects to the game server using the given host and port
        this.socket = new Socket(host, port);
        // this sets up an input stream to read messages from the server
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // this sets up an output stream to send messages to the server
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    // this method sends a message to the server
    public void sendMessage(String message) throws IOException {
        out.write(message);   // this writess the message to our output stream
        out.newLine();        // this adds a newline so the server can tell where the message ends
        out.flush();          // this flushes the stream immediately so the message is sent right away
    }

    // this method receives a message from the server
    // it blocks and waits until a full message is read
    public String receiveMessage() throws IOException {
        return in.readLine(); // this reads and returns the next line from the input stream
    }

    // this method is used to close the client connection and its streams when we're done
    public void close() throws IOException {
        socket.close();  // this closes the socket connection to the server
        in.close();      // this closes the input stream
        out.close();     // this closes the output stream
    }

    // this main method is just for quick testing
    public static void main(String[] args) {
        try {
            // attempt to connect to the server running on localhost at port 12345
            SocketGameClient client = new SocketGameClient("localhost", 12345);

            // this sends a hello message to the server as a test
            client.sendMessage("Hello from client!");
            // this waits for a response from the server and captures it
            String response = client.receiveMessage();
            // this prints out the server's response to our console
            System.out.println("Server says: " + response);

            // this cleans up by closing the connection once we're done messaging
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
