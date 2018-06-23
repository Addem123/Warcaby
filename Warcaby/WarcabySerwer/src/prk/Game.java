package prk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.Scanner;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Klasa reprezetujaca gre. Gra posiada dwoch graczy(whitePlayer,redPlayer),
 * plansze do gry(board),konczye sie zwycieñstwem lub remisem(won tie)
 * 
 * @author
 *
 */
class Game {
	private static final String dataFile = "src/Data/data.txt";

	/**
	 * Tworzenie nowej gry. Konstruktor wype³nia tablice obiektami klasy Piece
	 * reprezentujacymi pionki do gry
	 */
	public Game() {
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				Piece piece = null;
				if ((y + x) % 2 != 0 && y < 3)
					piece = new Piece(PieceType.RED, x, y, false);
				if ((y + x) % 2 != 0 && y > 4)
					piece = new Piece(PieceType.WHITE, x, y, false);
				board[x][y] = piece;
			}
		}
	}

	public synchronized void saveData() throws IOException {
		PrintWriter out = null;
		try {
			File file = new File(dataFile);
			if (file != null) {
				out = new PrintWriter(new FileWriter(file.getPath(),true));
				out.printf("%-10s %-10s %-10s %-10s %n", whitePlayer.get(), redPlayer.get(),
						(won == true || tie == true) ? "Ended" : "notEnded",
						won == true ? currentPlayer.getMark() == 'W'?whitePlayer.get():redPlayer.get() : "---");
			}
		} catch (FileNotFoundException e) {
		} finally {
			if (out != null)
				out.close();
		}
	}

	/**
	 * Obecny gracz
	 */
	Player currentPlayer;

	/**
	 * Tablica obiektów klasy Piece
	 */
	private Piece[][] board = new Piece[8][8];

	/**
	 * Zmienne mowiace czy gra zakoñczy³a sie zwycieñstwen jednego z graczy czy
	 * remisem
	 */
	boolean won = false, tie = false;

	/**
	 * ilosc ruchów damka. Zmienna wykorzystywana do ustalenia czy gra zakonczy sie
	 * remisem
	 */
	private int queenMove = 0;

	/**
	 * Nick gracza poruszajacego sie bia³ymi pionkami
	 */
	StringProperty redPlayer = new SimpleStringProperty("puste");
	/**
	 * Nick gracza poruszajacego sie czerwonymi pionkami
	 */
	StringProperty whitePlayer = new SimpleStringProperty("puste");

	/**
	 * Metoda sprawdzajaca czy obecny gracz nie wygra³ starcia lub go nie zremisowa³
	 * jesli tak ustawia zmienne won lub tie na true
	 * @throws IOException 
	 */
	private synchronized void checkWon() throws IOException {
		int yourMove = 0;
		int oppMove = 0;
		int yourPiece = 0;
		int oppPiece = 0;
		if (checkNumberOfStrikes(PieceType.WHITE) == 0 && checkNumberOfStrikes(PieceType.RED) == 0) {
			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x++) {
					if (board[x][y] != null && board[x][y].getType() == currentPlayer.type) {
						yourPiece++;
						int moveDir = board[x][y].getType().getMoveDir();
						if (!board[x][y].isQueen()) {
							if (x > 0 && !(board[x - 1][y + 1 * moveDir] != null))
								yourMove++;
							if (x < 7 && !(board[x + 1][y + 1 * moveDir] != null))
								yourMove++;
						} else {
							if (x > 0 && y < 7 && !(board[x - 1][y + 1] != null))
								yourMove++;
							if (x < 7 && y < 7 && !(board[x + 1][y + 1] != null))
								yourMove++;
							if (x > 0 && y > 0 && !(board[x - 1][y - 1] != null))
								yourMove++;
							if (x < 7 && y > 0 && !(board[x + 1][y - 1] != null))
								yourMove++;
						}
					}
					if (board[x][y] != null && board[x][y].getType() != currentPlayer.type) {
						oppPiece++;
						int moveDir = board[x][y].getType().getMoveDir();
						if (!board[x][y].isQueen()) {
							if (x > 0 && !(board[x - 1][y + 1 * moveDir] != null))
								oppMove++;
							if (x < 7 && !(board[x + 1][y + 1 * moveDir] != null))
								oppMove++;
						} else {
							if (x > 0 && y < 7 && !(board[x - 1][y + 1] != null))
								oppMove++;
							if (x < 7 && y < 7 && !(board[x + 1][y + 1] != null))
								oppMove++;
							if (x > 0 && y > 0 && !(board[x - 1][y - 1] != null))
								oppMove++;
							if (x < 7 && y > 0 && !(board[x + 1][y - 1] != null))
								oppMove++;
						}
					}
				}
			}
			if (((yourPiece > 0 && oppPiece == 0) || (yourMove > 0 && oppMove == 0))) {
				won = true;
				saveData();
			}
			if (queenMove >= 15) {
				tie = true;
				saveData();
			}
		}
	}

	/**
	 * Metoda sprawdzajaca czy ruch wykonany przez pionek bedacy damka jest
	 * prawid³owy
	 * 
	 * @param x
	 *            - obecna wspó³rzedna X pionka na planszy
	 * @param y-
	 *            obecna wspó³rzedna Y pionka na planszy
	 * @param newX-
	 *            nowa wspó³rzedna X pionka na planszy
	 * @param newY-
	 *            nowa wspó³rzedna Y pionka na planszy
	 * @return obiekt klasy Piece 1) null gdy na linii ruchu nie ma pionków 2) piece
	 *         typu przeciwnego do gracza wykonujacego ruch gry na linii ruchu jest
	 *         jeden pionek przeciwnika 3)piece tego samego typu gdy na linii ruchu
	 *         jest wiecej niz jeden pionek lub jest wlasny pionek
	 */
	private synchronized Piece checkQueenMove(int x, int y, int newX, int newY) {
		int pieceToKill = 0;
		int pieceCount = 0;
		Piece piece = null;
		int directX = (newX - x) / Math.abs(newX - x);
		int directY = (newY - y) / Math.abs(newY - y);
		for (int i = 1; i <= Math.abs(newX - x); i++) {
			if (board[x + i * directX][y + i * directY] != null) {
				pieceCount++;
				if (board[x + i * directX][y + i * directY].getType() != currentPlayer.type) {
					pieceToKill++;
					if (pieceToKill == 1)
						piece = board[x + i * directX][y + i * directY];
				}
			}
		}
		if (pieceCount == 0)
			return null;
		if (pieceToKill == 1 && pieceCount == 1)
			return piece;
		return new Piece(currentPlayer.type, 0, 0, false);
	}

	/**
	 * Metoda sprawdzajaca jaki rodzaj ruchu moze wykonac pionek przemieszczajacy
	 * sie z punktu(x,y) do (newX,newY)
	 * 
	 * @param x
	 *            - obecna wspó³rzedna X pionka na planszy
	 * @param y-
	 *            obecna wspó³rzedna Y pionka na planszy
	 * @param newX-
	 *            nowa wspó³rzedna X pionka na planszy
	 * @param newY-
	 *            nowa wspó³rzedna Y pionka na planszy
	 * @return zwraca obiekt klasy MoveResult
	 */
	public synchronized MoveResult tryMove(int oldX, int oldY, int newX, int newY) {

		Piece piece = board[oldX][oldY];
		if (newX >= 8 || newX < 0 || newY > 7 || newY < 0 || board[newX][newY] != null || (newX + newY) % 2 == 0
				|| piece.getType() != currentPlayer.type)
			return new MoveResult(MoveType.NONE);
		int numberOfStrikes = checkNumberOfStrikes(currentPlayer.type);
		if (!piece.isQueen()) {
			if (Math.abs(newX - oldX) == 1 && newY - oldY == piece.getType().getMoveDir()
					&& piece.getType() == currentPlayer.type && numberOfStrikes == 0) {
				return new MoveResult(MoveType.NORMAL);
			} else if (Math.abs(newX - oldX) == 2 && piece.getType() == currentPlayer.type) {
				int x1 = oldX + (newX - oldX) / 2;
				int y1 = oldY + (newY - oldY) / 2;
				if (board[x1][y1] != null && board[x1][y1].getType() != currentPlayer.type) {
					return new MoveResult(MoveType.KILL, board[x1][y1]);
				}
			}
		} else if (piece.isQueen()) {
			if (piece.getType() != currentPlayer.type || (Math.abs(newX - oldX) != Math.abs(newY - oldY))
					|| (numberOfStrikes != 0 && checkQueenMove(oldX, oldY, newX, newY) == null))
				return new MoveResult(MoveType.NONE);
			if (numberOfStrikes == 0 && (Math.abs(newX - oldX) == Math.abs(newY - oldY))
					&& checkQueenMove(oldX, oldY, newX, newY) == null) {
				return new MoveResult(MoveType.NORMAL);
			}
			if ((Math.abs(newX - oldX) == Math.abs(newY - oldY))
					&& checkQueenMove(oldX, oldY, newX, newY).getType() != currentPlayer.type) {
				return new MoveResult(MoveType.KILL, checkQueenMove(oldX, oldY, newX, newY));
			}
		}
		return new MoveResult(MoveType.NONE);
	}

	/**
	 * Metoda sprawdza ilosc biæ z danego typu pionkow(white,red)
	 * 
	 * @param type
	 *            - typ pionka
	 * @return zwraca ilosc biæ
	 */
	private synchronized int checkNumberOfStrikes(PieceType type) {
		int count = 0;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				if (board[x][y] != null && board[x][y].getType() == type) {
					if (pieceStrike(board[x][y]))
						count++;
				}
			}
		}
		// System.out.println("Bic w liczbie: "+count);
		return count;
	}

	/**
	 * Metoda tworzy string opisujacy poloszenie pionków na planszy
	 * 
	 * @return zwraca string z stanem gry
	 */
	public synchronized String gameStatus() {
		String gameStatus = "";
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				Piece piece = board[x][y];
				if (piece == null)
					gameStatus = gameStatus + "0";
				if (piece != null && piece.getType() == PieceType.WHITE && !piece.isQueen())
					gameStatus = gameStatus + "1";
				if (piece != null && piece.getType() == PieceType.WHITE && piece.isQueen())
					gameStatus = gameStatus + "2";
				if (piece != null && piece.getType() == PieceType.RED && !piece.isQueen())
					gameStatus = gameStatus + "3";
				if (piece != null && piece.getType() == PieceType.RED && piece.isQueen())
					gameStatus = gameStatus + "4";
			}
		}
		return gameStatus;
	}

	/**
	 * Metoda zmienia pole isQueen na true w obiekcie typu Piece gdy ten pojawi sie
	 * w ostatniej linii planszy w kierunku w którym sie porusza³
	 * 
	 * @param piece
	 *            -obiekt klasy Piece dla ktorego sprawdzamy czy nalezy mu zmienic
	 *            pole isQueen
	 */
	public synchronized void changeType(Piece piece) {
		if ((piece.getOldY() == 7 && piece.getType() == PieceType.RED && !piece.isQueen())
				|| (piece.getOldY() == 0 && piece.getType() == PieceType.WHITE && !piece.isQueen())) {
			piece.setQueen(true);
		}
	}

	/**
	 * Metoda sprawdzajaca czy dany pionek(obiekt typy Piece) ma koniecznosc zbicia
	 * pionka przeciwnika
	 * 
	 * @param piece
	 *            - obiekt klasy Piece dla ktorego sprawdzamy czy ma bicie
	 * @return zwraca true gdy jest bicie lub false gdy go nie ma
	 */
	private synchronized boolean pieceStrike(Piece piece) {
		int x = piece.getOldX();
		int y = piece.getOldY();
		if (!piece.isQueen()) {
			if ((x - 2 >= 0 && y - 2 >= 0 && board[x - 1][y - 1] != null
					&& board[x - 1][y - 1].getType() != piece.getType() && board[x - 2][y - 2] == null))
				return true;

			else if (x + 2 <= 7 && y - 2 >= 0 && board[x + 1][y - 1] != null
					&& board[x + 1][y - 1].getType() != piece.getType() && board[x + 2][y - 2] == null)
				return true;
			else if ((x - 2 >= 0 && y + 2 <= 7 && board[x - 1][y + 1] != null
					&& board[x - 1][y + 1].getType() != piece.getType() && board[x - 2][y + 2] == null))
				return true;

			else if (x + 2 <= 7 && y + 2 <= 7 && board[x + 1][y + 1] != null
					&& board[x + 1][y + 1].getType() != piece.getType() && board[x + 2][y + 2] == null)
				return true;
		} else {
			
			if (x >= 2 && y >= 2) {
				int xi = piece.getOldX();
				int yi = piece.getOldY();
				while (x >= 2 && y >= 2) {
					--xi;
					--yi;
					if (board[xi][yi] != null && (board[xi][yi].getType() == piece.getType()
							|| (board[xi][yi].getType() != piece.getType() && board[xi - 1][yi - 1] != null)))
						break;
					if (board[xi][yi] != null && board[xi][yi].getType() != piece.getType()
							&& board[xi - 1][yi - 1] == null) {
						return true;
					}

				}
			}
			if (x <= 5 && y >= 2) {
				int xi = piece.getOldX();
				int yi = piece.getOldY();
				while (xi <= 5 && yi >= 2) {
					xi++;
					yi--;
					if (board[xi][yi] != null && (board[xi][yi].getType() == piece.getType()
							|| (board[xi][yi].getType() != piece.getType() && board[xi + 1][yi - 1] != null)))
						break;
					if (board[xi][yi] != null && board[xi][yi].getType() != piece.getType()
							&& board[xi + 1][yi - 1] == null) {
						return true;
					}
				}
			}
			if (x >= 2 && y <= 5) {
				int xi = piece.getOldX();
				int yi = piece.getOldY();
				while (xi >= 2 && yi <= 5) {
					xi--;
					yi++;
					if (board[xi][yi] != null && (board[xi][yi].getType() == piece.getType()
							|| (board[xi][yi].getType() != piece.getType() && board[xi - 1][yi + 1] != null)))
						break;
					if (board[xi][yi] != null && board[xi][yi].getType() != piece.getType()
							&& board[xi - 1][yi + 1] == null) {
						return true;
					}
				}
			}
			if (x <=5 && y <=5) {
				int xi = piece.getOldX();
				int yi = piece.getOldY();
				while (xi <=5 && yi <=5) {
					xi++;
					yi++;
					if (board[xi][yi] != null && (board[xi][yi].getType() == piece.getType()
							|| (board[xi][yi].getType() != piece.getType() && board[xi + 1][yi + 1] != null)))
						break;
					if (board[xi][yi] != null && board[xi][yi].getType() != piece.getType()
							&& board[xi + 1][yi + 1] == null) {
						return true;
					}
				}
			}

		}
		return false;
	}

	/**
	 * Klasa wewnetrzna reprezentujaca gracza. Gracz na swój typ(white,red),ma
	 * opponenta, strumienie wymiany danych
	 * 
	 * @author
	 *
	 */
	class Player extends Thread {
		char mark;

		public char getMark() {
			return mark;
		}

		PieceType type;
		Player opponent;
		Socket socket;
		BufferedReader input;
		PrintWriter output;

		/**
		 * Konstruuje w¹tek obs³ugi dla danego gniazda, znak oraz typ dodaje listenery
		 * do pól okreslajacego nicki graczy inicjuje pola strumieniowe i wysy³a
		 * wiadomosci powitalne
		 */
		public Player(Socket socket, char mark, PieceType type) {
			this.socket = socket;
			this.mark = mark;
			this.type = type;
			redPlayer.addListener((obs, ov, nv) -> {
				if (mark == 'W')
					output.println("OPPONNENTNICK" + nv);
			});
			whitePlayer.addListener((obs, ov, nv) -> {
				if (mark == 'R')
					output.println("OPPONNENTNICK" + nv);
			});
			try {
				input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				output = new PrintWriter(socket.getOutputStream(), true);
				output.println("WELCOME " + mark);
				output.println("MESSAGE Waiting for opponent to connect");
				loadData();
			} catch (IOException e) {
				System.out.println("Player died: " + e);
			}
		}

		public void loadData() {
			File file = new File(dataFile);
			if (file != null) {
				Scanner in = null;
				String whitePlayer, redPlayer, gameStatus, winner;
				try {
					in = new Scanner(Paths.get(file.getPath()));
					while (in.hasNext()) {
						whitePlayer = in.next();
						redPlayer = in.next();
						gameStatus = in.next();
						winner = in.next();
						output.println("DATA" + whitePlayer + "," + redPlayer + "," + gameStatus + "," + winner);
					}
				} catch (IOException e) {
				} finally {
					if (in != null)
						in.close();
				}
			}
		}

		/**
		 * Akceptuje powiadomienie o tym, kto jest przeciwnikiem.
		 */
		public void setOpponent(Player opponent) {
			this.opponent = opponent;
		}

		/**
		 * Obs³uguje komunikat otherPlayerMoved i sprawdza czy po ruchu przeciwnika nie
		 * przegralismy lub nie ma remisu
		 */
		public void otherPlayerMoved() {
			output.println("OPPONENT_MOVED " + gameStatus());
			output.println(won ? "DEFEAT" : tie ? "TIE" : "");
		}

		/**
		 * Metoda run tego watku.
		 */
		public void run() {
			try {
				// W¹tek jest uruchamiany dopiero po tym, jak dwaj gracze sie po³¹cza siê
				// po³¹cz¹.
				output.println("MESSAGE All players connected");
				// Powiedz pierwszemu graczowi, ¿e nadszed³ jej ruch.
				if (mark == 'W') {
					output.println("MSG Your move");
					// Powiedz drugiemu graczowi, ¿e rusza³ bedzie sie przeciwnik
				} else if (mark == 'R') {
					output.println("MSG Opponen move");
				}
				// Wielokrotnie otrzymuj polecenia od klienta i przetwarzaj je.
				while (true) {
					String command = input.readLine();
					// Jesli wiadomosc zaczyna sie od MOVE sprawdz jakiego typu to by³ ruch:
					// -jesli ruch jest typu NONE- wiadodosc go gracza INVALIDMOVE
					// -jesli ruch jest typu NORMAL-przesuniecie pionka, sprawdzenie koñca gry
					// wys³anie wiadomosci do gracza z aktualnym stanem gry zmienienie gracza i
					// wys³anie jemu stanu gry
					// - jeœli ruch jest typu kill-przesuniecie pionka,usuniêcie pionka
					// zbitego,sprawdzenie koñca gry
					// wys³anie wiadomosci do gracza z aktualnym stanem gry zmienienie gracza i
					// wys³anie jemu stanu gry
					if (command.startsWith("MOVE")) {
						int oldX = Integer.parseInt(command.substring(5, 6));
						int oldY = Integer.parseInt(command.substring(6, 7));
						int newX = Integer.parseInt(command.substring(7, 8));
						int newY = Integer.parseInt(command.substring(8));
						if (tryMove(oldX, oldY, newX, newY).getType() == MoveType.NONE) {
							output.println("INVALID_MOVE" + gameStatus());
						} else if (tryMove(oldX, oldY, newX, newY).getType() == MoveType.NORMAL) {
							Piece piece = board[oldX][oldY];
							board[oldX][oldY] = null;
							Piece newPiece = new Piece(piece.getType(), newX, newY, piece.isQueen());
							board[newX][newY] = newPiece;
							changeType(newPiece);
							if (piece.isQueen()) {
								queenMove++;
							} else
								queenMove = 0;
							checkWon();
							output.println("VALID_MOVE" + gameStatus());
							output.println(won ? "VICTORY" : tie ? "TIE" : "");
							currentPlayer = currentPlayer.opponent;
							currentPlayer.otherPlayerMoved();
						} else if (tryMove(oldX, oldY, newX, newY).getType() == MoveType.KILL) {
							MoveResult result = tryMove(oldX, oldY, newX, newY);
							Piece piece = board[oldX][oldY];
							Piece newPiece = new Piece(piece.getType(), newX, newY, piece.isQueen());
							board[newX][newY] = newPiece;
							board[oldX][oldY] = null;
							Piece otherPiece = result.getPiece();
							board[otherPiece.getOldX()][otherPiece.getOldY()] = null;
							changeType(newPiece);
							queenMove = 0;
							// System.out.println(pieceStrike(newPiece)+"=========================");
							if (!pieceStrike(newPiece)) {
								checkWon();
								output.println("VALID_MOVE" + gameStatus());
								output.println(won ? "VICTORY" : tie ? "TIE" : "");
								currentPlayer = currentPlayer.opponent;
								currentPlayer.otherPlayerMoved();
							} else if (pieceStrike(newPiece)) {
								output.println("MOREKILL" + gameStatus());
							}
						}
						// ustawienie nicku gracza bia³ego
					} else if (command.startsWith("NICKW")) {
						whitePlayer.set(command.replace("NICKW", ""));
					}
					// ustawienie nicku gracza czerwonego
					else if (command.startsWith("NICKR")) {
						redPlayer.set(command.replace("NICKR", ""));
					} else if (command.startsWith("DATA")) {
						loadData();
					} else if (command.startsWith("QUIT")) {
						return;
					}
				}
			} catch (IOException e) {
				System.out.println("Player died: " + e);
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	}
}