package org.usfirst.frc.team95.robot;

// Important Imports!
import javax.swing.JFrame;
import org.opencv.core.Core;
import org.opencv.videoio.VideoCapture;

public class VisionDisplay
	{

		public VisionDisplay()
			{

				// Import OpenCV Libraries
				System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

				// Create a new OpenCV VideoCamera
				// Webcam at the moment, 0 means webcam, 1 means that corresponding usb port
				VideoCapture camera = new VideoCapture(0);

				// See if you are getting data from the camera
				if (camera.isOpened())
					{
						// If you are, print out
						System.out.println("Video is captured");
					}
				else
					{
						// If you can't get data, exit and say YOU FAILED
						System.out.println("Failed to setup");
						System.exit(0);
					}

				// Send data from OpenCV cam to process it
				VisionCameraSetUp cam = new VisionCameraSetUp(camera);

				// Create and setup JFrame
				// JFrame frame = new JFrame();
				// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				// Add the Camera output to the JFrame
				// frame.add(cam);
				// frame.setSize(800, 800);
				// frame.setVisible(true);

				// While the camera is open keep updating
				// while (camera.isOpened())
				// {
				// cam.repaint();
				// }

			}

	}
