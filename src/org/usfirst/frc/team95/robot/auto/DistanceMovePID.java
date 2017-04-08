package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.Constants;
import org.usfirst.frc.team95.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DistanceMovePID extends Auto
	{

		private double mDistanceL, mDistanceR, left, right, errorL, prevErrorL, sumL, slopeL, P, I, D, errorR, prevErrorR, sumR, slopeR, prevTime, newTime, prevSpeedL, prevSpeedR;
		private boolean done = false;
		private Timer brakeTimer;
		private boolean brakeRunOnce = true;

		public DistanceMovePID(double distance)
			{
				mDistanceL = distance;
				mDistanceR = distance;
			}

		@Override
		public void init()
			{
				brakeTimer = new Timer();
				done = false;
				P = .35;

				// System.out.println("in Init");
				//
				// I = 0; D = 0; sumL = 0; sumR = 0; prevTime = 0; prevSpeed = 0;
				//
				//
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
				brakeRunOnce = true;
				mDistanceL += (RobotMap.left1.getEncPosition() / Constants.ENCODER_TICKS_PER_FOOT);
				mDistanceR -= (RobotMap.right1.getEncPosition() / Constants.ENCODER_TICKS_PER_FOOT);
				System.out.println("dist start");
			}

		@Override
		public void update()
			{
				SmartDashboard.putNumber("errorL", errorL);
				SmartDashboard.putNumber("errorR", errorR);

				errorL = mDistanceL - (RobotMap.left1.getEncPosition() / Constants.ENCODER_TICKS_PER_FOOT);
				errorR = mDistanceR + (RobotMap.right1.getEncPosition() / Constants.ENCODER_TICKS_PER_FOOT);

				left = P * errorL;
				right = P * errorR;

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

				left = Math.min(0.3, Math.max(-0.3, left));
				right = Math.min(0.3, Math.max(-0.3, right));

				left = Math.min((prevSpeedL + .02), left);
				right = Math.min((prevSpeedL + .02), right);

				RobotMap.drive.tank(-left, -right);

				// prevErrorL = errorL;
				// //prevErrorR = errorR;
				prevSpeedL = left;
				prevSpeedR = right;

				if (errorL < .25 && errorR < .25)
					{
						RobotMap.brakes.set(true);
						if (brakeRunOnce)
							{
								brakeTimer.reset();
								brakeTimer.start();
								brakeRunOnce = false;
							}

						if (brakeTimer.get() > .4)
							{
								brakeTimer.stop();
								done = true;

							}

					}
			}

		@Override
		public void stop()
			{
				RobotMap.brakes.set(false);
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