package model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {
	private static int tileSize=70;
	public static int getTileSize() {
		return tileSize;
	}
	private Piece piece;

	public Piece getPiece() {
		return piece;
	}

	public void setPiece(Piece piece) {
		this.piece = piece;
	}
  public boolean hasPiece() {
	  return piece!=null;
  }
  public Tile(boolean light, int x, int y) {
	  this.setWidth(tileSize);
	  this.setHeight(tileSize);
	  this.relocate(x*tileSize, y*tileSize);
	  this.setFill(light? Color.valueOf("#feb"):Color.valueOf("#582"));
  }
}
