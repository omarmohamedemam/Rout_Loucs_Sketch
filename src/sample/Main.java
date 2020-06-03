package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;



public class Main extends Application {

    Canvas canvas = new Canvas(Controller.width,Controller.height);
    static FXMLLoader loader;
    @Override
    public void start(Stage primaryStage) throws Exception{
        GraphicsContext gc = canvas.getGraphicsContext2D();
         loader = new FXMLLoader(getClass().getResource("Main.fxml"));

        Parent root = loader.load();
        primaryStage.setTitle("Root Locus Graph ");
        primaryStage.setScene(new Scene(root, 850, 600));

        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
