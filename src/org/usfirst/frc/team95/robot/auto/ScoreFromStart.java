package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.Constants;
import org.usfirst.frc.team95.robot.RobotMap;

public class ScoreFromStart extends SequentialMove
	{

		// First Distance, Rotate, Second Distance
		private double dist1, dist2, rotate;
		
		private int mPosition;
		private boolean mRedSide;

		// 0 is far left
		public ScoreFromStart(boolean redSide, int position)
			{

				mPosition = position;
				mRedSide = redSide;

				if (mRedSide)
					{
						if (mPosition == 0)
							{
								dist1 = Constants.HOPPER_SIDE_DIST1;
								dist2 = Constants.HOPPER_SIDE_DIST2;
								rotate = Constants.ROTATE_RIGHT;
							}
						else if (mPosition == 1)
							{
								dist1 = Constants.CENTER_DIST1;
								dist2 = Constants.CENTER_DIST2;
								rotate = Constants.ROTATE_NONE;
							}
						else if (mPosition == 2)
							{
								dist1 = Constants.BOIL_SIDE_DIST1;
								dist2 = Constants.BOIL_SIDE_DIST2;
								rotate = Constants.ROTATE_LEFT;
							}
					}
				else if (mPosition == 0)
					{
						dist1 = Constants.BOIL_SIDE_DIST1;
						dist2 = Constants.BOIL_SIDE_DIST2;
						rotate = Constants.ROTATE_RIGHT;
					}
				else if (mPosition == 1)
					{
						dist1 = Constants.CENTER_DIST1;
						dist2 = Constants.CENTER_DIST2;
						rotate = Constants.ROTATE_NONE;
					}
				else if (mPosition == 2)
					{
						dist1 = Constants.HOPPER_SIDE_DIST1;
						dist2 = Constants.HOPPER_SIDE_DIST2;
						rotate = Constants.ROTATE_LEFT;
					}

				RobotMap.autoDist1 = dist1;
				RobotMap.autoDist2 = dist2;
				RobotMap.autoRotate = rotate;

				super.SetMoves(new Auto[]
					{ new DistanceMovePID(dist1), new RotateAndScoreGear(rotate) });
			}
	}