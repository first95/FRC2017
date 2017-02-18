package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.RobotMap;

public class OpenPooper extends Auto{

	boolean done = false;
	
	public void init() {
		
	}

	public void start() {
		RobotMap.gearPooper.set(true);
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
