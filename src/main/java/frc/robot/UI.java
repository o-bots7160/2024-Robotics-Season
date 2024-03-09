package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class UI 
{

    Joystick Joystick = new Joystick(0); // Joystick
    Joystick Buttons1 = new Joystick(1); // Button Board
    Joystick Buttons2 = new Joystick(2); // Button Board

    private boolean intake_active  = false;
    private boolean speaker_shoot  = false;
    private boolean speaker_target = false;
    private boolean amp_target     = false;
    private boolean amp_shoot      = false;
    private boolean stow_active    = true;

    public boolean intakeActive()
    {
        if (Joystick.getRawAxis(3) > 0.25) // Intake
        {
            intake_active = true;
            speaker_shoot = false;
            speaker_target = false;
        }
        else if (Joystick.getRawAxis(3) <= 0.25)
        {
            intake_active = false;
        }
        return intake_active;
    }

    public boolean speakerShoot()
    {
        if (Joystick.getRawButtonPressed(6) && !amp_shoot && speaker_shoot)
        {
            speaker_shoot = !speaker_shoot;
        }
        return speaker_shoot;
    }
    
    public boolean speakerTarget()
    {
        if (Buttons1.getRawButtonPressed(3) && !amp_target && speaker_target)
        {
            speaker_target = !speaker_target;
        }
        return speaker_target;
    }

    public boolean ampShoot()
    {
        if (Joystick.getRawButtonPressed(3) && amp_shoot && !speaker_shoot)
        {
            amp_target = !amp_target;
        }
        return speaker_target;
    }

    public boolean ampTarget()
    {
        if (Buttons1.getRawButtonPressed(3) && amp_target && !speaker_shoot)
        {
            amp_target = !amp_target;
        }
        return amp_target;
    }

    public boolean stowActive()
    {
        if (Buttons1.getRawButtonPressed(2))
        {
            stow_active = true;
            intake_active = false;
            speaker_shoot = false;
            speaker_target = false;
        }
        else if (Buttons1.getRawButtonReleased(2))
        {
            stow_active = false;
        }
        return stow_active;
    }

    public boolean lockActive()
    {
        return Buttons1.getRawButton(1);
    }

    public boolean leftClimbExtend()
    {
        return Buttons1.getRawButton(11);
    }

    public boolean leftClimbRetract()
    {
        return Buttons1.getRawButton(7);
    }

    public boolean rightClimbExtend()
    {
        return Buttons2.getRawButton(6);
    }

    public boolean rightClimbRetract()
    {
        return Buttons2.getRawButton(5);
    }

    public boolean manualRotUp()
    {
        return Buttons1.getRawButton(5);
    }

    public boolean manualRotDown()
    {
        return Buttons1.getRawButton(6);
    }

    public boolean limelightActive()
    {
        return Buttons1.getRawButton(4);
    }
}
