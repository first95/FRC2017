package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.Constants;
import org.usfirst.frc.team95.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class DistanceMovePID extends Auto {
	double left, right, distanceL, errorL, prevErrorL, sumL, slopeL, P, I, D, distanceR, errorR, 
	prevErrorR, sumR, slopeR, prevTime, newTime, prevSpeed;
	Timer timer = new Timer();
	boolean done = false;

	public DistanceMovePID(double P, double distance) {
		this.distanceL = distance;
		this.distanceR = distance;
		this.P = P;
	}

	@Override
	public void init() {
		if (RobotMap.driveLock == this || RobotMap.driveLock == null) {
			RobotMap.driveLock = this;
		}
		//System.out.println("in Init");
		//P = 1;
		I = 0;
		D = 0;
		sumL = 0;
		sumR = 0;
		prevTime = 0;
		prevSpeed = 0;
		distanceL += (RobotMap.left1.getEncPosition() / Constants.encoderTickPerFoot);
		distanceR += (RobotMap.right1.getEncPosition() / Constants.encoderTickPerFoot);
		P /= distanceL;
		timer.start();
	}
	
	@Override
	public void start() {
		
	}
	
	@Override
	public void update() {
		//System.out.println("in update");
		SmartDashboard.putDouble("errorL", errorL);
		//SmartDashboard.putDouble("errorR", errorR);
		errorL = distanceL - (RobotMap.left1.getEncPosition() / Constants.encoderTickPerFoot);
		//errorR = distanceR - (RobotMap.right1.getEncPosition() / Constants.encoderTickPerFoot);
		
		left = P * errorL;
		//right = P * errorR;
		
		newTime = timer.get();
		sumL += (errorL * (newTime - prevTime));
		//sumR += (errorR * (newTime - prevTime));
		
		slopeL = ((errorL - prevErrorL) / (newTime - prevTime));
		//slopeR = ((errorR - prevErrorR) / (newTime - prevTime));
		prevTime = timer.get();
		
		left += I * sumL;
		//right += I * sumR;
		
		left += D * slopeL;
		//right += D * slopeR;
		
		if (left > .3) {
			left = .3;
		} else if (left < -.3) {
			left = -.3;
		}
		/*if (right > 1) {
			right = 1;
		} else if (right < -1) {
			right = -1;
		}*/
		
		if (left > (prevSpeed + .025)) {
			left = prevSpeed +.025;
		}
		right = left;
		RobotMap.drive.tank(-left, -right);
		
		prevErrorL = errorL;
		//prevErrorR = errorR;
		prevSpeed = left;
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