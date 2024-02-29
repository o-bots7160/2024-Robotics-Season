package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Manipulator.MANIP_STATE;

public class Teleop implements OpModeInterface
{
   private RobotContainer robot;

   Joystick Joystick = new Joystick(0); // Joystick
   Joystick Buttons1 = new Joystick(1); // Button Board
   Joystick Buttons2 = new Joystick(2); // Button Board
   double x, y, hx, hy;

   public Translation2d blueSpeaker = new Translation2d( 0.5,  5.55);
   public Translation2d redSpeaker  = new Translation2d( 17.7592, 6.0);
   public Translation2d origin      = new Translation2d( 0.0, 0.0 );

   public Teleop()
   {
      robot = RobotContainer.getInstance();
   }
   public void Init()
   {
      // zero gyro? maybe not... only in Robot.init?
      robot.setManual( false );
   }
   public void Periodic()
   {
      x  = Joystick.getRawAxis(5);
      x  = -Math.pow( x, 3.0 ) * robot.driveBase.getMaximumVelocity()/4.0;
      y  = Joystick.getRawAxis(4);
      y  = -Math.pow( y, 3.0 ) * robot.driveBase.getMaximumVelocity()/4.0;
      hx = -Joystick.getRawAxis(0);
      hx = Math.pow( hx, 3.0 );
      hy = -Joystick.getRawAxis(1);
      hy = Math.pow( hy, 3.0 );
  
      if ( Joystick.getRawButton(7))
      {
        robot.driveBase.driveFacing( x, y, blueSpeaker );
      }
      else if ( Joystick.getRawButton( 9 ) )
      {
         robot.driveBase.driveHeading( x, y, -Math.PI/2.0 );
      }
      else
      {
         robot.driveBase.drive( x, y, hx);//x, hy );
      }
      if (Buttons1.getRawButtonPressed(2)) // Travel
      {
         robot.shooter.setState(MANIP_STATE.STOW, 0.0);
      }
      if (Joystick.getRawAxis(3) > 0.25) // Intake
      {
         robot.shooter.setState( MANIP_STATE.INTAKE, 0.0 );
      }
      else if (Buttons1.getRawButtonPressed(3)) // Shoot
      {
         robot.shooter.setState( MANIP_STATE.SPEAKER_TARGET, 0.0 );
      }
      else if (Joystick.getRawButton(1))
      {
         robot.shooter.setState( MANIP_STATE.SPEAKER_SHOOT, 0.0 );
      }
      else if (!Buttons1.getRawButton(4)) // Limelight Off
      {
         if (Buttons1.getRawButton(5)) // Rotate Up
         {
            robot.shooter.manualAngle(-0.45);
         }
         else if (Buttons1.getRawButton(6)) // Rotate Down
         {
            robot.shooter.manualAngle(1.2);
         }
         else
         {
            robot.shooter.manualAngle(0.0);
         }
      }
      else if (Buttons2.getRawButtonPressed(4)) // Amp
      {
         robot.shooter.setState( MANIP_STATE.AMPLIFIER_TARGET, 0.0 );
      }
      if ( Buttons1.getRawButton(4)) // Limelight On
      {
         robot.setManual( false );
      }
      else
      {
         robot.setManual( true );
      }
      if (Buttons1.getRawButtonPressed(1)) // Lock
      {
         robot.driveBase.stopDrive();
      }
      if (Buttons1.getRawButton(11)) // Left Climb Up
      {

      }
      else if (Buttons1.getRawButton(7)) // Left Climb Down
      {

      }
      if (Buttons2.getRawButton(6)) // Right Climb Up
      {

      }
      else if (Buttons2.getRawButton(5)) // Right Climb Down
      {

      }
      // else
      // {
      //    robot.shooter.setState( MANIP_STATE.STOW, 0.0 );
      // }
      // if (Joystick.getRawButton(3))
      // {
      //    robot.shooter.shooterSetVelocity( 0.50 );
      // }
      // else if (Joystick.getRawButton(1))
      // {
      //    robot.shooter.shooterSetVelocity(0.75);
      // }
      // else
      // {
      //    robot.shooter.shooterSetVelocity( 0.0 );
      // }
   }
   public void testPeriodic()
   {
   }
}
