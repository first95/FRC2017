package org.usfirst.frc.team95.robot.auto;

public class RotateAndScoreGear extends SequentialMove
	{
		public RotateAndScoreGear()
			{
				super.SetMoves(new Auto[]
					{ new RotateByUntilVision(-60*(Math.PI / 180)), new ScoreGear()});
			}
	}
