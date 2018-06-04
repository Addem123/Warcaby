package prk;

import java.net.ServerSocket;

public class WarcabySerwer {
	 public static void main(String[] args) throws Exception {
	        ServerSocket listener = new ServerSocket(22222);
	        System.out.println("Server is Running");
	        try {
	            while (true) {
	                Game game = new Game();
	                Game.Player playerW = game.new Player(listener.accept(), 'W',PieceType.WHITE);
	                Game.Player playerR = game.new Player(listener.accept(), 'R',PieceType.RED);
	                playerW.setOpponent(playerR);
	                playerR.setOpponent(playerW);
	                game.currentPlayer = playerW;
	                playerW.start();
	                playerR.start();
	            }
	        } finally {
	            listener.close();
	        }
	    }
}
