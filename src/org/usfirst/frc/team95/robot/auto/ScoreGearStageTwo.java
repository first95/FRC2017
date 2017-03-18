package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.RobotMap;

public class ScoreGearStageTwo extends SequentialMove
	{
		public ScoreGearStageTwo()
			{

				super(new Auto[]
					{ new GoToLiftAdvanced(), new TimedMove(-0.6, -0.6, .35), new OpenPooper(.5), new TimedMove(0.2, 0.2, .5), new DistanceMovePID(((RobotMap.autoDist2 + 15)) * -1), new RotateBy((-RobotMap.autoRotate)*0.50), new DistanceMovePID(RobotMap.autoDist1)  /**new ClosePooper()**/ });
				
			}
	}
