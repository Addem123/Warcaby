package prk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import prk.Game.Player;

class Game {

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

	/**
	 * The current player.
	 */

	Player currentPlayer;
	private Piece[][] board = new Piece[8][8];
	boolean won, tie;
	private int queenMove = 0;
	private int pieceCount = 0;

	/**
	 * Called by the player threads when a player tries to make a move. This method
	 * checks to see if the move is legal: that is, the player requesting the move
	 * must be the current player and the square in which she is trying to move must
	 * not already be occupied. If the move is legal the game state is updated (the
	 * square is set and the next player becomes current) and the other player is
	 * notified of the move so it can update its client.
	 */
	public int numberOfPiece() {
		int number = 0;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				if (board[x][y] != null)
					number++;
			}
		}
		return number;
	}

	private void checkWon() {
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

			}
//			if ((yourPiece == 0 && oppPiece > 0) || (yourMove == 0 && oppMove > 0)) {
//				enemyWon = true;
//			}
			if (queenMove >= 7 && pieceCount == (yourPiece + oppPiece)) {
				tie = true;
			}
		}
	}

	private Piece checkQueenMove(int x, int y, int newX, int newY) {
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

	public MoveResult tryMove(int oldX, int oldY, int newX, int newY) {
		Piece piece = board[oldX][oldY];
		if (newX >= 8 || newX < 0 || newY > 7 || newY < 0 || board[newX][newY] != null || (newX + newY) % 2 == 0
				|| piece.getType() != currentPlayer.type)
			return new MoveResult(MoveType.NONE);
		if (!piece.isQueen()) {
			if (Math.abs(newX - oldX) == 1 && newY - oldY == piece.getType().getMoveDir()
					&& piece.getType() == currentPlayer.type && checkNumberOfStrikes(currentPlayer.type) == 0) {
				System.out.println("bicia: " + checkNumberOfStrikes(currentPlayer.type));
				return new MoveResult(MoveType.NORMAL);
			} else if (Math.abs(newX - oldX) == 2 && piece.getType() == currentPlayer.type) {
				int x1 = oldX + (newX - oldX) / 2;
				int y1 = oldY + (newY - oldY) / 2;
				if (board[x1][y1] != null && board[x1][y1].getType() != currentPlayer.type) {
					return new MoveResult(MoveType.KILL, board[x1][y1]);
				}
			}
		}
		if (piece.isQueen()) {
			if (piece.getType() != currentPlayer.type || (Math.abs(newX - oldX) != Math.abs(newY - oldY))
					|| (checkNumberOfStrikes(currentPlayer.type) != 0
							&& checkQueenMove(oldX, oldY, newX, newY) == null))
				return new MoveResult(MoveType.NONE);
			if (checkNumberOfStrikes(currentPlayer.type) == 0 && (Math.abs(newX - oldX) == Math.abs(newY - oldY))
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

	private int checkNumberOfStrikes(PieceType type) {
		int c = 0;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				if (board[x][y] != null && board[x][y].getType() == type) {
					if (pieceStrike(board[x][y]))
						c++;
				}
			}
		}
		return c;
	}

	public String gameStatus() {
		String gameStatus = "";
		// if(!tie) {
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
		// }
		// else gameStatus="6";
		// return gameStatus;

	}

	public void changeType(Piece piece) {
		if ((piece.getOldY() == 7 && piece.getType() == PieceType.RED && !piece.isQueen())
				|| (piece.getOldY() == 0 && piece.getType() == PieceType.WHITE && !piece.isQueen())) {
			piece.setQueen(true);
		}
	}

	private boolean pieceStrike(Piece piece) {
		int x = piece.getOldX();
		int y = piece.getOldY();
		if (!piece.isQueen()) {
			if ((x - 2 >= 0 && y - 2 >= 0 && board[x - 1][y - 1] != null
					&& board[x - 1][y - 1].getType() != currentPlayer.type && board[x - 2][y - 2] == null))
				return true;

			if (x + 2 <= 7 && y - 2 >= 0 && board[x + 1][y - 1] != null
					&& board[x + 1][y - 1].getType() != currentPlayer.type && board[x + 2][y - 2] == null)
				return true;
			if ((x - 2 >= 0 && y + 2 <= 7 && board[x - 1][y + 1] != null
					&& board[x - 1][y + 1].getType() != currentPlayer.type && board[x - 2][y + 2] == null))
				return true;

			if (x + 2 <= 7 && y + 2 <= 7 && board[x + 1][y + 1] != null
					&& board[x + 1][y + 1].getType() != currentPlayer.type && board[x + 2][y + 2] == null)
				return true;
		} else {
			if ((x >= 2 && y >= 2)) {
				int a = 0;
				x = piece.getOldX();
				y = piece.getOldY();
				while (x >= 2 && y >= 2) {
					--x;
					--y;
					if (board[x][y] != null && (board[x][y].getType() == currentPlayer.type
							|| (board[x][y].getType() != currentPlayer.type && board[x - 1][y - 1] != null)))
						a++;
					if (board[x][y] != null && board[x][y].getType() != currentPlayer.type
							&& board[x - 1][y - 1] == null && a == 0)
						return true;
				}
			}
			if ((x <= 5 && y >= 2)) {
				x = piece.getOldX();
				y = piece.getOldY();
				int a = 0;
				while (x <= 5 && y >= 2) {
					x++;
					y--;
					if (board[x][y] != null && (board[x][y].getType() == currentPlayer.type
							|| (board[x][y].getType() != currentPlayer.type && board[x + 1][y - 1] != null)))
						a++;
					if (board[x][y] != null && board[x][y].getType() != currentPlayer.type
							&& board[x + 1][y - 1] == null && a == 0)
						return true;
				}
			}
			if ((x >= 2 && y <= 5)) {
				int a = 0;
				x = piece.getOldX();
				y = piece.getOldY();
				while (x >= 2 && y <= 5) {
					x--;
					y++;
					if (board[x][y] != null && (board[x][y].getType() == currentPlayer.type
							|| (board[x][y].getType() != currentPlayer.type && board[x - 1][y + 1] != null)))
						a++;
					if (board[x][y] != null && board[x][y].getType() != currentPlayer.type
							&& board[x - 1][y + 1] == null && a == 0)
						return true;
				}
			}
			if ((x <= 5 && y <= 5)) {
				x = piece.getOldX();
				y = piece.getOldY();
				int a = 0;
				while (x <= 5 && y <= 5) {
					x++;
					y++;
					if (board[x][y] != null && (board[x][y].getType() == currentPlayer.type
							|| (board[x][y].getType() != currentPlayer.type && board[x + 1][y + 1] != null)))
						a++;
					if (board[x][y] != null && board[x][y].getType() != currentPlayer.type
							&& board[x + 1][y + 1] == null && a == 0)
						return true;
				}
			}

		}
		return false;
	}

	class Player extends Thread {
		char mark;
		PieceType type;
		Player opponent;
		Socket socket;
		BufferedReader input;
		PrintWriter output;
		String nick;

		/**
		 * Constructs a handler thread for a given socket and mark initializes the
		 * stream fields, displays the first two welcoming messages.
		 */
		public Player(Socket socket, char mark, PieceType type) {
			this.socket = socket;
			this.mark = mark;
			this.type = type;
			try {
				input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				output = new PrintWriter(socket.getOutputStream(), true);
				output.println("WELCOME " + mark);
				output.println("MESSAGE Waiting for opponent to connect");
			} catch (IOException e) {
				System.out.println("Player died: " + e);
			}
		}

		/*public String getNick() throws IOException {
			String command = input.readLine();
			if (command.startsWith("NICK")) {
				nick = command.substring(command.indexOf("NICK"));
				System.out.println(nick);
			}
			return nick;
		};*/
		
		public void sendopponentNick(String nick) {
			output.println("OPPONNENTNICK" + nick);
		};

		/**
		 * Accepts notification of who the opponent is.
		 */
		public void setOpponent(Player opponent) {
			this.opponent = opponent;
		}

		/**
		 * Handles the otherPlayerMoved message.
		 */
		public void otherPlayerMoved() {
			output.println("OPPONENT_MOVED " + gameStatus());
			 output.println(won ? "DEFEAT" : tie ? "TIE" : "");
		}

		/**
		 * The run method of this thread.
		 */
		public void run() {
			try {
				// The thread is only started after everyone connects.
				output.println("MESSAGE All players connected");
				// Tell the first player that it is her turn.
				if (mark == 'W') {
					output.println("MSG Your move");
				} else
					output.println("MSG Opponen move");
				// Repeatedly get commands from the client and process them.
				while (true) {
					String command = input.readLine();
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
							board[newX][newY] = new Piece(piece.getType(), newX, newY, piece.isQueen());
							Piece newPiece = new Piece(piece.getType(), newX, newY, piece.isQueen());
							changeType(newPiece);
							if (piece.isQueen())
								queenMove++;
							else
								queenMove = 0;
							if (queenMove == 1)
								pieceCount = numberOfPiece();
							output.println("VALID_MOVE" + gameStatus());
							checkWon();
                            output.println(won ? "VICTORY" : tie ? "TIE": "");
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
							queenMove=0;
							if (!pieceStrike(newPiece)) {
								checkWon();
								output.println("VALID_MOVE" + gameStatus());
								output.println(won ? "VICTORY" : tie ? "TIE": "");
								currentPlayer = currentPlayer.opponent;
								currentPlayer.otherPlayerMoved();
							} else if (pieceStrike(newPiece)) {
								output.println("MOREKILL" + gameStatus());
							}
						}
					} else if(command.startsWith("NICK")) {
						nick = command.replace("NICK","");
						String tempnick = nick;
						currentPlayer = currentPlayer.opponent;
						currentPlayer.sendopponentNick(tempnick);
					}
					
					
					else if (command.startsWith("QUIT")) {
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