package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;

public class ClosePooper extends Auto
	{

		boolean done = false;

		public void init()
			{
				System.out.println("CLOSEPOOP Init");
				done = false;

			}

		public void start()
			{
				System.out.println("CLOSEPOOP Start");

				RobotMap.gearPooper.set(false);
			}

		public void update()
			{

				done = true;

			}

		public void stop()
			{
				System.out.println("CLOSEPOOP Stop");
			}

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
