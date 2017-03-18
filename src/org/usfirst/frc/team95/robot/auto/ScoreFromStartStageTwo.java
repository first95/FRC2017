package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.ADIS16448_IMU;

public class ScoreFromStartStageTwo extends SequentialMove
	{

		private double dist1, dist2, rotate;

		public ScoreFromStartStageTwo(double distance1, double distance2, double degreeRotate, ADIS16448_IMU poseidon)
			{
				
				System.out.println("Running 'Score Gear' Stage Two");

				dist1 = distance1;
				dist2 = distance2;
				rotate = degreeRotate;

				super.SetMoves(new Auto[]
					{ new DistanceMovePID(-dist2), new RotateBy(-rotate), new DistanceMovePID(dist1) });
			}
	}