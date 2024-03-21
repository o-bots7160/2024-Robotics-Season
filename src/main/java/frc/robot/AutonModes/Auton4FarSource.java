package frc.robot.AutonModes;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.OpModeInterface;
import frc.robot.RobotContainer;
import frc.robot.Manipulator.MANIP_STATE;

public class Auton4FarSource implements OpModeInterface
{

    private RobotContainer robot;

    //public Pose2d initPose;
    public Pose2d nextPose;
    public Timer autonTimer = new Timer();
    private int step = 0;

    public Auton4FarSource()
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
        nextPose = robot.landmarks.farRight;
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
                    robot.shooter.setState(MANIP_STATE.SPEAKER_TARGET, 0.0);
                    autonTimer.start();
                    step++;
                }
                break;
            case 1:
                if (autonTimer.get() > 0.3)
                {
                    autonTimer.stop();
                    autonTimer.reset();
                    robot.shooter.setState(MANIP_STATE.SPEAKER_SHOOT, 0.0);
                    autonTimer.start();
                    step++;
                }
                break;
            case 2:
                if (!robot.driveBase.move_Pose2d(nextPose))
                {
                    autonTimer.start();
                    robot.driveBase.stopDrive();
                    step++;
                }
                break;
            case 3:
                if (autonTimer.get() > 0.1)
                {
                    autonTimer.stop();
                    robot.shooter.setState(MANIP_STATE.INTAKE, 0.0);
                    autonTimer.reset();
                    nextPose = robot.landmarks.sourceShoot; //FIXME
                    step++;
                }
                break;
            case 4:
                if (!robot.driveBase.move_Pose2d(nextPose))
                {
                    autonTimer.start();
                    robot.driveBase.stopDrive();
                    step++;
                }
                break;
            case 5:
                if (!robot.driveBase.driveFacing(0.0, 0.0, robot.landmarks.speaker))
                {
                    autonTimer.start();
                    step++;
                }
                break;
            case 6:
                if (autonTimer.get() > 0.2)
                {
                    autonTimer.stop();
                    robot.shooter.setState(MANIP_STATE.INTAKE, 0.0);
                    autonTimer.reset();
                    nextPose = robot.landmarks.farRightCenter; //FIXME
                    step++;
                }
                break;
            case 7:
                if (!robot.driveBase.move_Pose2d(nextPose))
                {
                    autonTimer.start();
                    robot.driveBase.stopDrive();
                    step++;
                }
                break;
            case 8:
                if (autonTimer.get() > 0.1)
                {
                    autonTimer.stop();
                    robot.shooter.setState(MANIP_STATE.INTAKE, 0.0);
                    autonTimer.reset();
                    nextPose = robot.landmarks.sourceShoot; //FIXME
                    step++;
                }
                break;
            case 9:
                if (!robot.driveBase.move_Pose2d(nextPose))
                {
                    autonTimer.start();
                    robot.driveBase.stopDrive();
                    step++;
                }
                break;
            case 10:
                if (!robot.driveBase.driveFacing(0.0, 0.0, robot.landmarks.speaker))
                {
                    autonTimer.start();
                    step++;
                }
                break;
            case 11:
                if (autonTimer.get() > 0.2)
                {
                    autonTimer.stop();
                    robot.shooter.setState(MANIP_STATE.INTAKE, 0.0);
                    autonTimer.reset();
                    nextPose = robot.landmarks.farCenter; //FIXME
                    step++;
                }
                break;
            case 12:
                if (!robot.driveBase.move_Pose2d(nextPose))
                {
                    autonTimer.start();
                    robot.driveBase.stopDrive();
                    step++;
                }
                break;
            case 13:
                if (autonTimer.get() > 0.1)
                {
                    autonTimer.stop();
                    robot.shooter.setState(MANIP_STATE.INTAKE, 0.0);
                    autonTimer.reset();
                    nextPose = robot.landmarks.sourceShoot; //FIXME
                    step++;
                }
                break;
            case 14:
                if (!robot.driveBase.move_Pose2d(nextPose))
                {
                    autonTimer.start();
                    robot.driveBase.stopDrive();
                    step++;
                }
                break;
            case 15:
                if (!robot.driveBase.driveFacing(0.0, 0.0, robot.landmarks.speaker))
                {
                    autonTimer.start();
                    step++;
                }
                break;
            default:
                break;
        }
    }
}
