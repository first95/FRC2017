package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.Constants;
import org.usfirst.frc.team95.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RotateByUntilVisionWithTwoEncoders extends Auto
	{

		private double mAngle, startL, startR, desiredL, desiredR, errorL, errorR, P, speedL, speedR, prevSpeedL, prevSpeedR;
		private boolean done = false;
		private double lastError;
		private int checkBeforeFail = 0;
		private boolean succeeded = false;

		public RotateByUntilVisionWithTwoEncoders(double angle)
			{
				mAngle = angle;
			}

		@Override
		public void init()
			{
				RobotMap.sL.SystemLoggerWriteTimeline("RotateByUntillVisionWithTwoEncoders_Init");
				P = 0.35;
			}

		@Override
		public void start()
			{
				RobotMap.sL.SystemLoggerWriteTimeline("RotateByUntillVisionWithTwoEncoders_Start");
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

				startL = RobotMap.left1.getEncPosition();
				startR = RobotMap.right1.getEncPosition();

				desiredL = startL - (Constants.ENCODER_TICKS_PER_RADIAN * mAngle);
				desiredR = startR + (Constants.ENCODER_TICKS_PER_RADIAN * mAngle);

				prevSpeedL = 0;
				prevSpeedR = 0;

				errorL = desiredL + RobotMap.left1.getEncPosition();
				errorR = desiredR - RobotMap.right1.getEncPosition();

				speedL = P * errorL;
				speedR = P * errorR;

				done = false;
			}

		@Override
		public void update()
			{
				if (!RobotMap.debugModeEnabled)
					{
						RobotMap.gearLiftFinder.computeHeadingToTarget();
					}

				SmartDashboard.putNumber("errorL", errorL);
				SmartDashboard.putNumber("errorR", errorR);

				if ((RobotMap.driveLock == this || RobotMap.driveLock == null) && !done)
					{
						RobotMap.driveLock = this;

						if (RobotMap.gearLiftFinder.haveValidHeading())
							{
								checkBeforeFail++;

								if (Math.abs(lastError) < 15)
									{
										succeeded = true;
									}
								else
									{
										succeeded = false;
									}

								if (checkBeforeFail >= 3)
									{
										done = true;
									}
							}
						else
							{

								lastError = RobotMap.gearLiftFinder.getHeadingToTargetDegrees();

								errorL = desiredL + RobotMap.left1.getEncPosition();
								errorR = desiredR - RobotMap.right1.getEncPosition();

								// divide to make speed value reasonable
								speedL = (P * errorL) / 200;
								speedR = (P * errorR) / 200;

								if (speedL > .45)
									{
										speedL = .45;
									}
								else if (speedL < -.45)
									{
										speedL = -.45;
									}

								if (speedR > .45)
									{
										speedR = .45;
									}
								else if (speedR < -.45)
									{
										speedR = -.45;
									}

								if (speedL > (prevSpeedL + .08))
									{
										speedL = prevSpeedL + .08;
									}

								if (speedR > (prevSpeedR + .08))
									{
										speedR = prevSpeedR + .08;
									}

								RobotMap.drive.tank(speedL, speedR);

								prevSpeedL = speedL;
								prevSpeedR = speedR;
							}

					}

			}

		@Override
		public void stop()
			{
				RobotMap.sL.SystemLoggerWriteTimeline("RotateByUntillVisionWithTwoEncoders_Stop");
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
				RobotMap.sL.SystemLoggerWriteTimeline("RotateByUntillVisionWithTwoEncoders_Succeeded");
				return true;
			}
	}