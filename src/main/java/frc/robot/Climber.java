package frc.robot;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import frc.robot.Manipulator.MANIP_STATE;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkBase.SoftLimitDirection;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.CANSparkLowLevel.MotorType;

public class Climber {
    
    private final CANSparkMax _leftClimber;
    private final CANSparkMax _rightClimber;

    public Climber()
    {
        _leftClimber = new CANSparkMax(50, MotorType.kBrushless);
        _leftClimber.setSmartCurrentLimit(40);
        _leftClimber.setInverted(false);
        _leftClimber.enableSoftLimit(SoftLimitDirection.kReverse, false);
        _leftClimber.setSoftLimit(SoftLimitDirection.kReverse, 0);        //lower limit //FIXME
        _leftClimber.enableSoftLimit(SoftLimitDirection.kForward, false);
        _leftClimber.setSoftLimit(SoftLimitDirection.kForward, 100);      //upper limit //FIXME
        _leftClimber.setIdleMode(IdleMode.kBrake);

        _rightClimber = new CANSparkMax(51, MotorType.kBrushless);
        _rightClimber.setSmartCurrentLimit(40);
        _rightClimber.setInverted(false);
        _rightClimber.enableSoftLimit(SoftLimitDirection.kReverse, false);
        _rightClimber.setSoftLimit(SoftLimitDirection.kReverse, 0);        //lower limit //FIXME
        _rightClimber.enableSoftLimit(SoftLimitDirection.kForward, false);
        _rightClimber.setSoftLimit(SoftLimitDirection.kForward, 100);      //upper limit //FIXME
        _rightClimber.setIdleMode(IdleMode.kBrake);

    }
    
   public void disable( ) 
   {
      _leftClimber.set(0.0);
      _rightClimber.set(0.0);
   }

   public void periodic()
   {
    
   }

    public void leftExtend( )
    {
        _leftClimber.set(0.25);
    }

    public void rightExtend( )
    {
        _rightClimber.set(0.25);
    }

    public void leftRetract( )
    {
        _leftClimber.set(-0.25);
    }

    public void rightRetract( )
    {
        _rightClimber.set(-0.25);
    }

    public void leftStop( )
    {
        _leftClimber.set(0.0);
    }

    public void rightStop( )
    {
        _rightClimber.set(0.0);
    }
}