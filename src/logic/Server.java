package logic;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by nek on 04.08.16.
 */
public class Server implements  Runnable {
    private ServerSocket serverSocket;
    private String serverName;
    private int port;

    public Server(String serverName,int port) throws IOException {
        serverSocket = new ServerSocket(port);
        this.serverName = serverName;
        this.port = port;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket server = serverSocket.accept();
                OutputStream outputStream = server.getOutputStream();
                InputStream inputStream = server.getInputStream();

                server.close();
            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void sendData(String data)
    {
        try
        {
            Socket client = new Socket(serverName, port);
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF(data);
            InputStream inFromServer = client.getInputStream();
            DataInputStream in =
                    new DataInputStream(inFromServer);
            client.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}