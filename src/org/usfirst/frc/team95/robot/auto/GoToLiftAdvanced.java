package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.RobotMap;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GoToLiftAdvanced extends Auto
	{

		boolean done = false;

		private static final double MAX_DEAD_BAND = 0.5;
		private static final double MIN_DEAD_BAND = 0.5;

		private static final double MAX_ROTATE_THROTTLE = -0.1;
		private static final double MAX_ROTATE_THROTTLE_INVERTED = -0.1;
		private static final double MAX_DRIVE_THROTTLE = -0.1;
		private static final double MAX_DRIVE_THROTTLE_WHILE_TURNING = -0.1;

		@Override
		public void init()
			{
				RobotMap.visionProcessingInit();

				done = false;

				if (RobotMap.driveLock == this || RobotMap.driveLock == null)
					{
						RobotMap.driveLock = this;
						RobotMap.drive.arcade(MAX_DRIVE_THROTTLE, 0);
					}
			}

		@Override
		public void start()
			{
				// Not Being Used For This Automove
			}

		@Override
		public void update()
			{

				// Should be printed by SmartDashboard
				 SmartDashboard.putNumber("Degree Offset (X)", RobotMap.gearLiftFinder.getHeadingToTargetDegrees());
				 SmartDashboard.putBoolean("We can see the target", RobotMap.gearLiftFinder.haveValidHeading());

				if (RobotMap.gearLiftFinder.getHeadingToTargetDegrees() > MAX_DEAD_BAND)
					{
						RobotMap.drive.arcade((MAX_DRIVE_THROTTLE_WHILE_TURNING), (MAX_ROTATE_THROTTLE * RobotMap.gearLiftFinder.getHeadingToTargetDegrees()) / 25);
					}
				else if (RobotMap.gearLiftFinder.getHeadingToTargetDegrees() < MIN_DEAD_BAND)
					{
						RobotMap.drive.arcade(MAX_DRIVE_THROTTLE_WHILE_TURNING, (MAX_ROTATE_THROTTLE_INVERTED * RobotMap.gearLiftFinder.getHeadingToTargetDegrees()) / 25);
					}
				else
					{
						RobotMap.drive.arcade(MAX_DRIVE_THROTTLE, 0);
					}

				if ((RobotMap.gearLiftFinder.haveValidHeading() == false))
					{
						RobotMap.drive.arcade(0, 0);

						done = true;
						stop();					
					}

			}

		@Override
		public void stop()
			{
				RobotMap.drive.arcade(0, 0);
				RobotMap.driveLock = null;

				done();
			}

		@Override
		public boolean done()
			{
				RobotMap.stopVisionProcessing();
				return done;
			}

	}
