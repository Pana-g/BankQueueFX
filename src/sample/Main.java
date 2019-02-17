package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        Controller controller = (Controller)loader.getController();

        //we don't want to be resizable because it changes animation times and the program is not fully functional
        primaryStage.setResizable(false);
        primaryStage.setTitle("Bank");
        primaryStage.setScene(new Scene(root, 600, 400));

        //on close request we change a variable inside controller so any thread will be able to see it and terminate it self
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        controller.setClosed(true);
                    }
                });

                primaryStage.close();
            }
        });

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
