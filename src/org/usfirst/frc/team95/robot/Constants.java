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
		
		// Constants
		public final static double BOIL_SIDE_DIST1 = (69.68) / 12;
		public final static double BOIL_SIDE_DIST2 = (67.34 / 12) - 3;
		public final static double BOIL_SIDE_DIST1_STAGE_ALT = (-70 / 12);
		public final static double BOIL_SIDE_DIST2_STAGE_ALT = (-67.34 / 12);
		public final static double CENTER_DIST1 = ((110.517 - (Constants.ROBOT_WIDTH / 2)) / 12) - 6;
		public final static double CENTER_DIST2 = 0.0;
		public final static double HOPPER_SIDE_DIST1 = (70.94 / 12);
		public final static double HOPPER_SIDE_DIST2 = (65.06 / 12) - 3;
		public final static double ROTATE_LEFT = (-60 * (Math.PI / 180));
		public final static double ROTATE_RIGHT = 70 * (Math.PI / 180);
		public final static double ROTATE_LEFT_STAGE_ALT = (-20 * (Math.PI / 180));
		public final static double ROTATE_NONE = 0.0;
		
		public final static double HOPPER_TIME1 = 1.7;
		public final static double HOPPER_TIME2 = .7;
		public final static double BOIL_TIME1 = 1.7;
		public final static double BOIL_TIME2 = .7;
		public final static double CENTER_TIME1 = .7;
		public final static double CENTER_TIME2 = 0;
		public final static double ROTATE_TIME = 1.15;
}