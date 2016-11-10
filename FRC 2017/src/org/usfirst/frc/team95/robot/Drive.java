package org.usfirst.frc.team95.robot;

import javax.swing.text.StyleConstants.ColorConstants;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;

public class Drive {
	
	SpeedController left;
	SpeedController right;
	
	public Drive(SpeedController left, SpeedController right) {
		this.left = left;
		this.right = right;
	}
	
	public void tank(double leftsp, double rightsp) {
		left.set(leftsp);
		right.set(-rightsp);
	}
	
	public void arcade(double forward, double spin) {
		tank (forward - spin, forward + spin);
	}
	
	public void arcade(Joystick stick) {
		double y = stick.getY();
		double x = stick.getX();
		
		if (Math.abs(y) <= Constants.joystickDeadbandV) {
			y = 0;
		}
		
		if (Math.abs(x) <= Constants.joystickDeadbandH) {
			x = 0;
		}
		
		x *=Math.abs(x);
		y *=Math.abs(y);
		arcade(y,x);
		
	}
}