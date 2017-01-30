package org.usfirst.frc.team95.robot;

// Important Imports!
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

// This class setups up the frame that displays camera stuff
public class VisionCameraSetUp
	{

		// IGNORE THIS :D
		private static final long serialVersionUID = 2284274759981802504L;
		// IGNORE THIS :D

		// Setup a mat to use
		public static Mat mat = new Mat();
		public static Mat finalMat = new Mat();
		
		// Make a new OpenCV camera capture
		VideoCapture camera;

		// Send the camera through the process of stuff
		public VisionCameraSetUp(VideoCapture cam)
			{
				camera = cam;

				// Import pipelines to use
				VisionMainPipeline p = new VisionMainPipeline();

				// If the cam gets mat data, continue
				if (camera.read(mat))
					{

						// Run the Mat data that is from the camera through the pipeline
						p.setsource0(mat);
						p.process();

						// Draw the contours to the mat
						Imgproc.drawContours(mat, p.filterContoursOutput(), -1, new Scalar(0, 0, 255));
						
						finalMat = mat;

					}

			}

	}