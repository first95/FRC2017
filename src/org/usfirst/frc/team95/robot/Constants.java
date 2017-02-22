package org.usfirst.frc.team95.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Constants {
	
	public static double joystickDeadbandV = 0.07;
	public static double joystickDeadbandH = 0.05;
	public static Joystick driveStick = new Joystick(0);
	public static Joystick weaponStick = new Joystick(1);
	public static double poseidonNorthVal = 2.751;
	public static double encoderTickPerFoot = 1002;
	public static double robotWidth = 34.7;
	public static double visionLength = 3;
	public static double RFVoltsToFt(double voltage) {
		
		double distance;
		//Sonar Range finder based on data sheet almost acurrate
		//distance = (voltage * 100) / .977; //mV to mm
		
		//sonar based on experimentation (in cm)
		distance = (voltage * 107.96) - 3.0219;
		
		// convert from cm to ft
		distance = distance * 0.0328084;
		return distance;
	}
}
