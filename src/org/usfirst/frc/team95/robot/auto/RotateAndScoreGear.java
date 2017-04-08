package org.usfirst.frc.team95.robot.auto;

public class RotateAndScoreGear extends SequentialMove
	{
		
		private double mRotateAngle;
		
		public RotateAndScoreGear(double rotateAngle)
			{
				
				mRotateAngle = rotateAngle;
				
				// Rotates Specific Angle and Then Scores
				super.SetMoves(new Auto[]
					{ new RotateByUntilVision(mRotateAngle), new ScoreGear()});
			}
	}
