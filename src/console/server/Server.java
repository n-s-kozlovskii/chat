package console.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Created by nek on 06.08.16.
 */
public class Server {
    private ServerSocket serverSocket;
    private List<Connection> connections;

    public Server(){

        connections = Collections.synchronizedList(new LinkedList<>());

        try {
            serverSocket = new ServerSocket(8000);
            while (true){
                Socket socket = serverSocket.accept();
                if (!socket.isClosed()) {
                    Connection con = new Connection(socket);
                    connections.add(con);
                    con.start();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
    }

    private void closeAll() {
        try {
            serverSocket.close();
            synchronized(connections) {
                Iterator<Connection> iter = connections.iterator();
                while(iter.hasNext()) {
                    iter.next().close();
                }
            }

        } catch (IOException e) {
            System.out.print("1");
            e.printStackTrace();
        }
    }

    private class Connection extends  Thread{
        private BufferedReader in;
        private PrintWriter out;
        private Socket socket;

        private String name;

        public Connection(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

            } catch (IOException e) {
                System.out.print("4");
                e.printStackTrace();
            }
        }

        public boolean isClosed(){
            return socket.isClosed();
        }

        @Override
        public void run() {
            try {
                name = in.readLine();
                synchronized(connections) {
                    for (Connection c: connections) {
                        c.out.println(name + " cames now");
                    }
                }

                String text;
                while (true) {
                    text = in.readLine();
                    if (text.equals("exit")) {
                        break;
                    }
                    synchronized(connections) {
                        for (Connection c: connections) {
                            c.out.println(name + ": " + text);
                        }
                    }
                }

                synchronized(connections) {
                    for (Connection c: connections){
                        c.out.println(name + " has left");
                    }

                }
            } catch (IOException e) {
                System.out.print("2");
                e.printStackTrace();
            } finally {
                close();
            }
        }

        private void close() {
            try {
                in.close();
                out.close();
                socket.close();

                connections.remove(this);
                if (connections.size() == 0) {
                    System.out.print(" here we are ");
                    Server.this.closeAll();
                    System.exit(0);
                }
            } catch (IOException e) {
                System.err.println("потоки не закрыты");
                e.printStackTrace();
            }
        }
    }

}
