package frc.robot;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Manipulator
{
   private boolean elbowExtended = false;
   public Shooter  _shooter;
   private DoubleSolenoid _extension0 = new DoubleSolenoid( 9, PneumaticsModuleType.REVPH, 0, 1 );
   private DoubleSolenoid _elbow0     = new DoubleSolenoid( 9, PneumaticsModuleType.REVPH, 2, 3 );
   private DoubleSolenoid _elbow1     = new DoubleSolenoid( 9, PneumaticsModuleType.REVPH, 4, 5 );
   private Timer intake_timer = new Timer();
   private Timer amp_timer    = new Timer();
   private boolean wasIntaking = false;

   public static enum MANIP_STATE
   {
      DISABLE,
      STOW,
      INTAKE,
      AMPLIFIER_TARGET,
      AMPLIFIER_SHOOT,
      SPEAKER_TARGET,
      SPEAKER_SHOOT,
      CLIMBING
   };

   private MANIP_STATE manip_state = MANIP_STATE.STOW;

   public Manipulator( BooleanSupplier new_manual )
   {
      _shooter = new Shooter( new_manual );
      disable( );
   }

   public void disable( ) 
   {
      _shooter.disable();
      retract();
      elbowDown();
   }

   public void setState( MANIP_STATE new_state, double distance )
   {
      if ( manip_state != new_state )
      {
         switch( new_state )
         {
            case DISABLE:
               disable();
               break;
            case STOW:
               intake_timer.reset();
               intake_timer.start();
               amp_timer.reset();
               amp_timer.start();
               break;
            case INTAKE:
               intake_timer.reset();
               intake_timer.start();
               break;
            case AMPLIFIER_TARGET:
            case AMPLIFIER_SHOOT:
               amp_timer.reset();
               amp_timer.start();
               break;
            case CLIMBING:
               break;
         }
      }
      manip_state = new_state;
   }

   public void periodic( double distance )
   {
      switch( manip_state )
      {
         case DISABLE:
            disable();
            break;
         case STOW:
            _shooter.setState( manip_state, distance );
            //if ( _shooter.atPosition( ) )
            //{
            if ( intake_timer.get() > 1.0 ) //&& wasIntaking || amp_timer.get() > 0.1 && !wasIntaking)
            {
               retract();
               intake_timer.stop();
               wasIntaking = false;
            }
            //}
            if ( amp_timer.get() > 0.85 )
            {
               elbowDown();
               amp_timer.stop();
               wasIntaking = false;
            }
            break;
         case INTAKE:
            if ( _shooter.haveNote( ) )
            {
               setState( MANIP_STATE.STOW, distance ); // does this need a delay?
               wasIntaking = true;
            }
            else
            {
               wasIntaking = false;
               if ( intake_timer.get() > 1.0 )
               {
                  _shooter.setState( manip_state, distance ); // does this need a delay?
               }
               extend();
            }
            elbowDown();
            break;
         case AMPLIFIER_TARGET:
            elbowUp();
            if ( amp_timer.get() > 2.5 )
            {
               extend();         // does this need to be true to reach the amp?
               _shooter.setState( manip_state, distance ); // does this need a delay?
            }
            break;
         case AMPLIFIER_SHOOT:
            if ( _shooter.haveNote( ) )
            {
               // if ( _shooter.ready( ) )
               // {
                  System.out.println("ManipAmpShoot");
                  _shooter.setState( manip_state, distance );
               // }
               // else
               // {
               //    _shooter.setState( MANIP_STATE.AMPLIFIER_TARGET, distance );
               // }
            }
            else
            {
               wasIntaking = false;
               setState( MANIP_STATE.STOW, distance ); // does this need a delay?
               //retract();         // does this need to be true to reach the amp?
               //elbowDown();
            }
            break;
         case SPEAKER_TARGET:
            _shooter.setState( manip_state, distance ); // does this need a delay?
            retract();
            elbowDown();
            break;
         case SPEAKER_SHOOT:
            if ( _shooter.haveNote( ) )
            {
               //if ( _shooter.ready( ) )
               //{
                  _shooter.setState( manip_state, distance );
               //}
               //else
               //{
               //   _shooter.setState( MANIP_STATE.SPEAKER_TARGET, distance );
               //}
            }
            else
            {
               setState( MANIP_STATE.STOW, distance ); // does this need a delay?
            }
            //retract ();
            //elbowDown();
            break;
            case CLIMBING:
               elbowUp();
               break;
      }
      _shooter.periodic( distance );
   }
   public void extend()
   {
      _extension0.set( Value.kForward );
   }
   public void retract()
   {
      _extension0.set( Value.kReverse );
   }
   public void elbowUp()
   {
      elbowExtended = true;
      _elbow0.set( Value.kForward );
      _elbow1.set( Value.kForward );
   }
   public void elbowDown()
   {
      elbowExtended = false;
      _elbow0.set( Value.kReverse );
      _elbow1.set( Value.kReverse );
   }
   public boolean atPosition()
   {
      return _shooter.atPosition();
   }
}