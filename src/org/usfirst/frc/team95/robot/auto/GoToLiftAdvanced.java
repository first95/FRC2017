package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.RobotMap;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GoToLiftAdvanced extends Auto {

	boolean done = false;
	boolean succeeded = false;

	double lastError;

	// Max and Min error that code will take before turning
	// If Less than -.5 rotate, if greater than .5, rotate.
	private static final double MAX_DEAD_BAND = 0.5;
	private static final double MIN_DEAD_BAND = -0.5;

	private static final double MAX_ROTATE_THROTTLE = -0.4;
	private static final double MAX_DRIVE_THROTTLE = -0.2;
	private static final double MAX_DRIVE_THROTTLE_WHILE_TURNING = -0.2;

	private int checkBeforeFail = 0;

	@Override
	public void init() {
		System.out.println("vision init");
	}

	@Override
	public void start() {
		System.out.println("vision start");
		RobotMap.visionProcessingInit();

		done = false;
		succeeded = false;

		// Huge Number to confirm that it will work
		lastError = 5000;

		if (RobotMap.driveLock == this || RobotMap.driveLock == null) {
			RobotMap.driveLock = this;
			RobotMap.drive.arcade(MAX_DRIVE_THROTTLE, 0);
		}
	}

	@Override
	public void update() {
		// System.out.println("vision update");
		RobotMap.gearLiftFinder.computeHeadingToTarget();

		// Should be printed by SmartDashboard
		SmartDashboard.putNumber("Degree Offset (X)", RobotMap.gearLiftFinder.getHeadingToTargetDegrees());
		SmartDashboard.putBoolean("We can see the target", RobotMap.gearLiftFinder.haveValidHeading());

		if (RobotMap.gearLiftFinder.haveValidHeading()) {
			System.out.println("----SEE IT-------------------------");
			System.out.println("We can see the target");
			System.out.println("Last Distance Gathered " + RobotMap.gearLiftFinder.getDistanceFromCamToTarget());
			System.out.println("Heading to target in degress " + RobotMap.gearLiftFinder.getHeadingToTargetDegrees());
			System.out.println("----------------------------------");

		} else {
			System.out.println("----DON'T SEE IT-------------------");
			System.out.println("We can't see the target, the heading is in valid!");
			System.out.println("Last Distance Gathered " + RobotMap.gearLiftFinder.getDistanceFromCamToTarget());
			System.out.println("Heading to target in degress " + RobotMap.gearLiftFinder.getHeadingToTargetDegrees());
			System.out.println("----------------------------------");
		}

		double headingError = RobotMap.gearLiftFinder.getHeadingToTargetDegrees();

		
		// We flipped cam, this means degree is backwards, so a negative was added to the 25 to compensate.
		if (headingError > MAX_DEAD_BAND || headingError < MIN_DEAD_BAND) {
			RobotMap.drive.arcade(MAX_DRIVE_THROTTLE_WHILE_TURNING, (MAX_ROTATE_THROTTLE * headingError) / -25);
		} else {
			RobotMap.drive.arcade(MAX_DRIVE_THROTTLE, 0);
		}

		// See if the vision heading is wrong, check 3 times and if it is still
		// wrong, stop and don't poop gear
		if (!(RobotMap.gearLiftFinder.haveValidHeading())) {
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
			if (checkBeforeFail >= 3) {
				done = true;

			}

		}else {
			lastError = RobotMap.gearLiftFinder.getHeadingToTargetDegrees();
		}
	}

	@Override
	public void stop() {
		RobotMap.drive.arcade(0, 0);
		RobotMap.driveLock = null;

		// Call that deactivates vision so we don't get lag
		RobotMap.stopVisionProcessing();
		// System.out.println("vision stop");
	}

	@Override
	public boolean isDone() {
		return done;
	}

	@Override
	public boolean succeeded() {
		return succeeded;
	}

}
