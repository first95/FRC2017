package org.usfirst.frc.team95.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Constants {
	
	public static double joystickDeadbandV = 0.07;
	public static double joystickDeadbandH = 0.05;
	public static Joystick driveStick = new Joystick(0);
	public static double robotWidth = 25; //THIS NEEDS TO BE SET
	public static double wheelDiameter = 6; //THIS NEEDS TO BE SET
	public static double timeserRPM = 430.89; //THIS NEEDS TO BE SET
	public static double autonomousRotateSpeed = .3; //THIS NEEDS TO BE SET
	public static double RFVoltsToCm(double voltage) {
		
		double distance;
		//Sonar Range finder based on data sheet almost acurrate
		//distance = (voltage * 100) / .977; //mV to mm
		
		//sonar based on expeimentation
		distance = (voltage * 107.96) - 3.0219;	
		return distance;
	}
}