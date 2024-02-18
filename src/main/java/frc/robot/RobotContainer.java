package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.LimelightHelpers.LimelightResults;

public class RobotContainer
{
   //
   //  What are these lines for?
   //  They make it so that ANY instance of RobotContainer points to a single
   //  instance of RobotContainer, defined below
   //
   //
   private static RobotContainer RobotContainer_instance = null;
   //
   //  The devices this robot uses
   //
   //
   public DriveBase driveBase = new DriveBase();

   NetworkTable      table = NetworkTableInstance.getDefault().getTable("limelight");
   NetworkTableEntry tx      = table.getEntry("tx");
   NetworkTableEntry ty      = table.getEntry("ty");
   NetworkTableEntry ta      = table.getEntry("ta");
   NetworkTableEntry tid     = table.getEntry("tid");
   NetworkTableEntry tl      = table.getEntry("tl");
   NetworkTableEntry cl      = table.getEntry("cl");
   NetworkTableEntry botpose = table.getEntry("botpose");
   //Pose2dFilter      filter  = new Pose2dFilter();

   private RobotContainer()
   {
   }

   public static RobotContainer getInstance()
   {
      if (RobotContainer_instance == null)
      {
         RobotContainer_instance = new RobotContainer();
      }
      return RobotContainer_instance;
   }

   public void drive(Translation2d translation, double rotation, boolean fieldRelative, boolean isOpenLoop) 
   {
      //_drive.drive( translation, rotation, fieldRelative, isOpenLoop );
   }    

   public void periodic()
   {
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
         Pose2d ll_pose  = llresults.targetingResults.getBotPose2d();
         Pose2d new_pose = new Pose2d( ll_pose.getX() + 8.7532, ll_pose.getY() +4.106, ll_pose.getRotation() );
         // Use these lines to filter pose data from limelight
         //if ( filter.addData( new_pose, Timer.getFPGATimestamp() - ( latency / 1000.0 ) - ( capture / 1000.0 ) ) )
         //{
         //   driveBase.addVisionMeasurement( filter.resultPose, filter.resultTime );
         //   if ( DriverStation.isDisabled() )
         //   {
         //      driveBase.swerveController.lastAngleScalar = driveBase.getPose().getRotation().getRadians();
         //   }
         //}
         driveBase.addVisionMeasurement( new_pose, Timer.getFPGATimestamp() - ( latency / 1000.0 ) - ( capture / 1000.0 ) );
         if ( DriverStation.isDisabled() )
         {
            driveBase.swerveController.lastAngleScalar = driveBase.getPose().getRotation().getRadians();
         }
      }
      driveBase.periodic();
   }
   public void disable()
   {
   }
}