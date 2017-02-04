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
    public static CANTalon left1, left2, left3, right1, right2, right3, winchLeft, winchRight;
	public static Drive drive;
	// Autonomous moves wishing to control the robot's drive base
	// should set the driveLock object to "this" (that is, themselves).
	// They should also check that the driveLock object is null prior to
	// controlling the drive motors.
	public static Object driveLock = null;
	public static Solenoid gearPooper, gearMouth, pushFaceOut;
    
    public static void init() {
		// drive motors
		left1 = new CANTalon(1);
		left2 = new CANTalon(2);
		left3 = new CANTalon(3);
		right1 = new CANTalon(4);
		right2 = new CANTalon(5);
		right3 = new CANTalon(6);
		winchRight = new CANTalon(7);
		winchLeft = new CANTalon(8);
		drive = new Drive(left1, right1);
		left2.changeControlMode(CANTalon.TalonControlMode.Follower);
		left2.set(1);
		left3.changeControlMode(CANTalon.TalonControlMode.Follower);
		left3.set(1);
		right2.changeControlMode(CANTalon.TalonControlMode.Follower);
		right2.set(4);
		right3.changeControlMode(CANTalon.TalonControlMode.Follower);
		right3.set(4);
		winchLeft.changeControlMode(CANTalon.TalonControlMode.Follower);
		winchLeft.set(7);
		winchLeft.setInverted(true);
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
