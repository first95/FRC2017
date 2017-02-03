package org.usfirst.frc.team95.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Solenoid;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    // For example to map the left and right motors, you could define the
    // following variables to use with your drivetrain subsystem.
    public static CANTalon left1, left2, left3, right1, right2, right3;
	public static Drive drive;
	public static Object driveLock = null;
	public static Solenoid gearPooper, gearMouth, pushFaceOut;
    // If you are using multiple modules, make sure to define both the port
    // number and the module. For example you with a rangefinder:
    // public static int rangefinderPort = 1;
    // public static int rangefinderModule = 1;
    
    public static void init() {
		// drive motors
		left1 = new CANTalon(1);
		left2 = new CANTalon(2);
		left3 = new CANTalon(3);
		right1 = new CANTalon(4);
		right2 = new CANTalon(5);
		right3 = new CANTalon(6);
		drive = new Drive(left1, right1);
		left2.changeControlMode(CANTalon.TalonControlMode.Follower);
		left2.set(1);
		left3.changeControlMode(CANTalon.TalonControlMode.Follower);
		left3.set(1);
		right2.changeControlMode(CANTalon.TalonControlMode.Follower);
		right2.set(4);
		right3.changeControlMode(CANTalon.TalonControlMode.Follower);
		right3.set(4);
		gearPooper = new Solenoid(1);
		gearMouth = new Solenoid(2);
		pushFaceOut = new Solenoid(3);
		
		left1.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Absolute);
		left2.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Absolute);
		left3.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Absolute);
		right1.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Absolute);
		right2.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Absolute);
		right3.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Absolute);
    }
}
