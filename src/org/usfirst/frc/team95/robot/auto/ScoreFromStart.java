package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.ADIS16448_IMU;
import org.usfirst.frc.team95.robot.Constants;

public class ScoreFromStart extends SequentialMove{
	
	// Constants
	private final static double BOIL_SIDE_DIST1 = (69.68 / 12) - 1.5;
	private final static double BOIL_SIDE_DIST2 = (67.34 / 12) - 3;
	private final static double CENTER_DIST1 = ((110.517 - (Constants.robotWidth / 2)) / 12) -3;
	private final static double CENTER_DIST2 = 0.0;
	private final static double HOPPER_SIDE_DIST1 = (69.68 / 12) - 1.5;
	private final static double HOPPER_SIDE_DIST2 = (67.34 / 12) - 3;
	private final static double ROTATE_LEFT = -60 * (Math.PI /180); //sign might be wrong
	private final static double ROTATE_RIGHT = 60 * (Math.PI / 180);
	private final static double ROTATE_NONE = 0.0;
	
	// First Distance, Rotate, Second Distance
	private double dist1, dist2, rotate;
	
	//0 is far left
	public ScoreFromStart(boolean redSide, int position, ADIS16448_IMU poseidon) {
		if (redSide) {
			if (position == 0) {	
				dist1 = HOPPER_SIDE_DIST1;
				dist2 = HOPPER_SIDE_DIST2;
				rotate = ROTATE_RIGHT;
			}else if (position == 1) {
				dist1 = CENTER_DIST1;
				dist2 = CENTER_DIST2;
				rotate = ROTATE_NONE;
			}else if (position == 2) {
				dist1 = BOIL_SIDE_DIST1;
				dist2 = BOIL_SIDE_DIST2;
				rotate = ROTATE_LEFT;
			}
		}else if (position == 0) {
			dist1 = BOIL_SIDE_DIST1;
			dist2 = BOIL_SIDE_DIST2;
			rotate = ROTATE_RIGHT;
		}else if (position == 1) {
			dist1 = CENTER_DIST1;
			dist2 = CENTER_DIST2;
			rotate = ROTATE_NONE;
		}else if (position == 2) {
			dist1 = HOPPER_SIDE_DIST1;
			dist2 = HOPPER_SIDE_DIST2;
			rotate = ROTATE_LEFT;
		}
		
		
		super.SetMoves(new Auto[]{new DistanceMovePID(dist1), new RotateBy(rotate), 
		new DistanceMovePID(dist2), new ScoreGear()});
	}
}
