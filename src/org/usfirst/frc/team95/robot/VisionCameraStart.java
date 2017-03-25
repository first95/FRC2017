package org.usfirst.frc.team95.robot;

public class VisionCameraStart
	{

		private static Runnable threadRun = new Runnable()
			{
				@Override
				public void run()
					{
						System.out.println("---------------------------");
						System.out.println("- Camera Thread Activated -");
						System.out.println("---------------------------");
					}
			};

		private static Thread cameraStartThread = new Thread(threadRun);

		public VisionCameraStart()
			{
				System.out.println("-----------------------------");
				System.out.println("- Preparing To Start Thread -");
				System.out.println("-----------------------------");

				cameraStartThread.start();
			}

	}
