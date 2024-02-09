package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.LimelightHelpers.LimelightResults;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Robot extends TimedRobot {

  private Joystick joystick = new Joystick(0);
  private DriveBase driveBase = new DriveBase();

  double x, y, hx, hy;
  NetworkTable      table = NetworkTableInstance.getDefault().getTable("limelight");
  NetworkTableEntry tx      = table.getEntry("tx");
  NetworkTableEntry ty      = table.getEntry("ty");
  NetworkTableEntry ta      = table.getEntry("ta");
  NetworkTableEntry tid     = table.getEntry("tid");
  NetworkTableEntry tl      = table.getEntry("tl");
  NetworkTableEntry cl      = table.getEntry("cl");
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
    double latency = tl.getDouble(0.0); 
    double capture = cl.getDouble(0.0); 

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
      driveBase.addVisionMeasurement( new_pose, Timer.getFPGATimestamp() - ( latency / 1000.0 ) - ( capture / 1000.0 ) );
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
    x  = -joystick.getRawAxis(5);
    x  = Math.pow( x, 3.0 );
    y  = -joystick.getRawAxis(4);
    y  = Math.pow( x, 3.0 );
    hx = -joystick.getRawAxis(1);
    hx = Math.pow( x, 3.0 );
    hy = -joystick.getRawAxis(0);
    hy = Math.pow( x, 3.0 );

    driveBase.drive( x, y, hx, hy );
    //driveBase.move_Pose2d( new Pose2d( 5.0, 5.0,new Rotation2d( 0.0 ) ) );
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