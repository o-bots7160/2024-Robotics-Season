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
   Joystick Joystick2 = new Joystick(1); // Joystick
   double x, y, hx, hy;

   public Translation2d blueSpeaker = new Translation2d( 1.3,  6.0);
   public Translation2d redSpeaker  = new Translation2d( 17.7592, 6.0);
   public Translation2d origin      = new Translation2d( 0.0, 0.0 );

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
  
      //if ( Joystick2.getRawButton(1))
      //{
      //   robot.driveBase.driveFacing( x, y, blueSpeaker );
      // }
      // else if ( Joystick2.getRawButton( 3 ) )
      // {
      //    robot.driveBase.driveHeading( x, y, -Math.PI/2.0 );
      // }
      // else
      // {
      //    robot.driveBase.drive( x, y, hx);//x, hy );
      // }
      if (Joystick.getRawButtonPressed(4))
      {
         robot.shooter.setState(MANIP_STATE.STOW, 0.0);
      }
      if (Joystick.getRawButtonPressed(2))
      {
         robot.shooter.setState( MANIP_STATE.INTAKE, 0.0 );
      }
      if (Joystick.getRawButtonPressed(1))
      {
         robot.shooter.setState( MANIP_STATE.SPEAKER_TARGET, 0.0 );
      }
      if (Joystick.getRawButtonPressed(3))
      {
         robot.shooter.setState( MANIP_STATE.SPEAKER_SHOOT, 0.0 );
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
