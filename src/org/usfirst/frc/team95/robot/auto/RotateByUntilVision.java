package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.Constants;
import org.usfirst.frc.team95.robot.RobotMap;

public class RotateByUntilVision extends Auto
	{

		private double mAngle, start, desired, error, P, speed, prevSpeed;
		private boolean done = false;
		private double lastError;
		private int checkBeforeFail = 0;
		private boolean succeeded = false;

		public RotateByUntilVision(double angle)
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

				if (!RobotMap.debugModeEnabled)
					{
						RobotMap.visionProcessingInit();
					}

				// Huge Number to confirm that it will work
				lastError = 5000;

				if (RobotMap.driveLock == this || RobotMap.driveLock == null)
					{
						RobotMap.driveLock = this;
					}

				// Leaving This In So It Goes to Far, Allowing Us To Have Vision Stop The Turn
				// I.E. It Doesn't Stop Short
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

				if (!RobotMap.debugModeEnabled)
					{
						RobotMap.gearLiftFinder.computeHeadingToTarget();
					}

				if ((RobotMap.driveLock == this || RobotMap.driveLock == null) && !done)
					{

						RobotMap.driveLock = this;

						if (RobotMap.gearLiftFinder.haveValidHeading())
							{
								checkBeforeFail++;

								if (checkBeforeFail >= 3)
									{
										done = true;
									}
							}
						else
							{

								checkBeforeFail = 0;

								if (mAngle >= 0)
									{
										error = desired - RobotMap.left1.getEncPosition();
									}
								else
									{
										error = desired - RobotMap.right1.getEncPosition();
									}

								// divide to make speed value reasonable
								speed = (P * error) / 200;

								if (speed > .15)
									{
										speed = .15;
									}
								else if (speed < -.15)
									{
										speed = -.15;
									}

								if (speed > (prevSpeed + .08))
									{
										speed = prevSpeed + .08;
									}

								RobotMap.drive.tank(-speed, speed);

								prevSpeed = speed;
							}
					}
			}

		@Override
		public void stop()
			{

				if (!RobotMap.debugModeEnabled)
					{
						RobotMap.stopVisionProcessing();
					}

				if (RobotMap.driveLock == null || RobotMap.driveLock == this)
					{
						RobotMap.drive.tank(0, 0);
						RobotMap.driveLock = null;
					}

				// DO NOT DELETE PLEASE
				System.out.println("-- Rotate Done --");
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