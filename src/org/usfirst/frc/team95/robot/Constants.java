package org.usfirst.frc.team95.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Constants
	{
		
		public final static double MAX_FLOOR_INTAKE_CURRENT = 2;
		public final static double JOYSTICK_DEADBAND_V = 0.07;
		public final static double JOYSTICK_HEADBAND_H = 0.05;
		public final static Joystick DRIVE_STICK = new Joystick(0);
		public final static Joystick WEAPON_STICK = new Joystick(1);
		public final static double POSEIDON_NORTH_VAL = 2.751;
		public final static double ENCODER_TICKS_PER_FOOT = 1002;
		public final static double ENCODER_TICKS_PER_RADIAN = 1637.39265;//1100
		public final static double ROBOT_WIDTH = 34.7;
		public final static double VISION_LENGTH = 3;
		public final static double FLOOR_INTAKE_THROTTLE = 0.7;

		public final static double RFVoltsToFt(double voltage)
			{
				double distance;
				// Sonar Range finder based on data sheet almost acurrate
				// distance = (voltage * 100) / .977; //mV to mm

				// sonar based on experimentation (in cm)
				distance = (voltage * 107.96) - 3.0219;

				// convert from cm to ft
				distance = distance * 0.0328084;
				return distance;
			}
	}