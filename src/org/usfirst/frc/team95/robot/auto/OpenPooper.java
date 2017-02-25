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
		done = false;
	}

	public void start() {
		timer.reset();
		timer.start();
		RobotMap.gearPooper.set(true);
		
	}

	public void update() {
		if (timer.get() > time) {
			done = true;
		}
	}

	public void stop() {
		
	}

	public boolean isDone() {
		return done;
	}
	@Override
	public boolean succeeded() {
		// TODO Auto-generated method stub
		return true;
	}

}
