package frc.robot.AutonModes;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.OpModeInterface;
import frc.robot.RobotContainer;
import frc.robot.Manipulator.MANIP_STATE;

public class Auton1NearCenter implements OpModeInterface
{

    private RobotContainer robot;

    //public Pose2d initPose;
    public Pose2d nextPose;
    public Timer autonTimer = new Timer();
    private int step = 0;
    // public Translation2d blueSpeaker = new Translation2d( 0.5,  5.55 );
    // public Translation2d redSpeaker  = new Translation2d(  8.308975, 1.442593 );

    public Auton1NearCenter()
    {
        robot = RobotContainer.getInstance();
    }

    @Override
    public void Init()
    {
        step = 0;
        //initPose = robot.driveBase.getPose();
        robot.setManual( false );
        //initPose = new Pose2d();
        nextPose = robot.landmarks.backPose;
        autonTimer.restart();
    }

    @Override
    public void Periodic()
    {
        System.out.println(step);
        switch (step)
        {
            case 0:
                if (!robot.driveBase.driveFacing(0.0, 0.0, robot.landmarks.speaker) || autonTimer.get() > 1.0 )
                {
                    robot.shooter.setState(MANIP_STATE.SPEAKER_TARGET, 0.0);
                    autonTimer.restart();
                    step++;
                }
                break;
            case 1:
                if (robot.shooter._shooter.ready() || autonTimer.get() > 2.0)
                {
                    // autonTimer.stop();
                    // autonTimer.reset();
                    robot.shooter.setState(MANIP_STATE.SPEAKER_SHOOT, 0.0);
                    step++;
                }
                break;
            case 2:
                if (!robot.driveBase.move_Pose2d(nextPose))
                {
                    robot.driveBase.stopDrive();
                    step++;
                }
                break;
            default:
                break;
        }
    }
}