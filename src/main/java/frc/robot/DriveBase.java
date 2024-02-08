package frc.robot;

import java.io.File;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveParser;
import swervelib.telemetry.SwerveDriveTelemetry;
import swervelib.telemetry.SwerveDriveTelemetry.TelemetryVerbosity;

public class DriveBase
{
  private SwerveDrive swerveDrive;
  double maximumSpeed = Units.feetToMeters(0.1); //15.1

  public static final double JOYSTICK_X_POSITIVE_DEADBAND =  0.05;
  public static final double JOYSTICK_X_NEGATIVE_DEADBAND = -0.05;
  public static final double JOYSTICK_Y_POSITIVE_DEADBAND =  0.05;
  public static final double JOYSTICK_Y_NEGATIVE_DEADBAND = -0.05;
  public static final double JOYSTICK_Z_POSITIVE_DEADBAND =  0.35;
  public static final double JOYSTICK_Z_NEGATIVE_DEADBAND = -0.35;

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
    SwerveDriveTelemetry.verbosity = TelemetryVerbosity.HIGH;
    //swerveDrive.setHeadingCorrection( true );
  }
  void periodic( )
  {
    Pose2d current_pose = swerveDrive.swerveDrivePoseEstimator.getEstimatedPosition();
    SmartDashboard.putNumber("RobotX", current_pose.getX());
    SmartDashboard.putNumber("RobotY", current_pose.getY());
    SmartDashboard.putNumber("RobotZ", current_pose.getRotation().getDegrees());
  }
  void addVisionMeasurement( Pose2d new_pose)
  {
    swerveDrive.addVisionMeasurement( new_pose, Timer.getFPGATimestamp());
  }
  void drive( double x, double y, double z )
  {
    if (x > JOYSTICK_X_POSITIVE_DEADBAND)
    {
      x = x*x*swerveDrive.getMaximumVelocity();
    } else if (x < JOYSTICK_X_NEGATIVE_DEADBAND) 
    {
      x = x*-x*swerveDrive.getMaximumVelocity();
    } else
    {
      x = 0.0;
    }
    if (y > JOYSTICK_Y_POSITIVE_DEADBAND)
    {
      y = y*y*swerveDrive.getMaximumVelocity();
    } else if (y < JOYSTICK_Y_NEGATIVE_DEADBAND)
    {
      y = y*-y*swerveDrive.getMaximumVelocity();
    }else
    {
      y = 0.0;
    }
    if (z > JOYSTICK_Z_POSITIVE_DEADBAND)
    {
      z = z*z*swerveDrive.getMaximumAngularVelocity();
    } else if (z < JOYSTICK_Z_NEGATIVE_DEADBAND){
      z = z*-z*swerveDrive.getMaximumAngularVelocity();
    } else
    {
      z = 0.0;
    }
    x = x / 2;
    y = y / 2;
    z = z / 2;
    swerveDrive.driveFieldOriented(new ChassisSpeeds(x, y, z));
  }
}
