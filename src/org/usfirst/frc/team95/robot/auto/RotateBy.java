package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.ADIS16448_IMU;
import org.usfirst.frc.team95.robot.Constants;
import org.usfirst.frc.team95.robot.HeadingPreservation;
import org.usfirst.frc.team95.robot.RobotMap;
import org.usfirst.frc.team95.robot.VariableStore;

public class RotateBy extends Auto {
	double angle, distance, compValToHead;
	HeadingPreservation spinner;
	ADIS16448_IMU compass;
	boolean done = false;

	public RotateBy(double angle, ADIS16448_IMU poseidon) {
		this.angle = angle;
		compass = poseidon;
		spinner = new HeadingPreservation(compass);
		
		// System.out.println(time);

		// move * RPM
	}

	@Override
	public void init() {
		System.out.println("rotate init");
	}
	
	@Override
	public void start() {
		if (RobotMap.driveLock == this || RobotMap.driveLock == null) {
		RobotMap.driveLock = this;
		}
		spinner.setHeading(compValToHead);
		System.out.println("rotate start");
		compValToHead = compass.getHeading() + angle;
		if (compValToHead > Math.PI) {
			compValToHead = -(compValToHead % Math.PI);
		}
	}
	
	@Override
	public void update() {
		if ((RobotMap.driveLock == this || RobotMap.driveLock == null) && !done) {
			RobotMap.driveLock = this;
			if ((compValToHead) < (Math.PI / 18)) {
				done = true;
				RobotMap.driveLock = null;
				RobotMap.drive.tank(0, 0);
			} else {
				spinner.setHeading(compValToHead);
			}
		}
		System.out.println("rotate update");
	}

	@Override
	public void stop() {
		if (RobotMap.driveLock == null || RobotMap.driveLock == this) {
			RobotMap.drive.tank(0, 0);
		}
		System.out.println("rotate stop");
	}

	@Override
	public boolean isDone() {
		return done;
	}

	double sign(double a) {
		return a < 0 ? -1 : 1;
	}

	@Override
	public boolean succeeded() {
		// TODO Auto-generated method stub
		return true;
	}

}