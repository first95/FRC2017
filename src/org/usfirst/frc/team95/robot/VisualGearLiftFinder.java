package org.usfirst.frc.team95.robot;

import org.opencv.core.Mat;

import edu.wpi.cscore.CvSink;

public class VisualGearLiftFinder {
	CvSink imageSource = null;
	VisionMainPipeline pipeline;
	Mat debugAnnotatedFrame = null;
	double lastDeterminedHeading = 0.0;
	boolean lastHeadingDeterminationSucceeded = false;
	
	public VisualGearLiftFinder(CvSink imageSource) {
		this.imageSource = imageSource;
	}
	
	// Take in an image and process it.  Call this before calling 
	public void computeHeadingToTarget() {
		Mat input_image = new Mat();
		imageSource.grabFrame(input_image);
		// TODO: all the real work
		debugAnnotatedFrame = input_image;
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
