package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.ADIS16448_IMU;
import org.usfirst.frc.team95.robot.RobotMap;

public class ApproachPath extends Auto {
	
	boolean done = false;
	double idealToRobo, roboToNorth, roboToPeg, idealToNorth, heading, north, idealHeading;
	ADIS16448_IMU compass;
	public ApproachPath(ADIS16448_IMU poseidon, double idealHeading) {
		this.compass = poseidon;
		this.idealHeading = idealHeading;
	}
	
	@Override
	public void init() {
		done = false;
		north = Math.PI;
	}

	@Override
	public void start() {
	}

	@Override
	public void update() {
		heading = compass.getHeading();
		roboToNorth = north - heading;
		roboToPeg = RobotMap.gearLiftFinder.getHeadingToTargetDegrees() * (Math.PI/180);
		idealToNorth = idealHeading - north;
	}

	@Override
	public void stop() {
		
	}

	@Override
	public boolean done() {
		return done;
	}

}
