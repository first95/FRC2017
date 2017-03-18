package org.usfirst.frc.team95.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
import org.usfirst.frc.team95.robot.auto.ScoreFromStart;
import org.usfirst.frc.team95.robot.auto.ScoreFromStartStageTwo;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to each mode, as described in the IterativeRobot documentation. If you change the name of this class or the package after creating this project, you must also update the manifest file in the resource directory.
 */
public class Robot extends IterativeRobot
	{

		// This boolean and moveIt are used to run an automove on the press of a button
		boolean runOnceTest = true;
		GoToLiftAdvanced moveItToLift = new GoToLiftAdvanced();

		SendableChooser chooser;

		GyroReader gyro;
		VariableStore variableStore;
		ADIS16448_IMU poseidon;
		HeadingPreservation header;
		PowerDistributionPanel panel;
		double ymin, ymax, zmin, zmax, alpha, beta, tempy, tempz;

		Double headingToPres;
		double dist;
		Double[] angleRec;
		boolean twoStickMode, boop, agit;
		ButtonTracker headPres, compCal1, compCalReset, slowMo, changeDriveMode, brakes, tipHat, facePush, poopGear, incPID, decPID, alignToGearLiftAndDrive, dropFloorAcquisitionMechanism, intakeFloorGear, outFloorGear;

		Auto move;
		SendableChooser a, b, c;
		RangeBasedGearScorer rangeBasedGearScorer;

		/**
		 * This function is run when the robot is first started up and should be used for any initialization code.
		 */
		public void robotInit()
			{

				alpha = 0;
				beta = 0;

				RobotMap.init();

				// RobotMap.visionProcessingInit();

				boop = false;
				agit = false;

				chooser = new SendableChooser();
				// chooser.addDefault("Default Auto", new ExampleCommand());
				// chooser.addObject("My Auto", new MyAutoCommand());
				SmartDashboard.putData("Auto mode", chooser);
				variableStore = new VariableStore();
				poseidon = new ADIS16448_IMU(variableStore);
				header = new HeadingPreservation(poseidon);
				// shooter = new VoltageCompensatedShooter(RobotMap.shooter, 4);

				panel = new PowerDistributionPanel();
				twoStickMode = true;

				// drive buttons
				changeDriveMode = new ButtonTracker(Constants.driveStick, 4);
				brakes = new ButtonTracker(Constants.driveStick, 1);
				slowMo = new ButtonTracker(Constants.driveStick, 6);
				compCal1 = new ButtonTracker(Constants.driveStick, 7);
				compCalReset = new ButtonTracker(Constants.driveStick, 8);

				// weapon buttons
				tipHat = new ButtonTracker(Constants.weaponStick, 1);
				poopGear = new ButtonTracker(Constants.weaponStick, 2);
				facePush = new ButtonTracker(Constants.weaponStick, 5);
				alignToGearLiftAndDrive = new ButtonTracker(Constants.weaponStick, 7);
				// intake = new ButtonTracker(Constants.weaponStick, 3);
				intakeFloorGear = new ButtonTracker(Constants.weaponStick, 3);
				// agitate = new ButtonTracker(Constants.weaponStick, 4);
				dropFloorAcquisitionMechanism = new ButtonTracker(Constants.weaponStick, 6);
				// shoot = new ButtonTracker(Constants.weaponStick, 6);
				// scoreFloorGear = new ButtonTracker(Constants.weaponStick, 6);
				outFloorGear = new ButtonTracker(Constants.weaponStick, 4);

				// rangeFinder = new RangeFinder(initiateRangeFinder, new AnalogInput[]
				// { range1, range2 });
				// rangeBasedGearScorer = new RangeBasedGearScorer(RobotMap.gearPooper, RobotMap.pushFaceOut, rangeFinder);

				a = new SendableChooser();
				b = new SendableChooser();
				c = new SendableChooser();
				a.addDefault("None", new Nothing());
				a.addObject("Go Forward", new DistanceMovePID(7));
				a.addObject("Go Backward", new DistanceMove(-0.3, -0.3, 5));
				a.addObject("Turn 60 Right", new RotateBy((Math.PI / 180) * 60));
				a.addObject("Turn 60 left", new RotateBy((Math.PI / 180) * -60));

				// Automoves to Test, One Turns, One Moves and Turns
				a.addObject("red left", new ScoreFromStart(true, 0, poseidon));
				a.addObject("red mid", new ScoreFromStart(true, 1, poseidon));
				a.addObject("red right", new ScoreFromStart(true, 2, poseidon));
				a.addObject("blue left", new ScoreFromStart(false, 0, poseidon));
				a.addObject("blue mid", new ScoreFromStart(false, 1, poseidon));
				a.addObject("blue right", new ScoreFromStart(false, 2, poseidon));
				a.addObject("GoToLiftAdvanced", new GoToLiftAdvanced());
				a.addObject("AtLiftRotate", new AtLiftRotate(poseidon));
				a.addObject("Score Gear", new ScoreGear());

				b.addDefault("None", new Nothing());
				b.addObject("Score Gear From Start Stage Two", new ScoreFromStartStageTwo(poseidon));
				b.addObject("Go Forward", new DistanceMove(0.1, 0, 1));
				b.addObject("Go Backward", new DistanceMove(-0.3, -0.3, 5));
				b.addObject("Turn 45 Right", new RotateBy(Math.PI / 4));
				b.addObject("Turn 45 Left", new RotateBy(-Math.PI / 4));
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
		 * This function is called once each time the robot enters Disabled mode. You can use it to reset any subsystem information you want to clear when the robot is disabled.
		 */
		public void disabledInit()
			{
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
		 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and uncomment the getString code to get the auto name from the text box below the Gyro You can add additional auto modes by adding additional commands to the chooser code above (like the commented example) or additional comparisons to the switch structure below with additional strings & commands.
		 */
		public void autonomousInit()
			{
				RobotMap.left1.enableBrakeMode(true);
				RobotMap.left2.enableBrakeMode(true);
				RobotMap.left3.enableBrakeMode(true);
				RobotMap.right1.enableBrakeMode(true);
				RobotMap.right2.enableBrakeMode(true);
				RobotMap.right3.enableBrakeMode(true);
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
				RobotMap.left1.enableBrakeMode(true);
				RobotMap.left2.enableBrakeMode(true);
				RobotMap.left3.enableBrakeMode(true);
				RobotMap.right1.enableBrakeMode(true);
				RobotMap.right2.enableBrakeMode(true);
				RobotMap.right3.enableBrakeMode(true);
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

				// drive
				if (slowMo.isPressed())
					{
						RobotMap.drive.halfArcade(Constants.driveStick, twoStickMode);
					}
				else
					{
						RobotMap.drive.arcade(Constants.driveStick, twoStickMode);
					}

				// Pneumatics
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
				/*
				 * if (facePush.wasJustPressed()) { rangeBasedGearScorer.start(); } else if (facePush.wasJustReleased()) { rangeBasedGearScorer.stop(); }
				 */

				// RobotMap.intake.set(-Constants.weaponStick.getRawAxis(2));

				RobotMap.brakes.set(brakes.isPressed());

				// RobotMap.andyBooper9000.set(boop);

				RobotMap.lowerFloorLifter.set(dropFloorAcquisitionMechanism.isPressed());
				if (intakeFloorGear.isPressed())
					{
						RobotMap.floorIntake.set(-Constants.FLOOR_INTAKE_THROTTLE);
					}
				else if (outFloorGear.isPressed())
					{
						RobotMap.floorIntake.set(Constants.FLOOR_INTAKE_THROTTLE);
					}
				else
					{
						RobotMap.floorIntake.set(0);
					}

				// This runs the gotoLiftAdvanced automove when 7(select) on the weapon stick is pressed
				// It only runs when the button is held down
				if (alignToGearLiftAndDrive.isPressed())
					{
						if (runOnceTest)
							{
								moveItToLift.init();
								runOnceTest = false;
							}
						else
							{
								moveItToLift.update();
							}
					}
				else
					{
						if (!runOnceTest)
							{
								moveItToLift.stop();
							}

						runOnceTest = true;
					}

				/*
				 * if (shoot.wasJustPressed()) { shooter.turnOn(); } else if (shoot.wasJustReleased()) { shooter.turnOff(); }
				 */

				if (Math.abs(Constants.weaponStick.getY()) > .1)
					{
						RobotMap.winchRight.set(Constants.weaponStick.getY());
						RobotMap.winchLeft.set(-Constants.weaponStick.getY());
					}
				else
					{
						RobotMap.winchRight.set(0);
						RobotMap.winchLeft.set(0);
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

				// SmartDashboard.putNumber("Talon Left 1 Output Current", RobotMap.left1.getOutputCurrent());
				// SmartDashboard.putNumber("Talon Right 1 Output Current", RobotMap.right1.getOutputCurrent());

				// RobotMap.gearLiftFinder.computeHeadingToTarget();

				// System.out.println(RobotMap.gearLiftFinder.getHeadingToTargetDegrees());
				// RobotMap.smartDashboardVideoOutput.putFrame(RobotMap.gearLiftFinder.getAnnotatedFrame());

				// Show the edited video output from the camera
				// if (!RobotMap.visionProcessingActive)
				// {
				// RobotMap.gearLiftFinder.computeHeadingToTarget();
				// RobotMap.smartDashboardVideoOutput.putFrame(RobotMap.gearLiftFinder.getAnnotatedFrame());
				// SmartDashboard.putNumber("Hight Of Object In Pixels", RobotMap.gearLiftFinder.heightOfObjectInPixels);
				// SmartDashboard.putNumber("Distance From Cam To Target IN INCHES", RobotMap.gearLiftFinder.distanceFromCamToTarget);
				// SmartDashboard.putNumber("Degree Offset (X)", RobotMap.gearLiftFinder.getHeadingToTargetDegrees());
				// SmartDashboard.putBoolean("We can see the target", RobotMap.gearLiftFinder.haveValidHeading());
				// }
				// else
				// {
				// SmartDashboard.putString("Hight Of Object In Pixels", "Processing Not Active");
				// SmartDashboard.putString("Distance From Cam To Target IN INCHES", "Processing Not Active");
				// SmartDashboard.putString("Degree Offset (X)", "Processing Not Active");
				// SmartDashboard.putString("We can see the target", "Processing Not Active");
				// }

				SmartDashboard.putNumber("Heading", poseidon.getHeading());

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

						poseidon.compCal(alpha, beta);
					}

				brakes.update();
				compCal1.update();
				compCalReset.update();
				slowMo.update();
				tipHat.update();
				poopGear.update();
				facePush.update();
				intakeFloorGear.update();
				outFloorGear.update();
				dropFloorAcquisitionMechanism.update();
				// rangeBasedGearScorer.update();
			}
	}
