package org.usfirst.frc.team95.robot;

import com.ctre.CANTalon;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into to a variable name. This provides flexibility
 * changing wiring, makes checking the wiring easier and significantly reduces the number of magic numbers floating around.
 */
public class RobotMap
	{
		public static CANTalon left1, left2, left3, right1, right2, right3, winchLeft, winchRight, floorIntake;
		public static Drive drive;

		// Vision Stuff
		public static VisualGearLiftFinder gearLiftFinder = null;
		public static UsbCamera myCam = null;
		public static UsbCamera myCam2 = null;
		public static CvSource smartDashboardVideoOutput = null;
		public static CvSink cvSink = null;
		public static boolean debugModeEnabled = false;

		// Vision Checks
		public static boolean visionProcessingActive = false;
		public static boolean visionCamerasOn = false;
		public static boolean startFirstCam = true;
		public static boolean startSecondCam = false;
		public static boolean firstCamOn = false;
		public static boolean secondCamOn = false;

		// Autonomous moves wishing to control the robot's drive base
		// should set the driveLock object to "this" (that is, themselves).
		// They should also check that the driveLock object is null prior to
		// controlling the drive motors.
		public static Object driveLock = null;
		public static Compressor compressor;
		public static Solenoid gearPooper, hatTip, pushFaceOut, brakes, lowerFloorLifter;

		public static double autoDist1 = 0;
		public static double autoDist2 = 0;
		public static double autoRotate = 0;

		public static void init()
			{

				if (startFirstCam)
					{
						myCam = CameraServer.getInstance().startAutomaticCapture("Hephaestus", "/dev/video1");
						myCam.setResolution(640, 480);
						myCam.setExposureManual(16);
						myCam.setFPS(30);
						firstCamOn = true;

						smartDashboardVideoOutput = CameraServer.getInstance().putVideo("Debug", 640, 480);
						cvSink = CameraServer.getInstance().getVideo();

						visionCamerasOn = true;
					}
				else
					{
						System.out.println("---------------------------------------------------");
						System.out.println("- startVisionCameras is false!                    -");
						System.out.println("- This Mean Vision Could Not Start                -");
						System.out.println("- Plug In Cam And Restart Or Vision Will Not Work -");
						System.out.println("---------------------------------------------------");
					}

				// DRIVE MOTERS:
				left1 = new AdjustedTalon(1);
				left2 = new CANTalon(2);
				left3 = new CANTalon(3);
				right1 = new AdjustedTalon(4);
				right2 = new CANTalon(5);
				right3 = new CANTalon(6);
				winchLeft = new CANTalon(7);
				winchRight = new CANTalon(8);

				// intake = new CANTalon(10);
				// agitator = new CANTalon(9);
				// shooter = new CANTalon(11);

				compressor = new Compressor();
				
				floorIntake = new CANTalon(9);
				drive = new Drive(left1, right1);
				left2.changeControlMode(CANTalon.TalonControlMode.Follower);
				left2.set(1);
				left3.changeControlMode(CANTalon.TalonControlMode.Follower);
				left3.set(1);
				right2.changeControlMode(CANTalon.TalonControlMode.Follower);
				right2.set(4);
				right3.changeControlMode(CANTalon.TalonControlMode.Follower);
				right3.set(4);

				/*
				 * Inversion does nothing in Follower mode. We accomplished this by reversing the polarity on the motor wires, so
				 * that setting both motors to "forward" runs the winch without the motors fighting each other.
				 */
				gearPooper = new Solenoid(2);
				hatTip = new Solenoid(1);
				pushFaceOut = new Solenoid(0);
				lowerFloorLifter = new Solenoid(4);
				brakes = new Solenoid(3);
				// andyBooper9000 = new Solenoid(4);

				left1.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
				right1.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
				winchRight.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);

				left1.setEncPosition(0);
				right1.setEncPosition(0);
				winchRight.setEncPosition(0);
				left1.enableBrakeMode(true);
				left2.enableBrakeMode(true);
				left3.enableBrakeMode(true);
				right1.enableBrakeMode(true);
				right2.enableBrakeMode(true);
				right3.enableBrakeMode(true);
				// winchRight.enableBrakeMode(true);
				
			}

		public static void switchVisionCameras()
			{

				System.out.println("--------------------------------");
				System.out.println("- Attempting To Switch Cameras -");
				System.out.println("--------------------------------");

				if (firstCamOn == true)
					{
						myCam.setFPS(0);
						myCam.setResolution(0, 0);

						System.out.println("----------------------");
						System.out.println("- First Cam Disabled -");
						System.out.println("----------------------");

						if (secondCamOn == false)
							{
								System.out.println("-------------------------------------");
								System.out.println("- Attempting To Activate Second Cam -");
								System.out.println("-------------------------------------");

								myCam2 = CameraServer.getInstance().startAutomaticCapture("Theia", "/dev/video0");
								myCam2.setResolution(640, 480);
								myCam2.setFPS(30);

								System.out.println("---------------------");
								System.out.println("- Second Cam Enabled -");
								System.out.println("---------------------");
							}
						myCam2.setFPS(30);
						myCam2.setResolution(640, 480);
						secondCamOn = true;

						System.out.println("---------------------");
						System.out.println("- Second Cam Enabled -");
						System.out.println("---------------------");
					}
				else if (secondCamOn == true)
					{
						myCam2.setFPS(0);
						myCam2.setResolution(0, 0);
						secondCamOn = false;
						System.out.println("-----------------------");
						System.out.println("- Second Cam Disabled -");
						System.out.println("-----------------------");

						myCam.setFPS(30);
						myCam.setResolution(640, 480);
						firstCamOn = true;
						System.out.println("---------------------");
						System.out.println("- First Cam Enabled -");
						System.out.println("---------------------");
					}
			}

		// This starts vision processing
		public static void visionProcessingInit()
			{
				if (!visionCamerasOn)
					{
						System.out.println("------------------------------------");
						System.out.println("- Can't Process, Vision Camera Off -");
						System.out.println("------------------------------------");
					}
				else
					{
						visionProcessingActive = true;
						System.out.println("------------------------");
						System.out.println("- Vision Processing On -");
						System.out.println("------------------------");
						gearLiftFinder = new VisualGearLiftFinder(cvSink);

					}
			}

		// This stops vision processing
		public static void stopVisionProcessing()
			{
				if (!visionCamerasOn)
					{
						System.out.println("------------------------------------");
						System.out.println("- Can't Process, Vision Camera Off -");
						System.out.println("------------------------------------");
					}
				else
					{
						visionProcessingActive = false;
						System.out.println("-------------------------");
						System.out.println("- Vision Processing Off -");
						System.out.println("-------------------------");
					}
			}
	}