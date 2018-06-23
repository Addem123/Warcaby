package test;

import org.junit.Test;

import controller.*;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;

public class ControllerUserTest {


	@Test
	public void testA() throws InterruptedException {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				new JFXPanel(); // Initializes the JavaFx Platform
				Main main = new Main();						
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						main.start(new Stage()); // Create and initialize your app.
						try {
							main.getControllerUser().graj();	
							main.getControllerUser().getIpField().setText("LAP00132");
							main.getControllerUser().getTextFieldNick().setText("testuser1");
							main.getControllerUser().graj();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
			}
		});
		thread.start();// Initialize the thread
		Thread.sleep(10000); // Time to use the app, with out this, the thread
								// will be killed before you can tell.
	}





}
