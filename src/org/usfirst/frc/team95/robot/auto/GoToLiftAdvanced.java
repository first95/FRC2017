package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.ADIS16448_IMU;
import org.usfirst.frc.team95.robot.RangeFinder;
import org.usfirst.frc.team95.robot.RobotMap;
import org.usfirst.frc.team95.robot.VisualGearLiftFinder;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GoToLiftAdvanced extends Auto
	{
		private RangeFinder rangeFinder1;
		private VisualGearLiftFinder gearLiftFinder = null;
		private CvSource smartDashboardVideoOutput = null;
		private UsbCamera myCam = null;
		private ADIS16448_IMU compass;
		private static final double MAX_ROTATE_THROTTLE = 0.5;
		private static final double MAX_DRIVE_THROTTLE = 0.5;
		private static final double MAX_DRIVE_THROTTLE_WHILE_TURNING = 0.1;

		public GoToLiftAdvanced(RangeFinder rangeFinder)
			{
				rangeFinder1 = rangeFinder;
			}

		@Override
		public void init()
			{

				myCam = CameraServer.getInstance().startAutomaticCapture();
				myCam.setResolution(640, 480);
				myCam.setExposureManual(20);
				CvSink cvSink = CameraServer.getInstance().getVideo();
				gearLiftFinder = new VisualGearLiftFinder(cvSink);
				smartDashboardVideoOutput = CameraServer.getInstance().putVideo("Debug", 640, 480);

				if (RobotMap.driveLock == this || RobotMap.driveLock == null)
					{
						RobotMap.driveLock = this;
						RobotMap.drive.arcade(MAX_DRIVE_THROTTLE, 0);
					}

			}

		@Override
		public void start()
			{

			}

		@Override
		public void update()
			{

				gearLiftFinder.computeHeadingToTarget();
				smartDashboardVideoOutput.putFrame(gearLiftFinder.getAnnotatedFrame());

				SmartDashboard.putNumber("Degree Offset (X)", gearLiftFinder.getHeadingToTargetDegrees());
				SmartDashboard.putBoolean("We can see the target", gearLiftFinder.haveValidHeading());
				//

				if (gearLiftFinder.getHeadingToTargetDegrees() > 2)
					{
						RobotMap.drive.arcade(MAX_DRIVE_THROTTLE_WHILE_TURNING, (MAX_ROTATE_THROTTLE * gearLiftFinder.getHeadingToTargetDegrees()) / 25);
					}
				else if (gearLiftFinder.getHeadingToTargetDegrees() < -2)
					{
						RobotMap.drive.arcade(MAX_DRIVE_THROTTLE_WHILE_TURNING, ((MAX_ROTATE_THROTTLE * gearLiftFinder.getHeadingToTargetDegrees()) / 25) * -1);
					}

				if (rangeFinder1.getRangeInFeet() <= 1)
					{
						stop();
					}

			}

		@Override
		public void stop()
			{
				RobotMap.drive.arcade(0, 0);
			}

		@Override
		public boolean done()
			{
				return rangeFinder1.getRangeInFeet() <= 1;
			}

	}
