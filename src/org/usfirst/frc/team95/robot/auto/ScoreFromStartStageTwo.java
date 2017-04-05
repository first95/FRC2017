package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.ADIS16448_IMU;
import org.usfirst.frc.team95.robot.Constants;
import org.usfirst.frc.team95.robot.RobotMap;

public class ScoreFromStartStageTwo extends SequentialMove
	{

		// 0 is far left
		public ScoreFromStartStageTwo(ADIS16448_IMU poseidon)
			{

				super.SetMoves(new Auto[]
					{ new TimedMove(0.4, 0.4, 1), new RotateBy((-RobotMap.autoRotate) * 0.25), new DistanceMovePID(RobotMap.autoDist1 + 12) });
				
			}
	}