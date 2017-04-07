package org.usfirst.frc.team95.robot.auto;

public class RotateAndScoreGear extends SequentialMove
	{
		public RotateAndScoreGear(double rotateAngle)
			{
				super.SetMoves(new Auto[]
					{ new RotateByUntilVision(rotateAngle), new ScoreGear()});
			}
	}
