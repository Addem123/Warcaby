package model;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class Piece extends StackPane {
    private double mouseX,mouseY;
    private double oldX, oldY;
	private PieceType type;
	private boolean isQueen=false;
	
	public Piece(PieceType type, int x,int y,boolean isQueen) {
		this.isQueen=isQueen;
		this.type=type;
		int size = Tile.getTileSize();
		move(x, y);
		Text t=null;
		Ellipse black= new Ellipse(size*0.325,size*0.26);
		black.setFill((type==PieceType.RED)? Color.valueOf("#c40003"):Color.valueOf("#fff9f4"));
		black.setStrokeWidth(size*0.03);
		relocate(size*x,size* y);
		black.setTranslateX((size-size*0.3125*2)/2);
		black.setTranslateY((size-size*0.26*2)/2+size*0.1);
		black.setStroke(Color.BLACK);
		black.setStrokeWidth(size*0.03);
		
		Ellipse bg= new Ellipse(size*0.325,size*0.26);
		bg.setFill((type==PieceType.RED)? Color.valueOf("#c40003"):Color.valueOf("#fff9f4"));
		bg.setStroke(Color.BLACK);
		bg.setStrokeWidth(size*0.03);
		
	    bg.setTranslateX((size-size*0.3125*2)/2);
	    bg.setTranslateY((size-size*0.26*2)/2);
		if(isQueen) {
			t= new Text(10,20,"Q");
	        t.setFont(Font.font("Verdana",20));
	        t.setTranslateX(12);
	        t.setTranslateY(15);
		}
		this.getChildren().addAll(black,bg);
		if(t!=null)
			this.getChildren().add(t);
		
		this.setOnMousePressed(e->{
			mouseX=e.getSceneX();
			mouseY=e.getSceneY();
			
		});
		this.setOnMouseDragged(e->{
			relocate(e.getSceneX()-mouseX+oldX,e.getSceneY()-mouseY+oldY);
		});
	}
	public boolean isQueen() {
		return isQueen;
	}
	public void setQueen(boolean isQueen) {
		this.isQueen = isQueen;
	}
	public void move(int x,int y) {
		int size = Tile.getTileSize();
		oldX=size*x;
		oldY=size*y;
		relocate(oldX,oldY);
	}
	
	public void abortMove() {
		relocate(oldX,oldY);
	}
	public double getMouseX() {
		return mouseX;
	}
	public double getMouseY() {
		return mouseY;
	}
	public double getOldX() {
		return oldX;
	}
	public double getOldY() {
		return oldY;
	}
	public PieceType getType() {
		return type;
	}
	
}
