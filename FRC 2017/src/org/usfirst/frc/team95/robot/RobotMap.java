package org.usfirst.frc.team95.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	public static CANTalon left1, left2, left3, right1, right2, right3;
	public static CANTalon[] mot;
	
	public static void init(){
		//initialize speed controllers
		left1 = new CANTalon(1);
		left2 = new CANTalon(2);
		left3 = new CANTalon(3);
		right1 = new CANTalon(4);
		right2 = new CANTalon(5);
		right3 = new CANTalon(6);	
		
		//set control mode
		left1.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		right1.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		
		//set follower mode
		left2.changeControlMode(CANTalon.TalonControlMode.Follower);
		left3.changeControlMode(CANTalon.TalonControlMode.Follower);
		right2.changeControlMode(CANTalon.TalonControlMode.Follower);
		right3.changeControlMode(CANTalon.TalonControlMode.Follower);
		
		//set speed controller to follow
		left2.set(1);
		left3.set(1);
		right2.set(4);
		right3.set(4);
		
	}
	
}
