package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.Constants;
import org.usfirst.frc.team95.robot.RobotMap;


public class DistanceMovePID extends Auto {
	double left, right, distance, ramp, error, speed, P, I, D;
	boolean done = false;

	public DistanceMovePID(double left, double right, double distance) {
		this.left = left;
		this.right = right;
		this.distance = distance;
		
	}

	@Override
	public void init() {
		if (RobotMap.driveLock == this || RobotMap.driveLock == null) {
			RobotMap.driveLock = this;
			distance += (RobotMap.left1.getEncPosition() / Constants.encoderTickPerFoot);
		}
	}
	
	@Override
	public void start() {
	}
	
	@Override
	public void update() {
		error = distance - (RobotMap.left1.getEncPosition() / Constants.encoderTickPerFoot);
		
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