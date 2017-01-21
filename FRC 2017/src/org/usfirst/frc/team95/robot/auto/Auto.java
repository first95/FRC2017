package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.PollableSubsystem;

public abstract class Auto implements PollableSubsystem {

	public abstract void init();

	public abstract void update();

	public abstract void stop();

	public abstract boolean done();

}