package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.RobotMap;

public class ExtendFace extends Auto{
	
	boolean done = false;
	
	@Override
	public void init() {
		RobotMap.sL.SystemLoggerWriteTimeline("Extend_Face_Init");
		done = false;
	}

	@Override
	public void start() {
		RobotMap.sL.SystemLoggerWriteTimeline("Extend_Face_Start");
		RobotMap.pushFaceOut.set(true);
	}

	@Override
	public void update() {
		done = true;
	}

	@Override
	public void stop() {
		RobotMap.sL.SystemLoggerWriteTimeline("Extend_Face_Stop");
	}

	@Override
	public boolean isDone() {
		return done;
	}

	@Override
	public boolean succeeded() {
		RobotMap.sL.SystemLoggerWriteTimeline("Extend_Face_Succeeded");
		return true;
	}

}
