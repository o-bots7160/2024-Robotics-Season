package frc.robot;

import java.beans.beancontext.BeanContextServicesSupport;
import java.util.Optional;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
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
   private boolean       manual          = false;//private Joystick Buttons1 = new Joystick(1); // Button Board
   private Alliance      currentAlliance;
   public Translation2d  blueSpeaker     = new Translation2d( 1.3,  6.0);
   public Translation2d  redSpeaker      = new Translation2d( 17.7592, 6.0);
   public Translation2d  origin          = new Translation2d( 0.0, 0.0 );
   public DriveBase      driveBase       = new DriveBase();
   public LED            leds;
   public Manipulator    shooter         = new Manipulator( ()->{ return manual; } );
   //public Shooter        shooter       = new Shooter( ()->{ return manual; } );
   public Climber        climber         = new Climber();
   public AllianceLandmarks landmarks    = new AllianceLandmarks();
   public double         target_distance;
   public int id;

   NetworkTable      table = NetworkTableInstance.getDefault().getTable("limelight");
   NetworkTableEntry tx      = table.getEntry("tx");
   NetworkTableEntry ty      = table.getEntry("ty");
   NetworkTableEntry ta      = table.getEntry("ta");
   NetworkTableEntry tid     = table.getEntry("tid");
   NetworkTableEntry tl      = table.getEntry("tl");
   NetworkTableEntry cl      = table.getEntry("cl");
   NetworkTableEntry botpose = table.getEntry("botpose");
   Pose2dFilter      filter  = new Pose2dFilter( true );


   private RobotContainer()
   {
      leds = new LED(this);
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
      //driveBase.drive( translation, rotation, fieldRelative, isOpenLoop );
   }    

   public void opmodeInit( Alliance new_alliance )
   {
      currentAlliance = new_alliance;
      landmarks.newAlliance(currentAlliance);
   }

   public void periodic()
   {
      //read values periodically
      double x = tx.getDouble(0.0);
      double y = ty.getDouble(0.0);
      double tempid = tid.getDouble(0.0);
      double area = ta.getDouble(0.0); 
      double latency = tl.getDouble(0.0); 
      double capture = cl.getDouble(0.0); 

      if ( !manual )
      {
         //post to smart dashboard periodically
         SmartDashboard.putNumber("LimelightX", x);
         SmartDashboard.putNumber("LimelightY", y);
         SmartDashboard.putNumber("LimelightTID", id);
         SmartDashboard.putNumber("LimelightArea", area);
         SmartDashboard.putNumber("Shooter Distance", target_distance);

         LimelightResults llresults = LimelightHelpers.getLatestResults("");
         if ( tempid > 0 && llresults.targetingResults.valid )
         {
            id = (int)tempid;
            Pose2d ll_pose  = llresults.targetingResults.getBotPose2d();
            Pose2d new_pose = new Pose2d( ll_pose.getX() + 8.7532, ll_pose.getY() +4.106, ll_pose.getRotation() );
            // Use these lines to filter pose data from limelight
            if ( filter.addData( new_pose, Timer.getFPGATimestamp() - ( latency / 1000.0 ) - ( capture / 1000.0 ) ) )
            {
               driveBase.addVisionMeasurement( filter.avgPose, filter.avgTime );
               if ( DriverStation.isDisabled() )
               {
                  driveBase.swerveController.lastAngleScalar = driveBase.getPose().getRotation().getRadians();
               }
            }
            // driveBase.addVisionMeasurement( new_pose, Timer.getFPGATimestamp() - ( latency / 1000.0 ) - ( capture / 1000.0 ) );
            // if ( DriverStation.isDisabled() )
            // {
            //     driveBase.swerveController.lastAngleScalar = driveBase.getPose().getRotation().getRadians();
            // }
         }
      }

      driveBase.periodic();
      leds.periodic();
      Pose2d current_pose = driveBase.getPose();
      target_distance = Math.hypot(current_pose.getX() - landmarks.speaker.getX(), current_pose.getY() - landmarks.speaker.getY());
      target_distance = Math.sqrt(Math.pow(current_pose.getX() - landmarks.speaker.getX(), 2) + Math.pow(current_pose.getY() - landmarks.speaker.getY(), 2));
      shooter.periodic( target_distance );
   }
   public void disable()
   {
   }
   public void setManual( boolean new_manual )
   {
      manual = new_manual;
   }
}