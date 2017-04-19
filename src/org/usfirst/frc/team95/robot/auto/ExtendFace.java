package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.RobotMap;

public class ExtendFace extends Auto{
	
	boolean done = false;
	
	@Override
	public void init() {
		done = false;
	}

	@Override
	public void start() {
		RobotMap.pushFaceOut.set(true);
	}

	@Override
	public void update() {
		done = true;
	}

	@Override
	public void stop() {
		
	}

	@Override
	public boolean isDone() {
		return done;
	}

	@Override
	public boolean succeeded() {
		return true;
	}

}
