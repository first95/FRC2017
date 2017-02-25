package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.RobotMap;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GoToLiftAdvanced extends Auto
	{

		boolean done = false;

		private static final double MAX_DEAD_BAND = 0.5;
		private static final double MIN_DEAD_BAND = 0.5;

		private static final double MAX_ROTATE_THROTTLE = -0.2;
		private static final double MAX_DRIVE_THROTTLE = -0.1;
		private static final double MAX_DRIVE_THROTTLE_WHILE_TURNING = -0.1;

		@Override
		public void init()
			{
			System.out.println("vision init");
			}

		@Override
		public void start()
			{
				System.out.println("vision start");
				RobotMap.visionProcessingInit();
		
				done = false;
		
				if (RobotMap.driveLock == this || RobotMap.driveLock == null)
					{
						RobotMap.driveLock = this;
						RobotMap.drive.arcade(MAX_DRIVE_THROTTLE, 0);
					}
			}

		@Override
		public void update()
			{
				System.out.println("vision update");
				RobotMap.gearLiftFinder.computeHeadingToTarget();
				
				// Should be printed by SmartDashboard
				 SmartDashboard.putNumber("Degree Offset (X)", RobotMap.gearLiftFinder.getHeadingToTargetDegrees());
				 SmartDashboard.putBoolean("We can see the target", RobotMap.gearLiftFinder.haveValidHeading());
				 
				 double headingError = RobotMap.gearLiftFinder.getHeadingToTargetDegrees();

				if (headingError > MAX_DEAD_BAND || headingError < MIN_DEAD_BAND)
					{
						RobotMap.drive.arcade(MAX_DRIVE_THROTTLE_WHILE_TURNING, (MAX_ROTATE_THROTTLE * headingError) / 25);
					}
				else
					{
						RobotMap.drive.arcade(MAX_DRIVE_THROTTLE, 0);
					}

				if (!(RobotMap.gearLiftFinder.haveValidHeading()))
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
				RobotMap.stopVisionProcessing();
				System.out.println("vision stop");
			}

		@Override
		public boolean done()
			{
				return done;
			}

	}
