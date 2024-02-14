package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Joystick;

public class Teleop implements OpModeInterface
{
   private RobotContainer robot;

   Joystick Joystick = new Joystick(0); // Joystick
   double x, y, hx, hy;

   public Translation2d blueSpeaker = new Translation2d( -8.308975, 1.442593 );
   public Translation2d redSpeaker  = new Translation2d(  8.308975, 1.442593 );

   public Teleop()
   {
      robot = RobotContainer.getInstance();
   }
   public void Init()
   {
      // zero gyro? maybe not... only in Robot.init?
   }
   public void Periodic()
   {
      x  = -Joystick.getRawAxis(5);
      x  = Math.pow( x, 3.0 ) * robot.driveBase.getMaximumVelocity()/8.0;
      y  = -Joystick.getRawAxis(4);
      y  = Math.pow( y, 3.0 ) * robot.driveBase.getMaximumVelocity()/8.0;
      hx = -Joystick.getRawAxis(0);
      hx = Math.pow( hx, 3.0 );
      hy = -Joystick.getRawAxis(1);
      hy = Math.pow( hy, 3.0 );
  
      if ( Joystick.getRawButton( 7 ) )
      {
         robot.driveBase.driveFacing( x, y, blueSpeaker );
      }
      else if ( Joystick.getRawButton(8))
      {
         robot.driveBase.driveHeading( x, y, Math.PI/2.0 );
      } else 
      {
         robot.driveBase.drive( x, y, hx);//x, hy );
      }
      //robot.driveBase.move_Pose2d( new Pose2d( 5.0, 5.0,new Rotation2d( 0.0 ) ) );
   }
   public void testPeriodic()
   {
   }
}
