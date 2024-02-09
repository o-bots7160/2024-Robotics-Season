package frc.robot;

import com.playingwithfusion.TimeOfFlight;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkBase.SoftLimitDirection;
import com.revrobotics.CANSparkLowLevel.MotorType;

import frc.robot.Manipulator.MANIP_STATE;

//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter
{
   private TimeOfFlight sensor = new TimeOfFlight( 101 );

   private CANSparkMax _angle;
   private SparkPIDController pid_angle;
   private RelativeEncoder en_angle;
   private double angle_target = 0.0; // position

   private CANSparkMax _intake;
   private SparkPIDController pid_intake;
   private RelativeEncoder en_intake;
   private double intake_target = 0.0; // speed

   private CANSparkMax _shoot;
   private SparkPIDController pid_shoot;
   private RelativeEncoder en_shoot;
   private double shooter_target = 0.0;

   private MANIP_STATE state = MANIP_STATE.STOW;

   public Shooter( )
   {
      _angle = new CANSparkMax(50, MotorType.kBrushless);
      _angle.setSmartCurrentLimit(35);
      _angle.setInverted(true);
      _angle.enableSoftLimit(SoftLimitDirection.kReverse, true);
      _angle.setSoftLimit(SoftLimitDirection.kReverse, 6);        //lower limit //FIXME
      _angle.enableSoftLimit(SoftLimitDirection.kForward, true);
      _angle.setSoftLimit(SoftLimitDirection.kForward, 170);      //upper limit //FIXME
      _angle.setIdleMode(IdleMode.kBrake);
      pid_angle = _angle.getPIDController();
      pid_angle.setP          ( 0.8 );
      pid_angle.setI          ( 0.0 );
      pid_angle.setD          ( 0.0 );
      pid_angle.setIZone      ( 0.0 );
      pid_angle.setFF         ( 0.0 );
      pid_angle.setOutputRange( -0.9, 0.9 );
      en_angle = _angle.getEncoder();
      // Do we need this? _angle.burnFlash();

      _intake = new CANSparkMax(50, MotorType.kBrushless);
      _intake.setSmartCurrentLimit(35);
      _intake.setInverted(true);
      _intake.enableSoftLimit(SoftLimitDirection.kReverse, true);
      _intake.setSoftLimit(SoftLimitDirection.kReverse, 6);        //lower limit //FIXME
      _intake.enableSoftLimit(SoftLimitDirection.kForward, true);
      _intake.setSoftLimit(SoftLimitDirection.kForward, 170);      //upper limit //FIXME
      _intake.setIdleMode(IdleMode.kBrake);
      pid_intake = _intake.getPIDController();
      pid_intake.setP          ( 0.8 );
      pid_intake.setI          ( 0.0 );
      pid_intake.setD          ( 0.0 );
      pid_intake.setIZone      ( 0.0 );
      pid_intake.setFF         ( 0.0 );
      pid_intake.setOutputRange( -0.9, 0.9 );
      en_intake = _intake.getEncoder();
      // Do we need this? _intake.burnFlash();

      _shoot = new CANSparkMax(50, MotorType.kBrushless);
      _shoot.setSmartCurrentLimit(35);
      _shoot.setInverted(true);
      _shoot.enableSoftLimit(SoftLimitDirection.kReverse, true);
      _shoot.setSoftLimit(SoftLimitDirection.kReverse, 6);        //lower limit //FIXME
      _shoot.enableSoftLimit(SoftLimitDirection.kForward, true);
      _shoot.setSoftLimit(SoftLimitDirection.kForward, 170);      //upper limit //FIXME
      _shoot.setIdleMode(IdleMode.kBrake);
      pid_shoot = _shoot.getPIDController();
      pid_shoot.setP          ( 0.8 );
      pid_shoot.setI          ( 0.0 );
      pid_shoot.setD          ( 0.0 );
      pid_shoot.setIZone      ( 0.0 );
      pid_shoot.setFF         ( 0.0 );
      pid_shoot.setOutputRange( -0.9, 0.9 );
      en_shoot = _shoot.getEncoder();
      // Do we need this? _shoot.burnFlash();

      disable( );
   }

   public void disable( ) 
   {
      state = MANIP_STATE.DISABLE;

      _angle.set ( 0.0 );
      _intake.set( 0.0 );
      _shoot.set ( 0.0 );
   }

   public void setState( MANIP_STATE new_state, double distance )
   {
      state = new_state;
      switch( state )
      {
         case DISABLE:
            angle_target   = 0.0;
            intake_target  = 0.0;
            shooter_target = 0.0;
            break;
         case STOW:
            angle_target   = 0.0;
            intake_target  = 0.0;
            shooter_target = 0.0;
            break;
         case INTAKE:
            angle_target   = 20.0;
            intake_target  =  0.0;
            shooter_target =  0.0;
            break;
         case AMPLIFIER_TARGET:
            angle_target   = 20.0;  // Set angle for amplifier
            intake_target  =  0.0;
            shooter_target =  0.0;  // Set speed for amplifier
            break;
         case AMPLIFIER_SHOOT:
            angle_target   = 20.0;  // Set angle for amplifier
            intake_target  = 10.0;  // Set for passing to shooter
            shooter_target = 10.0;  // Set speed for amplifier
            break;
         case SPEAKER_TARGET:
            calculateAngleAndSpeedFrom( distance );
            intake_target =  0.0;
            break;
         case SPEAKER_SHOOT:
            calculateAngleAndSpeedFrom( distance );
            intake_target = 10.0;  // Set for passing to shooter
            break;
      }
   }

   public void periodic( double distance )
   {
      switch( state )
      {
         case DISABLE:
            disable( );  // All off
            break;
         case STOW:
         case INTAKE:
         case AMPLIFIER_TARGET:
         case AMPLIFIER_SHOOT:
            angleSetPosition  ( angle_target   );
            intakeSetVelocity ( intake_target  );
            shooterSetVelocity( shooter_target );
            break;
         case SPEAKER_TARGET:
         case SPEAKER_SHOOT:
            calculateAngleAndSpeedFrom( distance );
            angleSetPosition  ( angle_target   );
            intakeSetVelocity ( intake_target  );
            shooterSetVelocity( shooter_target );
            break;
      }
   }

   public boolean atPosition( )
   {
      return ( ready( ) && intakeAtVelocity( ) );
   }

   public boolean ready( )
   {
      return ( angleAtPosition( ) && shooterAtVelocity( ) );
   }

   public boolean haveNote( )
   {
      boolean return_value = false;
      double distance = sensor.getRange();

      if ( sensor.isRangeValid( ) )
      {
         if ( distance < 0.3 ) // TODO: set this
         {
            return_value = true;
         }
      }

      return ( return_value );
   }

   private boolean angleAtPosition()
   {
      double error = angle_target - en_angle.getPosition();
      if ( ( error < 3.0 ) && ( error > -3.0 ) ) // What should these be?
      {
         return true;
      }
      else
      {
         return false;
      }
   }

   private void angleSetPosition( double new_target)
   {
      if (new_target < 0.0 )
      {
         new_target = 0.0;
      }
      else if ( new_target > 230.0 )
      {
         new_target = 230.0;
      }
      angle_target = new_target;
      pid_angle.setReference(new_target, ControlType.kPosition);
   }

   private boolean intakeAtVelocity()
   {
      double error = intake_target - en_intake.getPosition();
      if ( ( error < 3.0 ) && ( error > -3.0 ) ) // What should these be?
      {
         return true;
      }
      else
      {
         return false;
      }
   }

   private void intakeSetVelocity( double new_target)
   {
      if (new_target < 0.0 )
      {
         new_target = 0.0;
      }
      else if ( new_target > 230.0 )
      {
         new_target = 230.0;
      }
      angle_target = new_target;
      pid_intake.setReference(new_target, ControlType.kVelocity);
   }

   private boolean shooterAtVelocity()
   {
      double error = shooter_target - en_shoot.getVelocity();
      if ( ( error < 3.0 ) && ( error > -3.0 ) ) // What should these be?
      {
         return true;
      }
      else
      {
         return false;
      }
   }

   private void shooterSetVelocity( double new_target)
   {
      if (new_target < 0.0 )
      {
         new_target = 0.0;
      }
      else if ( new_target > 230.0 )
      {
         new_target = 230.0;
      }
      shooter_target = new_target;
      pid_shoot.setReference(new_target, ControlType.kVelocity);
   }

   private void calculateAngleAndSpeedFrom( double distance )
   {
      angle_target   = 30.0;
      shooter_target = 40.0;
   }
}