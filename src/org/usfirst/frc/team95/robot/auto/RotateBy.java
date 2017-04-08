package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.Constants;
import org.usfirst.frc.team95.robot.RobotMap;

public class RotateBy extends Auto
	{

		private double mAngle, start, desired, error, P, speed, prevSpeed;
		private boolean done = false;

		public RotateBy(double angle)
			{
				mAngle = angle;
			}

		@Override
		public void init()
			{
				P = 0.35;
			}

		@Override
		public void start()
			{
				if (RobotMap.driveLock == this || RobotMap.driveLock == null)
					{
						RobotMap.driveLock = this;
					}

				// Rotate Angle Is Multiplied
				mAngle *= 1.35;

				if (mAngle >= 0)
					{
						start = RobotMap.left1.getEncPosition();
					}
				else
					{
						start = RobotMap.right1.getEncPosition();
					}
				desired = start + (Constants.ENCODER_TICKS_PER_RADIAN * mAngle);
				prevSpeed = 0;
				if (mAngle >= 0)
					{
						error = desired - RobotMap.left1.getEncPosition();
					}
				else
					{
						error = desired - RobotMap.right1.getEncPosition();
					}
				speed = P * error;
				done = false;
			}

		@Override
		public void update()
			{
				if ((RobotMap.driveLock == this || RobotMap.driveLock == null) && !done)
					{
						RobotMap.driveLock = this;
						if (Math.abs(error) <= Constants.ENCODER_TICKS_PER_RADIAN * .04)
							{
								done = true;
							}
						else
							{
								if (mAngle >= 0)
									{
										error = desired - RobotMap.left1.getEncPosition();
									}
								else
									{
										error = desired - RobotMap.right1.getEncPosition();
									}

								speed = (P * error) / 200;// divide to make speed value reasonable
								if (speed > .5)
									{
										speed = .5;
									}
								else if (speed < -.5)
									{
										speed = -.5;
									}

								if (speed > (prevSpeed + .08))
									{
										speed = prevSpeed + .08;
									}

								RobotMap.drive.tank(-speed, speed);

								prevSpeed = speed;
							}
					}
				else
					{

					}
			}

		@Override
		public void stop()
			{
				if (RobotMap.driveLock == null || RobotMap.driveLock == this)
					{
						RobotMap.drive.tank(0, 0);
						RobotMap.driveLock = null;
					}

				// DO NOT DELETE PLEASE
				System.out.println("--- Rotate Done ---");
			}

		private double sign(double a)
			{
				return a < 0 ? -1 : 1;
			}

		@Override
		public boolean isDone()
			{
				return done;
			}

		@Override
		public boolean succeeded()
			{
				return true;
			}
	}