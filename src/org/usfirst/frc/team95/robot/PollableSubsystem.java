package org.usfirst.frc.team95.robot;

public interface PollableSubsystem {
	// Called once, prior to calls to update()
	public void init();

	// Called every iteration
	public void update();

}