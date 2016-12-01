package org.usfirst.frc.team95.robot;

import edu.wpi.first.wpilibj.RobotDrive;

public final class Drive {

	public static void arcade(RobotDrive drive, double throttle, double turn) {
        double left = throttle + turn;
        double right = throttle - turn;
        drive.tankDrive(left, right);
    }
	private Drive(){
		
	}
}
