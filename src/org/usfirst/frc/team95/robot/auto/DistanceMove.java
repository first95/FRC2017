package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;

public class DistanceMove extends Auto {
	double left, right, distance, start;
	boolean done = false;

	public DistanceMove(double left, double right, double distance) {
		this.left = left;
		this.right = right;
		this.distance = distance;
	}

	@Override
	public void init() {
		if (RobotMap.driveLock == this || RobotMap.driveLock == null) {
			RobotMap.driveLock = this;
			RobotMap.drive.tank(left, right);
			start = RobotMap.left1.getEncPosition();
		}
	}

	@Override
	public void update() {
		// System.out.println("Update!");
		if ((start + RobotMap.left1.getEncPosition()) >= distance) {
			done = true;
			stop();
		}
	}

	@Override
	public void stop() {
		if (RobotMap.driveLock == null || RobotMap.driveLock == this) {
			RobotMap.drive.tank(0, 0);
			RobotMap.driveLock = null;
		}
	}

	@Override
	public boolean done() {
		return done;
	}

}