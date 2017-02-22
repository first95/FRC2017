package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.ADIS16448_IMU;

public class ScoreFromStart extends SequentialMove{
	double dist1, dist2, rotate;
	
	//THIS NEEDS SET NUMBERS, I MADE STUFF UP FOR NOW!
	ScoreFromStart(boolean redSide, int position, ADIS16448_IMU poseidon) {
		if (redSide) {
			if (position == 0) {
				dist1 = 0;//need new val
				dist2 = 0;//need new val
				rotate = 60 *(Math.PI / 180);//sign might be wrong
			}else if (position == 2) {
				dist1 = 0;//need new val
				dist2 = 0;//thiss one is good
				rotate = 0;
			}else if (position == 3) {
				dist1 = 0;//need new val
				dist2 = 0;//need new val
				rotate = -60 *(Math.PI / 180);//sign might be wrong
			}
		}else if (position == 0) {
			dist1 = 0;//need new val
			dist2 = 0;//need new val
			rotate = 60 *(Math.PI / 180);//sign might be wrong
		}else if (position == 2) {
			dist1 = 0;//need new val
			dist2 = 0;//thiss one is good
			rotate = 0;
		}else if (position == 3) {
			dist1 = 0;//need new val
			dist2 = 0;//need new val
			rotate = -60 *(Math.PI / 180);//sign might be wrong
		}
		
		super.SetMoves(new Auto[]{new DistanceMovePID(dist1), new RotateBy(rotate, poseidon), 
		new DistanceMovePID(dist2), new ScoreGear()});
	}
}
