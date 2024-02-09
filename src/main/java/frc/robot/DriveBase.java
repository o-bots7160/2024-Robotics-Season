package frc.robot;

import java.io.File;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveParser;

public class DriveBase
{
   private SwerveDrive swerveDrive;
   double maximumSpeed = Units.feetToMeters(15.1);

   private boolean       control_active = false;
   private double        last_heading   = 0.0;
   private double        rot_ctrl = 0.0;
   private Translation2d x_y_ctrl = new Translation2d( 0.0, 0.0 );

   //                                                 kP   Ki    Kd
   private PIDController rot_PID = new PIDController( 3.5, 0.0,   0.0 );
   private PIDController x_y_PID = new PIDController( 2.4, 0.145, 0.0 );
   //
   //   Create a YAGSL swerve drive and PID controllers
   //
   //
   DriveBase( )
   {
      try
      {
         swerveDrive = new SwerveParser(new File(Filesystem.getDeployDirectory(),"swerve")).createSwerveDrive(maximumSpeed);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      //SwerveDriveTelemetry.verbosity = TelemetryVerbosity.HIGH;
      //swerveDrive.setHeadingCorrection( true );

      rot_PID.setTolerance( Math.toRadians(1.0) );
      rot_PID.enableContinuousInput(-Math.PI, Math.PI);
      rot_PID.setIntegratorRange(-0.08, 0.08);
      x_y_PID.setTolerance( 0.03, 0.03);
      x_y_PID.setIntegratorRange(-0.04, 0.04);
   }
   //
   //   Must be called periodically to update telemetry
   //
   //
   void periodic( )
   {
      Pose2d current_pose = swerveDrive.swerveDrivePoseEstimator.getEstimatedPosition( );
      SmartDashboard.putNumber("RobotX", current_pose.getX( ) );
      SmartDashboard.putNumber("RobotY", current_pose.getY( ) );
      SmartDashboard.putNumber("RobotZ", current_pose.getRotation( ).getDegrees( ) );
      SmartDashboard.putNumber("ctrlx", x_y_ctrl.getX());
      SmartDashboard.putNumber("ctrly", x_y_ctrl.getY());
      SmartDashboard.putNumber("ctrlz", rot_ctrl);
   }
   //
   //   Add vision measurement collected to odometry
   //
   //
   void addVisionMeasurement( Pose2d new_pose, double timestamp )
   {
      swerveDrive.addVisionMeasurement( new_pose, timestamp );
   }
   //
   //   Drive using velocities m/s
   //
   //
   public void drive( double x, double y, double rot )
   {
      swerveDrive.driveFieldOriented( new ChassisSpeeds( x, y, rot ) );
   }
   //
   //   Convert cartesian point to angle
   //
   //
   private double toRadians( double x, double y )
   {
      double radians = 0.0;
      if ( x != 0.0 )
      {
         radians = Math.atan2( y, x );
      }
      else if ( y > 0.0 )
      {
         radians = Math.PI / 2.0;
      }
      else if ( y < 0.0 )
      {
         radians = Math.PI;
      }
      else
      {
         radians = last_heading;
      }
      return radians;
   }
   //
   //   Drive using raw joystick readings with second joystick 
   //
   //
   public void drive( double x, double y, double zx, double zy )
   {
      driveHeading( x, y, toRadians( zx, zy ) );
   }
   //
   //   Drive translating on the field but always orient towards
   //   given Pose2d
   //
   //
   public boolean driveFacing( double x, double y, Pose2d anchor )
   {
      return driveFacing( x, y, anchor.getTranslation() );
   }
   //
   //   Drive translating on the field but always orient towards
   //   given Translation2d
   //
   //
   public boolean driveFacing( double x, double y, Translation2d anchor )
   {
      Pose2d pose = swerveDrive.getPose();
      double radians = toRadians( pose.getY() - anchor.getY(), pose.getX() - anchor.getX() );
      return driveHeading( x, y, radians );
   }
   //
   //   Drive translating on the field but always orient towards
   //   given Rotation2d
   //
   //
   public boolean driveFacing( double x, double y, Rotation2d anchor )
   {
      return driveHeading( x, y, anchor.getRadians() );
   }
   //
   //   Drive translating on the field but always orient towards
   //   given Rotation2d
   //
   //
   public boolean driveHeading( double x, double y, double radians )
   {
      Pose2d pose = swerveDrive.getPose();
      if ( !control_active )
      {
         rot_PID.reset( );
         control_active = true;
      }
      last_heading = radians;
      rot_PID.setSetpoint( radians );
      rot_ctrl = rot_PID.calculate( pose.getRotation().getRadians() );

      swerveDrive.driveFieldOriented( new ChassisSpeeds( x, y, rot_ctrl ) );
      return ! rot_PID.atSetpoint();
   }
   //
   //   Move and orient to new_pose from current location
   //
   //
   public boolean move_Pose2d( Pose2d new_pose )
   {
      Pose2d  pose     = swerveDrive.getPose();
      double  x1       = new_pose.getX() - pose.getX();
      double  y1       = new_pose.getY() - pose.getY();
      double  x2       = 0.0;
      double  y2       = 0.0;
      double  angle    = new_pose.getRotation().getRadians() - pose.getRotation().getRadians();
      double  ctrl;
      double  distance = Math.hypot(x1, y1);
      x_y_ctrl = new Translation2d(  x1, y1 );

      if ( ! control_active )
      {
         x_y_PID.reset( );
         x_y_PID.setSetpoint( 0.0 );
         rot_PID.reset( );
         rot_PID.setSetpoint( 0.0 );
      }
      ctrl           =  x_y_PID.calculate( distance );
      rot_ctrl       = rot_PID.calculate(angle );
      control_active = !x_y_PID.atSetpoint( ) || !rot_PID.atSetpoint( );

      if ( ! x_y_PID.atSetpoint( ) )
      {
         x2 = x1 * ctrl / distance;
         y2 = y1 * ctrl / distance;
      }
      drive( x2, y2, rot_ctrl );
      return control_active;
   }
   //
   //   Tells PID controllers to reset upon next start of control period
   //
   //
   public void resetControl()
   {
      control_active = false;
   }
   //
   //   Moving into Teleop, need to set last heading
   //
   //
   public void setLastHeading()
   {
      last_heading = swerveDrive.getPose().getRotation().getRadians();
   }
   //
   //   Return the theoretical maximum velocity of this drive system
   //
   //
   public double getMaximumVelocity()
   {
      return swerveDrive.getMaximumVelocity();
   }
   //
   //   Returns the latest pose of the robot from odometery
   //
   //
   public Pose2d getPose()
   {
      return swerveDrive.getPose();
   }
}