package org.usfirst.frc.team95.robot;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;

public class VisualGearLiftFinder {
	CvSink imageSource = null;
	VisionMainPipeline pipeline;
	Mat debugAnnotatedFrame = null;
	double lastDeterminedHeading = 0.0;
	boolean lastHeadingDeterminationSucceeded = false;
	
	public VisualGearLiftFinder(CvSink imageSource) {
		this.imageSource = imageSource;
		pipeline = new VisionMainPipeline();
	}
	
	// Take in an image and process it.  Call this before calling 
	public void computeHeadingToTarget() {
		Mat input_image = new Mat();
		imageSource.grabFrame(input_image);
		VisionCameraSetUp.mat = input_image;
		pipeline.setsource0(input_image);
		pipeline.process(); // currently outputs to VisionCameraSetUp.mat
		debugAnnotatedFrame = input_image;
		
		Imgproc.drawContours(debugAnnotatedFrame, pipeline.filterContoursOutput(), -1, new Scalar(0, 0, 255));
		Imgproc.circle(debugAnnotatedFrame, new Point(10,10), 5, new Scalar(0, 0, 255), 5);
	}	
	
	public boolean haveValidHeading() {
		return lastHeadingDeterminationSucceeded;
	}
	
	public double getHeadingToTarget() {
		return lastDeterminedHeading;
	}
	
	public Mat getAnnotatedFrame() {
		return debugAnnotatedFrame;
	}
}
