package org.usfirst.frc.team95.robot.auto;

public class RotateGoToLift extends SequentialMove
	{
		public RotateGoToLift()
			{
				// TODO Auto-generated constructor stub

				super.SetMoves(new Auto[]
					{ new RotateByUntilVision(-60*(Math.PI / 180)), new GoToLiftAdvanced()});
			}
	}
