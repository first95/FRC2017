package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.ADIS16448_IMU;
import org.usfirst.frc.team95.robot.Constants;
import org.usfirst.frc.team95.robot.HeadingPreservation;
import org.usfirst.frc.team95.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;

public class RotateBy extends Auto {
	double angle, distance, time;
	HeadingPreservation spinner;
	ADIS16448_IMU compass2;
	boolean done = false;

	public RotateBy(double angle) {
		this.angle = angle;
		compass2 = new ADIS16448_IMU(null);
		spinner = new HeadingPreservation(compass2);
		// System.out.println(time);

		// move * RPM
	}

	@Override
	public void init() {
		if (RobotMap.driveLock == this || RobotMap.driveLock == null) {
			RobotMap.driveLock = this;
			//RobotMap.drive.tank(Constants.autonomousRotateSpeed * -sign(distance), 0);
			spinner.setHeading(angle);
		}
	}

	@Override
	public void update() {
		// System.out.println("Time: " + time);
		// System.out.println("Angle: " + angle);
		// System.out.println("Distance: " + distance);
		if ((RobotMap.driveLock == this || RobotMap.driveLock == null) && !done) {
			RobotMap.driveLock = this;
			if (compass2.getHeading() == angle) {
				done = true;
				RobotMap.driveLock = null;
				RobotMap.drive.tank(0, 0);
			} else {
				spinner.setHeading(angle);
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