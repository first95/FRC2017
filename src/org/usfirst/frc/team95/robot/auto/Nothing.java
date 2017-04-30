package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.RobotMap;

public class Nothing extends Auto
	{
		@Override
		public void init()
			{
				RobotMap.sL.SystemLoggerWriteTimeline("Nothing_Init");
			}

		@Override
		public void start()
			{
				RobotMap.sL.SystemLoggerWriteTimeline("Nothing_Start");
			}

		@Override
		public void update()
			{

			}

		@Override
		public void stop()
			{
				RobotMap.sL.SystemLoggerWriteTimeline("Nothing_Stop");
			}

		@Override
		public boolean isDone()
			{
				return true;
			}

		@Override
		public boolean succeeded()
			{
				RobotMap.sL.SystemLoggerWriteTimeline("Nothing_Succeeded");
				return true;
			}
	}