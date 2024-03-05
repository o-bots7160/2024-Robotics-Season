package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TestPose2d implements OpModeInterface {
   private RobotContainer robot;

   Joystick Joystick; // Joystick
   Pose2d   control_pose;
   boolean  moving       = false;

   public TestPose2d()
   {
      robot = RobotContainer.getInstance();
      Joystick = new Joystick(0);
      Joystick.getRawButtonPressed(1);
      Joystick.getRawButtonPressed( 4 );
      Joystick.getRawButtonPressed( 2 );
      Joystick.getRawButtonPressed( 3 );
      Joystick.getRawButtonPressed( 5 );
      Joystick.getRawButtonPressed( 6 );
   }
   public void Init()
   {   
      control_pose = robot.driveBase.getPose();
   }
   public void Periodic()
   {
      //System.out.println("Rotation: " + robot.driveBase.() );
      //System.out.println( "Getting button");
      SmartDashboard.putNumber("TestX", control_pose.getX( ) );
      SmartDashboard.putNumber("TestY", control_pose.getY( ) );
      SmartDashboard.putNumber("TestZ", control_pose.getRotation( ).getDegrees( ) );
      if ( Joystick.getRawButtonPressed(1) )
      {
         System.out.println( "Got A button");
         control_pose = new Pose2d( new Translation2d( control_pose.getX() - 1.0, control_pose.getY()), control_pose.getRotation());
         moving = true;
      }
      if ( Joystick.getRawButtonPressed( 4 ) )
      {
         System.out.println( "Got Y button");
         control_pose = new Pose2d( new Translation2d( control_pose.getX() + 1.0, control_pose.getY()), control_pose.getRotation());
         moving = true;
      }
      if ( Joystick.getRawButtonPressed( 2 ) )
      {
         System.out.println( "Got B button");
         control_pose = new Pose2d( new Translation2d( control_pose.getX(), control_pose.getY() - 1.0), control_pose.getRotation());
         moving = true;
      }
      if ( Joystick.getRawButtonPressed( 3 ) )
      {
         System.out.println( "Got X button");
         control_pose = new Pose2d( new Translation2d( control_pose.getX(), control_pose.getY() + 1.0), control_pose.getRotation());
         moving = true;
      }
      if ( Joystick.getRawButtonPressed(5) )
      {
         System.out.println( "Got Left Bumper");
         control_pose = control_pose.plus(new Transform2d( new Translation2d( 0, 0 ), new Rotation2d( Math.PI /2.0 )));
         //robot._drive.newPose2d(control_pose);
         moving = true;
      }
      if ( Joystick.getRawButtonPressed(6) )
      {
         System.out.println( "Got Right Bumper");
         control_pose = control_pose.plus(new Transform2d( new Translation2d( 0, 0 ), new Rotation2d( -Math.PI /2.0 )));
         //robot._drive.newPose2d(control_pose);
         moving = true;
      }
      if ( moving )
      {
         moving = robot.driveBase.move_Pose2d( control_pose );
      }
      else
      {
         //robot.drive(new Translation2d(0,0), 0, true, false);
         robot.driveBase.drive(0, 0, 0);
      }
      if ( Joystick.getPOV() == 0 )
      {
         robot.driveBase.drive( 0.0, 0.0, 0.0 );
         Joystick.getRawButtonPressed(1);
         Joystick.getRawButtonPressed( 4 );
         Joystick.getRawButtonPressed( 2 );
         Joystick.getRawButtonPressed( 3 );
         Joystick.getRawButtonPressed( 5 );
         Joystick.getRawButtonPressed( 6 );
         control_pose = robot.driveBase.getPose();
         moving = false;
      }
      System.out.println("moving: " + moving);
   }
}
