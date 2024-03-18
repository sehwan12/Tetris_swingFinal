package main;

import component.Board;

public class Tetris {

	public static void main(String[] args) {
		Board main = new Board();
		main.setSize(400, 800);
		main.setVisible(true);
	}
}
