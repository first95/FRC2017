
package org.usfirst.frc.team95.robot;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
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
    Timer cycleTime;   //for common periodic 
    double totalX, totalY, totalZ;
   
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
        

        cycleTime = new Timer();
        cycleTime.reset();  
    }// ADXL345_I2C.DataFormat_Range.k2G
	
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
    	autonomousCommand = (Command) chooser.getSelected();
        
		/* String autoSelected = SmartDashboard.getString("Auto Selector", "Default");
		switch(autoSelected) {
		case "My Auto":
			autonomousCommand = new MyAutoCommand();
			break;
		case "Default Auto":
		default:
			autonomousCommand = new ExampleCommand();
			break;
		} */
    	
    	// schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	commonPeriodic();
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
    	
    	RobotMap.drive.arcade(Constants.driveStick);
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
    	cycleTime.start();
    	
    	System.out.println("things");
    	System.out.println(gyro.getXAng());
    	
    	SmartDashboard.putNumber("X", gyro.getXAng());
    	SmartDashboard.putNumber("Y", gyro.getYAng());
    	SmartDashboard.putNumber("Z", gyro.getZAng());
    	
    	SmartDashboard.putNumber("CX", compass.getCompX()); 
    	//SmartDashboard.putNumber("CY", compass.getCompY()); 
    	SmartDashboard.putNumber("CZ", compass.getCompZ()); 
    	
    	totalX = totalX + (gyro.getXAng() * cycleTime.get());
    	totalY = totalY + (gyro.getYAng() * cycleTime.get());
    	totalZ = totalZ + (gyro.getZAng() * cycleTime.get());
    	SmartDashboard.putNumber("TX", totalX);
    	SmartDashboard.putNumber("TY", totalY);
    	SmartDashboard.putNumber("TZ", totalZ);
    	cycleTime.stop();
    	cycleTime.reset();
    }
}
