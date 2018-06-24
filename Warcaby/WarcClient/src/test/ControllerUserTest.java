package test;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.*;
import org.junit.runners.MethodSorters;

import controller.*;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ControllerUserTest {

	static Main main;

	private static class firsttest implements Runnable {
		public void run() {
			new JFXPanel(); // Initializes the JavaFx Platform
			Main main = new Main();

			javafx.application.Platform.runLater(new Runnable() {
				@Override
				public void run() {
					main.start(new Stage());
				}

			});

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			javafx.application.Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
						main.getControllerUser().graj();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			});

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			javafx.application.Platform.runLater(new Runnable() {
				@Override
				public void run() {
					main.getControllerUser().getIpField().setText("LAP00132");
					main.getControllerUser().getTextFieldNick().setText("testuser1");
				}

			});

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			javafx.application.Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
						main.getControllerUser().graj();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			});

		}
	}

	@Test
	public void testA() throws InterruptedException {

		Thread t = new Thread(new firsttest());
		t.start();

		Thread.sleep(10000); // Time to use the app, with out this, the thread
								// will be killed before you can tell.
	}

	@Test
	public void testB() throws InterruptedException, IOException {
		
		MainScreenController mainScreenController = new MainScreenController();
		
		String str = "WELCOME W";
		InputStream is = new ByteArrayInputStream(str.getBytes());
		BufferedReader in = new BufferedReader(new InputStreamReader(is));

		mainScreenController.setIn(in);
		mainScreenController.play();
		assertEquals(mainScreenController.getPlayerType(),'W');
	}

}
