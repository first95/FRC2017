package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.Constants;
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
			distance += (RobotMap.right1.getEncPosition() * Constants.encoderTickPerFoot);
		}
	}

	@Override
	public void update() {
		// System.out.println("Update!");
		if ((RobotMap.right1.getEncPosition() * Constants.encoderTickPerFoot) >= distance) {
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