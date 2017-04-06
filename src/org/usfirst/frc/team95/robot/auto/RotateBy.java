package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.Constants;
import org.usfirst.frc.team95.robot.RobotMap;

public class RotateBy extends Auto
	{
		double angle, start, desired, error, P, speed, prevSpeed;
		boolean done = false;

		public RotateBy(double angle)
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
				angle *= 1.35;
				if (angle >= 0)
					{
						start = RobotMap.left1.getEncPosition();
					}
				else
					{
						start = RobotMap.right1.getEncPosition();
					}
				desired = start + (Constants.encTicksPerRadian * angle);
				prevSpeed = 0;
				if (angle >= 0)
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
						if (Math.abs(error) <= Constants.encTicksPerRadian * .04)
							{
								done = true;
							}
						else
							{
								if (angle >= 0)
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