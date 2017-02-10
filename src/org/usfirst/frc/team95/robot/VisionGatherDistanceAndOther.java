package org.usfirst.frc.team95.robot;

// Important Imports!
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class VisionGatherDistanceAndOther
	{

		// Vars
		int centerXOfTar;
		int centerYOfTar;
		double constantPix2Deg = (3.0 / 16.0);
		double pixOffset;
		public static double pix2Deg;
		Point point;
		int distanceCam2Tar;
		
		public VisionGatherDistanceAndOther(Rect contourRect)
			{



				// Calculate Center of Tar
				centerYOfTar = (contourRect.height / 2);
				centerXOfTar = (contourRect.width / 2);

				int rectBottomLeftAndHalfWidthX = contourRect.x + centerXOfTar;

				// Create a point with the height + width
				point = new Point(contourRect.x + centerXOfTar, contourRect.y + centerYOfTar);

				// Get pixel offset
				pixOffset = (320 - rectBottomLeftAndHalfWidthX);

				// Turn the pixel offset to degrees
				pix2Deg = (pixOffset * constantPix2Deg);

				// Get the distance from camera to tar
				distanceCam2Tar = (8052 / contourRect.height);

				// Print everything out
				//System.out.println("X COORD: " + rectBottomLeftAndHalfWidthX);
				System.out.println("Degree: " + pix2Deg);
				System.out.println("Distance: " + distanceCam2Tar + "in");

				// Draw a circle in the center of the tar
				Imgproc.circle(VisionCameraSetUp.mat, point, 5, new Scalar(0, 0, 255), 5);
				
			}
		
	}