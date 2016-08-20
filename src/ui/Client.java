package ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by nek on 07.08.16.
 */
public class Client extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Client");
        mainWindow(primaryStage, "name");
    }

    private void logInWin (Stage primaryStage) throws Exception{
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(5));
        grid.setAlignment(Pos.CENTER);

        TextField nickNameField = new TextField();
        Label userNameLabel = new Label("Имя");

        PasswordField passwordField = new PasswordField();
        Label passwordLabel = new Label("Password");

        Button btn = new Button("Confirm");
        HBox hbBtn = new HBox();
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);


        grid.add(userNameLabel, 0, 0);
        grid.add(nickNameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1,1);
        grid.add(hbBtn, 1, 2);
        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();

        BooleanBinding binding = nickNameField.textProperty().isEmpty()
                .or(passwordField.textProperty().isEmpty());
        btn.disableProperty().bind(binding);

        btn.setOnAction(e ->
            logIn (nickNameField.getText(), passwordField.getText(), primaryStage)
        );

        EventHandler<KeyEvent> enterPressed = event -> {
            if (event.getCode() == KeyCode.ENTER
                  && !nickNameField.getText().isEmpty()
                    && !passwordField.getText().isEmpty()
                    ) {
                logIn(nickNameField.getText(), passwordField.getText(), primaryStage);
            }
        };


        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED,enterPressed);
    }

    private void logIn (String nickname, String password, Stage stage){
        if (success(nickname, password)) {
            mainWindow(stage,nickname);
        } else {
            failWindow (stage);
        }
    }

    private void failWindow(Stage primaryStage) {
        Scene scene = new Scene(new Label(
                "ты чертовски невезуч, если увидел это"
        ), 100, 100);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean success(String name, String password) {
        return true;
    }


    private void mainWindow(Stage primaryStage, String name) {
        ObservableList<String> messages = FXCollections.observableArrayList();

        Socket socket = null;
        try {
            socket = new Socket("localhost", 8000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println( socket == null);
        Service<Void> sendService = getSendService(socket, messages);
        //Service<String> getService = getGetService(socket);

        messages.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                System.out.println( "list changed");
                sendService.restart();
            }
        });

        TextField inputText = new TextField();
        inputText.setPrefWidth(250);
        Button sendBtn = new Button("send");
        sendBtn.setOnAction(event -> {
            messages.add(inputText.getText());
            inputText.clear();
        });
        HBox container = new HBox();
        container.setAlignment(Pos.BOTTOM_CENTER);
        container.getChildren().add(inputText);
        container.getChildren().add(sendBtn);
        Scene scene = new Scene(container, 300, 300 );

        primaryStage.setTitle(primaryStage.getTitle()+": "+name);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    Service<Void> getSendService(Socket socket, ObservableList<String > list){
        PrintWriter out = null;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter finalOut = out;
        return new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        System.out.println("from sendService: "+list.get(list.size()-1));
                        finalOut.println(list.get(list.size()-1));
                        finalOut.close();
                        return null;
                    }
                };
            }
        };
    }

    Service<String> getGetService(Socket socket){
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader finalIn = in;
        return new Service<String>() {
            @Override
            protected Task<String> createTask() {
                return new Task<String>() {
                    @Override
                    protected String call() throws Exception {
                        String res = finalIn.readLine();
                        finalIn.close();
                        return res;
                    }
                };
            }
        };
    }

    private class Connection extends Thread{
        Socket socket;
        BufferedReader in;
        PrintWriter out;
        boolean stopped = false;

        @Override
        public void run() {
            this.setDaemon(true);
            try {
                socket = new Socket("127.0.0.1", 8000);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream());

                while (!stopped) {

                }

            } catch (Exception e) {
                System.out.println ("exit");
            } finally {
                try {
                    socket.close();
                    out.close();
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }

    }
}
