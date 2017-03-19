package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.RobotMap;

public class ScoreGearWithStageTwo extends SequentialMove
	{
		public ScoreGearWithStageTwo()
			{

				super(new Auto[]
					{ new GoToLiftAdvanced(), new TimedMove(-0.6, -0.6, .35), new OpenPooper(.5), new TimedMove(0.4, 0.4, 1), new RotateBy((-RobotMap.autoRotate) * 0.25), new DistanceMovePID(RobotMap.autoDist1 + 12) /** new ClosePooper() **/
					});

			}
	}