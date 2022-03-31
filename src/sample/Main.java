package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;



public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("WeekScheduler");
        primaryStage.show();
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("file:icon.png"));

        //On pressing the close button:
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            exitApp(primaryStage);
        });
        
    }

    public void exitApp(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit");
        alert.setContentText("Are you sure you want to quit?\nAll unsaved changes will be lost.");

        if (alert.showAndWait().get() == ButtonType.OK){
            stage.close();
        }
    }




    public static void main(String[] args) {
        launch(args);
    }



}

