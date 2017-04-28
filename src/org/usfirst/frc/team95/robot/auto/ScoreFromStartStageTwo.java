package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.RobotMap;

public class ScoreFromStartStageTwo extends SequentialMove
	{

		public ScoreFromStartStageTwo()
			{

				super.SetMoves(new Auto[]
					{ new TimedMove(0.4, 0.4, 1), new RotateBy((RobotMap.autoRotate/-1.5)), new DistanceMovePID(RobotMap.autoDist1 + 12) });

			}
	}