package controller;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;

import controller.MainScreenController.Turn;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import model.Piece;
import model.PieceType;
import model.Tile;

public class MainScreenController implements Runnable {
	
	private Stage primaryStage;

	ControllerUser controllerUser;
	
	private BufferedReader in;
	private PrintWriter out;
	private IntegerProperty port = new SimpleIntegerProperty(22222);
	private Socket socket;
	private Thread thread;

	private boolean yourTurn = false;
	private boolean accepted = false;
	private boolean unableToCommunicateWithOpponent = false;
	private boolean won = false;
	private boolean enemyWon = false;
	private boolean tie = false;

	private String ip = "localhost";
	private String game = null;
	private String waitingString = "Oczekiwanie na drugiego gracza";
	private String unableToCommunicateWithOpponentString = "Nie mo�na po��czy� si� z drugim graczem";
	private String wonString = "Wygra�e�";
	private String enemyWonString = "Przeciwnik wygra�";
	private String tieString = "Gra zako�czona remisem";
	
	private String yourNick="puste";
	private String opponentNick="pusty";
	private char playerType;


	private int errors = 0;
	private int queenMove = 0;
	private int pieceCount = 0;
	private Label label = new Label();
	private Tile[][] tileMatrix = new Tile[8][8];
	Group tileGroup = new Group();
	Group pieceGroup = new Group();
	@FXML
	private BorderPane borderPane;
	@FXML
	private CheckMenuItem alwaysOnTop;
	@FXML
	private Pane gamePool;
	@FXML
	private TextField portField;
	@FXML
	private TextField hostField;
	@FXML
	private Button connectButton;
	@FXML
	private Button disconnectButton;
	@FXML
	private Label myTurn;
	@FXML
	private Label oppTurn;
	@FXML
	private Ellipse myElipse;
	@FXML
	private Ellipse oppElipse;
	@FXML
	private Label redPlayerNick;
	@FXML
	private Label whitePlayerNick;
	@FXML
	private Label LabelGamemssgs;
	
	public void setStage(ControllerUser controllerUser, Stage primaryStage) {
		this.controllerUser = controllerUser;
		this.primaryStage = primaryStage;
		yourNick=controllerUser.getLocaluser();
	};

	private void setNickLabel() {
		if(playerType=='R') {
			redPlayerNick.setText(yourNick);		
			}
		if(playerType=='W') {
			whitePlayerNick.setText(yourNick);
	}

		}

	public enum Turn {
		MyTurn, OpponentTurn, None
	}
	private void displayTurn(Turn turn) {
		switch (turn) {
		case MyTurn:
			myElipse.setVisible(true);
			myTurn.setVisible(true);
			oppElipse.setVisible(false);
			oppTurn.setVisible(false);
			break;
		case OpponentTurn:
			myElipse.setVisible(false);
			myTurn.setVisible(false);
			oppElipse.setVisible(true);
			oppTurn.setVisible(true);
			break;
		case None:
			myElipse.setVisible(false);
			myTurn.setVisible(false);
			oppElipse.setVisible(false);
			oppTurn.setVisible(false);
			break;
		}
	}

	private void createContent() {
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				Tile tile = new Tile((y + x) % 2 == 0, x, y);
				tileMatrix[x][y] = tile;
				tileGroup.getChildren().add(tile);
				Piece piece = null;
				if ((y + x) % 2 != 0 && y < 3)
					piece = movePiece(PieceType.RED, x, y, false);
				if ((y + x) % 2 != 0 && y > 4)
					piece = movePiece(PieceType.WHITE, x, y, false);
				if (piece != null) {
					pieceGroup.getChildren().add(piece);
					tile.setPiece(piece);
				}
			}
		}
	}

	private int toBoard(double pixel) {
		int a = (int) (pixel + Tile.getTileSize() / 2) / Tile.getTileSize();
		return a;

	}

	public Piece movePiece(PieceType type, int x, int y, boolean isQueen) {
		Piece piece = new Piece(type, x, y, isQueen);
		piece.setOnMouseReleased(e -> {
			int newX = toBoard(piece.getLayoutX());
			int newY = toBoard(piece.getLayoutY());
			if(accepted&&yourTurn)
			out.println("MOVE " + x + y + newX + newY);
			else piece.abortMove();
		});
	
		return piece;
	}

	private void repaint(String s) {
		if (s.length() == 64) {
			// ll.setText(s);
			// if(!s.equals("6")) {
			pieceGroup.getChildren().remove(0, pieceGroup.getChildren().size());
			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x++) {
					Tile tile = tileMatrix[x][y];
					Piece piece = null;
					if (s.charAt(x + y * 8) == '1') {
						piece = movePiece(PieceType.WHITE, x, y, false);
						tile.setPiece(piece);
					}
					if (s.charAt(x + y * 8) == '2') {
						piece = movePiece(PieceType.WHITE, x, y, true);
						tile.setPiece(piece);
					}
					if (s.charAt(x + y * 8) == '3') {
						piece = movePiece(PieceType.RED, x, y, false);
						tile.setPiece(piece);
					}
					if (s.charAt(x + y * 8) == '4') {
						piece = movePiece(PieceType.RED, x, y, true);
						tile.setPiece(piece);
					}
					if (s.charAt(x + y * 8) == '0')
						tile.setPiece(null);

					if (piece != null) {
						pieceGroup.getChildren().add(piece);
					}
				}
			}
		}
	}

	public void play() throws IOException {
		String response;
		try {
			response = in.readLine();
			//hostField.setText(response);
			//System.out.println(response);
			if (response.startsWith("WELCOME")) {
				char mark = response.charAt(8);
				System.out.print(mark);
				playerType=mark;				
			}
			else if(response.startsWith("MESSAGE Waiting for opponent to connect")) {
				javafx.application.Platform.runLater(new Runnable() {
					@Override
					public void run() {
						LabelGamemssgs.setText(waitingString);
					}
				});
			accepted=true;
			LabelGamemssgs.setText(waitingString);
		}
			if (response.startsWith("OPPONNENTNICK")) {
				opponentNick =response.replace("OPPONNENTNICK","");
				javafx.application.Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if(playerType=='W') {
							redPlayerNick.setText(opponentNick);
						}
						if(playerType=='R') {
							whitePlayerNick.setText(opponentNick);
						}
					}
				});
				System.out.println("|wys�any przez serwer nick przeciwnika," + " aktualny u�y:" + playerType + "otrzymany nick:" + opponentNick + ",");
				}
			
			else if(response.startsWith("MESSAGE All players connected")) {
				javafx.application.Platform.runLater(new Runnable() {
					@Override
					public void run() {
						setNickLabel();
						LabelGamemssgs.setText("Zaczynamy gr�");
					}
				});
				accepted=true;
				out.println("NICK" + yourNick);
			}
			else if(response.startsWith("MSG Your move")) {
				yourTurn=true;
				displayTurn(Turn.MyTurn);
			}
			else if(response.startsWith("MSG Opponen move")) {
				displayTurn(Turn.OpponentTurn);
				yourTurn=false;
			}
			else if(response.startsWith("MOREKILL")) {
				javafx.application.Platform.runLater(new Runnable() {
					@Override
					public void run() {
						repaint(response.substring(8));
					}
				});
			}
			else if (response.startsWith("VALID_MOVE")) {
				javafx.application.Platform.runLater(new Runnable() {
					@Override
					public void run() {
						repaint(response.substring(10));
					}
				});
				displayTurn(Turn.OpponentTurn);
				yourTurn=false;

			} else if (response.startsWith("OPPONENT_MOVED")) {
				javafx.application.Platform.runLater(new Runnable() {
					@Override
					public void run() {
						repaint(response.substring(15));
						LabelGamemssgs.setText("Niewa�ny ruch");
					}
				});
				yourTurn=true;
				displayTurn(Turn.MyTurn);
			}
			// } else if (response.startsWith("VICTORY")) {
			// LabelGamemssgs.setText(wonString);
			// break;
			// } else if (response.startsWith("DEFEAT")) {
			// LabelGamemssgs.setText(enemyWonString);
			// break;
			// } else if (response.startsWith("TIE")) {
			// messageLabel.setText(tieString);
			// break;
			// 
			else if (response.startsWith("INVALID_MOVE")) {
				javafx.application.Platform.runLater(new Runnable() {
					@Override
					public void run() {
						repaint(response.substring(12));
					}
				});
			}
			// }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// out.println("QUIT");
		// finally {
		// socket.close();
		// }
	}

	private void connect() {
		try {
			socket = new Socket(ip, port.intValue());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			thread = new Thread(this, "MainScreenController");
			// thread = new Thread();
			thread.start();
		} catch (IOException e) {
		}
	}

	@FXML
	private void initialize() throws Exception {
		createContent();
		connect();
		gamePool.getChildren().addAll(tileGroup, pieceGroup, label);
		displayTurn(Turn.None);
	}

	@FXML
	void closeApplication() {
		Platform.exit();
		System.exit(0);

	}

	@FXML
	void setClassic() {
		Stage stage = (Stage) borderPane.getScene().getWindow();
		stage.getScene().getStylesheets().clear();
		stage.getScene().getStylesheets().add(getClass().getResource("classic.css").toExternalForm());
	}

	@FXML
	void setModern() {
		Stage stage = (Stage) borderPane.getScene().getWindow();
		stage.getScene().getStylesheets().clear();
		stage.getScene().getStylesheets().add(getClass().getResource("modern.css").toExternalForm());
	}

	@FXML
	void setOnTop(ActionEvent event) {
		Stage stage = (Stage) borderPane.getScene().getWindow();
		boolean value = ((CheckMenuItem) event.getSource()).selectedProperty().get();
		stage.setAlwaysOnTop(value);
	}

	@FXML
	void about() {

	}

	@Override
	public void run() {
		while (true) {
				try {
					play();
					Toolkit.getDefaultToolkit().sync();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		}
	}

}