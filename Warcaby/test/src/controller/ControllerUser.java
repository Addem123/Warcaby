package controller;

import java.io.IOException;
import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ControllerUser {
	private Main main;
	private Stage primaryStage;
	public String localuser;

	public void setMain(Main main, Stage primaryStage) {
		this.main = main;
		this.primaryStage = primaryStage;
	}

	@FXML
	public void closeMainWindow() {
		primaryStage.close();
	}

	@FXML
	TextField textFieldNick;
	@FXML
	Button buttonGraj;

	@FXML
	void graj() {

		localuser = textFieldNick.getText();
		// System.out.println(localuser);

		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("/application/fxml.fxml"));
			AnchorPane pane = loader.load();

			Stage testStage = new Stage();
			testStage.setMinWidth(500.0);
			testStage.setMinHeight(350.0);
			testStage.setTitle("Test");
			testStage.initModality(Modality.WINDOW_MODAL);
			testStage.initOwner(primaryStage);

			Scene scene = new Scene(pane);
			testStage.setScene(scene);

			Kontroller kontrollerTest = loader.getController();
			kontrollerTest.setStage(this, testStage);

			primaryStage.close();
			testStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getLocaluser() {
		return localuser;
	}

	public void setLocaluser(String localuser) {
		this.localuser = localuser;
	}

}
