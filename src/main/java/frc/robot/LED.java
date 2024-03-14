package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.motorcontrol.Spark;

public class LED 
{
    private Spark LEDs = new Spark(1);
    private Timer timer = new Timer();
    private RobotContainer robot;

    public void init()
    {
        timer.reset();
        timer.start();
    }

    public void periodic()
    {
        if (timer.get() > 105)
        {
            LEDs.set(-0.25);
        }
        else if (robot.shooter._shooter.haveNote())
        {
            LEDs.set(0.77);
        }
    }

    public void disable()
    {

    }
}