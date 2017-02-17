package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.ADIS16448_IMU;
import org.usfirst.frc.team95.robot.RobotMap;
import org.usfirst.frc.team95.robot.VisualGearLiftFinder;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AtLiftRotate extends Auto
	{

		ADIS16448_IMU compass;

		public AtLiftRotate(ADIS16448_IMU compass2)
			{
				compass = compass2;
			}

		@Override
		public void init()
			{
				// Ignore this, I wanted to put something here
				Boolean on = true;
			}

		@Override
		public void start()
			{

			}

		@Override
		public void update()
			{

				

				SmartDashboard.putNumber("Degree Offset (X)", RobotMap.gearLiftFinder.getHeadingToTargetDegrees());
				SmartDashboard.putBoolean("We can see the target", RobotMap.gearLiftFinder.haveValidHeading());
				//

				if (RobotMap.gearLiftFinder.getHeadingToTargetDegrees() > 2 || RobotMap.gearLiftFinder.getHeadingToTargetDegrees() < -2)
					{
						RotateBy rb = new RotateBy(RobotMap.gearLiftFinder.getHeadingToTargetRadians(), compass);
					}
				else
					{
						stop();
					}

			}

		@Override
		public void stop()
			{

			}

		@Override
		public boolean done()
			{
				return false;
			}

	}
