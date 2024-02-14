package frc.robot.AutonModes;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import frc.robot.OpModeInterface;
import frc.robot.RobotContainer;

public class Auton1Speaker implements OpModeInterface
{

    private RobotContainer robot;

    public Pose2d nextPose = new Pose2d(2.0, 2.0, new Rotation2d(Math.PI/2.0));
    public Translation2d blueSpeaker = new Translation2d( -8.308975, 1.442593 );
    public Translation2d redSpeaker  = new Translation2d(  8.308975, 1.442593 );

    public Auton1Speaker()
    {
        robot = RobotContainer.getInstance();
    }

    @Override
    public void Init()
    {
    }

    @Override
    public void Periodic()
    {
        // if (!robot.driveBase.move_Pose2d(nextPose))
        // {
        //     robot.driveBase.drive(0, 0, 0);
        // }
        robot.driveBase.driveFacing( 0.0, 0.0, blueSpeaker );
        
    }
    
}
