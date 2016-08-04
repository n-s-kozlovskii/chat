package logic;

import java.io.*;
import java.net.Socket;

/**
 * Created by nek on 04.08.16.
 */
public class Client{
    private String serverName;
    private int port;

    public Client(String serverName, int port) {
        this.serverName = serverName;
        this.port = port;
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
