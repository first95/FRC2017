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

import org.usfirst.frc.team95.robot.auto.AlternateScoreGearFromStartStageTwo;
import org.usfirst.frc.team95.robot.auto.Auto;
import org.usfirst.frc.team95.robot.auto.DistanceMove;
import org.usfirst.frc.team95.robot.auto.DistanceMovePID;
import org.usfirst.frc.team95.robot.auto.MagicMotionAuto;
import org.usfirst.frc.team95.robot.auto.Nothing;
import org.usfirst.frc.team95.robot.auto.RotateAndScoreGear;
import org.usfirst.frc.team95.robot.auto.RotateBy;
import org.usfirst.frc.team95.robot.auto.RotateByUntilVision;
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

		// Variables That Are Used In Auto Pickup Of A Ground Gear
		private Timer gearCurrentTimer;
		private double lastKnownGearCurrent;
		private boolean gearInGroundLoader = false;
		private boolean autoGearInGroundLoaderJustRan = false;

		// Other Variables
		private SendableChooser chooser;
		private VariableStore variableStore;
		private ADIS16448_IMU poseidon;
		private PowerDistributionPanel panel;
		private double ymin, ymax, zmin, zmax, alpha, beta, tempy, tempz;
		private AnalogInput artemis;
		private double dist;
		private Double[] angleRec;
		private boolean gotGear, compressorMode;
		private ButtonTracker compCal1, compCalReset, slowMo, brakes, 
		tipHat, facePush, poopGear, dropGroundLoader, intakeFloorGear, 
		outFloorGear, autoPickerUpper, disableCompressor;
		private Auto move;
		private SendableChooser a, b, c;
		private Timer cycleTimer;

		/**
		 * This function is run when the robot is first started up and should be used for any initialization code.
		 */
		public void robotInit()
			{

				// This Needs To Be Above RobotMap.init()!
				RobotMap.debugModeEnabled = true;

				RobotMap.init();

				// Vision Debug Mode: This will shows the vision output on SmartDashboard if enabled
				if (RobotMap.debugModeEnabled)
					{
						RobotMap.visionProcessingInit();
					}

				gearCurrentTimer = new Timer();

				alpha = 0;
				beta = 0;

				chooser = new SendableChooser();
				SmartDashboard.putData("Auto mode", chooser);

				artemis = new AnalogInput(0);
				variableStore = new VariableStore();
				poseidon = new ADIS16448_IMU(variableStore);
				panel = new PowerDistributionPanel();
				gotGear = false;
				compressorMode = true;
				// shooter = new VoltageCompensatedShooter(RobotMap.shooter, 4);

				// DRIVE BUTTONS:
				brakes = new ButtonTracker(Constants.DRIVE_STICK, 1); // A
				slowMo = new ButtonTracker(Constants.DRIVE_STICK, 6); // R Bumber
				compCal1 = new ButtonTracker(Constants.DRIVE_STICK, 7); // Select
				compCalReset = new ButtonTracker(Constants.DRIVE_STICK, 8); // Start
				// changeDriveMode = new ButtonTracker(Constants.driveStick, 4);

				// WEAPON BUTTONS:
				tipHat = new ButtonTracker(Constants.WEAPON_STICK, 1); // A
				autoPickerUpper = new ButtonTracker(Constants.WEAPON_STICK, 7);// select
				poopGear = new ButtonTracker(Constants.WEAPON_STICK, 2); // B
				facePush = new ButtonTracker(Constants.WEAPON_STICK, 5); // L Bumber
				dropGroundLoader = new ButtonTracker(Constants.WEAPON_STICK, 6); // R Bumber
				intakeFloorGear = new ButtonTracker(Constants.WEAPON_STICK, 3); // X
				outFloorGear = new ButtonTracker(Constants.WEAPON_STICK, 4); // Y
				disableCompressor = new ButtonTracker(Constants.WEAPON_STICK, 8); // start
				// intake = new ButtonTracker(Constants.weaponStick, 3);
				// rangeFinder = new RangeFinder(initiateRangeFinder, new AnalogInput[] { range1, range2 });
				// rangeBasedGearScorer = new RangeBasedGearScorer(RobotMap.gearPooper, RobotMap.pushFaceOut, rangeFinder);
				// agitate = new ButtonTracker(Constants.weaponStick, 4);
				// shoot = new ButtonTracker(Constants.weaponStick, 6);
				// scoreFloorGear = new ButtonTracker(Constants.weaponStick, 6);

				a = new SendableChooser();
				b = new SendableChooser();
				c = new SendableChooser();

				// SENDABLE CHOSER ONE:
				a.addDefault("None", new Nothing());
				a.addObject("Go Forward", new DistanceMovePID(7));
				a.addObject("Go Backward", new DistanceMove(-0.3, -0.3, 5));
				a.addObject("Turn 60 Right", new RotateBy((Math.PI / 180) * 60));
				a.addObject("Turn 60 left", new RotateBy((Math.PI / 180) * -60));

				// TEST MOVES:
				// a.addObject("Test - Go Forward", new DistanceMovePID((69.68) / 12));
				// a.addObject("Test - RotateBy Left", new RotateBy(-60 * (Math.PI / 180)));
				// a.addObject("Test - Two Encoder Rotate Left", new RotateByWithTwoEncoders(-60 * (Math.PI / 180)));
				// a.addObject("Test - Rotate Left With Vision", new RotateByUntilVision2Enc(-60 * (Math.PI / 180)));
				a.addObject("Test - Rotate Right With Vision and GoToLift", new RotateAndScoreGear(60 * (Math.PI / 180)));
				a.addObject("Test - Rotate Right With Vision and One Encoder", new RotateByUntilVision(60 * (Math.PI / 180)));
				a.addObject("rotate 90", new RotateBy(Math.PI / 2));
				a.addObject("rotate -90", new RotateBy(-Math.PI / 2));
				a.addObject("go 5 feet", new DistanceMovePID(5));
				a.addObject("Motion Profile Stright", new MagicMotionAuto(5));
				// SCORE GEARS FROM STARTING POSITION:
				a.addObject("Red Left", new ScoreFromStart(true, 0));
				a.addObject("Red Middle", new ScoreFromStart(true, 1));
				a.addObject("Red Right", new ScoreFromStart(true, 2));
				a.addObject("Blue Left", new ScoreFromStart(false, 0));
				a.addObject("Blue Middle", new ScoreFromStart(false, 1));
				a.addObject("Blue Right", new ScoreFromStart(false, 2));

				// SENDABLE CHOSER TWO:
				b.addObject("Score Gear Stage Two", new ScoreFromStartStageTwo());
				b.addObject("Score Gear Stage Two Alternate", new AlternateScoreGearFromStartStageTwo());
				b.addDefault("None", new Nothing());
				b.addObject("Go Forward", new DistanceMove(0.1, 0, 1));
				b.addObject("Go Backward", new DistanceMove(-0.3, -0.3, 5));
				b.addObject("Turn 45 Right", new RotateBy(Math.PI / 4));
				b.addObject("Left", new RotateBy(-Math.PI / 4));
				b.addObject("Score Gear", new ScoreGear());

				// SENDAZBLE CHOSER THREE:
				c.addDefault("None", new Nothing());
				c.addObject("Go Forward", new DistanceMove(0.3, 0.3, 5));
				c.addObject("Go Backward", new DistanceMove(-0.3, -0.3, 5));
				c.addObject("Turn 45 Right", new RotateBy(Math.PI / 4));
				c.addObject("Turn 45 Left", new RotateBy(-Math.PI / 4));

				// DISPLAY CHOSERS TO DASHBOARD:
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

				// RUN SELECTED AUTO MOVE:
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
						RobotMap.drive.halfArcade(Constants.DRIVE_STICK, true);
					}
				else
					{
						RobotMap.drive.arcade(Constants.DRIVE_STICK, true);
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

				// PUSH FACE:
				RobotMap.pushFaceOut.set(facePush.isPressed());

				// DEPLOY BRAKES:
				RobotMap.brakes.set(brakes.isPressed());

				// GROUND GEAR HANDLER CHECK:
				if (autoPickerUpper.wasJustReleased())
					{
						autoGearInGroundLoaderJustRan = false;
						RobotMap.lowerFloorLifter.set(false);
						RobotMap.floorIntake.set(0);
						gearCurrentTimer.stop();
					}

				// AUTO GROUND GEAR HANDLER:
				if (autoPickerUpper.isPressed() && !autoGearInGroundLoaderJustRan)
					{

						RobotMap.lowerFloorLifter.set(true);

						RobotMap.floorIntake.set(-Constants.FLOOR_INTAKE_THROTTLE);

						if (RobotMap.floorIntake.getOutputCurrent() > Constants.MAX_FLOOR_INTAKE_CURRENT)
							{
								if (lastKnownGearCurrent < Constants.MAX_FLOOR_INTAKE_CURRENT)
									{
										gearCurrentTimer.reset();
										gearCurrentTimer.start();
									}
								if (gearCurrentTimer.get() > 1.2 && (RobotMap.floorIntake.getOutputCurrent() > Constants.MAX_FLOOR_INTAKE_CURRENT))
									{
										gearInGroundLoader = true;
										gearCurrentTimer.stop();
									}
							}

						if (gearInGroundLoader)
							{
								autoGearInGroundLoaderJustRan = true;
								RobotMap.floorIntake.set(0);
								RobotMap.lowerFloorLifter.set(false);
							}
					}

				// GROUND GEAR HANDLER
				else
					{

						if (intakeFloorGear.isPressed())
							{

								RobotMap.floorIntake.set(-Constants.FLOOR_INTAKE_THROTTLE);

								if (RobotMap.floorIntake.getOutputCurrent() > Constants.MAX_FLOOR_INTAKE_CURRENT)
									{
										if (lastKnownGearCurrent < Constants.MAX_FLOOR_INTAKE_CURRENT)
											{
												gearCurrentTimer.reset();
												gearCurrentTimer.start();
											}
										if (gearCurrentTimer.get() > .8 && (RobotMap.floorIntake.getOutputCurrent() > Constants.MAX_FLOOR_INTAKE_CURRENT))
											{
												gearInGroundLoader = true;
												gearCurrentTimer.stop();
											}
									}

							}
						else if (outFloorGear.isPressed())
							{
								RobotMap.floorIntake.set(Constants.FLOOR_INTAKE_THROTTLE);

								if (RobotMap.floorIntake.getOutputCurrent() < Constants.MAX_FLOOR_INTAKE_CURRENT)
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
				if (Math.abs(Constants.WEAPON_STICK.getY()) > .18)

					{
						RobotMap.winchRight.set(Constants.WEAPON_STICK.getY());
						RobotMap.winchLeft.set(-Constants.WEAPON_STICK.getY());
					}
				else
					{
						RobotMap.winchRight.set(0);
						RobotMap.winchLeft.set(0);
					}

				// DISABLE COMPRESSOR:
				if (disableCompressor.wasJustPressed())
					{
						compressorMode = !compressorMode;
					}
				if (compressorMode)
					{
						if (!RobotMap.compressor.enabled())
							{
								RobotMap.compressor.start();
							}
					}
				else
					{
						if (RobotMap.compressor.enabled())
							{
								RobotMap.compressor.stop();
							}
					}

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

				if (RobotMap.debugModeEnabled)
					{
						RobotMap.gearLiftFinder.computeHeadingToTarget();
						RobotMap.smartDashboardDebugVideoOutput.putFrame(RobotMap.gearLiftFinder.getAnnotatedFrame());
					}

				// SMART DAHSBOARD OUTPUT:
				SmartDashboard.putBoolean("Ground Loaded Gear", gotGear);
				SmartDashboard.putBoolean("Compressor", compressorMode);
				SmartDashboard.putNumber("Heading", poseidon.getHeading());
				SmartDashboard.putNumber("Left Encoder", RobotMap.left1.getEncPosition());
				SmartDashboard.putNumber("Right Encoder", RobotMap.right1.getEncPosition());
				SmartDashboard.putNumber("Alpha", variableStore.GetDouble(CompassReader.compassAlphaVariableName, 0));
				SmartDashboard.putNumber("Beta", variableStore.GetDouble(CompassReader.compassBetaVariableName, 0));
				SmartDashboard.putNumber("Voltage", panel.getVoltage());
				SmartDashboard.putNumber("CurrentR", RobotMap.right1.getOutputCurrent());
				SmartDashboard.putNumber("CurrentL", RobotMap.left1.getOutputCurrent());
				SmartDashboard.putNumber("Current Floor Intake", RobotMap.floorIntake.getOutputCurrent());

				/*
				 * Compass calibration. button tracker is disabled (not updated)
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

				// Display if Gear In In Ground Loader:
				SmartDashboard.putBoolean("Gear In Ground Loader", gearInGroundLoader);

				// UPDATES:
				disableCompressor.update();
				brakes.update();
				slowMo.update();
				tipHat.update();
				poopGear.update();
				facePush.update();
				intakeFloorGear.update();
				outFloorGear.update();
				dropGroundLoader.update();

			}
	}