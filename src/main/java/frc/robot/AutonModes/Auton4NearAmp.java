package frc.robot.AutonModes;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.OpModeInterface;
import frc.robot.RobotContainer;
import frc.robot.Manipulator.MANIP_STATE;

public class Auton4NearAmp implements OpModeInterface
{

    private RobotContainer robot;

    //public Pose2d initPose;
    public Pose2d nextPose;
    public Timer autonTimer = new Timer();
    private int step = 0;

    public Auton4NearAmp()
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
        nextPose = robot.landmarks.nearLeft;
    }

    @Override
    public void Periodic()
    {
        System.out.println(step);
        switch (step)
        {
            case 0:
                if (!robot.driveBase.driveFacing(0.0, 0.0, robot.landmarks.speaker))
                {
                    robot.shooter.setState(MANIP_STATE.SPEAKER_TARGET, robot.target_distance);
                    step++;
                }
                break;
            case 1:
                if (robot.shooter._shooter.ready())
                {
                    robot.shooter.setState(MANIP_STATE.SPEAKER_SHOOT, robot.target_distance);
                    if (!robot.shooter._shooter.haveNote())
                    {
                        robot.shooter.setState(MANIP_STATE.INTAKE, 0.0);
                        step++;
                    }
                }
                break;
            case 2:
                if (!robot.driveBase.move_Pose2d(nextPose))
                {
                    robot.driveBase.stopDrive();
                    step++;
                }
                break;
            case 3:
                if (!robot.driveBase.driveFacing(0.0, 0.0, robot.landmarks.speaker))
                {
                    robot.shooter.setState(MANIP_STATE.SPEAKER_TARGET, robot.target_distance);
                    step++;
                }
                break;
            case 4:
                if (robot.shooter._shooter.ready())
                {
                    robot.shooter.setState(MANIP_STATE.SPEAKER_SHOOT, robot.target_distance);
                    if (!robot.shooter._shooter.haveNote())
                    {
                        robot.shooter.setState(MANIP_STATE.INTAKE, 0.0);
                        nextPose = robot.landmarks.nearCenter;
                        step++;
                    }
                }
                break;
            case 5:
                if (!robot.driveBase.move_Pose2d(nextPose))
                {
                    robot.driveBase.stopDrive();
                    step++;
                }
                break;
            case 6:
                if (!robot.driveBase.driveFacing(0.0, 0.0, robot.landmarks.speaker))
                {
                    robot.shooter.setState(MANIP_STATE.SPEAKER_TARGET, robot.target_distance);
                    step++;
                }
                break;
            case 7:
                if (robot.shooter._shooter.ready())
                {
                    robot.shooter.setState(MANIP_STATE.SPEAKER_SHOOT, robot.target_distance);
                    if (!robot.shooter._shooter.haveNote())
                    {
                        robot.shooter.setState(MANIP_STATE.INTAKE, 0.0);
                        nextPose = robot.landmarks.nearRight;
                        step++;
                    }
                }
                break;
            case 8:
                if (!robot.driveBase.move_Pose2d(nextPose))
                {
                    robot.driveBase.stopDrive();
                    step++;
                }
                break;
            case 9:
                if (!robot.driveBase.driveFacing(0.0, 0.0, robot.landmarks.speaker))
                {
                    robot.shooter.setState(MANIP_STATE.SPEAKER_TARGET, robot.target_distance);
                    step++;
                }
                break;
            case 10:
                if (robot.shooter._shooter.ready())
                {
                    robot.shooter.setState(MANIP_STATE.SPEAKER_SHOOT, robot.target_distance);
                    if (!robot.shooter._shooter.haveNote())
                    {
                        nextPose = robot.landmarks.backPose;
                        step++;
                    }
                }
                break;
            case 11:
                if (!robot.driveBase.move_Pose2d(nextPose))
                {
                    robot.driveBase.stopDrive();
                    step++;
                }
            default:
                break;
        }
    }
}