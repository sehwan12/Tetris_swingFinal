package blocks;

import java.awt.Color;

public class LBlock extends Block {
	
	public LBlock() {
		shape = new int[][] {
				{1, 1, 1},
				{1, 0, 0}
		};
		color = Color.ORANGE;
		rotate_status=1;
	}

	@Override
	public int rotate_x(){
		int rotate_x=0;
		switch (rotate_status){
			case 1:
				rotate_x=1;
				break;
			case 2:
				rotate_x=-1;
				break;
			case 3:
				rotate_x=0;
				break;
			case 4:
				rotate_x=0;
				break;
		}
		return rotate_x;
	}

	@Override
	public int rotate_y(){
		int rotate_y=0;
		switch (rotate_status){
			case 1:
				rotate_y=0;
				break;
			case 2:
				rotate_y=1;
				break;
			case 3:
				rotate_y=-1;
				break;
			case 4:
				rotate_y=0;
				break;
		}
		return rotate_y;
	}

}
