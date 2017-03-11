package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.Constants;
import org.usfirst.frc.team95.robot.RobotMap;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DistanceMovePID extends Auto
	{
		double left, right, distanceL, errorL, prevErrorL, sumL, slopeL, P, I, D, distanceR, errorR, prevErrorR, sumR, slopeR, prevTime, newTime, prevSpeed;

		boolean done = false;

		public DistanceMovePID(double distance)
			{
				this.distanceL = distance;
				this.distanceR = distance;
			}

		@Override
		public void init()
			{
				done = false;
				// System.out.println("in Init");
				P = .35;// .175
				/*
				 * //I = 0; D = 0; sumL = 0; sumR = 0; prevTime = 0; prevSpeed = 0;
				 */

				// distanceR += (RobotMap.right1.getEncPosition() / Constants.encoderTickPerFoot);
				// P = P / distanceL;
				// timer.reset();
				// timer.start();
			}

		@Override
		public void start()
			{
				if (RobotMap.driveLock == this || RobotMap.driveLock == null)
					{
						RobotMap.driveLock = this;
					}
				done = false;
				distanceL += (RobotMap.left1.getEncPosition() / Constants.encoderTickPerFoot);
				System.out.println("dist start");
			}

		@Override
		public void update()
			{
				// System.out.println("in update");
				// System.out.println("dist update");
				SmartDashboard.putNumber("errorL", errorL);
				// SmartDashboard.putDouble("errorR", errorR);
				errorL = distanceL - (RobotMap.left1.getEncPosition() / Constants.encoderTickPerFoot);
				// errorR = distanceR - (RobotMap.right1.getEncPosition() / Constants.encoderTickPerFoot);

				left = P * errorL;
				// right = P * errorR;

				// newTime = timer.get();
				// sumL += (errorL * (newTime - prevTime));
				// //sumR += (errorR * (newTime - prevTime));
				//
				// slopeL = ((errorL - prevErrorL) / (newTime - prevTime));
				// //slopeR = ((errorR - prevErrorR) / (newTime - prevTime));
				// prevTime = timer.get();
				//
				// left += I * sumL;
				// //right += I * sumR;
				// System.out.println("dist up");
				// left += D * slopeL;
				// //right += D * slopeR;

				if (left > .4)
					{
						left = .4;
					}
				else if (left < -.4)
					{
						left = -.4;
					}
				/*
				 * if (right > 1) { right = 1; } else if (right < -1) { right = -1; }
				 */

				if (left > (prevSpeed + .08))
					{
						left = prevSpeed + .08;
					}
				right = left;
				RobotMap.drive.tank(-left, -right);

				// prevErrorL = errorL;
				// //prevErrorR = errorR;
				prevSpeed = left;

				if (errorL < .25)
					{
						done = true;
					}
			}

		@Override
		public void stop()
			{
				if (RobotMap.driveLock == null || RobotMap.driveLock == this)
					{
						RobotMap.drive.tank(0, 0);
						RobotMap.driveLock = null;
						System.out.println("dist stop");
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