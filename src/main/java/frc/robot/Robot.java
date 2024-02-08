package frc.robot;

import java.io.File;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveParser;
import swervelib.telemetry.SwerveDriveTelemetry;
import swervelib.telemetry.SwerveDriveTelemetry.TelemetryVerbosity;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.LimelightHelpers.LimelightResults;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Robot extends TimedRobot {

  private Joystick joystick = new Joystick(0);
  private DriveBase driveBase = new DriveBase();
  private double x;
  private double y;
  private double z;
  NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
  NetworkTableEntry tx = table.getEntry("tx");
  NetworkTableEntry ty = table.getEntry("ty");
  NetworkTableEntry ta = table.getEntry("ta");
  NetworkTableEntry tid = table.getEntry("tid");
  NetworkTableEntry botpose = table.getEntry("botpose");


  
  

  @Override
  public void robotInit() 
  {
  }

  @Override
  public void robotPeriodic() {
    //read values periodically
    double x = tx.getDouble(0.0);
    double y = ty.getDouble(0.0);
    double id   = tid.getDouble(0.0);
    double area = ta.getDouble(0.0); 

    //post to smart dashboard periodically
    SmartDashboard.putNumber("LimelightX", x);
    SmartDashboard.putNumber("LimelightY", y);
    SmartDashboard.putNumber("LimelightTID", id);
    SmartDashboard.putNumber("LimelightArea", area);

    LimelightResults llresults = LimelightHelpers.getLatestResults("");
    if ( id > 0 && llresults.targetingResults.valid )
    {
      Pose2d ll_pose = llresults.targetingResults.getBotPose2d();
      Pose2d new_pose = new Pose2d( ll_pose.getX() + 8.7532, ll_pose.getY() +4.106, ll_pose.getRotation() );
      driveBase.addVisionMeasurement( new_pose );
    }

    driveBase.periodic();
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
    x = -joystick.getRawAxis(5);
    y = -joystick.getRawAxis(4);
    z = -joystick.getRawAxis(0);
    driveBase.drive( x, y, z );
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {
  }
}