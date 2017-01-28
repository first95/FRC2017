package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;

public class TimedMove extends Auto {
	Timer timer = new Timer();
	double left, right, time;
	boolean done = false;

	public TimedMove(double left, double right, double time) {
		this.left = left;
		this.right = right;
		this.time = time;
	}

	@Override
	public void init() {
		timer.reset();
		timer.start();
		if (RobotMap.driveLock == this || RobotMap.driveLock == null) {
			RobotMap.driveLock = this;
			RobotMap.drive.tank(left, right);
		}
	}

	@Override
	public void update() {
		// System.out.println("Update!");
		if (timer.get() > time) {
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