package frc.robot.AutonModes;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.OpModeInterface;
import frc.robot.RobotContainer;

public class Auton1Speaker implements OpModeInterface
{

    private RobotContainer robot;

    public Pose2d initPose = new Pose2d();
    public Pose2d nextPose = new Pose2d(3.0, 7.25, new Rotation2d(0.0));
    public Timer autonTimer = new Timer();
    private int step = 0;
    public Translation2d blueSpeaker = new Translation2d( 1.3, 6.0 );
    public Translation2d redSpeaker  = new Translation2d(  8.308975, 1.442593 );

    public Auton1Speaker()
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
                if (!robot.driveBase.driveFacing(0.0, 0.0, blueSpeaker))
                {
                    autonTimer.start();
                    step++;
                }
                break;
            case 1:
                if (autonTimer.get() > 0.2)
                {
                    autonTimer.stop();
                    autonTimer.reset();
                    step++;
                }
                break;
            case 2:
                if (!robot.driveBase.move_Pose2d(nextPose))
                {
                    autonTimer.start();
                    step++;
                }
                break;
            case 3:
                if (autonTimer.get() > 0.1)
                {
                    autonTimer.stop();
                    autonTimer.reset();
                    step++;
                }
                break;
            case 4:
                if (!robot.driveBase.driveFacing(0.0, 0.0, blueSpeaker))
                {
                    autonTimer.start();
                    step++;
                }
                break;
            case 5:
                if (autonTimer.get() > 0.2)
                {
                    autonTimer.stop();
                    autonTimer.reset();
                    nextPose = new Pose2d(3.0, 5.5, new Rotation2d(0.0));
                    step++;
                }
                break;
            case 6:
                if (!robot.driveBase.move_Pose2d(nextPose))
                {
                    autonTimer.start();
                    step++;
                }
                break;
            case 7:
                if (autonTimer.get() > 0.1)
                {
                    autonTimer.stop();
                    autonTimer.reset();
                    step++;
                }
                break;
            case 8:
                if (!robot.driveBase.driveFacing(0.0, 0.0, blueSpeaker))
                {
                    autonTimer.start();
                    step++;
                }
                break;
            case 9:
                if (autonTimer.get() > 0.2)
                {
                    autonTimer.stop();
                    autonTimer.reset();
                    nextPose = new Pose2d(3.0, 4.15, new Rotation2d(0.0));
                    step++;
                }
                break;
            case 10:
                if (!robot.driveBase.move_Pose2d(nextPose))
                {
                    autonTimer.start();
                    step++;
                }
            case 11:
                if (autonTimer.get() > 0.1)
                {
                    autonTimer.stop();
                    autonTimer.reset();
                    step++;
                }
                break;
            case 12:
                if (!robot.driveBase.driveFacing(0.0, 0.0, blueSpeaker))
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
