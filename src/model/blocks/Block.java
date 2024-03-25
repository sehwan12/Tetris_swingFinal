package model.blocks;

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
		
		//rotate_status는 비유적으로 0, 90, 180, 270도 의미
		//360도 되면 0도로 돌아가기
		rotate_status++;
		if(rotate_status>=5) rotate_status=1;
	}

	//블럭 회전할 때 임의의 회전 축을 중심으로 회전하도록 하기 위한
	//x, y위치 변경 시 필요한 rotate_x, rotate_y
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
