package model.blocks;

import java.awt.Color;

public abstract class Block {
		
	protected int[][] shape;
	protected Color color;
	protected String text;
	// Code Review : Static 변수도 아니고, 생성자에서 초기화 해주고 있는데, 굳이 멤버 변수에 값을 미리 대입할 필요는 없다
	// protected String text="O";
	protected int rotate_status=1;

	public Block() {
		shape = new int[][]{ 
				{1, 1}, 
				{1, 1}
		};
		color = Color.YELLOW;
		text="O"; //블럭의 무늬를 나타내기 위한 Text 추가
	}
	public int getShape(int x, int y){
		return shape[y][x];
	}
	public void setShape(int x, int y,int i) {
		shape[y][x]=i;
	}

	public int[][] getShapeArray() {
		return shape;
	}
	
	public Color getColor() {
		return color;
	}

	public String getText() {
		return text;
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
		// Code review : return 0 해주면 끝남
//		int rotate_x=0;
//		return rotate_x;
		return 0;
	}

	public int rotate_y(){
		// Code review : return 0 해주면 끝남
		return 0;
	}

	public int height() {
		return shape.length;
	}
	
	public int width() {
		if(shape.length > 0)
			return shape[0].length;
		return 0;
	}

	public int getRotate_status() {
		return rotate_status;
	}

	public void setRotate_status(int rotate_status) {
		this.rotate_status = rotate_status;
	}
}
