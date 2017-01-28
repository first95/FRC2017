package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.Constants;
import org.usfirst.frc.team95.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;

public class RotateBy extends Auto {
	double angle, distance, time;
	Timer timer = new Timer();
	boolean done = false;

	public RotateBy(double angle) {
		this.angle = angle; // Note to self: the problem is with units
		distance = angle * Constants.robotWidth;
		time = Math.abs(distance / (Constants.wheelDiameter * Math.PI));
		time /= (Constants.timeserRPM * Constants.autonomousRotateSpeed);
		time *= 60;
		// System.out.println(time);

		// move * RPM
	}

	@Override
	public void init() {
		timer.reset();
		timer.start();
		if (RobotMap.driveLock == this || RobotMap.driveLock == null) {
			RobotMap.driveLock = this;
			RobotMap.drive.tank(Constants.autonomousRotateSpeed * -sign(distance), 0);
		}
	}

	@Override
	public void update() {
		// System.out.println("Time: " + time);
		// System.out.println("Angle: " + angle);
		// System.out.println("Distance: " + distance);
		if ((RobotMap.driveLock == this || RobotMap.driveLock == null) && !done) {
			RobotMap.driveLock = this;
			if (timer.get() > time) {
				done = true;
				RobotMap.driveLock = null;
				RobotMap.drive.tank(0, 0);
			} else {
				RobotMap.drive.tank(Constants.autonomousRotateSpeed * -sign(distance), 0);
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