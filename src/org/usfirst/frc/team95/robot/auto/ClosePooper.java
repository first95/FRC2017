package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.RobotMap;

public class ClosePooper extends Auto
	{
		boolean done = false;

		public void init()
			{
				
				RobotMap.sL.SystemLoggerWriteTimeline("Close_Pooper_Init");
				done = false;
			}

		public void start()
			{
				RobotMap.sL.SystemLoggerWriteTimeline("Close_Pooper_Start");
				RobotMap.gearPooper.set(false);
			}

		public void update()
			{
				done = true;
			}

		public void stop()
			{
				RobotMap.sL.SystemLoggerWriteTimeline("Close_Pooper_Stop");
			}

		public boolean isDone()
			{
				return done;
			}

		@Override
		public boolean succeeded()
			{
				RobotMap.sL.SystemLoggerWriteTimeline("Close_Pooper_Succeeded");
				return true;
			}
	}