package org.usfirst.frc.team95.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HeadingPreservation
	{
		double currentHeading, forward, spin;
		ADIS16448_IMU m_compass;

		/*
		 * public HeadingPreservation(CompassReader compass) { m_compass = compass; }
		 */

		public HeadingPreservation(ADIS16448_IMU poseidon)
			{
				m_compass = poseidon;
			}

		public void setHeading(double angle)
			{
				currentHeading = m_compass.getHeading();
				forward = 0;
				spin = angle - currentHeading;
				
				if (spin > Math.PI)
					{
						spin = (spin - (Math.PI * 2));
					}
				else if (spin < -Math.PI)
					{
						spin = (spin - (-Math.PI * 2));
					}
				
				spin *= (.3);
				SmartDashboard.putNumber("Spin", spin);
				SmartDashboard.putNumber("Angle pres", angle);
				RobotMap.drive.arcade(forward, -spin);
			}
	}