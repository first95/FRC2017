package org.usfirst.frc.team95.robot;

import java.util.ArrayList;
import java.util.Comparator;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;

public class VisualGearLiftFinder {
	static final int NUM_BOXES_TO_CONSIDER = 2;
	
	CvSink imageSource = null;
	VisionMainPipeline pipeline;
	Mat curFrame = new Mat(); // gets annotated after processing
	double lastDeterminedHeading = 0.0;
	boolean lastHeadingDeterminationSucceeded = false;
	
	public VisualGearLiftFinder(CvSink imageSource) {
		this.imageSource = imageSource;
		pipeline = new VisionMainPipeline();
	}
	
	// Take in an image and process it.  Call this before calling 
	public void computeHeadingToTarget() {
		Mat input_image = new Mat();
		imageSource.grabFrame(curFrame);
		pipeline.process(curFrame);
		lastHeadingDeterminationSucceeded = false;
		
		// Draw output of pipeline
		ArrayList<Rect> boxes = pipeline.filterContoursBbOutput();
		Imgproc.drawContours(curFrame, pipeline.filterContoursOutput(), -1, new Scalar(0, 0, 255));
		for (Rect bb : boxes) {
			Imgproc.rectangle(curFrame, bb.br(), bb.tl(), new Scalar(128, 255, 128));
		}
		
		// If we only see one box then it's not enough information to see the target
		if(boxes.size() >= 2) {
			lastHeadingDeterminationSucceeded = true;
			// Sort by largest to smallest (in terms of bounding box area)
			boxes.sort(new Comparator<Rect>() {
				@Override
				public int compare(Rect o1, Rect o2) {
					// 0 for equal, >0 for "o2 belongs before o1",
					// so subtraction works as a shortcut.
					return (int)(o2.area() - o1.area());
				}
			});
			
			// Assume the two largest boxes are the ones to use.
			// Find their boundaries.
			double left_bound   = curFrame.cols(); // start at the right
			double right_bound  = 0; // start at the left
			double top_bound    = curFrame.rows(); // start at the bottom
			double bottom_bound = 0; // start at the top
			for(int i = 0; i < 2 && i < NUM_BOXES_TO_CONSIDER; ++i) {
				Rect bb = boxes.get(i);
				Imgproc.putText(curFrame, "" + i, bb.tl(), 0, 0.75, new Scalar(255, 255, 255));
				left_bound    = Math.min(left_bound   , bb.tl().x);
				right_bound   = Math.max(right_bound  , bb.br().x);
				top_bound     = Math.min(top_bound    , bb.tl().y);
				bottom_bound  = Math.max(bottom_bound , bb.br().y);
			}
			Imgproc.rectangle(curFrame, new Point(left_bound, top_bound), new Point(right_bound, bottom_bound), new Scalar(255, 255, 255));
		}
	}	
	
	public boolean haveValidHeading() {
		return lastHeadingDeterminationSucceeded;
	}
	
	public double getHeadingToTarget() {
		return lastDeterminedHeading;
	}
	
	public Mat getAnnotatedFrame() {
		return curFrame	;
	}
}
