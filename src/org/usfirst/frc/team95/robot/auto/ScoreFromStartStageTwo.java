package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.ADIS16448_IMU;
import org.usfirst.frc.team95.robot.RobotMap;

public class ScoreFromStartStageTwo extends SequentialMove
	{

		public ScoreFromStartStageTwo(ADIS16448_IMU poseidon)
			{

				super.SetMoves(new Auto[]
					{ new DistanceMovePID(-RobotMap.autoDist2), new RotateBy(-RobotMap.autoRotate), new DistanceMovePID(RobotMap.autoDist1) });

			}
	}