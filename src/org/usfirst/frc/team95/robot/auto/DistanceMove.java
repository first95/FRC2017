package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.Constants;
import org.usfirst.frc.team95.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;

public class DistanceMove extends Auto {
	double left, right, distance, ramp, rampRate, minSpeed, speed;
	boolean done = false;

	public DistanceMove(double left, double right, double distance) {
		this.left = left;
		this.right = right;
		this.distance = distance;
		if (left > 0) {
			minSpeed = .15;
		} else {
		minSpeed = -.15;
		}
	}

	@Override
	public void init() {
		if (RobotMap.driveLock == this || RobotMap.driveLock == null) {
			RobotMap.driveLock = this;
			if (left > 0) {
				minSpeed = .15;
			} else {
			minSpeed = -.15;
			}
			speed = minSpeed;
			rampRate = .05;
			ramp = distance * .05;
			ramp += (RobotMap.left1.getEncPosition() / Constants.encoderTickPerFoot);
			RobotMap.drive.tank(-minSpeed, -minSpeed);
			distance += (RobotMap.left1.getEncPosition() / Constants.encoderTickPerFoot);
		}
	}

	@Override
	public void update() {
		// System.out.println("Update!");
		if ((RobotMap.left1.getEncPosition() / Constants.encoderTickPerFoot) <= ramp) {
			RobotMap.drive.tank(-speed, -speed);
			speed += rampRate;
		}else if ((RobotMap.left1.getEncPosition() / Constants.encoderTickPerFoot) >= (distance - ramp)){
			RobotMap.drive.tank(-speed, -speed);
			speed += rampRate;
		} else {
			speed = left;
			RobotMap.drive.tank(-speed, -speed);
		}
		if ((RobotMap.left1.getEncPosition() / Constants.encoderTickPerFoot) >= distance) {
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