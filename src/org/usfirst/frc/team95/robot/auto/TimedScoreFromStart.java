package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.Constants;
import org.usfirst.frc.team95.robot.RobotMap;

public class TimedScoreFromStart extends SequentialMove
	{

		// First Distance, Rotate, Second Distance
		private double time1, time2, timeRotate, rotateL, rotateR;
		
		private int mPosition;
		private boolean mRedSide;

		// 0 is far left
		public TimedScoreFromStart(boolean redSide, int position)
			{

				mPosition = position;
				mRedSide = redSide;
				timeRotate = Constants.ROTATE_TIME;

				if (mRedSide)
					{
						if (mPosition == 0)
							{
								time1 = Constants.HOPPER_TIME1;
								time2 = Constants.HOPPER_TIME2;
								rotateL = -.32;
								rotateR = .32;
							}
						else if (mPosition == 1)
							{
								time1 = Constants.CENTER_TIME1;
								time2 = Constants.CENTER_TIME2;
								rotateL = 0;
								rotateR = 0;
								timeRotate = 0;
							}
						else if (mPosition == 2)
							{
								time1 = Constants.BOIL_TIME1;
								time2 = Constants.BOIL_TIME2;
								rotateL = .3;
								rotateR = -.3;
							}
					}
				else if (mPosition == 0)
					{
						time1 = Constants.BOIL_TIME1;
						time2 = Constants.BOIL_TIME2;
						rotateL = -.32;
						rotateR = .32;
					}
				else if (mPosition == 1)
					{
						time1 = Constants.CENTER_TIME1;
						time2 = Constants.CENTER_TIME2;
						rotateL = 0;
						rotateR = 0;
						timeRotate = 0;
					}
				else if (mPosition == 2)
					{
						time1 = Constants.HOPPER_TIME1;
						time2 = Constants.HOPPER_TIME2;
						rotateL = .3;
						rotateR = -.3;
					}
				

				super.SetMoves(new Auto[]
					{ new TimedMove(-.3, -.3, time1), new TimedMove(rotateL, rotateR, timeRotate),
							new TimedMove(-.3, -.3, time2), new ScoreGear() });
			}
	}