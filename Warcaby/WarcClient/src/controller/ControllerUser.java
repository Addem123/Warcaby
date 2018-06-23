package controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ControllerUser {
	private Main main;
	private Stage primaryStage;
	private String localuser;
	private String ip;

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
	Label wpiszNick;
	@FXML
	Label wpiszip;
	@FXML
	TextField ipField;

	@FXML
	void graj() throws InterruptedException {

		wpiszNick.setVisible(false);
		wpiszip.setVisible(false);
		
		if(textFieldNick.getText().equals("")) 
			{wpiszNick.setVisible(true);}
		
		if (ipField.getText().equals("")) 
		{wpiszip.setVisible(true);}
			
		else {
		localuser = textFieldNick.getText();
		ip = ipField.getText();
		// System.out.println(localuser);

		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/MainScreen.fxml"));
			BorderPane pane = loader.load();

			Stage testStage = new Stage();
			testStage.setMinWidth(500.0);
			testStage.setMinHeight(350.0);
			testStage.setTitle("Checkers");
			testStage.initModality(Modality.WINDOW_MODAL);
			testStage.initOwner(primaryStage);

			Scene scene = new Scene(pane);
			testStage.setScene(scene);

			MainScreenController mainScreenController = loader.getController();
			mainScreenController.setStage(this, testStage);

			primaryStage.close();
			testStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}}

	}

	public String getLocaluser() {
		return localuser;
	}

	public void setLocaluser(String localuser) {
		this.localuser = localuser;
	}

	public String getIp() {
		// TODO Auto-generated method stub
		return ip;
	}

}
