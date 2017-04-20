package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.Constants;
import org.usfirst.frc.team95.robot.RobotMap;

import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Timer;

public class MagicMotionAuto extends Auto
	{

		private boolean done;
		private double mDistance;
		private Timer time;

		public MagicMotionAuto(double distance)
			{
				mDistance = distance;
			}

		@Override
		public void init()
			{

				done = false;

				time = new Timer();

				RobotMap.left1.setProfile(0);
				RobotMap.right1.setProfile(0);

				RobotMap.left1.setF(2);
				RobotMap.right1.setF(2);

				RobotMap.left1.setP(0);
				RobotMap.left1.setI(0);
				RobotMap.left1.setD(0);
				RobotMap.right1.setP(0);
				RobotMap.right1.setI(0);
				RobotMap.right1.setD(0);

				RobotMap.left1.changeControlMode(TalonControlMode.MotionMagic);
				RobotMap.right1.changeControlMode(TalonControlMode.MotionMagic);

				RobotMap.left1.setMotionMagicAcceleration(200);
				RobotMap.left1.setMotionMagicCruiseVelocity(200);

				RobotMap.right1.setMotionMagicAcceleration(200);
				RobotMap.right1.setMotionMagicCruiseVelocity(200);
			}

		@Override
		public void start()
			{
				if (RobotMap.driveLock == this || RobotMap.driveLock == null)
					{
						RobotMap.driveLock = this;
					}

				time.reset();
				time.start();

				RobotMap.right1.set(mDistance * Constants.ENCODER_TICKS_PER_FOOT * 100);
				RobotMap.left1.set(-mDistance * Constants.ENCODER_TICKS_PER_FOOT * 100);

				done = false;
			}

		@Override
		public void update()
			{
			System.out.println("left" + RobotMap.left1.get());
			System.out.println("right" + RobotMap.right1.get());

//				if (time.get() > 2)
//					{
//
//						if (RobotMap.left1.getSpeed() == 0)
//							{
//								done = true;
//							}
//					}

			}

		@Override
		public void stop()
			{

				if (RobotMap.driveLock == this || RobotMap.driveLock == null)
					{
						RobotMap.driveLock = null;
					}

				RobotMap.left1.changeControlMode(TalonControlMode.PercentVbus);
				RobotMap.right1.changeControlMode(TalonControlMode.PercentVbus);
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
