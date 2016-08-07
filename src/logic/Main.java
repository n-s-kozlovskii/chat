package logic;

import java.io.IOException;

/**
 * Created by nek on 04.08.16.
 */
public class Main {
    public static void main(String[] args) {
        try {
            new Thread(new Server("127.0.0.1", 8000)).start();
            new Client("127.0.0.1", 8000).sendData("Hi!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
