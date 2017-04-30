package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;

public class TimedMove extends Auto
	{
		Timer timer = new Timer();
		double left, right, time;
		boolean done = false;

		public TimedMove(double left, double right, double time)
			{
				this.left = left;
				this.right = right;
				this.time = time;
			}

		@Override
		public void init()
			{
				RobotMap.sL.SystemLoggerWriteTimeline("TimedMove_Init");
				done = false;
			}

		@Override
		public void start()
			{
				RobotMap.sL.SystemLoggerWriteTimeline("TimedMove_Start");
				timer.reset();
				timer.start();
				if (RobotMap.driveLock == this || RobotMap.driveLock == null)
					{
						RobotMap.driveLock = this;
						RobotMap.drive.tank(left, right);
					}
			}

		@Override
		public void update()
			{
				// System.out.println("Update!");
				if (timer.get() > time)
					{
						done = true;
					}
			}

		@Override
		public void stop()
			{
				RobotMap.sL.SystemLoggerWriteTimeline("TimedMove_Stop");
				if (RobotMap.driveLock == null || RobotMap.driveLock == this)
					{
						RobotMap.drive.tank(0, 0);
						RobotMap.driveLock = null;
					}
				timer.stop();
			}

		@Override
		public boolean isDone()
			{
				System.out.println("Timed Move 'isDone'");
				return done;
			}

		@Override
		public boolean succeeded()
			{
				RobotMap.sL.SystemLoggerWriteTimeline("TimedMove_Succeeded");
				System.out.println("-- Timed Moved Succeeded --");
				return true;
			}
	}