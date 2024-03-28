package old_main;

import IO.ImportSettings;
import old_component.Board;

public class Tetris {

	private static ImportSettings settings;

	private static TetrisMain main;

	private static Board tetrisGame;


	private static int ResolutionSizeX;

	private static int ResolutionSizeY;

	private static void initialize() {
		settings = ImportSettings.getInstance();
		ResolutionSizeX = Integer.parseInt(settings.getSetting("ResolutionSizeX"));
		ResolutionSizeY = Integer.parseInt(settings.getSetting("ResolutionSizeY"));
	}

	public static void startGame() {
		main.setVisible(false);
		tetrisGame = new Board();
		tetrisGame.setSize(ResolutionSizeX, ResolutionSizeY);
		tetrisGame.setVisible(true);
	}

	public static void LevelMain() {
		tetrisGame.setVisible(false);
		main.setVisible(true);
	}

//we
	public static void main(String[] args) {
		initialize();
		main = new TetrisMain(ResolutionSizeX,ResolutionSizeY);
		main.setVisible(true);
	}
}

