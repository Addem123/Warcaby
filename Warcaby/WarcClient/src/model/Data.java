package model;

public class Data {
	private String whitePlayer, redPlayer,gameStatus,winner;
	private int points,wins,defeats,ties;

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getDefeats() {
		return defeats;
	}

	public void setDefeats(int defeats) {
		this.defeats = defeats;
	}

	public int getTies() {
		return ties;
	}

	public void setTies(int tie) {
		this.ties = tie;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}


	public Data(String whitePlayer, int points, int wins, int defeats, int tie) {
		super();
		this.whitePlayer = whitePlayer;
		this.points = points;
		this.wins = wins;
		this.defeats = defeats;
		this.ties = tie;
	}

	public String getWhitePlayer() {
		return whitePlayer;
	}

	public void setWhitePlayer(String whitePlayer) {
		this.whitePlayer = whitePlayer;
	}

	public String getRedPlayer() {
		return redPlayer;
	}

	public void setRedPlayer(String redPlayer) {
		this.redPlayer = redPlayer;
	}

	public String getGameStatus() {
		return gameStatus;
	}

	public void setGameStatus(String gameStatus) {
		this.gameStatus = gameStatus;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public Data(String whitePlayer, String redPlayer, String gameStatus, String winner) {
		super();
		this.whitePlayer = whitePlayer;
		this.redPlayer = redPlayer;
		this.gameStatus = gameStatus;
		this.winner = winner;
	}
	
}
