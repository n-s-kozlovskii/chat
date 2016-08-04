package ui;/**
 * Created by nek on 04.08.16.
 */

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Client;
import logic.Server;

import java.io.IOException;

public class MainWindow extends Application {
    private Client client = new Client("127.0.0.1", 8000);

    public static void main(String[] args) {
        try {
            new Thread(new Server(8000)).start();
            launch(args);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX Welcome");


        GridPane grid = new GridPane();
        //grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        TextField userTextField = new TextField();
        userTextField.setMinWidth(primaryStage.getWidth());
        grid.add(userTextField, 0, 0);
        Button btn = new Button("Send");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_LEFT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 0, 1);

        //grid.setGridLinesVisible(true);

        Scene scene = new Scene(grid, 300, 275);
        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        btn.setOnAction(e -> client.sendData(userTextField.getText()));

        EventHandler<KeyEvent> keyEventHandler = event -> {
            if (event.getCode() == KeyCode.ENTER){
                client.sendData(userTextField.getText());
            }
        };
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED,keyEventHandler);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}
