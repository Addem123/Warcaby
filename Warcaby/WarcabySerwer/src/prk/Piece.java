package prk;

public class Piece{
    private int oldX, oldY;
	private PieceType type;
	private boolean isQueen=false;
	
	public Piece(PieceType type, int x,int y,boolean isQueen) {
		this.isQueen=isQueen;
		this.type=type;
		this.oldX=x;
		this.oldY=y;
	}
	public boolean isQueen() {
		return isQueen;
	}
	public int getOldX() {
		return oldX;
	}
	public int getOldY() {
		return oldY;
	}
	public PieceType getType() {
		return type;
	}
}
	
