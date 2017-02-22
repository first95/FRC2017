package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.ADIS16448_IMU;
import org.usfirst.frc.team95.robot.Constants;

public class ScoreFromStart extends SequentialMove{
	double dist1, dist2, rotate;
	
	//0 is far left
	ScoreFromStart(boolean redSide, int position, ADIS16448_IMU poseidon) {
		if (redSide) {
			if (position == 0) {
				dist1 = (88.44 - (Constants.robotWidth/2)) / 12;
				dist2 = ((82.56 - (Constants.robotWidth/2)) / 12) - 3;
				rotate = 60 *(Math.PI / 180);//sign might be wrong
			}else if (position == 1) {
				dist1 = (110.517 - (Constants.robotWidth/2) / 12) - 3;
				dist2 = 0;
				rotate = 0;
			}else if (position == 2) {
				dist1 = (81.177 - (Constants.robotWidth/2) / 12);
				dist2 = (84.84 - (Constants.robotWidth/2) / 12) - 3;
				rotate = -60 *(Math.PI / 180);//sign might be wrong
			}
		}else if (position == 0) {
			dist1 = (88.44 - (Constants.robotWidth/2)) / 12;
			dist2 = ((82.56 - (Constants.robotWidth/2)) / 12) - 3;
			rotate = -60 *(Math.PI / 180);//sign might be wrong
		}else if (position == 1) {
			dist1 = (110.517 - (Constants.robotWidth/2) / 12) - 3;
			dist2 = 0;
			rotate = 0;
		}else if (position == 2) {
			dist1 = (81.177 - (Constants.robotWidth/2) / 12);
			dist2 = (84.84 - (Constants.robotWidth/2) / 12) - 3;
			rotate = 60 *(Math.PI / 180);//sign might be wrong
		}
		
		super.SetMoves(new Auto[]{new DistanceMovePID(dist1), new RotateBy(rotate, poseidon), 
		new DistanceMovePID(dist2), new ScoreGear()});
	}
}
