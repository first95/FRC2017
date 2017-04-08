package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.Constants;
import org.usfirst.frc.team95.robot.RobotMap;

public class DistanceMove extends Auto
	{

		private double mLeft, mRight, mDistance, ramp, rampRate, minSpeed, speed;
		private boolean done = false;

		public DistanceMove(double left, double right, double distance)
			{
				mLeft = left;
				mRight = right;
				mDistance = distance;

			}

		@Override
		public void init()
			{

			}

		@Override
		public void start()
			{
				if (RobotMap.driveLock == this || RobotMap.driveLock == null)
					{
						RobotMap.driveLock = this;
						if (mLeft > 0)
							{
								minSpeed = .1;
							}
						else
							{
								minSpeed = -.1;
							}
						speed = minSpeed;
						rampRate = mLeft * .03;
						ramp = mDistance * .15;
						ramp += (RobotMap.left1.getEncPosition() / Constants.ENCODER_TICKS_PER_FOOT);
						RobotMap.drive.tank(-minSpeed, -minSpeed);
						mDistance += (RobotMap.left1.getEncPosition() / Constants.ENCODER_TICKS_PER_FOOT);
					}
			}

		@Override
		public void update()
			{
				// System.out.println("Update!");
				if ((RobotMap.left1.getEncPosition() / Constants.ENCODER_TICKS_PER_FOOT) <= ramp)
					{
						RobotMap.drive.tank(-speed, -speed);
						speed += rampRate;
						if (speed > mLeft)
							{
								speed = mLeft;
							}
					}
				else if ((RobotMap.left1.getEncPosition() / Constants.ENCODER_TICKS_PER_FOOT) >= (mDistance - ramp))
					{
						RobotMap.drive.tank(-speed, -speed);
						speed -= rampRate;

						if (speed < minSpeed)
							{
								speed = minSpeed;
							}
					}
				else
					{
						speed = mLeft;
						RobotMap.drive.tank(-speed, -speed);
					}
				if ((RobotMap.left1.getEncPosition() / Constants.ENCODER_TICKS_PER_FOOT) >= mDistance)
					{
						done = true;
						RobotMap.drive.tank(0, 0);
						stop();
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