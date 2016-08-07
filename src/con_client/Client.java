package con_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Created by nek on 06.08.16.
 */
public class Client {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Client(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите айпи сервера:");

        String ip = scanner.nextLine();
        try {
            socket = new Socket(ip, 8000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Введите свой ник: ");
            out.println(scanner.nextLine());

            Resender resend = new Resender();
            resend.start();


            String text = "";
            while (!text.equals("exit")){
                text = scanner.nextLine();
                out.println(text);
            }

            resend.setStopped();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }

    }

    private void close (){
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private class Resender extends Thread{
        private boolean stopped = false;

        public void setStopped(){
            stopped = true;
        }

        @Override
        public void run() {
           while (!stopped){
               try {
                   System.out.println(in.readLine());
               } catch (IOException e) {
                   System.err.println("Ошибка при получении сообщения");
                   e.printStackTrace();
               }
           }
        }
    }
}
