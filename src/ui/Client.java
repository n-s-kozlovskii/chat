package ui;

import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
import models.Message;

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
        logInWin(primaryStage);
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
        TextField inputText = new TextField();
        inputText.setPrefWidth(250);
        Button sendBtn = new Button("send");
        HBox container = new HBox();
        container.setAlignment(Pos.BOTTOM_CENTER);
        container.getChildren().add(inputText);
        container.getChildren().add(sendBtn);

        Scene scene = new Scene(container, 300, 300 );
        primaryStage.setTitle(primaryStage.getTitle()+": "+name);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
