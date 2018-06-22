package prk;

public class Data {
	@Override
	public String toString() {
		return (whitePlayer + "," + redPlayer + "," + gameStatus+ "," + winner);
	}

	String whitePlayer, redPlayer,gameStatus,winner;

	public Data(String whitePlayer, String redPlayer, String gameStatus, String winner) {
		super();
		this.whitePlayer = whitePlayer;
		this.redPlayer = redPlayer;
		this.gameStatus = gameStatus;
		this.winner = winner;
	}
	
}
