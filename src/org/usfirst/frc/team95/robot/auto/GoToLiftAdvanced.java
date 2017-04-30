package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.RobotMap;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GoToLiftAdvanced extends Auto
	{

		// CONSTANTS
		// Max and Min error that code will take before turning
		// If Less than -.5 rotate, if greater than .5, rotate.
		private static final double MAX_DEAD_BAND = 0.5;
		private static final double MIN_DEAD_BAND = -0.5;
		private static final double MAX_ROTATE_THROTTLE = -0.6;
		private static final double MAX_DRIVE_THROTTLE = -0.2;
		private static final double MAX_DRIVE_THROTTLE_WHILE_TURNING = -0.2;
		private static final double MIN_DISTANCE_AWAY_STOP = 14.0;

		private int checkBeforeFail = 0;
		private boolean done = false;
		private boolean succeeded = false;
		private double lastError;

		@Override
		public void init()
			{
				RobotMap.sL.SystemLoggerWriteTimeline("GoToLift_Advanced_Init");
			}

		@Override
		public void start()
			{
				RobotMap.sL.SystemLoggerWriteTimeline("GoToLift_Advanced_Start");

				if (!RobotMap.debugModeEnabled)
					{
						RobotMap.visionProcessingInit();
					}

				done = false;
				succeeded = false;

				// Huge Number to confirm that it will work
				lastError = 5000;

				if (RobotMap.driveLock == this || RobotMap.driveLock == null)
					{
						RobotMap.driveLock = this;
						RobotMap.drive.arcade(MAX_DRIVE_THROTTLE, 0);
					}
			}

		@Override
		public void update()
			{
				if (!RobotMap.debugModeEnabled)
					{
						RobotMap.gearLiftFinder.computeHeadingToTarget();
					}

				// Should be printed by SmartDashboard
				SmartDashboard.putNumber("Degree Offset (X)", RobotMap.gearLiftFinder.getHeadingToTargetDegrees());
				SmartDashboard.putBoolean("We can see the target", RobotMap.gearLiftFinder.haveValidHeading());

				if (RobotMap.gearLiftFinder.haveValidHeading())
					{
						System.out.println("----SEE IT-------------------------");
						System.out.println("We can see the target");
						System.out.println("Last Distance Gathered " + RobotMap.gearLiftFinder.getDistanceFromCamToTarget());
						System.out.println("Heading to target in degress " + RobotMap.gearLiftFinder.getHeadingToTargetDegrees());
						System.out.println("----------------------------------");

					}
				else
					{
						System.out.println("----DON'T SEE IT-------------------");
						System.out.println("We can't see the target, the heading is in valid!");
						System.out.println("Last Distance Gathered " + RobotMap.gearLiftFinder.getDistanceFromCamToTarget());
						System.out.println("Heading to target in degress " + RobotMap.gearLiftFinder.getHeadingToTargetDegrees());
						System.out.println("----------------------------------");
					}

				double headingError = RobotMap.gearLiftFinder.getHeadingToTargetDegrees();

				// We flipped cam, this means degree is backwards, so a negative was added to the 25 to compensate.
				if (headingError > MAX_DEAD_BAND || headingError < MIN_DEAD_BAND)
					{
						RobotMap.drive.arcade(MAX_DRIVE_THROTTLE_WHILE_TURNING, (MAX_ROTATE_THROTTLE * headingError) / -25);
					}
				else
					{
						RobotMap.drive.arcade(MAX_DRIVE_THROTTLE, 0);
					}

				// See if the vision heading is wrong, check 3 times and if it is still
				// wrong, stop and don't poop gear
				if (!(RobotMap.gearLiftFinder.haveValidHeading()))
					{
						RobotMap.drive.arcade(0, 0);

						checkBeforeFail++;

						if (Math.abs(lastError) < 15)
							{
								succeeded = true;
							}
						else
							{
								succeeded = false;
							}

						if (checkBeforeFail >= 3)
							{
								done = true;
							}

					}
				else
					{
						checkBeforeFail = 0;

						lastError = RobotMap.gearLiftFinder.getHeadingToTargetDegrees();

						if (RobotMap.gearLiftFinder.getDistanceFromCamToTarget() <= MIN_DISTANCE_AWAY_STOP)
							{
								System.out.println("RAMMING AWAY!!!!");
								succeeded = true;
								done = true;
							}
					}
			}

		@Override
		public void stop()
			{
				RobotMap.sL.SystemLoggerWriteTimeline("GoToLift_Advanced_Stop");
				RobotMap.drive.arcade(0, 0);
				RobotMap.driveLock = null;

				// Call that deactivates vision so we don't get lag
				if (!RobotMap.debugModeEnabled)
					{
						RobotMap.stopVisionProcessing();
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
				RobotMap.sL.SystemLoggerWriteTimeline("GoToLIft_Advanced_Succeeded");
				return succeeded;
			}
	}