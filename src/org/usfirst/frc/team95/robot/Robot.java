package org.usfirst.frc.team95.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team95.robot.auto.Auto;
import org.usfirst.frc.team95.robot.auto.DistanceMove;
import org.usfirst.frc.team95.robot.auto.DistanceMovePID;
import org.usfirst.frc.team95.robot.auto.GoToLiftAdvanced;
import org.usfirst.frc.team95.robot.auto.Nothing;
import org.usfirst.frc.team95.robot.auto.RotateBy;
import org.usfirst.frc.team95.robot.auto.RotateBy2Enc;
import org.usfirst.frc.team95.robot.auto.RotateByUntilVision;
import org.usfirst.frc.team95.robot.auto.RotateGoToLift;
import org.usfirst.frc.team95.robot.auto.ScoreGear;
import org.usfirst.frc.team95.robot.auto.SequentialMove;
import org.usfirst.frc.team95.robot.auto.ScoreFromStart;
import org.usfirst.frc.team95.robot.auto.ScoreFromStartStageTwo;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to each mode, as described in the
 * IterativeRobot documentation. If you change the name of this class or the package after creating this project, you must also
 * update the manifest file in the resource directory.
 */
public class Robot extends IterativeRobot
	{

		// THESE VARS ARE USED TO RUN A GOTOLIFYADVANCED AUTOMOVE ON THE PRESS OF A BUTTON -- CURRENTLY NOT BEING USED:
		boolean moveItToTestCheck = true;
		GoToLiftAdvanced moveItToLift = new GoToLiftAdvanced();
		boolean runOnce = true;
		double maxFloorIntakeCurrent = 2;
		double lastKnownGearCurrent;
		boolean gearInGroundLoader = false;
		boolean autoGearInGroundLoaderJustRan = false;
		Timer gearCurrentTimer;

		SendableChooser chooser;

		VariableStore variableStore;
		ADIS16448_IMU poseidon;
		PowerDistributionPanel panel;
		double ymin, ymax, zmin, zmax, alpha, beta, tempy, tempz;

		AnalogInput artemis;

		double dist;
		Double[] angleRec;
		boolean gotGear;
		ButtonTracker compCal1, compCalReset, slowMo, brakes, tipHat, facePush, poopGear, dropGroundLoader, intakeFloorGear, outFloorGear, autoPickerUpper;

		ButtonTracker testSwitchCam;

		Auto move;
		SendableChooser a, b, c;
		Timer cycleTimer;

		/**
		 * This function is run when the robot is first started up and should be used for any initialization code.
		 */
		public void robotInit()
			{
				gearCurrentTimer = new Timer();
				cycleTimer = new Timer();
				alpha = 0;
				beta = 0;

				RobotMap.init();

				//RobotMap.visionProcessingInit();
				
				chooser = new SendableChooser();
				// chooser.addDefault("Default Auto", new ExampleCommand());
				// chooser.addObject("My Auto", new MyAutoCommand());
				SmartDashboard.putData("Auto mode", chooser);

				artemis = new AnalogInput(0);
				variableStore = new VariableStore();
				poseidon = new ADIS16448_IMU(variableStore);
				panel = new PowerDistributionPanel();
				gotGear = false;
				// shooter = new VoltageCompensatedShooter(RobotMap.shooter, 4);

				// DRIVE BUTTONS:
				brakes = new ButtonTracker(Constants.driveStick, 1); // A
				slowMo = new ButtonTracker(Constants.driveStick, 6); // R Bumber
				compCal1 = new ButtonTracker(Constants.driveStick, 7); // Select
				compCalReset = new ButtonTracker(Constants.driveStick, 8); // Start
				// changeDriveMode = new ButtonTracker(Constants.driveStick, 4);

				// WEAPON BUTTONS:
				tipHat = new ButtonTracker(Constants.weaponStick, 1); // A
				autoPickerUpper = new ButtonTracker(Constants.weaponStick, 7);
				poopGear = new ButtonTracker(Constants.weaponStick, 2); // B
				facePush = new ButtonTracker(Constants.weaponStick, 5); // L Bumber
				dropGroundLoader = new ButtonTracker(Constants.weaponStick, 6); // R Bumber
				intakeFloorGear = new ButtonTracker(Constants.weaponStick, 3); // X
				outFloorGear = new ButtonTracker(Constants.weaponStick, 4); // Y
				// testSwitchCam = new ButtonTracker(Constants.weaponStick, 7);
				// intake = new ButtonTracker(Constants.weaponStick, 3);
				// rangeFinder = new RangeFinder(initiateRangeFinder, new AnalogInput[] { range1, range2 });
				// rangeBasedGearScorer = new RangeBasedGearScorer(RobotMap.gearPooper, RobotMap.pushFaceOut, rangeFinder);
				// agitate = new ButtonTracker(Constants.weaponStick, 4);
				// shoot = new ButtonTracker(Constants.weaponStick, 6);
				// scoreFloorGear = new ButtonTracker(Constants.weaponStick, 6);

				a = new SendableChooser();
				b = new SendableChooser();
				c = new SendableChooser();

				a.addDefault("None", new Nothing());
				
				a.addObject("Test Go Forward", new DistanceMovePID((69.68) / 12));
				a.addObject("Test Rotate, left", new RotateBy(-60 * (Math.PI / 180)));
				a.addObject("Test 2Enc Rotate", new RotateBy2Enc(-60 * (Math.PI / 180)));
				a.addObject("Test Roate With Vision", new RotateByUntilVision(-60 * (Math.PI / 180)));
				a.addObject("Test Vision Rotate + Go TO Lift", new RotateGoToLift());
				
				a.addObject("Go Forward", new DistanceMovePID(7));
				a.addObject("Go Backward", new DistanceMove(-0.3, -0.3, 5));
				a.addObject("Turn 60 Right", new RotateBy((Math.PI / 180) * 60));
				a.addObject("Turn 60 left", new RotateBy((Math.PI / 180) * -60));

				// SCORE GEARS FROM STARTING POSITION:
				a.addObject("Red Left", new ScoreFromStart(true, 0, poseidon));
				a.addObject("Red Middle", new ScoreFromStart(true, 1, poseidon));
				a.addObject("Red Right", new ScoreFromStart(true, 2, poseidon));
				a.addObject("Blue Left", new ScoreFromStart(false, 0, poseidon));
				a.addObject("Blue Middle", new ScoreFromStart(false, 1, poseidon));
				a.addObject("Blue Right", new ScoreFromStart(false, 2, poseidon));
				// a.addObject("GoToLiftAdvanced", new GoToLiftAdvanced());
				// a.addObject("AtLiftRotate", new AtLiftRotate(poseidon));
				// a.addObject("Score Gear", new ScoreGear());
				b.addObject("Score Gear Stage Two", new ScoreFromStartStageTwo(poseidon));

				b.addDefault("None", new Nothing());
				b.addObject("Go Forward", new DistanceMove(0.1, 0, 1));
				b.addObject("Go Backward", new DistanceMove(-0.3, -0.3, 5));
				b.addObject("Turn 45 Right", new RotateBy(Math.PI / 4));
				b.addObject("Left", new RotateBy(-Math.PI / 4));
				b.addObject("Score Gear", new ScoreGear());

				c.addDefault("None", new Nothing());
				c.addObject("Go Forward", new DistanceMove(0.3, 0.3, 5));
				c.addObject("Go Backward", new DistanceMove(-0.3, -0.3, 5));
				c.addObject("Turn 45 Right", new RotateBy(Math.PI / 4));
				c.addObject("Turn 45 Left", new RotateBy(-Math.PI / 4));

				SmartDashboard.putData("1st", a);
				SmartDashboard.putData("2nd", b);
				SmartDashboard.putData("3rd", c);

			}

		/**
		 * This function is called once each time the robot enters Disabled mode. You can use it to reset any subsystem
		 * information you want to clear when the robot is disabled.
		 */
		public void disabledInit()
			{

				// WHEN DISABLE ROBOT DISABLE BRAKES:
				RobotMap.left1.enableBrakeMode(false);
				RobotMap.left2.enableBrakeMode(false);
				RobotMap.left3.enableBrakeMode(false);
				RobotMap.right1.enableBrakeMode(false);
				RobotMap.right2.enableBrakeMode(false);
				RobotMap.right3.enableBrakeMode(false);

			}

		public void disabledPeriodic()
			{

				commonPeriodic();
				Scheduler.getInstance().run();

			}

		/**
		 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes using the
		 * dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard, remove
		 * all of the chooser code and uncomment the getString code to get the auto name from the text box below the Gyro You can
		 * add additional auto modes by adding additional commands to the chooser code above (like the commented example) or
		 * additional comparisons to the switch structure below with additional strings & commands.
		 */
		public void autonomousInit()
			{

				// WHEN ENABLE ROBOT, ENABLE BRAKES:
				RobotMap.left1.enableBrakeMode(true);
				RobotMap.left2.enableBrakeMode(true);
				RobotMap.left3.enableBrakeMode(true);
				RobotMap.right1.enableBrakeMode(true);
				RobotMap.right2.enableBrakeMode(true);
				RobotMap.right3.enableBrakeMode(true);

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
				move.init();
				move.start();

			}

		/**
		 * This function is called periodically during autonomous
		 */
		public void autonomousPeriodic()
			{

				commonPeriodic();
				move.update();
				Scheduler.getInstance().run();

			}

		public void teleopInit()
			{
				cycleTimer.reset();
				cycleTimer.start();
				// WHEN ENABLE ROBOT, ENABLE BRAKES:
				RobotMap.left1.enableBrakeMode(true);
				RobotMap.left1.enableBrakeMode(true);
				RobotMap.left2.enableBrakeMode(true);
				RobotMap.left3.enableBrakeMode(true);
				RobotMap.right1.enableBrakeMode(true);
				RobotMap.right2.enableBrakeMode(true);
				RobotMap.right3.enableBrakeMode(true);

				// AFTER AUTO AND VISION IS DONE, SWITCH CAMS -- FRONT TO BACK:
				RobotMap.switchVisionCameras();

				/*
				 * This makes sure that the autonomous stops running when / teleop starts running. If you want the autonomous to /
				 * continue until interrupted by another command, remove / this line or comment it out.
				 */
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

				// SLOW MOVE DRIVE:
				if (slowMo.isPressed())
					{
						RobotMap.drive.halfArcade(Constants.driveStick, true);
					}
				else
					{
						RobotMap.drive.arcade(Constants.driveStick, true);
					}

				// PNEUMATICS:
				if (tipHat.isPressed())
					{
						RobotMap.hatTip.set(true);
						RobotMap.gearPooper.set(false);
					}
				else
					{
						RobotMap.hatTip.set(false);
						RobotMap.gearPooper.set(poopGear.isPressed());
					}
				RobotMap.pushFaceOut.set(facePush.isPressed());

				// if (facePush.wasJustPressed()) { rangeBasedGearScorer.start(); } else if (facePush.wasJustReleased())
				// {rangeBasedGearScorer.stop(); }

				// RobotMap.intake.set(-Constants.weaponStick.getRawAxis(2));

				RobotMap.brakes.set(brakes.isPressed());

				if (autoPickerUpper.wasJustReleased())
					{
						autoGearInGroundLoaderJustRan = false;
						RobotMap.lowerFloorLifter.set(false);
						RobotMap.floorIntake.set(0);
						gearCurrentTimer.stop();
					}

				if (autoPickerUpper.isPressed() && !autoGearInGroundLoaderJustRan)
					{

						RobotMap.lowerFloorLifter.set(true);
						// System.out.println("Auto Picker Upper FloorLifter Deploy");

						RobotMap.floorIntake.set(-Constants.FLOOR_INTAKE_THROTTLE);
						// System.out.println("Roll Intake");

						if (RobotMap.floorIntake.getOutputCurrent() > maxFloorIntakeCurrent)
							{
								if (lastKnownGearCurrent < maxFloorIntakeCurrent)
									{
										gearCurrentTimer.reset();
										gearCurrentTimer.start();
									}
								if (gearCurrentTimer.get() > 1.2 && (RobotMap.floorIntake.getOutputCurrent() > maxFloorIntakeCurrent))
									{
										gearInGroundLoader = true;
										// System.out.println("Gear In Gorund Loader");
										gearCurrentTimer.stop();
									}
							}

						if (gearInGroundLoader)
							{
								// System.out.println("Shutting Off");
								autoGearInGroundLoaderJustRan = true;
								RobotMap.floorIntake.set(0);
								RobotMap.lowerFloorLifter.set(false);
							}
					}
				else
					{

						if (intakeFloorGear.isPressed())
							{

								RobotMap.floorIntake.set(-Constants.FLOOR_INTAKE_THROTTLE);

								if (RobotMap.floorIntake.getOutputCurrent() > maxFloorIntakeCurrent)
									{
										if (lastKnownGearCurrent < maxFloorIntakeCurrent)
											{
												gearCurrentTimer.reset();
												gearCurrentTimer.start();
											}
										if (gearCurrentTimer.get() > .8 && (RobotMap.floorIntake.getOutputCurrent() > maxFloorIntakeCurrent))
											{
												gearInGroundLoader = true;
												gearCurrentTimer.stop();
											}
									}

							}
						else if (outFloorGear.isPressed())
							{
								RobotMap.floorIntake.set(Constants.FLOOR_INTAKE_THROTTLE);

								if (RobotMap.floorIntake.getOutputCurrent() < maxFloorIntakeCurrent)
									{
										gearInGroundLoader = false;
									}
							}
						else
							{
								RobotMap.floorIntake.set(0);
							}

						RobotMap.lowerFloorLifter.set(dropGroundLoader.isPressed());

						if (dropGroundLoader.wasJustPressed())
							{
								autoGearInGroundLoaderJustRan = false;
								gearInGroundLoader = false;
							}

					}

				// CLIMBER:
				if (Math.abs(Constants.weaponStick.getY()) > .18)

					{
						RobotMap.winchRight.set(Constants.weaponStick.getY());
						RobotMap.winchLeft.set(-Constants.weaponStick.getY());
					}
				else
					{
						RobotMap.winchRight.set(0);
						RobotMap.winchLeft.set(0);
					}

				// if (testSwitchCam.isPressed())
				// {
				//
				// System.out.println("TRYING TO CLOSE CAM");
				// RobotMap.switchVisionCameras();
				//
				// }

				// System.out.println(cycleTimer.get());
				lastKnownGearCurrent = RobotMap.floorIntake.getOutputCurrent();

			}

		/**
		 * This function is called periodically during test mode
		 */
		public void testPeriodic()
			{

				LiveWindow.run();

			}

		/*
		 * This is run in disabled, teleop, and auto periodics.
		 */
		public void commonPeriodic()
			{

				//RobotMap.gearLiftFinder.computeHeadingToTarget();
				
				//RobotMap.smartDashboardVideoOutput.putFrame(RobotMap.gearLiftFinder.getAnnotatedFrame());
				
				// SmartDashboard.putNumber("Talon
				// Left 1 Output Current",
				// RobotMap.left1.getOutputCurrent());
				// SmartDashboard.putNumber("Talon
				// Right 1 Output Current",
				// RobotMap.right1.getOutputCurrent());

				// RobotMap.gearLiftFinder.computeHeadingToTarget();

				// System.out.println(RobotMap.gearLiftFinder.getHeadingToTargetDegrees());
				// RobotMap.smartDashboardVideoOutput.putFrame(RobotMap.gearLiftFinder.getAnnotatedFrame());

				// Show the edited video output
				// from the camera
				// if
				// (!RobotMap.visionProcessingActive)
				// {
				// RobotMap.gearLiftFinder.computeHeadingToTarget();
				// RobotMap.smartDashboardVideoOutput.putFrame(RobotMap.gearLiftFinder.getAnnotatedFrame());
				// SmartDashboard.putNumber("Hight
				// Of Object In Pixels",
				// RobotMap.gearLiftFinder.heightOfObjectInPixels);
				// SmartDashboard.putNumber("Distance
				// From Cam To Target IN INCHES",
				// RobotMap.gearLiftFinder.distanceFromCamToTarget);
				// SmartDashboard.putNumber("Degree
				// Offset (X)",
				// RobotMap.gearLiftFinder.getHeadingToTargetDegrees());
				// SmartDashboard.putBoolean("We
				// can see the target",
				// RobotMap.gearLiftFinder.haveValidHeading());
				// }
				// else
				// {
				// SmartDashboard.putString("Hight
				// Of Object In Pixels",
				// "Processing Not Active");
				// SmartDashboard.putString("Distance
				// From Cam To Target IN INCHES",
				// "Processing Not Active");
				// SmartDashboard.putString("Degree
				// Offset (X)", "Processing Not
				// Active");
				// SmartDashboard.putString("We
				// can see the target",
				// "Processing Not Active");
				// }

				// SMART DAHSBOARD OUTPUT:
				SmartDashboard.putBoolean("Ground Loaded Gear", gotGear);
				SmartDashboard.putNumber("Heading", poseidon.getHeading());
				SmartDashboard.putNumber("Left Encoder", RobotMap.left1.getEncPosition());
				SmartDashboard.putNumber("Right Encoder", RobotMap.right1.getEncPosition());
				SmartDashboard.putNumber("Alpha", variableStore.GetDouble(CompassReader.compassAlphaVariableName, 0));
				SmartDashboard.putNumber("Beta", variableStore.GetDouble(CompassReader.compassBetaVariableName, 0));
				SmartDashboard.putNumber("Voltage", panel.getVoltage());
				SmartDashboard.putNumber("CurrentR", RobotMap.right1.getOutputCurrent());
				SmartDashboard.putNumber("CurrentL", RobotMap.left1.getOutputCurrent());

				SmartDashboard.putNumber("Current Floor Intake", RobotMap.floorIntake.getOutputCurrent());
				
				//System.out.println("LEFT ENC: " + RobotMap.left1.getEncPosition());
				//System.out.println("RIGHT ENC: " + RobotMap.right1.getEncPosition());

				/*
				 * compass calibration. button tracker is disabled (not updated)
				 */
				if (compCal1.isPressed())
					{// && compCal2.Pressedp()) {
						// AUTO CAL
						tempy = poseidon.getMagX();
						tempz = poseidon.getMagZ();
						if (compCal1.wasJustPressed())
							{// &&
								// compCal2.justPressedp())
								// {
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

						poseidon.compCal(alpha, beta);

					}

				SmartDashboard.putBoolean("Gear In Ground Loader", gearInGroundLoader);

				// UPDATES:
				brakes.update();
				slowMo.update();
				tipHat.update();
				poopGear.update();
				facePush.update();
				intakeFloorGear.update();
				outFloorGear.update();
				dropGroundLoader.update();
				// compCal1.update();
				// compCalReset.update();

			}
	}