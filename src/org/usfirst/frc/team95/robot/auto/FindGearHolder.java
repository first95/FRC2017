package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.VisionCameraSetUp;
import org.usfirst.frc.team95.robot.VisionDisplay;
import org.usfirst.frc.team95.robot.VisionGatherDistanceAndOther;

public class FindGearHolder extends Auto
	{

		@Override
		public void init()
			{
				VisionCameraSetUp cam = new VisionCameraSetUp(VisionDisplay.camera);
			}

		@Override
		public void update()
			{
				VisionCameraSetUp cam = new VisionCameraSetUp(VisionDisplay.camera);
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
