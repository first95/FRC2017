
package org.usfirst.frc.team95.robot;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

import java.lang.reflect.Array;
import java.util.ArrayList;

import org.usfirst.frc.team95.robot.auto.Auto;
import org.usfirst.frc.team95.robot.auto.Nothing;
import org.usfirst.frc.team95.robot.auto.RotateBy;
import org.usfirst.frc.team95.robot.auto.SequentialMove;
import org.usfirst.frc.team95.robot.auto.TimedMove;
import org.usfirst.frc.team95.robot.commands.ExampleCommand;
import org.usfirst.frc.team95.robot.subsystems.ExampleSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI oi;

    Command autonomousCommand;
    SendableChooser chooser;

    //ADXL345_I2C Giro;
    GyroReader gyro;
    CompassReader compass;
    HeadingPreservation header;
    Timer cycleTime;   //for common periodic 
    double totalX, totalY, totalZ;
   
    Double angle, angDead, prevADead, angAvg, headingToPres;
    Double[] angleRec;
    ButtonTracker headPres;
    
    Auto move;
    SendableChooser a, b, c;
    ArrayList<PollableSubsystem> updates = new ArrayList<PollableSubsystem>();
    ArrayList<Auto> runningAutonomousMoves = new ArrayList<Auto>();
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
		oi = new OI();
		RobotMap.init();
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", new ExampleCommand());
//        chooser.addObject("My Auto", new MyAutoCommand());
        SmartDashboard.putData("Auto mode", chooser);
        //ADXL345_I2C Giro = new ADXL345_I2C(I2C.Port.kOnboard, ADXL345_I2C.Range.k2G);
        //Giro = new ADXL345_I2C(I2C.Port.kOnboard, ADXL345_I2C.Range.k2G);
        gyro = new GyroReader();
        compass = new CompassReader();
        header = new HeadingPreservation();
        headPres = new ButtonTracker(Constants.driveStick,2);
        		
        cycleTime = new Timer();
        cycleTime.reset();
        cycleTime.start();
        angleRec = new Double[4];       
        prevADead = 5.3;
        angleRec[3] = 0.1;
        angleRec[2] = 0.1;
        angleRec[1] = 0.1;
        angleRec[0] = 0.1;
        
        for (PollableSubsystem p : updates) {
			p.init();
		}
		a = new SendableChooser();
		b = new SendableChooser();
		c = new SendableChooser();
		a.addDefault("None", new Nothing());
		a.addObject("Go Forward", new TimedMove(0.3, 0.3, 5));
		a.addObject("Go Backward", new TimedMove(-0.3, -0.3, 5));
		a.addObject("Turn 45 Right", new RotateBy(Math.PI / 4));
		a.addObject("Turn 45 Left", new RotateBy(-Math.PI / 4));
		b.addDefault("None", new Nothing());
		b.addObject("Go Forward", new TimedMove(0.3, 0.3, 5));
		b.addObject("Go Backward", new TimedMove(-0.3, -0.3, 5));
		b.addObject("Turn 45 Right", new RotateBy(Math.PI / 4));
		b.addObject("Turn 45 Left", new RotateBy(-Math.PI / 4));
		c.addDefault("None", new Nothing());
		c.addObject("Go Forward", new TimedMove(0.3, 0.3, 5));
		c.addObject("Go Backward", new TimedMove(-0.3, -0.3, 5));
		c.addObject("Turn 45 Right", new RotateBy(Math.PI / 4));
		c.addObject("Turn 45 Left", new RotateBy(-Math.PI / 4));
		SmartDashboard.putData("1st", a);
		SmartDashboard.putData("2nd", b);
		SmartDashboard.putData("3rd", c);
		
    }
	
	/**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
     */
    public void disabledInit(){

    }
	
	public void disabledPeriodic() {
		commonPeriodic();
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString code to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the chooser code above (like the commented example)
	 * or additional comparisons to the switch structure below with additional strings & commands.
	 */
    public void autonomousInit() {

		System.out.println("Auto INIT");

		Auto am = (Auto) a.getSelected();
		Auto bm = (Auto) b.getSelected();
		Auto cm = (Auto) c.getSelected();
		String picked = "We picked: ";
		picked += am.getClass().getName() + ", ";
		picked += bm.getClass().getName() + ", ";
		picked += cm.getClass().getName();
		DriverStation.reportError(picked, false);
		Auto[] m = { am, bm, cm };

		move = new SequentialMove(m);
		//move = new TimedStraightMove(0.3, 10);
		move.init();
	}

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	commonPeriodic();
    	for (Auto x : runningAutonomousMoves) {
			// System.out.println("Running " + x.getClass().getName());
			x.update();
			if (x.done()) {
				x.stop();
				runningAutonomousMoves.remove(x);
			}
		}

		System.out.println("Auto Periodic");
		move.update();

        Scheduler.getInstance().run();
    }

    public void teleopInit() {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        commonPeriodic();
    	Scheduler.getInstance().run();
    	
    	if(Constants.driveStick.getRawButton(2)) {
    		if(headPres.justPressedp()) {
    			headingToPres = compass.getHeading();
    		}
    		
    		header.setHeading(headingToPres);
    	} else {
    		RobotMap.drive.arcade(Constants.driveStick);
    	}
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
    
    //This is run in disabled, teleop, and auto periodics.
    public void commonPeriodic() {
    	//cycleTime.reset();
    	//cycleTime.start();
        
        angle = Math.atan2(compass.getRawCompY(), compass.getRawCompX());
        
        //storing 4 most recent angle values
        angleRec[3] = angleRec[2];
        angleRec[2] = angleRec[1];
        angleRec[1] = angleRec[0];
        angleRec[0] = angle;
        
        //angle Dead banding
        if (angle <= prevADead +.15 && angle >= prevADead -.15) {
        	angDead = prevADead;
        } else {
        	angDead = angle;
        }
        prevADead = angDead;
        
        //angle averaging
        angAvg = ((angleRec[0] + angleRec[1] + angleRec[2] + angleRec[3]) / 4);
        
        System.out.println(compass.getRawCompX() + ", " + compass.getRawCompY() + ", " + compass.getRawCompZ() + ", " + gyro.getXAng() + ", " + gyro.getYAng() + ", " + gyro.getZAng() + ", " + compass.getHeading() + ", "  + cycleTime.get() + ", " );
        
    	SmartDashboard.putNumber("X", Math.atan2(compass.getRawCompZ(), compass.getRawCompX()));
    	SmartDashboard.putNumber("Y", Math.atan2(compass.getRawCompY(), compass.getRawCompX()));
    	SmartDashboard.putNumber("Z", Math.atan2(compass.getRawCompZ(), compass.getRawCompY()));
    	
    	SmartDashboard.putNumber("CX", compass.getRawCompX()); 
    	SmartDashboard.putNumber("CY", compass.getRawCompY()); 
    	SmartDashboard.putNumber("CZ", compass.getRawCompZ()); 
    	
    	SmartDashboard.putNumber("Angle", angle);
    	SmartDashboard.putNumber("Angle Dead", angDead);
    	SmartDashboard.putNumber("Angle avg", angAvg);
    	
    	SmartDashboard.putNumber("Heading", compass.getHeading());
    	
    	SmartDashboard.putString("hex x", Double.toHexString(compass.getRawCompX())); 
    	SmartDashboard.putString("hex y", Double.toHexString(compass.getRawCompY()));
    	SmartDashboard.putString("hex z", Double.toHexString(compass.getRawCompZ()));
    	
    	/*System.out.println("new cycle");
    	System.out.println("CompX");
    	System.out.println(compass.getRawCompX());
    	System.out.println("CompY");
    	System.out.println(compass.getRawCompY());
    	System.out.println("CompZ");
    	System.out.println(compass.getRawCompZ());
    	System.out.println("GyroX");
    	System.out.println(gyro.getXAng());
    	System.out.println("GyroY");
    	System.out.println(gyro.getYAng());
    	System.out.println("GyroZ");
    	System.out.println(gyro.getZAng());
    	System.out.println("time");
    	System.out.println(cycleTime.get());*/
    	headPres.update();
    }
}
