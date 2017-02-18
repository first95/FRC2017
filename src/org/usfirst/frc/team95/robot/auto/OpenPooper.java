package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;

public class OpenPooper extends Auto{

	boolean done = false;
	double time;
	Timer timer = new Timer();
	public OpenPooper(double time) {// milliseconds
		this.time = time;
	}
	public void init() {
		
	}

	public void start() {
		
		timer.reset();
		timer.start();
		
	}

	public void update() {
		
		while (timer.get() < time) {
			RobotMap.gearPooper.set(true);
		}
		
		done = true;
	}

	public void stop() {
		
	}

	public boolean done() {
		return done;
	}

}
