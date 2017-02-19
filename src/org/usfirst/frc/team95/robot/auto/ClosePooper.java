package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.RobotMap;

public class ClosePooper extends Auto {

	boolean done = false;
	
	public void init() {
		done = false;

	}

	public void start() {
		RobotMap.gearPooper.set(false);
	}

	public void update() {
		done = true;
	}

	public void stop() {

	}

	public boolean done() {
		return done;
	}

}
