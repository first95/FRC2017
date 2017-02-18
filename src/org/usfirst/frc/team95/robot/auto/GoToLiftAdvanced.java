package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.RobotMap;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GoToLiftAdvanced extends Auto
	{

		Timer t = new Timer();

		private static final double MAX_DEAD_BAND = 0.5;
		private static final double MIN_DEAD_BAND = 0.5;
		
		private static final double MAX_ROTATE_THROTTLE = -0.1;
		private static final double MAX_ROTATE_THROTTLE_INVERTED = -0.1;
		private static final double MAX_DRIVE_THROTTLE = -0.1;
		private static final double MAX_DRIVE_THROTTLE_WHILE_TURNING = -0.1;

		public GoToLiftAdvanced()
			{

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

						t.reset();
						t.start();
						RobotMap.drive.arcade(0, 0);

						while (t.get() < 500)
							{
								RobotMap.gearPooper.set(true);
								t.stop();
							}

						stop();
					}

			}

		@Override
		public void stop()
			{
				RobotMap.drive.arcade(0, 0);
				RobotMap.gearPooper.set(false);

				RobotMap.driveLock = null;
			}

		@Override
		public boolean done()
			{
				return RobotMap.gearLiftFinder.getDistanceFromCamToTarget() <= 13;
			}

	}
