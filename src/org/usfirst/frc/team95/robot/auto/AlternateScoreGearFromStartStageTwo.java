package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.Constants;

public class AlternateScoreGearFromStartStageTwo extends SequentialMove
	{

		public AlternateScoreGearFromStartStageTwo()
			{

				super.SetMoves(new Auto[]
					{ new DistanceMovePID(Constants.BOIL_SIDE_DIST2_STAGE_ALT), new RotateBy(Constants.ROTATE_LEFT_STAGE_ALT), new DistanceMovePID(-Constants.BOIL_SIDE_DIST1_STAGE_ALT) });

			}
		
	}
