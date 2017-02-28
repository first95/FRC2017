package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.ADIS16448_IMU;
import org.usfirst.frc.team95.robot.Constants;
import org.usfirst.frc.team95.robot.HeadingPreservation;
import org.usfirst.frc.team95.robot.RobotMap;
import org.usfirst.frc.team95.robot.VariableStore;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RotateBy extends Auto {
	double angle, start, desired, error, P, speed, prevSpeed;
	boolean done = false;

	public RotateBy(double angle) {
		this.angle = angle;
		
	}

	@Override
	public void init() {
		P = .35;
	}
	
	@Override
	public void start() {
		if (RobotMap.driveLock == this || RobotMap.driveLock == null) {
		RobotMap.driveLock = this;
		}
		start = RobotMap.left1.getEncPosition();
		desired = start + (Constants.encTicksPerRadian * angle);
		prevSpeed = 0;
		error = desired - RobotMap.left1.getEncPosition();
		speed = P * error;
		done = false;
	}
	
	@Override
	public void update() {
		if ((RobotMap.driveLock == this || RobotMap.driveLock == null) && !done) {
			RobotMap.driveLock = this;
			if (error <= .087) {//5 degrees
				done = true;
			}else {
				error = desired - RobotMap.left1.getEncPosition();
				speed = P * error;
				System.out.println("else");
				if (speed > .4) {
					speed = .4;
				} else if (speed < -.4) {
					speed = -.4;
				}
				
				if (speed > (prevSpeed + .08)) {
					speed = prevSpeed +.08;
				}
				
				RobotMap.drive.tank(-speed, speed);
				
				prevSpeed = speed;
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
	public boolean isDone() {
		return done;
	}

	double sign(double a) {
		return a < 0 ? -1 : 1;
	}

	@Override
	public boolean succeeded() {
		return true;
	}

}