package con_main;

import con_client.Client;
import con_server.Server;

import java.util.Scanner;

/**
 * Created by nek on 06.08.16.
 */
public class Main {
    public static void main(String[] args) {
        System.out.print("Выбирете режим работы (S/C): ");
        char key = Character.toLowerCase( new  Scanner(System.in).nextLine().charAt(0));
        if (key == 'c') {
            new Client();
        } else if (key == 's'){
            new Server();
        }
    }
}
