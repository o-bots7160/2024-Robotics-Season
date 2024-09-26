package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class UI 
{

    Joystick Joystick = new Joystick(0); // Joystick
    Joystick Buttons1 = new Joystick(1); // Button Board
    //Joystick Buttons2 = new Joystick(2); // Button Board

    private boolean intake_active  = false;
    private boolean speaker_shoot  = false;
    private boolean speaker_target = false;
    private boolean amp_target     = false;
    private boolean amp_shoot      = false;
    private boolean robot_oriented = false;
    private boolean limelight      = true;
    private boolean stow_active    = true;

    public boolean intakeActive()
    {
        if (Joystick.getRawAxis(3) > 0.5) // Intake
        {
            intake_active = true;
            speaker_shoot = false;
            speaker_target = false;
            amp_shoot = false;
            amp_target = false;
            stow_active = false;
        }
        else if (Joystick.getRawAxis(3) <= 0.5)
        {
            intake_active = false;
        }
        return intake_active;
    }

    public boolean speakerShoot()
    {
        if (Joystick.getRawButton(6) && speaker_target)
        {
            speaker_shoot = true;
        }
        else
        {
            speaker_shoot = false;
        }
        return speaker_shoot;
    }
    
    public boolean speakerTarget()
    {
        if (Joystick.getRawButtonPressed(1))
        {
            speaker_target = !speaker_target;
        }
        return speaker_target;
    }

    public boolean ampShoot()
    {
        if (Joystick.getRawButton(6) && amp_target)
        {
            amp_shoot = true;
        }
        else
        {
            amp_shoot = false;
        }
        return amp_shoot;
    }

    public boolean ampTarget()
    {
        if (Buttons1.getRawButtonPressed(6))
        {
            amp_target = !amp_target;
        }
        return amp_target;
    }

    public boolean stowActive()
    {
        if (Buttons1.getRawButtonPressed(5))
        {
            stow_active = true;
            intake_active = false;
            speaker_shoot = false;
            speaker_target = false;
            amp_shoot = false;
            amp_target = false;
        }
        else if (Buttons1.getRawButtonReleased(5))
        {
            stow_active = false;
        }
        return stow_active;
    }

    public boolean lockActive()
    {
        return Joystick.getRawButton(3);
    }

    public boolean leftClimbExtend()
    {
        stow_active = false;
        if (Buttons1.getRawButton(3))
        {
            intake_active = false;
            speaker_shoot = false;
            speaker_target = false;
            amp_shoot = false;
            amp_target = false;
        }
        return Buttons1.getRawButton(3);
    }

    public boolean leftClimbRetract()
    {
        return Buttons1.getRawButton(2);
    }

    public boolean rightClimbExtend()
    {
        stow_active = false;
        if (Buttons1.getRawButton(4))
        {
            intake_active = false;
            speaker_shoot = false;
            speaker_target = false;
            amp_shoot = false;
            amp_target = false;
        }
        return Buttons1.getRawButton(4);
    }

    public boolean rightClimbRetract()
    {
        return Buttons1.getRawButton(1);
    }

    public boolean manualRotUp()
    {
        return Buttons1.getRawButton(7);
    }

    public boolean manualRotDown()
    {
        return Buttons1.getRawButton(8);
    }

    public boolean limelightActive()
    {
        if (Buttons1.getRawButtonPressed(9))
        {
            limelight = !limelight;
        }
        return limelight;
    }

    public boolean robotOriented()
    {
        if (Joystick.getRawButtonPressed(8))
        {
            robot_oriented = !robot_oriented;
        }
        return robot_oriented;
    }
}
