package ui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

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

        TextField textField = new TextField();
        Label userNameLable = new Label("Имя");

        PasswordField passwordField = new PasswordField();
        Label passwordLabel = new Label("Password");

        Button btn = new Button("Confirm");
        HBox hbBtn = new HBox();
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);


        grid.add(userNameLable, 0, 0);
        grid.add(textField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1,1);
        grid.add(hbBtn, 1, 2);
        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();

        btn.setOnAction(e ->{
            if (success(textField.getText(), passwordField.getText())) {
                mainWindow(primaryStage,textField.getText());
            } else {
                failWindow (primaryStage);
            }
        });
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
        ObservableList<models.Message> messages = FXCollections.observableArrayList();
        ScrollPane pane = new ScrollPane();
        GridPane pane1 = new GridPane();

        Scene scene = new Scene(pane, 100, 100);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
