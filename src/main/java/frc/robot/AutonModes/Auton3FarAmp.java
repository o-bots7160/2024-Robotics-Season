package frc.robot.AutonModes;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.OpModeInterface;
import frc.robot.RobotContainer;
import frc.robot.Manipulator.MANIP_STATE;

public class Auton3FarAmp implements OpModeInterface
{

    private RobotContainer robot;

    //public Pose2d initPose;
    public Pose2d nextPose;
    public Timer autonTimer = new Timer(); //Centerline: nextPose = new Pose2d(6.1, 7.0, new Rotation2d(0.0)); //FIXME
    private int step = 0;

    public Auton3FarAmp()
    {
        robot = RobotContainer.getInstance();
    }

    @Override
    public void Init()
    {
        step = 0;
        //initPose = robot.driveBase.getPose();
        robot.setManual( false );
        nextPose = robot.landmarks.nearLeft;
        //initPose = new Pose2d();
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
                    autonTimer.restart();
                    step++;
                }
                break;
            case 1:
                if (robot.shooter._shooter.ready() && autonTimer.get() > 1.0)
                {
                    robot.shooter.setState(MANIP_STATE.SPEAKER_SHOOT, robot.target_distance);
                    if (!robot.shooter._shooter.haveNote())
                    {
                        robot.shooter.setState(MANIP_STATE.INTAKE, 0.0);
                        autonTimer.restart();
                        step++;
                    }
                }
                break;
            case 2:
                if (autonTimer.get() > 0.75)
                {
                    step++;
                }
                break;
            case 3:
                if (!robot.driveBase.move_Pose2d(nextPose))
                {
                    robot.driveBase.stopDrive();
                    step++;
                }
                break;
            case 4:
                if (!robot.driveBase.driveFacing(0.0, 0.0, robot.landmarks.speaker))
                {
                    robot.shooter.setState(MANIP_STATE.SPEAKER_TARGET, robot.target_distance);
                    autonTimer.restart();
                    step++;
                }
                break;
            case 5:
                if (robot.shooter._shooter.ready() && autonTimer.get() > 1.0)
                {
                    robot.shooter.setState(MANIP_STATE.SPEAKER_SHOOT, robot.target_distance);
                    if (!robot.shooter._shooter.haveNote())
                    {
                        robot.shooter.setState(MANIP_STATE.INTAKE, 0.0);
                        nextPose = robot.landmarks.farLeft;
                        step++;
                    }
                }
                break;
            case 6:
                if (!robot.driveBase.move_Pose2d(nextPose))
                {
                    robot.driveBase.stopDrive();
                    nextPose = robot.landmarks.nearLeft;
                    step++;
                }
                break;
            case 7:
                if (!robot.driveBase.move_Pose2d(nextPose))
                {
                    robot.driveBase.stopDrive();
                    step++;
                }
                break;
            case 8:
                if (!robot.driveBase.driveFacing(0.0, 0.0, robot.landmarks.speaker))
                {
                    autonTimer.restart();
                    step++;
                }
                break;
            case 9:
                if (robot.shooter._shooter.ready() && autonTimer.get() > 1.0)
                {
                    robot.shooter.setState(MANIP_STATE.SPEAKER_SHOOT, robot.target_distance);
                    step++;
                }
                break;
            default:
                break;
        }
    }
}
