package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.Constants;
import org.usfirst.frc.team95.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;


public class DistanceMovePID extends Auto {
	double left, right, distanceL, errorL, prevErrorL, sumL, slopeL, P, I, D, distanceR, errorR, prevErrorR, sumR, slopeR, prevTime, newTime;
	Timer timer = new Timer();
	boolean done = false;

	public DistanceMovePID(double distance) {
		this.distanceL = distance;
		
	}

	@Override
	public void init() {
		if (RobotMap.driveLock == this || RobotMap.driveLock == null) {
			RobotMap.driveLock = this;
			distanceL += (RobotMap.left1.getEncPosition() / Constants.encoderTickPerFoot);
			distanceR += (RobotMap.right1.getEncPosition() / Constants.encoderTickPerFoot);
			P = 1;
			P /= distanceL;
			I = 0;
			D = 0;
			sumL = 0;
			sumR = 0;
			prevTime = 0;
			
			timer.start();
		}
	}
	
	@Override
	public void start() {
		
	}
	
	@Override
	public void update() {
		
		errorL = distanceL - (RobotMap.left1.getEncPosition() / Constants.encoderTickPerFoot);
		errorR = distanceR - (RobotMap.right1.getEncPosition() / Constants.encoderTickPerFoot);
		
		left = P * errorL;
		right = P * errorR;
		
		newTime = timer.get();
		sumL += (errorL * (newTime - prevTime));
		sumR += (errorR * (newTime - prevTime));
		
		slopeL = ((errorL - prevErrorL) / (newTime - prevTime));
		slopeR = ((errorR - prevErrorR) / (newTime - prevTime));
		prevTime = timer.get();
		
		left += I * sumL;
		right += I * sumR;
		
		left += D * slopeL;
		right += D * slopeR;
		
		if (left > 1) {
			left = 1;
		} else if (left < -1) {
			left = -1;
		}
		if (right > 1) {
			right = 1;
		} else if (right < -1) {
			right = -1;
		}
		
		RobotMap.drive.tank(left, right);
		
		prevErrorL = errorL;
		prevErrorR = errorR;
		
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