package frc.robot;

import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.playingwithfusion.TimeOfFlight;
import com.playingwithfusion.TimeOfFlight.RangingMode;
import com.playingwithfusion.CANVenom.BrakeCoastMode;
import com.playingwithfusion.CANVenom.ControlMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkBase.SoftLimitDirection;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.SparkMaxAlternateEncoder.Type;

import frc.robot.Manipulator.MANIP_STATE;

//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter
{
   private TimeOfFlight sensor = new TimeOfFlight( 101 );

   private CANSparkMax _angle;
   private SparkPIDController pid_angle;
   private DutyCycleEncoder en_angle;
   private double angle_target = 0.0; // position

   private TalonFX _intake;
   //private SparkPIDController pid_intake;
   //private RelativeEncoder en_intake;
   //private double intake_target = 0.0; // speed

   private TalonFX _topShoot;
   //private SparkPIDController pid_topShoot;
   //private RelativeEncoder en_topShoot;
   private double topShooter_target = 0.0;

   private TalonFX _bottomShoot;

   private MANIP_STATE state = MANIP_STATE.STOW;

   public Shooter( )
   {
      sensor.setRangingMode(RangingMode.Short, 24);

      _angle = new CANSparkMax(56, MotorType.kBrushless);
      _angle.setSmartCurrentLimit(40);
      _angle.setInverted(false);
      _angle.enableSoftLimit(SoftLimitDirection.kReverse, true);
      _angle.setSoftLimit(SoftLimitDirection.kReverse, 0);        //lower limit //FIXME
      _angle.enableSoftLimit(SoftLimitDirection.kForward, true);
      _angle.setSoftLimit(SoftLimitDirection.kForward, 100);      //upper limit //FIXME
      _angle.setIdleMode(IdleMode.kBrake);
      pid_angle = _angle.getPIDController();
      pid_angle.setP          ( 0.5 );
      pid_angle.setI          ( 0.0 );
      pid_angle.setD          ( 0.0 );
      pid_angle.setIZone      ( 0.0 );
      pid_angle.setFF         ( 0.0 );
      pid_angle.setOutputRange( -0.9, 0.9 );
      en_angle = new DutyCycleEncoder( 0);//_angle.getAlternateEncoder(8192);
      // Do we need this? _angle.burnFlash();

      _intake = new TalonFX(53);
      //_intake.setControl( );
      // _intake.setSmartCurrentLimit(35);
      // _intake.setInverted(true);
      // _intake.enableSoftLimit(SoftLimitDirection.kReverse, true);
      // _intake.setSoftLimit(SoftLimitDirection.kReverse, 6);        //lower limit //FIXME
      // _intake.enableSoftLimit(SoftLimitDirection.kForward, true);
      // _intake.setSoftLimit(SoftLimitDirection.kForward, 170);      //upper limit //FIXME
      _intake.setNeutralMode(NeutralModeValue.Brake);
      // pid_intake = _intake.getPIDController();
      // pid_intake.setP          ( 0.8 );
      // pid_intake.setI          ( 0.0 );
      // pid_intake.setD          ( 0.0 );
      // pid_intake.setIZone      ( 0.0 );
      // pid_intake.setFF         ( 0.0 );
      // pid_intake.setOutputRange( -0.9, 0.9 );
      // en_intake = _intake.getEncoder();
      // Do we need this? _intake.burnFlash();

      _topShoot = new TalonFX(54);
      // _topShoot.setVoltage(35);
      // _topShoot.setInverted(true);
      // _topShoot.setReverseLimit(SoftLimitDirection.kReverse, true);
      // _topShoot.setSoftLimit(SoftLimitDirection.kReverse, 6);        //lower limit //FIXME
      // _topShoot.enableSoftLimit(SoftLimitDirection.kForward, true);
      // _topShoot.setSoftLimit(SoftLimitDirection.kForward, 170);      //upper limit //FIXME
      // _topShoot.setNeutralMode(NeutralModeValue.Brake);
      // pid_topShoot = _topShoot.
      // pid_topShoot.setP          ( 0.8 );
      // pid_topShoot.setI          ( 0.0 );
      // pid_topShoot.setD          ( 0.0 );
      // pid_topShoot.setIZone      ( 0.0 );
      // pid_topShoot.setFF         ( 0.0 );
      // pid_topShoot.setOutputRange( -0.9, 0.9 );
      // en_topShoot = _topShoot.getEncoder();
      // Do we need this? _topShoot.burnFlash();

      _bottomShoot = new TalonFX(55);
      _bottomShoot.setControl(new Follower(54, true));

      disable( );
   } 

   public void disable( ) 
   {
      state = MANIP_STATE.DISABLE;

      _angle.set ( 0.0 );
      intakeSetVelocity( 0.0 );
      shooterSetVelocity( 0.0 );
   }

   public void setState( MANIP_STATE new_state, double distance )
   {
      state = new_state;
      switch( state )
      {
         case DISABLE:
            //angleSetPosition( 0.0 );
            intakeSetVelocity( 0.0 );
            shooterSetVelocity( 0.0 );
            break;
         case STOW:
            //angleSetPosition( 0.0 );
            intakeSetVelocity( 0.0 );            // topShooter_target = 0.0;
            shooterSetVelocity( 0.0 );
            break;
         case INTAKE:
            if ( ! haveNote() )
            {
               //angleSetPosition( Math.PI / 2.0 );
               intakeSetVelocity( 80.0 );
               shooterSetVelocity( 0.0 );
            }
            else
            {
               setState(MANIP_STATE.STOW, distance);
            }
            break;
         case AMPLIFIER_TARGET:
            //angleSetPosition( 0.0 );
            // intake_target  =  0.0;
            // topShooter_target =  0.0;  // Set speed for amplifier
            break;
         case AMPLIFIER_SHOOT:
            //angleSetPosition( 0.0 );
            // intake_target  = 10.0;  // Set for passing to topShooter
            // topShooter_target = 10.0;  // Set speed for amplifier
            break;
         case SPEAKER_TARGET:
            if ( haveNote( ) )
            {
               intakeSetVelocity( 0.0 );            // topShooter_target = 0.0;
               calculateAngleAndSpeedFrom( distance );
            }
            else
            {
               setState(MANIP_STATE.STOW, distance);
            }
            break;
         case SPEAKER_SHOOT:
            if ( haveNote( ) )
            {
               calculateAngleAndSpeedFrom( distance );
               intakeSetVelocity( 80.0 );
            }
            else
            {
               setState(MANIP_STATE.STOW, distance);
            }
            break;
      }
      System.out.println( "state:" + state );
   }

   public void periodic( double distance )
   {
      SmartDashboard.putNumber("angle position", en_angle.getAbsolutePosition());
      SmartDashboard.putNumber("TOF", sensor.getRange() );
      switch( state )
      {
         case DISABLE:
            disable( );  // All off
            break;
         case STOW:
            //angleSetPosition( 0.0 );
            intakeSetVelocity( 0.0 );
            shooterSetVelocity( 0.0 );
            break;
         case INTAKE:
            if ( haveNote() )
            {
               setState( MANIP_STATE.STOW, distance );
            }
            break;
         case AMPLIFIER_TARGET:
         case AMPLIFIER_SHOOT:
            //angleSetPosition  ( angle_target   );
            // intakeSetVelocity ( intake_target  );
            //topShooterSetVelocity( topShooter_target );
            break;
         case SPEAKER_TARGET:
         case SPEAKER_SHOOT:
            if ( ! haveNote() )
            {
               setState( MANIP_STATE.STOW, distance );
            }
            else
            {
               calculateAngleAndSpeedFrom( distance );
            }
            break;
      }
   }

   public boolean atPosition( )
   {
      return ( ready( ) && intakeAtVelocity( ) );
   }

   public boolean ready( )
   {
      return ( angleAtPosition( ) && topShooterAtVelocity( ) );
   }

   public boolean haveNote( )
   {
      boolean return_value = false;
      double distance      = sensor.getRange();

      //if ( sensor.isRangeValid( ) )
      //{
         if ( distance < 215.0 )
         {
            return_value = true;
         }
      //}
      return ( return_value );
   }

   private boolean angleAtPosition()
   {
      double error = angle_target - en_angle.getAbsolutePosition();
      if ( ( error < 3.0 ) && ( error > -3.0 ) ) // What should these be?
      {
         return true;
      }
      else
      {
         return false;
      }
   }

   public void angleSetPosition( double new_target)
   {
      // if (new_target < 0.0 )
      // {
      //    new_target = 0.0;
      // }
      // else if ( new_target > 230.0 )
      // {
      //    new_target = 230.0;
      // }
      // angle_target = new_target;
      //pid_angle.setReference(new_target, ControlType.kPosition);
      _angle.set( new_target );
   }

   private boolean intakeAtVelocity()
   {
      // double error = intake_target - en_intake.getPosition();
      // if ( ( error < 3.0 ) && ( error > -3.0 ) ) // What should these be?
      // {
      //    return true;
      // }
      // else
      // {
         return false;
      // }
   }

   public void intakeSetVelocity( double new_target)
   {
      // if (new_target < 0.0 )
      // {
      //    new_target = 0.0;
      // }
      // else if ( new_target > 230.0 )
      // {
      //    new_target = 230.0;
      // }
      //angle_target = new_target;
      _intake.setControl( new DutyCycleOut( new_target ) );
      // pid_intake.setReference(new_target, ControlType.kVelocity);
   }

   private boolean topShooterAtVelocity()
   {
      // double error = topShooter_target - en_topShoot.getVelocity();
      // if ( ( error < 3.0 ) && ( error > -3.0 ) ) // What should these be?
      // {
      //    return true;
      // }
      // else
      // {
         return false;
      // }
   }

   public void shooterSetVelocity( double new_target)
   {
      // if (new_target < 0.0 )
      // {
      //    new_target = 0.0;
      // }
      // else if ( new_target > 230.0 )
      // {
      //    new_target = 230.0;
      // }
      // topShooter_target = new_target;
      // pid_topShoot.setReference(new_target, ControlType.kVelocity);
      _topShoot.setControl( new DutyCycleOut( new_target ) );
   }

   private void calculateAngleAndSpeedFrom( double distance )
   {
      angle_target   = 30.0;
      shooterSetVelocity( 0.40 );
   }
}