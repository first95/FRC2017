package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.Constants;
import org.usfirst.frc.team95.robot.RobotMap;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RotateByWithTwoEncoders extends Auto
	{
		double angle, startL, startR, desiredL, desiredR, errorL, errorR, P, speedL, speedR, prevSpeedL, prevSpeedR;
		boolean done = false;

		public RotateByWithTwoEncoders(double angle)
			{
				this.angle = angle;
			}

		@Override
		public void init()
			{
				P = 0.35; // original .35
			}

		@Override
		public void start()
			{
				if (RobotMap.driveLock == this || RobotMap.driveLock == null)
					{
						RobotMap.driveLock = this;
					}

				// angle *= 1.35;

				startL = RobotMap.left1.getEncPosition();
				startR = RobotMap.right1.getEncPosition();

				desiredL = startL - (Constants.encTicksPerRadian * angle);
				desiredR = startR + (Constants.encTicksPerRadian * angle);

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
						if (Math.abs(errorL) <= Constants.encTicksPerRadian * .04 && Math.abs(errorR) <= Constants.encTicksPerRadian * .04)
							{
								done = true;
							}
						else if (Math.abs(errorL) <= Constants.encTicksPerRadian * .04)
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
						else if (Math.abs(errorR) <= Constants.encTicksPerRadian * .04)
							{
								errorL = desiredL + RobotMap.left1.getEncPosition();

								speedL = (P * errorL) / 200;// divide to make speed value reasonable

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

								speedL = (P * errorL) / 200;// divide to make speed value reasonable
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
				
				RobotMap.stopVisionProcessing();
				
				if (RobotMap.driveLock == null || RobotMap.driveLock == this)
					{
						RobotMap.drive.tank(0, 0);
						RobotMap.driveLock = null;
					}

				// DO NOT DELETE PLEASE
				System.out.println("TESTER");
			}

		@Override
		public boolean isDone()
			{
				return done;
			}

		double sign(double a)
			{
				return a < 0 ? -1 : 1;
			}

		@Override
		public boolean succeeded()
			{
				return true;
			}
	}