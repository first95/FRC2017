package org.usfirst.frc.team95.robot;

// Important Imports!
import org.opencv.core.Core;
import org.opencv.videoio.VideoCapture;

public class VisionDisplay
	{

		public static VideoCapture camera;
		
		public VisionDisplay()
			{

				
				
				// Import OpenCV Libraries
				System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

				// Create a new OpenCV VideoCamera
				// 0 is the bottom USB PORT on the Athena
				VideoCapture camera = new VideoCapture(0);
				VisionDisplay.camera = camera;
				
				// See if you are getting data from the camera
				if (camera.isOpened())
					{
						// If you are, print out
						System.out.println("Video is captured");
					}
				else
					{
						// If you can't get data, exit and say YOU FAILED
						System.out.println("The Camera Failed to Setup");
						System.out.println("Plug it in please!");
						
					}

			}

	}
