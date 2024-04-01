package model.blocks;

import java.awt.Color;

public class OBlock extends Block {

	public OBlock() {
		shape = new int[][] { 
			{1, 1}, 
			{1, 1}
		};
		color = Color.YELLOW;
		text = "O";
		rotate_status=1;
	}

	public OBlock(boolean color_blind, int pattern) {
		shape = new int[][] {
				{1, 1},
				{1, 1}
		};
		if(color_blind) color = new Color(240,228,66);
		else color = Color.YELLOW;
		text="O";

		rotate_status=1;
	}
}
