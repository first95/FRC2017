package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.ADIS16448_IMU;
import org.usfirst.frc.team95.robot.RangeFinder;
import org.usfirst.frc.team95.robot.RobotMap;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GoToLiftAdvanced extends Auto
	{
		private RangeFinder rangeFinder1;

		private ADIS16448_IMU compass;
		private static final double MAX_ROTATE_THROTTLE = 0.5;
		private static final double MAX_DRIVE_THROTTLE = 0.5;
		private static final double MAX_DRIVE_THROTTLE_WHILE_TURNING = 0.1;

		public GoToLiftAdvanced(RangeFinder rangeFinder)
			{
				rangeFinder1 = rangeFinder;
			}

		@Override
		public void init()
			{

				if (RobotMap.driveLock == this || RobotMap.driveLock == null)
					{
						RobotMap.driveLock = this;
						RobotMap.drive.arcade(MAX_DRIVE_THROTTLE, 0);
					}

			}

		@Override
		public void start()
			{

			}

		@Override
		public void update()
			{

				
				
				

				SmartDashboard.putNumber("Degree Offset (X)", RobotMap.gearLiftFinder.getHeadingToTargetDegrees());
				SmartDashboard.putBoolean("We can see the target", RobotMap.gearLiftFinder.haveValidHeading());
				//

				if (RobotMap.gearLiftFinder.getHeadingToTargetDegrees() > 2)
					{
						RobotMap.drive.arcade(MAX_DRIVE_THROTTLE_WHILE_TURNING, (MAX_ROTATE_THROTTLE * RobotMap.gearLiftFinder.getHeadingToTargetDegrees()) / 25);
					}
				else if (RobotMap.gearLiftFinder.getHeadingToTargetDegrees() < -2)
					{
						RobotMap.drive.arcade(MAX_DRIVE_THROTTLE_WHILE_TURNING, ((MAX_ROTATE_THROTTLE * RobotMap.gearLiftFinder.getHeadingToTargetDegrees()) / 25) * -1);
					}

				if (rangeFinder1.getRangeInFeet() <= 1)
					{
						stop();
					}

			}

		@Override
		public void stop()
			{
				RobotMap.drive.arcade(0, 0);
			}

		@Override
		public boolean done()
			{
				return rangeFinder1.getRangeInFeet() <= 1;
			}

	}
