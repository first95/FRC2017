package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.Constants;
import org.usfirst.frc.team95.robot.RobotMap;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RotateByWithTwoEncoders extends Auto
	{

		// THIS CLASS IS ON THE FRITZ AGAIN

		private double mAngle, startL, startR, desiredL, desiredR, errorL, errorR, P, speedL, speedR, prevSpeedL, prevSpeedR;
		private boolean done = false;

		public RotateByWithTwoEncoders(double angle)
			{
				mAngle = angle;
			}

		@Override
		public void init()
			{
				RobotMap.sL.SystemLoggerWriteTimeline("RotateByWithTwoEncoders_Init");
				P = 0.35;
			}

		@Override
		public void start()
			{
				RobotMap.sL.SystemLoggerWriteTimeline("RotateByWithTwoEncoders_Start");
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

				SmartDashboard.putNumber("errorL", errorL);
				SmartDashboard.putNumber("errorR", errorR);

				if ((RobotMap.driveLock == this || RobotMap.driveLock == null) && !done)
					{
						RobotMap.driveLock = this;

						if (Math.abs(errorL) <= Constants.ENCODER_TICKS_PER_RADIAN * .04 && Math.abs(errorR) <= Constants.ENCODER_TICKS_PER_RADIAN * .04)
							{
								done = true;
							}
						else if (Math.abs(errorL) <= Constants.ENCODER_TICKS_PER_RADIAN * .04)
							{

								errorR = desiredR - RobotMap.right1.getEncPosition();

								speedR = (P * errorR) / 200;

								if (speedR > .3)
									{
										speedR = .3;
									}
								else if (speedR < -.3)
									{
										speedR = -.3;
									}

								if (speedR > (prevSpeedR + .08))
									{
										speedR = prevSpeedR + .08;
									}

								SmartDashboard.putNumber("speedR", speedR);

								RobotMap.drive.tank(0, speedR);

								prevSpeedR = speedR;

							}
						else if (Math.abs(errorR) <= Constants.ENCODER_TICKS_PER_RADIAN * .04)
							{
								errorL = desiredL + RobotMap.left1.getEncPosition();

								// divide to make speed value reasonable
								speedL = (P * errorL) / 200;

								if (speedL > .3)
									{
										speedL = .3;
									}
								else if (speedL < -.3)
									{
										speedL = -.3;
									}

								if (speedL > (prevSpeedL + .08))
									{
										speedL = prevSpeedL + .08;
									}

								SmartDashboard.putNumber("speedL", speedL);

								RobotMap.drive.tank(speedL, 0);

								prevSpeedL = speedL;

							}
						else
							{
								errorL = desiredL + RobotMap.left1.getEncPosition();
								errorR = desiredR - RobotMap.right1.getEncPosition();

								// divide to make speed value reasonable
								speedL = (P * errorL) / 200;
								speedR = (P * errorR) / 200;

								if (speedL > .3)
									{
										speedL = .3;
									}
								else if (speedL < -.3)
									{
										speedL = -.3;
									}

								if (speedR > .3)
									{
										speedR = .3;
									}
								else if (speedR < -.3)
									{
										speedR = -.3;
									}

								if (speedL > (prevSpeedL + .08))
									{
										speedL = prevSpeedL + .08;
									}

								if (speedR > (prevSpeedR + .08))
									{
										speedR = prevSpeedR + .08;
									}

								SmartDashboard.putNumber("speedL", speedL);
								SmartDashboard.putNumber("speedR", speedR);

								RobotMap.drive.tank(speedL, speedR);

								prevSpeedL = speedL;
								prevSpeedR = speedR;
							}
					}

			}

		@Override
		public void stop()
			{
				RobotMap.sL.SystemLoggerWriteTimeline("RotateByWithTwoEncoders_Stop");
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
				RobotMap.sL.SystemLoggerWriteTimeline("RotateByWithTwoEncoder_Succeeded");
				return true;
			}
	}