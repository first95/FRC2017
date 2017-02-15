package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.ADIS16448_IMU;
import org.usfirst.frc.team95.robot.VisualGearLiftFinder;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AtLiftRotate extends Auto
	{

		// TODO: These eventually belong inside an auto move
		VisualGearLiftFinder gearLiftFinder = null;
		CvSource smartDashboardVideoOutput = null;
		UsbCamera myCam = null;
		ADIS16448_IMU compass;

		public AtLiftRotate(ADIS16448_IMU compass2)
			{
				compass = compass2;
			}

		@Override
		public void init()
			{
				// Vision Stuff

				// TODO: this eventually belongs inside an auto move
				myCam = CameraServer.getInstance().startAutomaticCapture();
				// myCam.setResolution(1280, 720); // Doesn't seem to work
				myCam.setResolution(640, 480);
				// myCam.setBrightness(10);
				myCam.setExposureManual(20);
				CvSink cvSink = CameraServer.getInstance().getVideo();
				gearLiftFinder = new VisualGearLiftFinder(cvSink);
				smartDashboardVideoOutput = CameraServer.getInstance().putVideo("Debug", 640, 480);
				// smartDashboardVideoOutput = CameraServer.getInstance().putVideo("Debug", 640, 360);
			}

		@Override
		public void start()
			{

			}

		@Override
		public void update()
			{
				// Test Stuff For Vision
				// TODO: this eventually belongs inside an auto move
				gearLiftFinder.computeHeadingToTarget();
				smartDashboardVideoOutput.putFrame(gearLiftFinder.getAnnotatedFrame());

				SmartDashboard.putNumber("Degree Offset (X)", gearLiftFinder.getHeadingToTargetDegrees());
				SmartDashboard.putBoolean("We can see the target", gearLiftFinder.haveValidHeading());
				//

				if (gearLiftFinder.getHeadingToTargetDegrees() > 2 || gearLiftFinder.getHeadingToTargetDegrees() < -2)
					{
						RotateBy rb = new RotateBy(gearLiftFinder.getHeadingToTargetRadians(), compass);
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
