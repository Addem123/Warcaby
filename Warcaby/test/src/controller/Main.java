package v;

import java.io.IOException;

import controller.ControllerUser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class Main extends Application {
	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage=primaryStage;
		mainWindow();
	}
	
	public void mainWindow() {
		try {
		  FXMLLoader loader=
		    new FXMLLoader(
			  Main.class.getResource("/application/User.fxml"));
		    AnchorPane pane=loader.load();
		    primaryStage.setMinWidth(350.0);
		    primaryStage.setMinHeight(250.0);
		    Scene scene=new Scene(pane);
		    ControllerUser controllerUser=
		    		loader.getController();
		    controllerUser.setMain(this,primaryStage);
		    primaryStage.setScene(scene);
		    primaryStage.show();
		    scene.getStylesheets().add
	        (Main.class.getResource("application.css").toExternalForm());
		} catch (IOException e) {
		  e.printStackTrace();	
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
