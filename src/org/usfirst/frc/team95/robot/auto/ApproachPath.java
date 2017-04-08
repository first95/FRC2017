package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.ADIS16448_IMU;
import org.usfirst.frc.team95.robot.Constants;
import org.usfirst.frc.team95.robot.RobotMap;

public class ApproachPath extends Auto
	{

		private boolean done = false;
		private double mIdealHeading, idealToRobo, roboToNorth, roboToPeg, idealToNorth, startHeading, north, perpIdeal;
		private ADIS16448_IMU mCompass;

		public ApproachPath(ADIS16448_IMU poseidon, double idealHeading)
			{
				mCompass = poseidon;
				mIdealHeading = idealHeading;
			}

		@Override
		public void init()
			{
				done = false;
				north = Constants.POSEIDON_NORTH_VAL;
			}

		@Override
		public void start()
			{
				startHeading = mCompass.getHeading();
				roboToNorth = north - startHeading;
				roboToPeg = RobotMap.gearLiftFinder.getHeadingToTargetDegrees() * (Math.PI / 180);
				idealToNorth = mIdealHeading - north;
				idealToRobo = roboToNorth + idealToNorth - roboToPeg;
				perpIdeal = mIdealHeading + Math.PI / 2;
			}

		@Override
		public void update()
			{

			}

		@Override
		public void stop()
			{

			}

		@Override
		public boolean isDone()
			{
				return done;
			}

		@Override
		public boolean succeeded()
			{
				return true;
			}
	}