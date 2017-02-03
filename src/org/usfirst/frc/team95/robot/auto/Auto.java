package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.PollableSubsystem;

public abstract class Auto implements PollableSubsystem {

	// Called once to initialize the auto move
	public abstract void init();

	// Called each iteration
	public abstract void update();

	// Called once to stop the auto move
	public abstract void stop();

	// Make this return false until the auto move has completed its tasks
	public abstract boolean done();

}