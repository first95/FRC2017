package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.ADIS16448_IMU;
import org.usfirst.frc.team95.robot.Constants;

public class ScoreFromStart extends SequentialMove{
	double dist1, dist2, rotate;
	
	//0 is far left
	public ScoreFromStart(boolean redSide, int position, ADIS16448_IMU poseidon) {
		if (redSide) {
			if (position == 0) {
				dist1 = (70.94 / 12);
				dist2 = (65.06 / 12) - 3;
				rotate = 60 *(Math.PI / 180);
			}else if (position == 1) {
				dist1 = ((110.517 - (Constants.robotWidth/2)) / 12) - 3;
				dist2 = 0;
				rotate = 0;
			}else if (position == 2) {
				dist1 = (69.68 / 12)-1.5;
				dist2 = (67.34 / 12) - 3;
				rotate = -60 *(Math.PI / 180);//sign might be wrong
			}
		}else if (position == 0) {
			dist1 = (69.68 / 12);
			dist2 = (67.34 / 12) - 3;
			rotate = 60 *(Math.PI / 180);
		}else if (position == 1) {
			dist1 = ((110.517 - (Constants.robotWidth/2)) / 12) - 3;
			dist2 = 0;
			rotate = 0;
		}else if (position == 2) {
			dist1 = (70.94 / 12);
			dist2 = (65.06 / 12) - 3;
			rotate = -60 *(Math.PI / 180);
		}
		
		
		super.SetMoves(new Auto[]{new DistanceMovePID(dist1), new RotateBy(rotate), 
		new DistanceMovePID(dist2), new ScoreGear()});
	}
}
