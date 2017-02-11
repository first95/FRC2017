package org.usfirst.frc.team95.robot;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import java.util.ArrayList;

import org.opencv.core.Mat;
import org.usfirst.frc.team95.robot.auto.Auto;
import org.usfirst.frc.team95.robot.auto.FindGearHolder;
import org.usfirst.frc.team95.robot.auto.Nothing;
import org.usfirst.frc.team95.robot.auto.RotateBy;
import org.usfirst.frc.team95.robot.auto.SequentialMove;
import org.usfirst.frc.team95.robot.auto.TimedMove;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalOutput;


/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to each mode, as described in the IterativeRobot documentation. If you change the name of this class or the package after creating this project, you must also update the manifest file in the resource directory.
 */
public class Robot extends IterativeRobot
	{
		VisionDisplay test;
		NetworkTable visionData = NetworkTable.getTable("CameraOutput");

		Command autonomousCommand;
		SendableChooser chooser;

		GyroReader gyro;
		VariableStore variableStore;
		CompassReader compass;
		ADIS16448_IMU compass2;
		HeadingPreservation header;
		Timer cycleTime; // for common periodic
		double ymin, ymax, zmin, zmax, alpha, beta, tempy, tempz;

		Double headingToPres, dist;
		Double[] angleRec;
		ButtonTracker headPres, compCal1, compCal2, compCalReset, eatGear, facePush, poopGear, intake, agitate, shoot, driveForward, xBoxControl;

		Auto move;
		SendableChooser a, b, c;
		ArrayList<PollableSubsystem> updates = new ArrayList<PollableSubsystem>();
		ArrayList<Auto> runningAutonomousMoves = new ArrayList<Auto>();

		AnalogInput range1, range2, range3, range4;
		
		DigitalOutput rangeFinder;
		
		VoltageCompensatedShooter shooter;

		/**
		 * This function is run when the robot is first started up and should be used for any initialization code.
		 */
		public void robotInit()
			{
				RobotMap.init();
				chooser = new SendableChooser();
				// chooser.addDefault("Default Auto", new ExampleCommand());
				// chooser.addObject("My Auto", new MyAutoCommand());
				SmartDashboard.putData("Auto mode", chooser);
				// ADXL345_I2C Giro = new ADXL345_I2C(I2C.Port.kOnboard, ADXL345_I2C.Range.k2G);
				// Giro = new ADXL345_I2C(I2C.Port.kOnboard, ADXL345_I2C.Range.k2G);
				gyro = new GyroReader();
				variableStore = new VariableStore();
				compass = new CompassReader(variableStore);
				compass2 = new ADIS16448_IMU(variableStore);
				header = new HeadingPreservation(compass2);
				shooter = new VoltageCompensatedShooter(4);
				
				headPres = new ButtonTracker(Constants.driveStick, 2);
				compCal1 = new ButtonTracker(Constants.driveStick, 11);
				compCal2 = new ButtonTracker(Constants.driveStick, 16);
				compCalReset = new ButtonTracker(Constants.driveStick, 5);
				xBoxControl = new ButtonTracker(Constants.driveStickX, 1);
				eatGear = new ButtonTracker(Constants.weaponStick, 5);
				poopGear = new ButtonTracker(Constants.weaponStick, 4);
				facePush = new ButtonTracker(Constants.weaponStick, 3);
				intake = new ButtonTracker(Constants.weaponStick, 1);
				agitate = new ButtonTracker(Constants.weaponStick, 2);
				shoot = new ButtonTracker(Constants.weaponStick, 6);
				driveForward = new ButtonTracker(Constants.driveStick, 1);
				range1 = new AnalogInput(0);
				range2 = new AnalogInput(1);
				range3 = new AnalogInput(2);
				range4 = new AnalogInput(3);
				
				rangeFinder = new DigitalOutput(0);
				
				// Vision Stuff
				VisionGatherDistanceAndOther.pix2Deg = 0;
				test = new VisionDisplay();
				
				//CameraServer.getInstance().startAutomaticCapture();
				//CvSink cvSink = CameraServer.getInstance().getVideo();
				//CvSource outputStream = CameraServer.getInstance().putVideo("TEST", 640, 480);
				
				//

				cycleTime = new Timer();
				cycleTime.reset();
				cycleTime.start();
				angleRec = new Double[4];
				angleRec[3] = 0.1;
				angleRec[2] = 0.1;
				angleRec[1] = 0.1;
				angleRec[0] = 0.1;

				for (PollableSubsystem p : updates)
					{
						p.init();
					}
				a = new SendableChooser();
				b = new SendableChooser();
				c = new SendableChooser();
				a.addDefault("None", new Nothing());
				a.addObject("Go Forward", new TimedMove(0.3, 0.3, 5));
				a.addObject("Go Backward", new TimedMove(-0.3, -0.3, 5));
				a.addObject("Turn 45 Right", new RotateBy(Math.PI / 4, compass2));
				a.addObject("Turn 45 Left", new RotateBy(-Math.PI / 4, compass2));
				b.addDefault("None", new Nothing());
				b.addObject("Go Forward", new TimedMove(0.3, 0.3, 5));
				b.addObject("Go Backward", new TimedMove(-0.3, -0.3, 5));
				b.addObject("Turn 45 Right", new RotateBy(Math.PI / 4, compass2));
				b.addObject("Turn 45 Left", new RotateBy(-Math.PI / 4, compass2));
				c.addDefault("None", new Nothing());
				c.addObject("Go Forward", new TimedMove(0.3, 0.3, 5));
				c.addObject("Go Backward", new TimedMove(-0.3, -0.3, 5));
				c.addObject("Turn 45 Right", new RotateBy(Math.PI / 4, compass2));
				c.addObject("Turn 45 Left", new RotateBy(-Math.PI / 4, compass2));
				c.addObject("Find + Rotate", new FindGearHolder());
				SmartDashboard.putData("1st", a);
				SmartDashboard.putData("2nd", b);
				SmartDashboard.putData("3rd", c);

			}

		/**
		 * This function is called once each time the robot enters Disabled mode. You can use it to reset any subsystem information you want to clear when the robot is disabled.
		 */
		public void disabledInit()
			{

			}

		public void disabledPeriodic()
			{
				commonPeriodic();
				Scheduler.getInstance().run();
			}

		/**
		 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and uncomment the getString code to get the auto name from the text box below the Gyro You can add additional auto modes by adding additional commands to the chooser code above (like the commented example) or additional comparisons to the switch structure below with additional strings & commands.
		 */
		public void autonomousInit()
			{

				// System.out.println("Auto INIT");

				Auto am = (Auto) a.getSelected();
				Auto bm = (Auto) b.getSelected();
				Auto cm = (Auto) c.getSelected();
				String picked = "We picked: ";
				picked += am.getClass().getName() + ", ";
				picked += bm.getClass().getName() + ", ";
				picked += cm.getClass().getName();
				DriverStation.reportError(picked, false);
				Auto[] m =
					{ am, bm, cm };

				move = new SequentialMove(m);
				// move = new TimedStraightMove(0.3, 10);
				move.init();
			}

		/**
		 * This function is called periodically during autonomous
		 */
		public void autonomousPeriodic()
			{
				commonPeriodic();
				for (Auto x : runningAutonomousMoves)
					{
						// System.out.println("Running " + x.getClass().getName());
						x.update();
						if (x.done())
							{
								x.stop();
								runningAutonomousMoves.remove(x);
							}
					}

				// System.out.println("Auto Periodic");
				move.update();

				Scheduler.getInstance().run();
			}

		public void teleopInit()
			{
				// This makes sure that the autonomous stops running when
				// teleop starts running. If you want the autonomous to
				// continue until interrupted by another command, remove
				// this line or comment it out.
				if (autonomousCommand != null)
					autonomousCommand.cancel();
			}

		/**
		 * This function is called periodically during operator control
		 */
		public void teleopPeriodic()
			{
				commonPeriodic();
				Scheduler.getInstance().run();

				// drive
				if (Constants.driveStick.getRawButton(2))
					{
						if (headPres.wasJustPressed())
							{
								headingToPres = compass.getHeading();
							}

						header.setHeading(headingToPres);
					}
				else if (xBoxControl.isPressed()) {
						RobotMap.drive.arcade(Constants.driveStickX);
					} else {
						RobotMap.drive.arcade(Constants.driveStick);
					}

				dist = (double) 0;
		    	while (driveForward.isPressed() && dist <= 5) {
		    		RobotMap.drive.tank(.3, .3);
		    		dist = (double) RobotMap.left1.getEncPosition();
		    	}
		    	
		    	if (driveForward.wasJustReleased()) {
		    		RobotMap.drive.tank(0, 0);
		    	}
		    	
				// alpha gear code
		    	RobotMap.gearMouth.set(eatGear.isPressed());
		    	RobotMap.pushFaceOut.set(facePush.isPressed());
		    	RobotMap.gearPooper.set(poopGear.isPressed());
		    	
		    	if (intake.isPressed()) {
		    		RobotMap.intake.set(.3);
		    	}else {
		    		RobotMap.intake.set(0);
		    	}
		    	
		    	if (agitate.isPressed()) {
		    		RobotMap.agitator.set(.3);
		    	}else {
		    		RobotMap.agitator.set(0);
		    	}
		    	
		    	if (shoot.wasJustPressed()) {
		    		shooter.turnOn();
		    	}else if (shoot.wasJustReleased()) {
		    		shooter.turnOff();
		    	}
				
			}

		/**
		 * This function is called periodically during test mode
		 */
		public void testPeriodic()
			{
				LiveWindow.run();
			}

		// This is run in disabled, teleop, and auto periodics.
		public void commonPeriodic()
			{

				// Test Stuff For Vision
				SmartDashboard.putNumber("MatNumCols", VisionCameraSetUp.finalMat.cols());
				SmartDashboard.putNumber("MatNumRows", VisionCameraSetUp.finalMat.rows());
				SmartDashboard.putNumber("Degree Offset (X)", VisionGatherDistanceAndOther.pix2Deg);
				//

				// System.out.println(compass2.getMagX() + ", " + compass2.getMagY() + ", " + compass2.getMagZ());// + ", " + gyro.getXAng() + ", " + gyro.getYAng() + ", " + gyro.getZAng() + ", " + compass.getHeading() + ", " + cycleTime.get() + ", " );

				rangeFinder.pulse(.02);
				
		    	SmartDashboard.putNumber("X", compass2.getMagX());
		    	SmartDashboard.putNumber("Y", compass2.getMagY());
		    	SmartDashboard.putNumber("Z", compass2.getMagZ());
		    	SmartDashboard.putNumber("ATanXY", Math.atan2(compass2.getMagX(), compass2.getMagY()));
		    	SmartDashboard.putNumber("ATanZY", Math.atan2(compass2.getMagZ(), compass2.getMagY()));
		    	SmartDashboard.putNumber("ATanXZ", Math.atan2(compass2.getMagX(), compass2.getMagZ()));
		    	
		    	SmartDashboard.putNumber("CX", compass.getRawCompX()); 
		    	SmartDashboard.putNumber("CY", compass.getRawCompY()); 
		    	SmartDashboard.putNumber("CZ", compass.getRawCompZ()); 
		    	
		    	
		    	SmartDashboard.putNumber("Heading", compass2.getHeading());
		    	
		    	SmartDashboard.putNumber("Range1 Finder cm", Constants.RFVoltsToCm(range1.getVoltage()));
		    	SmartDashboard.putNumber("Range2 Finder cm", Constants.RFVoltsToCm(range2.getVoltage()));
		    	SmartDashboard.putNumber("Range3 Finder cm", Constants.RFVoltsToCm(range3.getVoltage()));
		    	SmartDashboard.putNumber("Range3 Finder cm", Constants.RFVoltsToCm(range4.getVoltage()));
		    	SmartDashboard.putNumber("Range finder Volts", range1.getVoltage());
		    	
		    	SmartDashboard.putNumber("Left Encoder", RobotMap.left1.getEncPosition());
		    	SmartDashboard.putNumber("Right Encoder", RobotMap.right1.getEncPosition());

				SmartDashboard.putNumber("Alpha", variableStore.GetDouble(CompassReader.compassAlphaVariableName, 0));
				SmartDashboard.putNumber("Beta", variableStore.GetDouble(CompassReader.compassBetaVariableName, 0));

				if (compCal1.isPressed())
					{// && compCal2.Pressedp()) {
						// auto cal
						tempy = compass2.getMagX();
						tempz = compass2.getMagZ();
						if (compCal1.wasJustPressed())
							{// && compCal2.justPressedp()) {
								ymax = tempy;
								ymin = tempy;
								zmax = tempz;
								zmin = tempz;
							}
						if (tempy > ymax)
							{
								ymax = tempy;
							}
						else if (tempy < ymin)
							{
								ymin = tempy;
							}

						if (tempz > zmax)
							{
								zmax = tempz;
							}
						else if (tempz < zmin)
							{
								zmin = tempz;
							}

						alpha = (ymax + ymin) / 2;
						beta = (zmax + zmin) / 2;
						// System.out.println("ymax" + ymax);
						// System.out.println("ymin" + ymin);
						// System.out.println("zmax" + zmax);
						// System.out.println("zmin" + zmin);
						// System.out.println("alpha" + alpha);
						// System.out.println("beta" + beta);
						// overides alpha and beta in compreader.
						// lasts until code is rebooted rewriting code will be needed
						compass.compCal(alpha, beta);
					}
				// resets compass to original calibration
				if (compCalReset.isPressed())
					{
						alpha = -164;
						beta = -25;
						compass.compReset();
					}

				// Send data from OpenCV cam to process it
				// VisionCameraSetUp cam = new VisionCameraSetUp(VisionDisplay.camera);

				headPres.update();
		    	compCal1.update();
		    	compCal2.update();
		    	compCalReset.update();
		    	eatGear.update();
		    	poopGear.update();
		    	facePush.update();
		    	intake.update();
		    	agitate.update();
		    	shoot.update();
		    	xBoxControl.update();
		    	shooter.adjustVoltage();
			}
	}
