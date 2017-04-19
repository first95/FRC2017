package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.Constants;
import org.usfirst.frc.team95.robot.RobotMap;

import com.ctre.CANTalon.TalonControlMode;

public class MagicMotionAuto extends Auto
	{

		private boolean done;
		private double mDistance;

		public MagicMotionAuto(double distance)
			{
				mDistance = distance;
			}

		@Override
		public void init()
			{

				done = false;
				
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

				RobotMap.left1.setMotionMagicAcceleration(50);
				RobotMap.left1.setMotionMagicCruiseVelocity(100);

				RobotMap.right1.setMotionMagicAcceleration(50);
				RobotMap.right1.setMotionMagicCruiseVelocity(-100);	
			}

		@Override
		public void start()
			{
				if (RobotMap.driveLock == this || RobotMap.driveLock == null)
					{
						RobotMap.driveLock = this;
					}
				
				done = false;
			}

		@Override
		public void update()
			{
				
				
				RobotMap.right1.set(-mDistance * Constants.ENCODER_TICKS_PER_FOOT);
				RobotMap.left1.set(mDistance * Constants.ENCODER_TICKS_PER_FOOT);
				

				if (RobotMap.left1.getEncPosition() > (mDistance * Constants.ENCODER_TICKS_PER_FOOT))
					{
						done = true;
					}
				if (RobotMap.right1.getEncPosition() > (mDistance * Constants.ENCODER_TICKS_PER_FOOT))
					{
						done = true;
					}
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
				return false;
			}

	}
