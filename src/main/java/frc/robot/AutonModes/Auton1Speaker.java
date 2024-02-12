package frc.robot.AutonModes;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import frc.robot.OpModeInterface;
import frc.robot.RobotContainer;

public class Auton1Speaker implements OpModeInterface
{

    private RobotContainer robot;

    public Pose2d nextPose = new Pose2d(Units.metersToFeet(-1), Units.metersToFeet(1), new Rotation2d(0));

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
        if (!robot.driveBase.move_Pose2d(nextPose))
        {
            robot.driveBase.drive(0, 0, 0);
        }
    }
    
}
