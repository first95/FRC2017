package org.usfirst.frc.team95.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;

public class Drive
	{

		private SpeedController mLeft;
		private SpeedController mRight;

		public Drive(SpeedController left, SpeedController right)
			{
				mLeft = left;
				mRight = right;
				
				//RobotMap.sL.SystemLoggerWriteRAW("Drive_Started");
			}

		public void tank(double leftsp, double rightsp)
			{
				mLeft.set(-leftsp);
				mRight.set(rightsp);
			}

		public void arcade(double forward, double spin)
			{
				tank(forward - spin, forward + spin);
			}

		public void halfArcade(double forward, double spin)
			{
				tank((forward - spin) / 2, (forward + spin) / 2);
			}

		public void arcade(Joystick stick, boolean twostick)
			{
				double y = stick.getY();
				double x;
				if (twostick)
					{
						x = stick.getRawAxis(4);
					}
				else
					{
						x = stick.getX();
					}

				if (Math.abs(y) <= Constants.JOYSTICK_DEADBAND_V)
					{
						y = 0;
					}

				if (Math.abs(x) <= Constants.JOYSTICK_HEADBAND_H)
					{
						x = 0;
					}

				// "Exponential" drive, where the movements are more sensitive during slow movement,
				// permitting easier fine control
				x = Math.pow(x, 3);
				y = Math.pow(y, 3);
				arcade(y, x);
			}

		public void halfArcade(Joystick stick, boolean twostick)
			{
				double y = stick.getY();
				double x;
				if (twostick)
					{
						x = stick.getRawAxis(4);
					}
				else
					{
						x = stick.getX();
					}
				if (Math.abs(y) <= Constants.JOYSTICK_DEADBAND_V)
					{
						y = 0;
					}

				if (Math.abs(x) <= Constants.JOYSTICK_HEADBAND_H)
					{
						x = 0;
					}

				// "Exponential" drive, where the movements are more sensitive during slow movement,
				// permitting easier fine control
				x = Math.pow(x, 3);
				y = Math.pow(y, 3);
				halfArcade(y, x);
			}
	}