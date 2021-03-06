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

import java.io.IOException;

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
import org.usfirst.frc.team95.robot.auto.TimedMove;
import org.usfirst.frc.team95.robot.auto.TimedScoreFromStart;
import org.usfirst.frc.team95.robot.auto.ScoreFromStart;
import org.usfirst.frc.team95.robot.auto.ScoreFromStartStageTwo;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to each mode, as described in the
 * IterativeRobot documentation. If you change the name of this class or the package after creating this project, you must also
 * update the manifest file in the resource directory.
 */
public class Robot extends IterativeRobot
	{

		private String autoMovePicked;

		// Variables That Are Used In Auto Pickup Of A Ground Gear
		private Timer gearCurrentTimer;
		private double lastKnownGearCurrent;
		private boolean gearInGroundLoader = false;
		private boolean autoGearInGroundLoaderJustRan = false;

		// Other Variables
		private SendableChooser chooser;
		private VariableStore variableStore;

		// private ADIS16448_IMU poseidon;

		private PowerDistributionPanel panel;
		private double ymin, ymax, zmin, zmax, alpha, beta, tempy, tempz;
		private AnalogInput artemis;
		private double dist;
		private Double[] angleRec;
		private boolean gotGear, compressorMode;
		private ButtonTracker compCal1, compCalReset, slowMo, brakes, tipHat, facePush, poopGear, dropGroundLoader, intakeFloorGear, outFloorGear, autoPickerUpper, disableCompressor;
		private Auto move;
		private SendableChooser a, b, c;
		private Timer cycleTimer;

		/**
		 * This function is run when the robot is first started up and should be used for any initialization code.
		 */
		public void robotInit()
			{

				RobotMap.systemLoggerTimer = new Timer();
				RobotMap.systemLoggerTimer.reset();
				RobotMap.systemLoggerTimer.start();

				// This Needs To Be Above RobotMap.init()!
				RobotMap.debugModeEnabled = false;

				RobotMap.init();

				////RobotMap.sL.SystemLoggerWriteRAW("Robot_Init");

				if (RobotMap.debugModeEnabled)
					{
						//RobotMap.sL.SystemLoggerWriteRAW("Vision_Debug_Mode_Active");

					}

				// Vision Debug Mode: This will shows the vision output on SmartDashboard if enabled
				if (RobotMap.debugModeEnabled)
					{
						RobotMap.visionProcessingInit();

						//RobotMap.sL.SystemLoggerWriteRAW("Starting Vision Processing");
					}

				gearCurrentTimer = new Timer();
				////RobotMap.sL.SystemLoggerWriteRAW("gearCurrentTimer_Created");


				alpha = 0;
				beta = 0;

				chooser = new SendableChooser();

				////RobotMap.sL.SystemLoggerWriteRAW("SendableChooser_Created");
				SmartDashboard.putData("Auto mode", chooser);

				artemis = new AnalogInput(0);
				////RobotMap.sL.SystemLoggerWriteRAW("artemis_Created");
				variableStore = new VariableStore();
				////RobotMap.sL.SystemLoggerWriteRAW("variableStore_Created");


				// poseidon = new ADIS16448_IMU(variableStore);

				panel = new PowerDistributionPanel();

				////RobotMap.sL.SystemLoggerWriteRAW("PowerDistributionPanel_Created");
				gotGear = false;
				////RobotMap.sL.SystemLoggerWriteRAW("gotGear_Set_To_False");
				compressorMode = true;
				////RobotMap.sL.SystemLoggerWriteRAW("compressorMode_Set_To_True");
				// shooter = new VoltageCompensatedShooter(RobotMap.shooter, 4);

				// DRIVE BUTTONS:
				brakes = new ButtonTracker(Constants.DRIVE_STICK, 8); // A
				//slowMo = new ButtonTracker(Constants.DRIVE_STICK, 6); // R Bumber
				//compCal1 = new ButtonTracker(Constants.DRIVE_STICK, 7); // Select
				//compCalReset = new ButtonTracker(Constants.DRIVE_STICK, 8); // Start
				////RobotMap.sL.SystemLoggerWriteRAW("Drive_Button_Created");

				// changeDriveMode = new ButtonTracker(Constants.driveStick, 4);

				// WEAPON BUTTONS:
				tipHat = new ButtonTracker(Constants.DRIVE_STICK, 1); // A
				autoPickerUpper = new ButtonTracker(Constants.WEAPON_STICK, 7);// select
				poopGear = new ButtonTracker(Constants.DRIVE_STICK, 2); // B
				facePush = new ButtonTracker(Constants.DRIVE_STICK, 5); // L Bumber
				dropGroundLoader = new ButtonTracker(Constants.DRIVE_STICK, 6); // R Bumber
				intakeFloorGear = new ButtonTracker(Constants.DRIVE_STICK, 3); // X
				outFloorGear = new ButtonTracker(Constants.DRIVE_STICK, 4); // Y
				disableCompressor = new ButtonTracker(Constants.WEAPON_STICK, 8); // start

				////RobotMap.sL.SystemLoggerWriteRAW("Weapon_Buttons_Created");
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
				// a.addObject("Test - Rotate Right With Vision and GoToLift", new RotateAndScoreGear(60 * (Math.PI / 180)));
				// a.addObject("Test - Rotate Right With Vision and One Encoder", new RotateByUntilVision(60 * (Math.PI / 180)));
				// a.addObject("rotate 90", new RotateBy(Math.PI / 2));
				// a.addObject("rotate -90", new RotateBy(-Math.PI / 2));
				// a.addObject("go 5 feet", new DistanceMovePID(5));
				// a.addObject("Timed testing", new TimedMove(.3, -.3, 1.15));

				// SCORE GEARS FROM STARTING POSITION:
				a.addObject("Red Left", new ScoreFromStart(true, 0));
				a.addObject("Red Middle", new ScoreFromStart(true, 1));
				a.addObject("Red Right", new ScoreFromStart(true, 2));
				a.addObject("Blue Left", new ScoreFromStart(false, 0));
				a.addObject("Blue Middle", new ScoreFromStart(false, 1));
				a.addObject("Blue Right", new ScoreFromStart(false, 2));

				a.addObject("Timed Red Left", new TimedScoreFromStart(true, 0));
				a.addObject("Timed Red Middle", new TimedScoreFromStart(true, 1));
				a.addObject("Timed Red Right", new TimedScoreFromStart(true, 2));
				a.addObject("Timed Blue Left", new TimedScoreFromStart(false, 0));
				a.addObject("Timed Blue Middle", new TimedScoreFromStart(false, 1));
				a.addObject("Timed Blue Right", new TimedScoreFromStart(false, 2));

				//RobotMap.sL.SystemLoggerWriteRAW("Sendable_Chooser_Objects_A_Created");

				// SENDABLE CHOSER TWO:
				b.addDefault("None", new Nothing());
				b.addObject("Score Gear Stage Two", new ScoreFromStartStageTwo());
				b.addObject("Score Gear Stage Two Alternate", new AlternateScoreGearFromStartStageTwo());
				b.addObject("Go Forward", new DistanceMove(0.1, 0, 1));
				b.addObject("Go Backward", new DistanceMove(-0.3, -0.3, 5));
				b.addObject("Turn 45 Right", new RotateBy(Math.PI / 4));
				b.addObject("Left", new RotateBy(-Math.PI / 4));
				b.addObject("Score Gear", new ScoreGear());

				//RobotMap.sL.SystemLoggerWriteRAW("Sendable_Chooser_Objects_B_Created");

				// SENDAZBLE CHOSER THREE:
				c.addDefault("None", new Nothing());
				c.addObject("Go Forward", new DistanceMove(0.3, 0.3, 5));
				c.addObject("Go Backward", new DistanceMove(-0.3, -0.3, 5));
				c.addObject("Turn 45 Right", new RotateBy(Math.PI / 4));
				c.addObject("Turn 45 Left", new RotateBy(-Math.PI / 4));

				//RobotMap.sL.SystemLoggerWriteRAW("Sendable_Chooser_Objects_C_Created'");

				// DISPLAY CHOSERS TO DASHBOARD:
				SmartDashboard.putData("1st", a);
				SmartDashboard.putData("2nd", b);
				SmartDashboard.putData("3rd", c);
				//RobotMap.sL.SystemLoggerWriteRAW("Sendable_Choosers_Put_On_Dashboard");

			}

		/**
		 * This function is called once each time the robot enters Disabled mode. You can use it to reset any subsystem
		 * information you want to clear when the robot is disabled.
		 */
		public void disabledInit()
			{
				
				RobotMap.sL.SystemLoggerWriteTimeline("Disabled_Init");

				// WHEN DISABLE ROBOT DISABLE BRAKES:
				RobotMap.left1.enableBrakeMode(false);
				RobotMap.left2.enableBrakeMode(false);
				RobotMap.left3.enableBrakeMode(false);
				RobotMap.right1.enableBrakeMode(false);
				RobotMap.right2.enableBrakeMode(false);
				RobotMap.right3.enableBrakeMode(false);

				//RobotMap.sL.SystemLoggerWriteRAW("Left_And_Right_Brake_Mode_Disabled");

			}

		public void disabledPeriodic()
			{

				RobotMap.sL.SystemLoggerWriteTimeline("Disabled_Periodic");
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

				RobotMap.sL.SystemLoggerWriteTimeline("Atonomous_Init");
				// WHEN ENABLE ROBOT, ENABLE BRAKES:
				RobotMap.left1.enableBrakeMode(true);
				RobotMap.left2.enableBrakeMode(true);
				RobotMap.left3.enableBrakeMode(true);
				RobotMap.right1.enableBrakeMode(true);
				RobotMap.right2.enableBrakeMode(true);
				RobotMap.right3.enableBrakeMode(true);

				//RobotMap.sL.SystemLoggerWriteRAW("Left_Right_Brake_Mode_Enabled");

				// RUN SELECTED AUTO MOVE:
				Auto am = (Auto) a.getSelected();
				Auto bm = (Auto) b.getSelected();
				Auto cm = (Auto) c.getSelected();
				String picked = "We picked: ";
				picked += am.getClass().getName() + ", ";
				picked += bm.getClass().getName() + ", ";
				picked += cm.getClass().getName();

				autoMovePicked = picked;

				//RobotMap.sL.SystemLoggerWriteRAW("Auto_Move_Picked_" + autoMovePicked);

				DriverStation.reportError(picked, false);
				Auto[] m =
					{ am, bm, cm };
				move = new SequentialMove(m);
				move.init();

				//RobotMap.sL.SystemLoggerWriteRAW("Move_Init_Started");
				move.start();
				//RobotMap.sL.SystemLoggerWriteRAW("Move_Start_Started");

			}

		/**
		 * This function is called periodically during autonomous
		 */
		public void autonomousPeriodic()
			{
				RobotMap.sL.SystemLoggerWriteTimeline("Autonomous+Periodic");
				commonPeriodic();
				move.update();

				//RobotMap.sL.SystemLoggerWriteRAW("Move_Update");
				Scheduler.getInstance().run();

			}

		public void teleopInit()
			{
				RobotMap.sL.SystemLoggerWriteTimeline("Teleop_Init");
				// WHEN ENABLE ROBOT, ENABLE BRAKES:
				RobotMap.left1.enableBrakeMode(true);
				RobotMap.left1.enableBrakeMode(true);
				RobotMap.left2.enableBrakeMode(true);
				RobotMap.left3.enableBrakeMode(true);
				RobotMap.right1.enableBrakeMode(true);
				RobotMap.right2.enableBrakeMode(true);
				RobotMap.right3.enableBrakeMode(true);

				//RobotMap.sL.SystemLoggerWriteRAW("Left_Right_Brake_Mode_Disabled");

				// AFTER AUTO AND VISION IS DONE, SWITCH CAMS -- FRONT TO BACK:
				//RobotMap.switchVisionCameras();
				//RobotMap.sL.SystemLoggerWriteRAW("Vision_Cameras_Switched");

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
				RobotMap.sL.SystemLoggerWriteTimeline("Teleop_Periodic");
				commonPeriodic();
				Scheduler.getInstance().run();

				// SLOW MOVE DRIVE:

//				if (slowMo.isPressed())
//					{
//						//RobotMap.sL.SystemLoggerWriteRAW("Slow_Mo_Button_Pressed");
//						RobotMap.drive.halfArcade(Constants.DRIVE_STICK, true);
//					}
//				else
//					{
//						RobotMap.drive.arcade(Constants.DRIVE_STICK, true);
//					}
				RobotMap.drive.arcade(Constants.DRIVE_STICK, true);
				
				// PNEUMATICS:
				if (tipHat.isPressed())
					{
						//RobotMap.sL.SystemLoggerWriteRAW("Tip_Hat_Pressed");
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
				if (facePush.isPressed())
					{

						//RobotMap.sL.SystemLoggerWriteRAW("Face_Push_Presssed");
					}

				// DEPLOY BRAKES:
				RobotMap.brakes.set(brakes.isPressed());
				if (brakes.isPressed())
					{
						//RobotMap.sL.SystemLoggerWriteRAW("Brakes_Pressed");
					}

				// GROUND GEAR HANDLER CHECK:
				if (autoPickerUpper.wasJustReleased())
					{
						//RobotMap.sL.SystemLoggerWriteRAW("Auto_Pick_Up_Was_Just_Pressed");
						autoGearInGroundLoaderJustRan = false;
						RobotMap.lowerFloorLifter.set(false);
						RobotMap.floorIntake.set(0);
						gearCurrentTimer.stop();
				//RobotMap.sL.SystemLoggerWriteRAW("gearCurrentTimer_Stopped");
					}

				// AUTO GROUND GEAR HANDLER:
				if (autoPickerUpper.isPressed() && !autoGearInGroundLoaderJustRan)
					{

						//RobotMap.sL.SystemLoggerWriteRAW("Running_Gear_Auto_Pick_Up");

						RobotMap.lowerFloorLifter.set(true);

						RobotMap.floorIntake.set(-Constants.FLOOR_INTAKE_THROTTLE);

						if (RobotMap.floorIntake.getOutputCurrent() > Constants.MAX_FLOOR_INTAKE_CURRENT)
							{
								if (lastKnownGearCurrent < Constants.MAX_FLOOR_INTAKE_CURRENT)
									{
										gearCurrentTimer.reset();
										gearCurrentTimer.start();
										//RobotMap.sL.SystemLoggerWriteRAW("Gear_Current_Timer_Started");
									}
								if (gearCurrentTimer.get() > 1.2 && (RobotMap.floorIntake.getOutputCurrent() > Constants.MAX_FLOOR_INTAKE_CURRENT))
									{
										gearInGroundLoader = true;
										gearCurrentTimer.stop();
										//RobotMap.sL.SystemLoggerWriteRAW("Gear_Current_Timer_Stopped");
									}
							}

						if (gearInGroundLoader)
							{
								//RobotMap.sL.SystemLoggerWriteRAW("Gear_Is_In_Ground_Loader");
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

								//RobotMap.sL.SystemLoggerWriteRAW("Gear_Intake_Running");

								RobotMap.floorIntake.set(-Constants.FLOOR_INTAKE_THROTTLE);

								if (RobotMap.floorIntake.getOutputCurrent() > Constants.MAX_FLOOR_INTAKE_CURRENT)
									{
										if (lastKnownGearCurrent < Constants.MAX_FLOOR_INTAKE_CURRENT)
											{
												gearCurrentTimer.reset();
												gearCurrentTimer.start();
												//RobotMap.sL.SystemLoggerWriteRAW("Gear_Current_Timer_Start");
											}
										if (gearCurrentTimer.get() > .8 && (RobotMap.floorIntake.getOutputCurrent() > Constants.MAX_FLOOR_INTAKE_CURRENT))
											{
												gearInGroundLoader = true;
												gearCurrentTimer.stop();
												//RobotMap.sL.SystemLoggerWriteRAW("Gear_Current_Timer_Stop");
											}
									}

							}
						else if (outFloorGear.isPressed())
							{
								//RobotMap.sL.SystemLoggerWriteRAW("Gear_Out_Running");

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
						if (dropGroundLoader.isPressed())
							{
								//RobotMap.sL.SystemLoggerWriteRAW("Ground_Loader_Dropped");
							}

						if (dropGroundLoader.wasJustPressed())
							{
								autoGearInGroundLoaderJustRan = false;
								gearInGroundLoader = false;
							}

					}

				// CLIMBER:
				if (Math.abs(Constants.DRIVE_STICK.getRawAxis(3)) > .18)

					{
						RobotMap.winchRight.set(Constants.DRIVE_STICK.getRawAxis(3));
						RobotMap.winchLeft.set(-Constants.DRIVE_STICK.getRawAxis(3));
						//RobotMap.sL.SystemLoggerWriteRAW("Winch_Running");
					}
				else if (Math.abs(Constants.DRIVE_STICK.getRawAxis(2)) > .18) {

					RobotMap.winchRight.set(-Constants.DRIVE_STICK.getRawAxis(2));
					RobotMap.winchLeft.set(Constants.DRIVE_STICK.getRawAxis(2));
				}else 
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
								//RobotMap.sL.SystemLoggerWriteRAW("Starting_Compressor");
							}
					}
				else
					{
						if (RobotMap.compressor.enabled())
							{
								RobotMap.compressor.stop();
								//RobotMap.sL.SystemLoggerWriteRAW("Stopping_Compressor");
							}
					}

				lastKnownGearCurrent = RobotMap.floorIntake.getOutputCurrent();

			}

		/**
		 * This function is called periodically during test mode
		 */
		public void testPeriodic()
			{
				RobotMap.sL.SystemLoggerWriteTimeline("Test_Periodic");
				LiveWindow.run();
			}

		/*
		 * This is run in disabled, teleop, and auto periodics.
		 */
		public void commonPeriodic()
			{

				RobotMap.sL.SystemLoggerWriteTimeline("Common_Periodic");
				// 11 Doubles
				// 2 Booleans
				// 4 Solenoids
				// 1 String

//				try
//					{
//						Double[] doubleArray =
//							{ RobotMap.systemLoggerTimer.get(), RobotMap.right1.getOutputCurrent(), RobotMap.left1.getOutputCurrent(), RobotMap.right1.getOutputVoltage(), RobotMap.left1.getOutputVoltage(), (double) RobotMap.right1.getEncPosition(), (double) RobotMap.left1.getEncPosition(), RobotMap.right1.getSpeed(), RobotMap.left1.getSpeed(), //RobotMap.sL.pixHeight, //RobotMap.sL.pixDis };
//						Boolean[] booleanArray =
//							{ compressorMode, gearInGroundLoader };
//
//						//RobotMap.sL.SystemLoggerNullCheck(doubleArray, booleanArray, autoMovePicked);
//						//RobotMap.sL.SystemLoggerWrite(RobotMap.systemLoggerTimer.get() + "," + RobotMap.right1.getOutputCurrent() + "," + RobotMap.left1.getOutputCurrent() + "," + RobotMap.right1.getOutputVoltage() + "," + RobotMap.left1.getOutputVoltage() + "," + RobotMap.right1.getEncPosition() + "," + RobotMap.left1.getEncPosition() + "," + RobotMap.right1.getSpeed() + "," + RobotMap.left1.getSpeed() + "," + //RobotMap.sL.pixHeight + "," + //RobotMap.sL.pixDis + "," + compressorMode + "," + gearInGroundLoader + "," + RobotMap.brakes.get() + "," + RobotMap.pushFaceOut.get() + "," + RobotMap.hatTip.get() + "," + RobotMap.gearPooper.get() + "," + autoMovePicked);
//					}
//				catch (Exception e)
//					{
//						e.printStackTrace();
//					}

				if (RobotMap.debugModeEnabled)
					{
						RobotMap.gearLiftFinder.computeHeadingToTarget();
						RobotMap.smartDashboardDebugVideoOutput.putFrame(RobotMap.gearLiftFinder.getAnnotatedFrame());
						//RobotMap.sL.SystemLoggerWriteRAW("Running_Vision_Processing");
					}

				// SMART DAHSBOARD OUTPUT:
				SmartDashboard.putBoolean("Ground Loaded Gear", gotGear);
				SmartDashboard.putBoolean("Compressor", compressorMode);

				// SmartDashboard.putNumber("Heading", poseidon.getHeading());
				//System.out.println(Constants.DRIVE_STICK.getRawAxis(2));
				SmartDashboard.putNumber("Left Encoder", RobotMap.left1.getEncPosition());
				SmartDashboard.putNumber("Right Encoder", RobotMap.right1.getEncPosition());
				SmartDashboard.putNumber("Alpha", variableStore.GetDouble(CompassReader.compassAlphaVariableName, 0));
				SmartDashboard.putNumber("Beta", variableStore.GetDouble(CompassReader.compassBetaVariableName, 0));
				SmartDashboard.putNumber("Voltage", panel.getVoltage());
				SmartDashboard.putNumber("CurrentR", RobotMap.right1.getOutputCurrent());
				SmartDashboard.putNumber("CurrentL", RobotMap.left1.getOutputCurrent());
				SmartDashboard.putNumber("Current Floor Intake", RobotMap.floorIntake.getOutputCurrent());

				SmartDashboard.putNumber("RPMs For Left", RobotMap.left1.getSpeed());
				SmartDashboard.putNumber("RPMs For Right", RobotMap.right1.getSpeed());
				//RobotMap.sL.SystemLoggerWriteRAW("Outputting_SmartDashboard_Values");
				// System.out.println(RobotMap.left1.getSpeed());

				/*
				 * Compass calibration. button tracker is disabled (not updated)
				 */
				// if (compCal1.isPressed())
				// {// && compCal2.Pressedp()) {
				// // AUTO CAL
				// tempy = poseidon.getMagX();
				// tempz = poseidon.getMagZ();
				// if (compCal1.wasJustPressed())
				// {// &&
				// // compCal2.justPressedp())
				// // {
				// ymax = tempy;
				// ymin = tempy;
				// zmax = tempz;
				// zmin = tempz;
				// }
				// if (tempy > ymax)
				// {
				// ymax = tempy;
				// }
				// else if (tempy < ymin)
				// {
				// ymin = tempy;
				// }
				//
				// if (tempz > zmax)
				// {
				// zmax = tempz;
				// }
				// else if (tempz < zmin)
				// {
				// zmin = tempz;
				// }
				//
				// alpha = (ymax + ymin) / 2;
				// beta = (zmax + zmin) / 2;
				//
				// poseidon.compCal(alpha, beta);
				//
				// }

				// Display if Gear In In Ground Loader:
				SmartDashboard.putBoolean("Gear In Ground Loader", gearInGroundLoader);

				// UPDATES:
				//disableCompressor.update();
				brakes.update();
				//slowMo.update();
				tipHat.update();
				poopGear.update();
				facePush.update();
				intakeFloorGear.update();
				outFloorGear.update();
				dropGroundLoader.update();
				//RobotMap.sL.SystemLoggerWriteRAW("Running_Button_Updates");

			}
	}