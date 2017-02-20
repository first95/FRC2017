package org.usfirst.frc.team95.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import java.util.ArrayList;
import org.usfirst.frc.team95.robot.auto.AtLiftRotate;
import org.usfirst.frc.team95.robot.auto.Auto;
import org.usfirst.frc.team95.robot.auto.DistanceMove;
import org.usfirst.frc.team95.robot.auto.DistanceMovePID;
import org.usfirst.frc.team95.robot.auto.GoToLiftAdvanced;
import org.usfirst.frc.team95.robot.auto.Nothing;
import org.usfirst.frc.team95.robot.auto.RangeBasedGearScorer;
import org.usfirst.frc.team95.robot.auto.RotateBy;
import org.usfirst.frc.team95.robot.auto.ScoreGear;
import org.usfirst.frc.team95.robot.auto.SequentialMove;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalOutput;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to each mode, as described in the IterativeRobot documentation. If you change the name of this class or the package after creating this project, you must also update the manifest file in the resource directory.
 */
public class Robot extends IterativeRobot
	{

		SendableChooser chooser;

		GyroReader gyro;
		VariableStore variableStore;
		CompassReader compass;
		ADIS16448_IMU poseidon;
		HeadingPreservation header;
		Timer cycleTime, booperTimer; // for common periodic
		double ymin, ymax, zmin, zmax, alpha, beta, tempy, tempz;
		AnalogInput range1, range2, range3, range4;
		DigitalOutput initiateRangeFinder;

		Double headingToPres;
		double dist;
		Double[] angleRec;
		boolean twoStickMode;
		ButtonTracker headPres, compCal1, compCalReset, slowMo, changeDriveMode, brakes, 
		tipHat, facePush, poopGear, intake, agitate, shoot, 
		incPID, decPID;

		Auto move;
		SendableChooser a, b, c;
		RangeBasedGearScorer rangeBasedGearScorer;
		VoltageCompensatedShooter shooter;

		RangeFinder rangeFinder;

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
				poseidon = new ADIS16448_IMU(variableStore);
				header = new HeadingPreservation(poseidon);
				shooter = new VoltageCompensatedShooter(RobotMap.shooter, 4);
				
				twoStickMode = false;
				//drive buttons
				changeDriveMode = new ButtonTracker(Constants.driveStick, 4);
				headPres = new ButtonTracker(Constants.driveStick, 2);
				brakes = new ButtonTracker(Constants.driveStick, 1);
				slowMo = new ButtonTracker(Constants.driveStick, 6);
				compCal1 = new ButtonTracker(Constants.driveStick, 7);
				compCalReset = new ButtonTracker(Constants.driveStick, 8);
				
				//weapon buttons
				tipHat = new ButtonTracker(Constants.weaponStick, 1);
				poopGear = new ButtonTracker(Constants.weaponStick, 2);
				intake = new ButtonTracker(Constants.weaponStick, 3);
				agitate = new ButtonTracker(Constants.weaponStick, 4);
				facePush = new ButtonTracker(Constants.weaponStick, 5);
				shoot = new ButtonTracker(Constants.weaponStick, 6);

//				range1 = new AnalogInput(0);
//				range2 = new AnalogInput(1);
//				range3 = new AnalogInput(2);
//				range4 = new AnalogInput(3);
//
//				initiateRangeFinder = new DigitalOutput(0);

//				rangeFinder = new RangeFinder(initiateRangeFinder, new AnalogInput[]
//					{ range1, range2 });
//				rangeBasedGearScorer = new RangeBasedGearScorer(RobotMap.gearPooper, RobotMap.pushFaceOut, rangeFinder);

				cycleTime = new Timer();
				cycleTime.reset();
				cycleTime.start();
				booperTimer = new Timer();
				booperTimer.reset();
				angleRec = new Double[4];
				angleRec[3] = 0.1;
				angleRec[2] = 0.1;
				angleRec[1] = 0.1;
				angleRec[0] = 0.1;


				a = new SendableChooser();
				b = new SendableChooser();
				c = new SendableChooser();
				a.addDefault("None", new Nothing());
				a.addObject("Go Forward", new DistanceMovePID(5));
				a.addObject("Go Backward", new DistanceMove(-0.3, -0.3, 5));
				a.addObject("Turn 45 Right", new RotateBy(Math.PI / 4, poseidon));

				// Automoves to Test, One Turns, One Moves and Turns
				a.addObject("GoToLiftAdvanced", new GoToLiftAdvanced());
				a.addObject("AtLiftRotate", new AtLiftRotate(poseidon));
				a.addObject("Score Gear", new ScoreGear());
				b.addDefault("None", new Nothing());
				b.addObject("Go Forward", new DistanceMove(0.1, 0, 1));
				b.addObject("Go Backward", new DistanceMove(-0.3, -0.3, 5));
				b.addObject("Turn 45 Right", new RotateBy(Math.PI / 4, poseidon));
				b.addObject("Turn 45 Left", new RotateBy(-Math.PI / 4, poseidon));
				b.addObject("Score Gear", new ScoreGear());
				c.addDefault("None", new Nothing());
				c.addObject("Go Forward", new DistanceMove(0.3, 0.3, 5));
				c.addObject("Go Backward", new DistanceMove(-0.3, -0.3, 5));
				c.addObject("Turn 45 Right", new RotateBy(Math.PI / 4, poseidon));
				c.addObject("Turn 45 Left", new RotateBy(-Math.PI / 4, poseidon));
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
				move.start();
			}

		/**
		 * This function is called periodically during autonomous
		 */
		public void autonomousPeriodic()
			{
				commonPeriodic();

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
				if (move != null)
					move.stop();
			}

		/**
		 * This function is called periodically during operator control
		 */
		public void teleopPeriodic()
			{
				commonPeriodic();
				Scheduler.getInstance().run();
				
				if (changeDriveMode.wasJustPressed()) {
					twoStickMode = !twoStickMode;
				}
				// drive
				if (Constants.driveStick.getRawButton(2))
					{
						if (headPres.wasJustPressed())
							{
								headingToPres = compass.getHeading();
							}

						header.setHeading(headingToPres);
					}

				else if (slowMo.isPressed())
					{
						RobotMap.drive.halfArcade(Constants.driveStick, twoStickMode);
					}
				else
					{
						RobotMap.drive.arcade(Constants.driveStick, twoStickMode);
					}

				/*
				 * dist = 0; while (driveForward.isPressed() && dist <= -5) { RobotMap.drive.tank(-.3, -.3); dist = (RobotMap.right1.getEncPosition() * Constants.encoderTickPerFoot); driveForward.update(); } if (driveForward.wasJustReleased()) { RobotMap.drive.tank(0, 0); }
				 */

				// alpha gear code
				if(tipHat.isPressed()) {
					RobotMap.hatTip.set(true);
					RobotMap.pushFaceOut.set(false);
					RobotMap.gearPooper.set(false);
				} else {
					RobotMap.hatTip.set(false);
					RobotMap.pushFaceOut.set(facePush.isPressed());
					RobotMap.gearPooper.set(poopGear.isPressed());
				}

				/*
				 * if (facePush.wasJustPressed()) { rangeBasedGearScorer.start(); } else if (facePush.wasJustReleased()) { rangeBasedGearScorer.stop(); }
				 */

				/*if (intake.isPressed())
					{
						RobotMap.intake.set(.3);
					}
				else
					{
						RobotMap.intake.set(0);
					}*/
				RobotMap.intake.set(-Constants.weaponStick.getRawAxis(2));

				
				RobotMap.brakes.set(brakes.isPressed());
				
				
				booperTimer.start();
				if (agitate.isPressed())
					{
						//RobotMap.agitator.set(.3);
//						if (booperTimer.get() < .25) {
							RobotMap.andyBooper9000.set(true);
//						} else if (booperTimer.get() >= .25 && booperTimer.get() < .5){
//							RobotMap.andyBooper9000.set(false);
//							booperTimer.reset();
//						}
					}
				else
					{
						//RobotMap.agitator.set(0);
						RobotMap.andyBooper9000.set(false);				
//						booperTimer.stop();
//						booperTimer.reset();
					}

				/*if (shoot.wasJustPressed())
					{
						shooter.turnOn();
					}
				else if (shoot.wasJustReleased())
					{
						shooter.turnOff();
					}*/
				RobotMap.shooter.set(-Constants.weaponStick.getRawAxis(3));
				
				if (Math.abs(Constants.weaponStick.getY()) > Constants.joystickDeadbandV) {
				  RobotMap.winchRight.set(Constants.weaponStick.getY());
				 }
				 
				
				//RobotMap.winchRight.set(Constants.weaponStick.getY());//add deadbanding
				//RobotMap.winchLeft.set(-Constants.weaponStick.getY());
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
				
				// System.out.println(compass2.getMagX() + ", " + compass2.getMagY() + ", " + compass2.getMagZ());// + ", " + gyro.getXAng() + ", " + gyro.getYAng() + ", " + gyro.getZAng() + ", " + compass.getHeading() + ", " + cycleTime.get() + ", " );

				// Show the edited video output from the camera
			
//				if (RobotMap.gearLiftFinder != null) {
//					RobotMap.gearLiftFinder.computeHeadingToTarget();
//					RobotMap.smartDashboardVideoOutput.putFrame(RobotMap.gearLiftFinder.getAnnotatedFrame());
//					// Note: The following items are not locked and may give you incorrect values. Further re-entrancy is required.
//					SmartDashboard.putNumber("Hight Of Object In Pixels", RobotMap.gearLiftFinder.heightOfObjectInPixels);
//					SmartDashboard.putNumber("Distance From Cam To Target IN INCHES", RobotMap.gearLiftFinder.distanceFromCamToTarget);
//					SmartDashboard.putNumber("Degree Offset (X)", RobotMap.gearLiftFinder.getHeadingToTargetDegrees());
//					SmartDashboard.putBoolean("We can see the target", RobotMap.gearLiftFinder.haveValidHeading());
//				}
				// rangeFinder.pulse(.02);

				SmartDashboard.putNumber("X", poseidon.getMagX());
				SmartDashboard.putNumber("Y", poseidon.getMagY());
				SmartDashboard.putNumber("Z", poseidon.getMagZ());
				SmartDashboard.putNumber("ATanXY", Math.atan2(poseidon.getMagX(), poseidon.getMagY()));
				SmartDashboard.putNumber("ATanZY", Math.atan2(poseidon.getMagZ(), poseidon.getMagY()));
				SmartDashboard.putNumber("ATanXZ", Math.atan2(poseidon.getMagX(), poseidon.getMagZ()));

				SmartDashboard.putNumber("CX", compass.getRawCompX());
				SmartDashboard.putNumber("CY", compass.getRawCompY());
				SmartDashboard.putNumber("CZ", compass.getRawCompZ());

				SmartDashboard.putNumber("Heading", poseidon.getHeading());

				//SmartDashboard.putNumber("RangeFinder ft", Constants.RFVoltsToFt(rangeFinder.getRangeInFeet()));
				// SmartDashboard.putNumber("Range1 Finder ft", Constants.RFVoltsToFt(range1.getVoltage()));
				// SmartDashboard.putNumber("Range2 Finder ft", Constants.RFVoltsToFt(range2.getVoltage()));
				// SmartDashboard.putNumber("Range3 Finder ft", Constants.RFVoltsToFt(range3.getVoltage()));
				// SmartDashboard.putNumber("Range4 Finder ft", Constants.RFVoltsToFt(range4.getVoltage()));
				// SmartDashboard.putNumber("Range finder Volts", range1.getVoltage());

				SmartDashboard.putNumber("Left Encoder", RobotMap.left1.getEncPosition());
				SmartDashboard.putNumber("Right Encoder", RobotMap.right1.getEncPosition());

				SmartDashboard.putNumber("Alpha", variableStore.GetDouble(CompassReader.compassAlphaVariableName, 0));
				SmartDashboard.putNumber("Beta", variableStore.GetDouble(CompassReader.compassBetaVariableName, 0));

				if (compCal1.isPressed())
					{// && compCal2.Pressedp()) {
						// auto cal
						tempy = poseidon.getMagX();
						tempz = poseidon.getMagZ();
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

				headPres.update();
				brakes.update();
				compCal1.update();
				compCalReset.update();
				slowMo.update();
				tipHat.update();
				poopGear.update();
				facePush.update();
				intake.update();
				agitate.update();
				shoot.update();
				shooter.adjustVoltage();
				//rangeBasedGearScorer.update();
			}
	}
