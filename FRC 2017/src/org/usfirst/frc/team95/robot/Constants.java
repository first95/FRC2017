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
		/*if (voltage - 0.0625 == 0) {
			return 4;
		}
		//ada's rf code
		if (voltage > 40 || voltage < 0) {
			return 40;
		}
		
		return 11.75 / (voltage - 0.0625) - 0.42;*/
		double distance; //centimeters   IR range finder
		//voltage -= 0.052271;
		//distance = 13.378 / voltage;
		//return (distance * 0.393701); //inches conversion
		
		//Sonar Range finder
		distance = (voltage * 100) / .977; //mV to mm
		return distance;
	}
}
