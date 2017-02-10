package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.VisionDisplay;
import org.usfirst.frc.team95.robot.VisionGatherDistanceAndOther;

public class FindGearHolder extends Auto
	{

		@Override
		public void init()
			{

				for (int i = 0; i < 50; i++)
					{
						new VisionDisplay();
					}
				
				//new RotateBy((VisionGatherDistanceAndOther.pix2Deg) * (Math.PI / 180));

			}

		@Override
		public void update()
			{

			}

		@Override
		public void stop()
			{
				// TODO Auto-generated method stub

			}

		@Override
		public boolean done()
			{
				// TODO Auto-generated method stub
				return false;
			}

	}
