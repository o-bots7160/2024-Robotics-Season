package frc.robot;

import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LED 
{
    private Spark LEDs = new Spark(1);
    private Timer timer = new Timer();
    private Timer hasNoteTimer = new Timer();
    private PneumaticHub pressure = new PneumaticHub(9);
    private RobotContainer robot;
    private boolean noNote = false;

    public LED( RobotContainer new_robot )
    {
        robot = new_robot;
    }

    public void init()
    {
        timer.reset();
        timer.start();
        hasNoteTimer.reset();
    }

    public void periodic()
    {
        if (robot.target_distance < 2.7) // In Shoot Range
        {
            LEDs.set(-0.99); // Rainbow, Rainbow Palette
            noNote = true;
        }
        else if (robot.shooter._shooter.haveNote() && noNote) // Have Note
        {
            LEDs.set(0.77); // Solid Green
            hasNoteTimer.start();
            if (hasNoteTimer.get() < 2.0)
            {   
                noNote = true;
            }
            else
            {
                hasNoteTimer.stop();
                hasNoteTimer.reset();
                noNote = false;
            }
        }
        else if (pressure.getPressure(0) <= 50) // Low Pressure
        {
            LEDs.set(-0.23); // Flash Blue
            noNote = true;
        }
        else if (timer.get() > 105) // EndGame
        {
            LEDs.set(-0.25); // Flash Red
        }
        else // Idle
        {
            LEDs.set(0.93); // White
            noNote = true;
        }
        SmartDashboard.putNumber( "Pneumatic PSI" , pressure.getPressure(0));
    }

    public void disable()
    {

    }
}