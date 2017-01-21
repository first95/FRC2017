package org.usfirst.frc.team95.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HeadingPreservation {
	
	double currentHeading, forward, spin;
	CompassReader compass = new CompassReader();
	public void setHeading(double angle) {
		currentHeading = compass.getHeading();
		forward = Constants.driveStick.getY();
		spin = angle - currentHeading;
		if (spin > Math.PI) {
			spin = ((Math.PI) - Math.abs(spin)) * (spin / Math.abs(spin));
		}
		spin *= (.85);
		SmartDashboard.putNumber("Spin", spin);
		SmartDashboard.putNumber("Angle pres", angle);
		RobotMap.drive.arcade(forward, -spin);
		
	}
}
