package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.RobotMap;

public class ScoreGear extends SequentialMove
	{
		public ScoreGear()
			{

				super(new Auto[]
					{ new GoToLiftAdvanced(), new TimedMove(-0.6, -0.6, .35), new OpenPooper(.5), new TimedMove(0.2, 0.2, .5), new DistanceMovePID(-RobotMap.autoDist2), new RotateBy(-RobotMap.autoRotate), new DistanceMovePID(RobotMap.autoDist1) /** new ClosePooper() **/
					});

			}
	}