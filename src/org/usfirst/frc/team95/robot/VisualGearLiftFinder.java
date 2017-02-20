package org.usfirst.frc.team95.robot;

import java.util.ArrayList;
import java.util.Comparator;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VisualGearLiftFinder 
{
	static final int NUM_BOXES_TO_CONSIDER = 2;
	// Aspect ratios here are width to height.
	// Ideal target is twice as wide as it is tall (10.25" x 5")
	static final double WIDEST_ASPECT_RATIO = 2.5 / 1.0;
	static final double TALLEST_ASPECT_RATIO = 1.0 / 1.5;
	static final double DEGREES_PER_PIXEL = (-0.075); // Determined from google drive sheet

	// This was only from one measurement and is off by a bit, will fix later
	static final double FOCAL_LENGTH = 3300.00;

	static final String REASON = "Reason for not finding gearlift";

	CvSink imageSource = null;
	VisionMainPipeline pipeline;

	Mat outputFrame = new Mat();
	Mat curFrame = new Mat(); // gets annotated after processing
	double lastDeterminedHeadingDegrees = 0.0;
	boolean lastHeadingDeterminationSucceeded = false;

	double heightOfObjectInPixels = 0.0;
	double distanceFromCamToTarget = 0.0;
	

	public VisualGearLiftFinder(CvSink imageSource)
	{
		this.imageSource = imageSource;
		pipeline = new VisionMainPipeline();
	}

	
	// Take in an image and process it. Call this before calling
	public void computeHeadingToTarget()
	{
		if(imageSource.isValid()) {
//			imageSource.grabFrame(curFrame);
			imageSource.grabFrameNoTimeout (curFrame);
			if (!curFrame.empty()) {
				pipeline.process(curFrame);
				lastHeadingDeterminationSucceeded = false;
			
			
				// Draw output of pipeline
				ArrayList<Rect> boxes = pipeline.filterContoursBbOutput();
				Imgproc.drawContours(curFrame, pipeline.filterContoursOutput(), -1, new Scalar(0, 0, 255));
				for (Rect bb : boxes) {
					Imgproc.rectangle(curFrame, bb.br(), bb.tl(), new Scalar(128, 255, 128));
		
					heightOfObjectInPixels = bb.height;
					distanceFromCamToTarget = (FOCAL_LENGTH / bb.height);
				}
		
				// If we only see one box then it's not enough information to see the target
				if (boxes.size() >= 2) {
					// Sort by largest to smallest (in terms of bounding box area)
					boxes.sort(new Comparator<Rect>() {
						@Override
						public int compare(Rect o1, Rect o2)
							{
								// 0 for equal, >0 for "o2 belongs before o1",
								// so subtraction works as a shortcut.
								return (int) (o2.area() - o1.area());
							}
					});
	
					// Assume the two largest boxes are the ones to use.
					// Find their boundaries.
					double left_bound = curFrame.cols(); // start at the right
					double right_bound = 0; // start at the left
					double top_bound = curFrame.rows(); // start at the bottom
					double bottom_bound = 0; // start at the top
					for (int i = 0; i < 2 && i < NUM_BOXES_TO_CONSIDER; ++i) {
						Rect bb = boxes.get(i);
						Imgproc.putText(curFrame, "" + i, bb.tl(), 0, 0.75, new Scalar(255, 255, 255));
						left_bound = Math.min(left_bound, bb.tl().x);
						right_bound = Math.max(right_bound, bb.br().x);
						top_bound = Math.min(top_bound, bb.tl().y);
						bottom_bound = Math.max(bottom_bound, bb.br().y);
					}
					Imgproc.rectangle(curFrame, new Point(left_bound, top_bound), new Point(right_bound, bottom_bound), new Scalar(255, 255, 255));
		
					// Confirm that the aspect ratio of the target is what we expect.
					double aspect_ratio = (right_bound - left_bound) / (bottom_bound - top_bound);
					if (aspect_ratio > TALLEST_ASPECT_RATIO && aspect_ratio < WIDEST_ASPECT_RATIO) {
						// Finally, compute the heading
						double target_center_in_image = (right_bound + left_bound) / 2.0;
						double target_offset_from_center = target_center_in_image - (curFrame.cols() / 2.0);
						Imgproc.line(curFrame, new Point(target_center_in_image, 0), new Point(target_center_in_image, curFrame.rows()), new Scalar(0, 255, 0));
						SmartDashboard.putNumber("Target center (pixels)", target_offset_from_center);
						lastDeterminedHeadingDegrees = target_offset_from_center * DEGREES_PER_PIXEL;
						lastHeadingDeterminationSucceeded = true;
						SmartDashboard.putString(REASON, "None.");
					} else {
						SmartDashboard.putString(REASON, "Aspect ratio of " + aspect_ratio + " outside limits.");
					}
				} else {
					SmartDashboard.putString(REASON, "Insufficient contours passing filter");
				}
				curFrame.copyTo(outputFrame);
			} else {
				SmartDashboard.putString(REASON, "Sink not valid!");
			}
		}
	}

	public boolean haveValidHeading()
	{
		return lastHeadingDeterminationSucceeded;
	}

	public double getHeadingToTargetDegrees()
	{
		return lastDeterminedHeadingDegrees;
	}

	public double getHeadingToTargetRadians()
	{
		return getHeadingToTargetDegrees() * (Math.PI / 180.0);
	}

	public double getHightOfObjectInPixels()
	{
		return heightOfObjectInPixels;
	}

	public double getDistanceFromCamToTarget()
	{
		return distanceFromCamToTarget;
	}

	public Mat getAnnotatedFrame()
	{
		return outputFrame;
	}
}
