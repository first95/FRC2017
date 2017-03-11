package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.ADIS16448_IMU;
import org.usfirst.frc.team95.robot.RobotMap;

public class ScoreFromStartStageTwo extends SequentialMove
	{

		public ScoreFromStartStageTwo(ADIS16448_IMU poseidon)
			{

				if (RobotMap.autoDist1 == 0 || RobotMap.autoDist2 == 0 || RobotMap.autoRotate == 0)
					{
						System.out.println("ScoreFromStart Was not Run!");
					}
				else
					{
						super.SetMoves(new Auto[]
							{ new DistanceMovePID(-(RobotMap.autoDist2)), new RotateBy(-(RobotMap.autoRotate)), new DistanceMovePID(RobotMap.autoDist1) });
					}

			}

	}