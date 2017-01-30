package org.usfirst.frc.team95.robot;

// Important Imports!
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import javax.swing.JPanel;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;
import org.opencv.core.CvType;

// This class setups up the frame that displays camera stuff
public class VisionCameraSetUp
	{

		// IGNORE THIS :D
		//private static final long serialVersionUID = 2284274759981802504L;
		// IGNORE THIS :D

		// Setup a mat to use
		public static Mat mat = new Mat();
		
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

					}

				// Convert mat data to an image
				//BufferedImage image = Mat2BufferedImage(mat);

				// Draw the image to the JPanel
				//g.drawImage(image, 10, 10, image.getWidth(), image.getHeight(), null);
				
				Size size = new Size(800, 800);

				VideoWriter v = new VideoWriter("/home/video.mpeg", Videoio.CAP_FFMPEG, 30, size, true);
			
				v.write(mat);
				System.out.println("IT WORKED");
			}

		// THIS SECTION PAINTS AND COVERTS EVERYTHING
		// THIS SECTION PAINTS AND COVERTS EVERYTHING
		// THIS SECTION PAINTS AND COVERTS EVERYTHING
		
		// Paint buffered image to JPanel
//		public void paintComponent(Graphics g)
//			{
//				super.paintComponent(g);

		
			

			

		// Method to convert the OpenCV Mat data to a buffered image
//		public BufferedImage Mat2BufferedImage(Mat m)
//			{
//
//				int type = BufferedImage.TYPE_BYTE_GRAY;
//
//				if (m.channels() > 1)
//					{
//						type = BufferedImage.TYPE_3BYTE_BGR;
//					}
//				int bufferSize = m.channels() * m.cols() * m.rows();
//				byte[] b = new byte[bufferSize];
//				m.get(0, 0, b);
//				BufferedImage img = new BufferedImage(m.cols(), m.rows(), type);
//				final byte[] targetPixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
//				System.arraycopy(b, 0, targetPixels, 0, b.length);
//				return img;
//
//			}

	}