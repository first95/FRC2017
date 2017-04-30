package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.RobotMap;

public class RotateAndScoreGear extends SequentialMove
	{
		
		private double mRotateAngle;
		
		public RotateAndScoreGear(double rotateAngle)
			{
				RobotMap.sL.SystemLoggerWriteTimeline("Rotate_And_ScoreGear_Active");
				mRotateAngle = rotateAngle;
				
				// Rotates Specific Angle and Then Scores
				super.SetMoves(new Auto[]
					{ new RotateBy(mRotateAngle), new ScoreGear()});
			}
	}
