package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.ADIS16448_IMU;
import org.usfirst.frc.team95.robot.Constants;
import org.usfirst.frc.team95.robot.RobotMap;

public class ApproachPath extends Auto {
	
	boolean done = false;
	double idealToRobo, roboToNorth, roboToPeg, idealToNorth, startHeading, north, idealHeading, perpIdeal;
	ADIS16448_IMU compass;
	public ApproachPath(ADIS16448_IMU poseidon, double idealHeading) {
		this.compass = poseidon;
		this.idealHeading = idealHeading;
	}
	
	@Override
	public void init() {
		done = false;
		north = Constants.poseidonNorthVal;
	}

	@Override
	public void start() {
		startHeading = compass.getHeading();
		roboToNorth = north - startHeading;
		roboToPeg = RobotMap.gearLiftFinder.getHeadingToTargetDegrees() * (Math.PI/180);
		idealToNorth = idealHeading - north;
		idealToRobo = roboToNorth + idealToNorth - roboToPeg;
		perpIdeal = idealHeading + Math.PI/2;
	}

	@Override
	public void update() {
		
	}

	@Override
	public void stop() {
		
	}

	@Override
	public boolean done() {
		return done;
	}

}
