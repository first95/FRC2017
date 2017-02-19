package org.usfirst.frc.team95.robot;

import com.ctre.CANTalon;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into to a variable name. This provides flexibility changing wiring, makes checking the wiring easier and significantly reduces the number of magic numbers floating around.
 */
public class RobotMap
	{
		public static CANTalon left1, left2, left3, right1, right2, right3, winchLeft, winchRight, intake, agitator, shooter;
		public static Drive drive;

		// Vision Stuff
		public static VisualGearLiftFinder gearLiftFinder = null;
		public static UsbCamera myCam = null;
		public static CvSource smartDashboardVideoOutput = null;

		// Autonomous moves wishing to control the robot's drive base
		// should set the driveLock object to "this" (that is, themselves).
		// They should also check that the driveLock object is null prior to
		// controlling the drive motors.
		public static Object driveLock = null;
		public static Solenoid gearPooper, hatTip, pushFaceOut;

		public static void init()
			{

				// Start Vision Processing, and allow us to grab it from anywhere
				myCam = CameraServer.getInstance().startAutomaticCapture("Hephaestus", "/dev/video0");
				
				myCam.setResolution(640, 480);
				myCam.setExposureManual(35);
				myCam.setFPS(30);
				smartDashboardVideoOutput = CameraServer.getInstance().putVideo("Debug", 640, 480);
				CvSink cvSink = CameraServer.getInstance().getVideo();
				gearLiftFinder = new VisualGearLiftFinder(cvSink);

				// drive motors
				left1 = new CANTalon(1);
				left2 = new CANTalon(2);
				left3 = new CANTalon(3);
				right1 = new CANTalon(4);
				right2 = new CANTalon(5);
				right3 = new CANTalon(6);
				winchRight = new CANTalon(7);
				winchLeft = new CANTalon(8);
				intake = new CANTalon(9);
				agitator = new CANTalon(10);
				shooter = new CANTalon(11);
				drive = new Drive(left1, right1);
				left2.changeControlMode(CANTalon.TalonControlMode.Follower);
				left2.set(1);
				left3.changeControlMode(CANTalon.TalonControlMode.Follower);
				left3.set(1);
				right2.changeControlMode(CANTalon.TalonControlMode.Follower);
				right2.set(4);
				right3.changeControlMode(CANTalon.TalonControlMode.Follower);
				right3.set(4);
				winchLeft.changeControlMode(CANTalon.TalonControlMode.Follower);
				winchLeft.set(7);
				// Inversion does nothing in Follower mode. We accomplished this by reversing the polarity on the motor wires,
				// so that setting both motors to "forward" runs the winch without the motors fighting each other.
				winchLeft.setInverted(true); // can't invert followers
				gearPooper = new Solenoid(2);
				hatTip = new Solenoid(1);
				pushFaceOut = new Solenoid(0);

				left1.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
				right1.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);

				left1.setEncPosition(0);
				right1.setEncPosition(0);
				left1.enableBrakeMode(true);
				right1.enableBrakeMode(true);
			}
	}
