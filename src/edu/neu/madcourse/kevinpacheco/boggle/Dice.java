package edu.neu.madcourse.kevinpacheco.boggle;

import android.app.Activity;
import edu.neu.madcourse.kevinpacheco.R;

public class Dice extends Activity{

	private int[] DiceR1C1 = {
			R.id.button_2,
			R.id.button_6,
			R.id.button_7
	};

	private int[] DiceR1C2 = {
			R.id.button_1,
			R.id.button_3,
			R.id.button_6,
			R.id.button_7,
			R.id.button_8
	};

	private int[] DiceR1C3 = {
			R.id.button_2,
			R.id.button_4,
			R.id.button_7,
			R.id.button_8,
			R.id.button_9
	};

	private int[] DiceR1C4 = {
			R.id.button_3,
			R.id.button_5,
			R.id.button_8,
			R.id.button_9,
			R.id.button_10
	};

	private int[] DiceR1C5 = {
			R.id.button_4,
			R.id.button_9,
			R.id.button_10,
	};

	private int[] DiceR2C1 = {
			R.id.button_1,
			R.id.button_2,
			R.id.button_7,
			R.id.button_11,
			R.id.button_12
	};

	private int[] DiceR2C2 = {
			R.id.button_1,
			R.id.button_2,
			R.id.button_3,
			R.id.button_6,
			R.id.button_8,
			R.id.button_11,
			R.id.button_12,
			R.id.button_13
	};

	private int[] DiceR2C3 = {
			R.id.button_2,
			R.id.button_3,
			R.id.button_4,
			R.id.button_7,
			R.id.button_9,
			R.id.button_12,
			R.id.button_13,
			R.id.button_14
	};

	private int[] DiceR2C4 = {
			R.id.button_3,
			R.id.button_4,
			R.id.button_5,
			R.id.button_8,
			R.id.button_10,
			R.id.button_13,
			R.id.button_14,
			R.id.button_15
	};

	private int[] DiceR2C5 = {
			R.id.button_4,
			R.id.button_5,
			R.id.button_9,
			R.id.button_14,
			R.id.button_15
	};

	private int[] DiceR3C1 = {
			R.id.button_6,
			R.id.button_7,
			R.id.button_12,
			R.id.button_16,
			R.id.button_17
	};

	private int[] DiceR3C2 = {
			R.id.button_6,
			R.id.button_7,
			R.id.button_8,
			R.id.button_11,
			R.id.button_13,
			R.id.button_16,
			R.id.button_17,
			R.id.button_18
	};

	private int[] DiceR3C3 = {
			R.id.button_7,
			R.id.button_8,
			R.id.button_9,
			R.id.button_12,
			R.id.button_14,
			R.id.button_17,
			R.id.button_18,
			R.id.button_19
	};

	private int[] DiceR3C4 = {
			R.id.button_8,
			R.id.button_9,
			R.id.button_10,
			R.id.button_13,
			R.id.button_15,
			R.id.button_18,
			R.id.button_19,
			R.id.button_20
	};

	private int[] DiceR3C5 = {
			R.id.button_9,
			R.id.button_10,
			R.id.button_14,
			R.id.button_19,
			R.id.button_20
	};

	private int[] DiceR4C1 = {
			R.id.button_17,
			R.id.button_11,
			R.id.button_12,
			R.id.button_21,
			R.id.button_22,
	};

	private int[] DiceR4C2 = {
			R.id.button_16,
			R.id.button_18,
			R.id.button_11,
			R.id.button_12,
			R.id.button_13,
			R.id.button_21,
			R.id.button_22,
			R.id.button_23,
	};

	private int[] DiceR4C3 = {
			R.id.button_17,
			R.id.button_19,
			R.id.button_12,
			R.id.button_13,
			R.id.button_14,
			R.id.button_22,
			R.id.button_23,
			R.id.button_24,
	};

	private int[] DiceR4C4 = {
			R.id.button_18,
			R.id.button_20,
			R.id.button_13,
			R.id.button_14,
			R.id.button_15,
			R.id.button_23,
			R.id.button_24,
			R.id.button_25
	};

	private int[] DiceR4C5 = {
			R.id.button_19,
			R.id.button_14,
			R.id.button_15,
			R.id.button_24,
			R.id.button_25
	};
	
	private int[] DiceR5C1 = {
			R.id.button_16,
			R.id.button_17,
			R.id.button_22
	};

	private int[] DiceR5C2 = {
			R.id.button_21,
			R.id.button_23,
			R.id.button_16,
			R.id.button_17,
			R.id.button_18
	};

	private int[] DiceR5C3 = {
			R.id.button_22,
			R.id.button_24,
			R.id.button_17,
			R.id.button_18,
			R.id.button_19
	};

	private int[] DiceR5C4 = {
			R.id.button_23,
			R.id.button_25,
			R.id.button_18,
			R.id.button_19,
			R.id.button_20,
	};

	private int[] DiceR5C5 = {
			R.id.button_24,
			R.id.button_19,
			R.id.button_20,
	};

	public Dice() {
		// TODO Auto-generated constructor stub
	}

	// checks whether the dice is valid
	public boolean isValid (int previous_Dice, int current_Dice){

		int[] diSet = getDiceSet(previous_Dice);

		if (previous_Dice == current_Dice){
			return true;
		}else{
			for (int i = 0; i < diSet.length; i++){
				if (diSet[i] == current_Dice){
					return true;
				}
			}
		}
		return false;
	}

	// gets the the dice based on the dice ID 
	private int[] getDiceSet(int dice_ID){
		switch (dice_ID) {
		case R.id.button_1:
			return DiceR1C1;
		case R.id.button_2:
			return DiceR1C2;
		case R.id.button_3:
			return DiceR1C3;
		case R.id.button_4:
			return DiceR1C4;
		case R.id.button_5:
			return DiceR1C5;
		case R.id.button_6:
			return DiceR2C1;
		case R.id.button_7:
			return DiceR2C2;
		case R.id.button_8:
			return DiceR2C3;
		case R.id.button_9:
			return DiceR2C4;
		case R.id.button_10:
			return DiceR2C5;
		case R.id.button_11:
			return DiceR3C1;
		case R.id.button_12:
			return DiceR3C2;
		case R.id.button_13:
			return DiceR3C3;
		case R.id.button_14:
			return DiceR3C4;
		case R.id.button_15:
			return DiceR3C5;
		case R.id.button_16:
			return DiceR4C1;
		case R.id.button_17:
			return DiceR4C2;
		case R.id.button_18:
			return DiceR4C3;
		case R.id.button_19:
			return DiceR4C4;
		case R.id.button_20:
			return DiceR4C5;
		case R.id.button_21:
			return DiceR5C1;
		case R.id.button_22:
			return DiceR5C2;
		case R.id.button_23:
			return DiceR5C3;
		case R.id.button_24:
			return DiceR5C4;
		case R.id.button_25:
			return DiceR5C5;
		}
		return null;

	}

}
