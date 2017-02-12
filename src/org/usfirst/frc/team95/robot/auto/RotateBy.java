package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.ADIS16448_IMU;
import org.usfirst.frc.team95.robot.Constants;
import org.usfirst.frc.team95.robot.HeadingPreservation;
import org.usfirst.frc.team95.robot.RobotMap;
import org.usfirst.frc.team95.robot.VariableStore;

public class RotateBy extends Auto {
	double angle, distance, time;
	HeadingPreservation spinner;
	ADIS16448_IMU compass;
	boolean done = false;

	public RotateBy(double angle, ADIS16448_IMU compass2) {
		this.angle = angle;
		compass = compass2;
		spinner = new HeadingPreservation(compass);
		
		// System.out.println(time);

		// move * RPM
	}

	@Override
	public void init() {
		if (RobotMap.driveLock == this || RobotMap.driveLock == null) {
			RobotMap.driveLock = this;
			//RobotMap.drive.tank(Constants.autonomousRotateSpeed * -sign(distance), 0);
			spinner.setHeading(angle + compass.getHeading());
		}
	}
	
	@Override
	public void start() {
	}
	
	@Override
	public void update() {
		// System.out.println("Time: " + time);
		// System.out.println("Angle: " + angle);
		// System.out.println("Distance: " + distance);
		if ((RobotMap.driveLock == this || RobotMap.driveLock == null) && !done) {
			RobotMap.driveLock = this;
			if (compass.getHeading() == angle) {
				done = true;
				RobotMap.driveLock = null;
				RobotMap.drive.tank(0, 0);
			} else {
				spinner.setHeading(angle + compass.getHeading());
			}
		}

	}

	@Override
	public void stop() {
		if (RobotMap.driveLock == null || RobotMap.driveLock == this) {
			RobotMap.drive.tank(0, 0);
		}

	}

	@Override
	public boolean done() {
		return done;
	}

	double sign(double a) {
		return a < 0 ? -1 : 1;
	}

}