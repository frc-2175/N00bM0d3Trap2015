/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends SimpleRobot {
    
    Joystick leftStick, rightStick;
    
    RobotDrive drivetrain;
    Talon shooterWheels;
    Solenoid shooter;
    Compressor compressor;
    
    public RobotTemplate() {
        leftStick = new Joystick(1);
        rightStick = new Joystick(2);
        
        drivetrain = new RobotDrive(9,10);
        shooterWheels = new Talon(8);
        shooter = new Solenoid(2);
        
        compressor = new Compressor(2,1);
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        drivetrain.setSafetyEnabled(true);
        compressor.start();
        while (isOperatorControl() && isEnabled()) {
            // The throttle goes from 1 to -1, bottom to top. This
            // scales it to go 0 to 1, bottom to top.
            double throttle = (-leftStick.getRawAxis(3) + 1) / 2;
            
            double drive = deadband(leftStick.getY()) * throttle;
            double steer = deadband(-rightStick.getX()) * throttle;
            drivetrain.arcadeDrive(drive, steer);
            
            boolean shouldSpinWheels = leftStick.getRawButton(1);
            boolean shouldFire = shouldSpinWheels && rightStick.getRawButton(1);
            shooterWheels.set(shouldSpinWheels ? -1 : 0);
            shooter.set(shouldFire);
            
            Timer.delay(0.005);	// wait for a motor update time
        }
    }
    
    public void disabled() {
        compressor.stop();
    }
    
    private double deadband(double input) {
        if (Math.abs(input) < 0.05) {
            return 0;
        }
        return input;
    }
}
