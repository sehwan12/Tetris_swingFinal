package blocks;

import java.awt.Color;

public abstract class Block {
		
	protected int[][] shape;
	protected Color color;

	protected int rotate_status=1;
	
	public Block() {
		shape = new int[][]{ 
				{1, 1}, 
				{1, 1}
		};
		color = Color.YELLOW;
	}
	
	public int getShape(int x, int y) {
		return shape[y][x];
	}
	
	public Color getColor() {
		return color;
	}
	
	public void rotate() {
		//Rotate the block 90 deg. clockwise.
		int[][] rotatedShape = new int[shape[0].length][shape.length];
		for (int i = 0; i < shape.length; i++) {
			for (int j = 0; j < shape[i].length; j++) {
				rotatedShape[j][shape.length - 1 - i] = shape[i][j];
			}
		}
		shape = rotatedShape;
		rotate_status++;
		if(rotate_status>=5) rotate_status=1;
		System.out.println(rotate_status);
	}

	public int rotate_x(){
		int rotate_x=0;
		return rotate_x;
	}

	public int rotate_y(){
		int rotate_y=0;
		return rotate_y;
	}

	public int height() {
		return shape.length;
	}
	
	public int width() {
		if(shape.length > 0)
			return shape[0].length;
		return 0;
	}
}
