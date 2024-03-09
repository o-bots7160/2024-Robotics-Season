package frc.robot.AutonModes;

import com.revrobotics.ColorSensorV3.MainControl;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.OpModeInterface;
import frc.robot.RobotContainer;
import frc.robot.Manipulator.MANIP_STATE;

public class Auton3NearAmp implements OpModeInterface
{

    private RobotContainer robot;

    public Pose2d initPose = new Pose2d();
    public Pose2d nextPose = new Pose2d(3.0, 7.1, new Rotation2d(0.0));
    public Timer autonTimer = new Timer();
    private int step = 0;

    public Auton3NearAmp()
    {
        robot = RobotContainer.getInstance();
    }

    @Override
    public void Init()
    {
        step = 0;
        initPose = robot.driveBase.getPose();
        robot.setManual( false );
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
                    robot.shooter.setState(MANIP_STATE.SPEAKER_SHOOT, 0.0);
                    if (autonTimer.get() > 0.6)
                    {
                        robot.shooter.setState(MANIP_STATE.INTAKE, 0.0);
                        autonTimer.stop();
                        autonTimer.reset();
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
                    autonTimer.start();
                    step++;
                }
                break;
            case 4:
                    robot.shooter.setState(MANIP_STATE.SPEAKER_SHOOT, 0.0);
                    if (autonTimer.get() > 0.5)
                    {
                        robot.shooter.setState(MANIP_STATE.INTAKE, 0.0);
                        autonTimer.stop();
                        autonTimer.reset();
                        nextPose = new Pose2d(3.3, 6.0, new Rotation2d(-Math.PI/2));
                        step++;
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
                    autonTimer.start();
                    robot.shooter.setState(MANIP_STATE.SPEAKER_TARGET, 0.0);
                    step++;
                }
                break;
            case 7:
                if (autonTimer.get() > 0.3)
                {
                    robot.shooter.setState(MANIP_STATE.SPEAKER_SHOOT, 0.0);
                    if (autonTimer.get() > 0.5)
                    {
                        nextPose = new Pose2d(3.5, 5.5, new Rotation2d(-Math.PI/4));
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
            default:
                break;
        }
    }
}