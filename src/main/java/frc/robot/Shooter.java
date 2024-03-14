package frc.robot;

import java.util.function.BooleanSupplier;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.playingwithfusion.TimeOfFlight;
import com.playingwithfusion.TimeOfFlight.RangingMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkBase.SoftLimitDirection;
import com.revrobotics.CANSparkLowLevel.MotorType;

import frc.robot.Manipulator.MANIP_STATE;

//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter
{
   private TimeOfFlight sensor = new TimeOfFlight( 0 );

   private CANSparkMax _angle;
   private PIDController pid_angle = new PIDController(6.65000, 0.0, 0.0);
   private DutyCycleEncoder en_angle;
   private double angle_target = 0.0; // position
   final double travel_angle = Math.toRadians(290.0);
   final double intake_angle = Math.toRadians(340.0);
   final double amp_angle    = Math.toRadians(320.0);
   private RelativeEncoder angleEncoder;

   private TalonFX _intake;
   //private SparkPIDController pid_intake;
   //private RelativeEncoder en_intake;
   //private double intake_target = 0.0; // speed

   private TalonFX _topShoot;
   private Slot0Configs pid_shoot = new Slot0Configs();
   //private PIDController pid_topShoot;
   //private RelativeEncoder en_topShoot;
   private double topShooter_target = 0.0;

   private TalonFX _bottomShoot;

   Timer intakeTimer = new Timer();
   Timer shootTimer = new Timer();
   BooleanSupplier manual;
   double manualAngleCommand = 0.0;

   private MANIP_STATE state = MANIP_STATE.STOW;
   boolean hasNote = false;

   public Shooter(  BooleanSupplier new_manual )
   {
      manual = new_manual;
      sensor.setRangingMode(RangingMode.Short, 24);

      _angle = new CANSparkMax(56, MotorType.kBrushless);
      _angle.setSmartCurrentLimit(40);
      _angle.setInverted(false);
      _angle.enableSoftLimit(SoftLimitDirection.kReverse, true);
      _angle.setSoftLimit(SoftLimitDirection.kReverse, 0);        //lower limit //FIXME
      _angle.enableSoftLimit(SoftLimitDirection.kForward, true);
      _angle.setSoftLimit(SoftLimitDirection.kForward, 100);      //upper limit //FIXME
      _angle.setIdleMode(IdleMode.kBrake);
      angleEncoder = _angle.getEncoder( );

      pid_angle.enableContinuousInput(0.0, 2.0 * Math.PI);
      pid_angle.setTolerance(Math.toRadians(1.0));
      //pid_angle.setIZone      ( 0.0 );
      //pid_angle.setFF         ( 0.0 );
      //pid_angle.setOutputRange( -0.9, 0.9 );
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
      pid_shoot.kP = 0.5;
      pid_shoot.kI = 0.0;
      pid_shoot.kD = 0.0;
      _topShoot.getConfigurator().apply(pid_shoot);
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
      angleSetPosition( travel_angle );
      intakeSetVelocity( 0.0 );
      shooterSetVelocity( 0.0 );
   }

   public void setState( MANIP_STATE new_state, double distance )
   {
      state = new_state;
      switch( state )
      {
         case DISABLE:
            angleSetPosition( travel_angle );
            intakeSetVelocity( 0.0 );
            shooterSetVelocity( 0.0 );
            break;
         case STOW:
            angleSetPosition( travel_angle );
            intakeSetVelocity( 0.0 );
            shooterSetVelocity( 0.0 );
            break;
         case INTAKE:
            if ( ! haveNote() )
            {
               angleSetPosition( intake_angle );
               intakeSetVelocity( 40.0 );
               shooterSetVelocity( 0.0 );
            }
            else
            {
               setState(MANIP_STATE.STOW, distance);
            }
            break;
         case AMPLIFIER_TARGET:
            if ( haveNote( ) )
            {
               intakeSetVelocity( 0.0 );
               angleSetPosition( amp_angle );
               shooterSetVelocity( 0.5 );
            }
            else
            {
               setState(MANIP_STATE.STOW, distance);
            }
            break;
         case AMPLIFIER_SHOOT:
            if ( haveNote( ) )
            {
               intakeSetVelocity( 80.0 );
               angleSetPosition( amp_angle );
               shooterSetVelocity( 0.5 );
            }
            else
            {
               setState(MANIP_STATE.STOW, distance);
            }
            break;
         case SPEAKER_TARGET:
            if ( haveNote( ) )
            {
               intakeSetVelocity( 0.0 );
               calculateAngleAndSpeedFrom( distance );
               angleSetPosition( angle_target );
               shooterSetVelocity(topShooter_target);
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
               angleSetPosition( angle_target );
               shooterSetVelocity(topShooter_target);
            }
            else
            {
               setState(MANIP_STATE.STOW, distance);
            }
            break;
      }
      //System.out.println( "state:" + state );
   }

   public void periodic( double distance )
   {
      double angleRadians = en_angle.getAbsolutePosition() * 2.0 * Math.PI;
      SmartDashboard.putNumber("shooter/angle position", Math.toDegrees( angleRadians ));
      SmartDashboard.putNumber("shooter/angle target", Math.toDegrees( pid_angle.getSetpoint() ));
      SmartDashboard.putNumber("shooter/TOF", sensor.getRange() );
      SmartDashboard.putBoolean("shooter/manual", manual.getAsBoolean() );
      SmartDashboard.putNumber("shooter/encoder", angleEncoder.getPosition() );
      switch( state )
      {
         case DISABLE:
            disable( );  // All off
            break;
         case STOW:
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
            if ( ! haveNote() )
            {
               setState( MANIP_STATE.STOW, distance );
            }
            else
            {
               intakeSetVelocity( 0.0 );
               angleSetPosition( amp_angle );
               shooterSetVelocity( 0.5 );
            }
            break;
         case AMPLIFIER_SHOOT:
            if ( ! haveNote() )
            {
               setState( MANIP_STATE.STOW, distance );
            }
            else
            {
               intakeSetVelocity( 80.0 );
               angleSetPosition( amp_angle );
               shooterSetVelocity( 0.5 );
            }
            break;
         case SPEAKER_TARGET:
            if ( ! haveNote() )
            {
               setState( MANIP_STATE.STOW, distance );
            }
            else
            {
               intakeSetVelocity(0.0);
               calculateAngleAndSpeedFrom( distance );
               angleSetPosition( angle_target );
               shooterSetVelocity(topShooter_target);
            }
            break;
         case SPEAKER_SHOOT:
            if ( ! haveNote() )
            {
               setState( MANIP_STATE.STOW, distance );
            }
            else
            {
               intakeSetVelocity(80.0);
               calculateAngleAndSpeedFrom( distance );
               angleSetPosition( angle_target );
               shooterSetVelocity(topShooter_target);
            }
            break;
      }
      if ( manual.getAsBoolean() )
      {
         _angle.setVoltage( manualAngleCommand );
      }
      else
      {
        _angle.setVoltage( pid_angle.calculate( angleRadians ) + Math.cos( angle_target - travel_angle ) * -0.45 );
      }
      shooterSetVelocity(topShooter_target);
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
            hasNote = true;
         }
         // else
         // {
         //    if (hasNote)
         //    {
         //       intakeTimer.reset();
         //       intakeTimer.start();
         //    }
         //    if ( intakeTimer.get() > 1.0 )
         //    {
         //       return_value = false;
         //    }
         //    else
         //    {
         //       return_value = true;
         //    }
         //    hasNote = false;
         // }
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
      angle_target = new_target;
      pid_angle.setSetpoint(new_target);
      //_angle.set( new_target );
      _angle.enableSoftLimit(SoftLimitDirection.kReverse, true);
      _angle.enableSoftLimit(SoftLimitDirection.kForward, true);
   }

   public void manualAngle( double speed )
   {
      manualAngleCommand = speed;
      if ( speed < 0 )
      {
         if ( angleEncoder.getPosition() < 0.0 )
         {
            angleEncoder.setPosition( 0.0 );
         }
      }
      _angle.enableSoftLimit(SoftLimitDirection.kReverse, false);
      _angle.enableSoftLimit(SoftLimitDirection.kForward, false);
   }

   private boolean intakeAtVelocity()
   {
      // double error = intake_target - en_intake.getPosition();
      if ( intakeTimer.get() > 0.5 ) // What should these be?
      {
         return true;
      }
      else
      {
         return false;
      }
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
      intakeTimer.reset();
      intakeTimer.start();
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
       topShooter_target = new_target;
      // pid_topShoot.setReference(new_target, ControlType.kVelocity);
      _topShoot.setControl( new DutyCycleOut( new_target ) );//new VelocityDutyCycle( new_target ) );
   }

   private void calculateAngleAndSpeedFrom( double distance )
   {
      double min_distance = 1.2446;
      double max_distance = 2.7;
      double max_angle    = Math.toRadians(14);

      if (distance < min_distance)
      {
         distance = min_distance;
      }
      else if (distance > max_distance)
      {
         distance = max_distance;
      }

      //angle_target   = travel_angle; 
      angle_target   = travel_angle + ((max_angle)*((max_distance-distance)/(max_distance-min_distance))) - Math.toRadians(2.0);
      topShooter_target = 0.65;
   }
}