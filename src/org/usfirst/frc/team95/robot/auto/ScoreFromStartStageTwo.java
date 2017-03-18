package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.ADIS16448_IMU;
import org.usfirst.frc.team95.robot.RobotMap;

public class ScoreFromStartStageTwo extends SequentialMove
	{
		
		private double dist2a, dist1a, rotatea;

		public ScoreFromStartStageTwo(double dist2, double dist1, double rotate, ADIS16448_IMU poseidon)
			{
				
				dist2a = dist2;
				dist1a = dist1;
				rotatea = rotate;
				
				System.out.println("Stage Two");
				
				super.SetMoves(new Auto[]
					{ new DistanceMovePID(-dist2a), new RotateBy(-rotatea), new DistanceMovePID(dist1a) });
			}
	}