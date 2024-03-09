package frc.robot;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Manipulator
{
   private Shooter  _shooter;
   private DoubleSolenoid _extension0 = new DoubleSolenoid( 9, PneumaticsModuleType.REVPH, 0, 1 );
   private DoubleSolenoid _elbow0     = new DoubleSolenoid( 9, PneumaticsModuleType.REVPH, 2, 3 );
   private DoubleSolenoid _elbow1     = new DoubleSolenoid( 9, PneumaticsModuleType.REVPH, 4, 5 );

   public static enum MANIP_STATE
   {
      DISABLE,
      STOW,
      INTAKE,
      AMPLIFIER_TARGET,
      AMPLIFIER_SHOOT,
      SPEAKER_TARGET,
      SPEAKER_SHOOT
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

   public void setState( MANIP_STATE new_state )
   {
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
            if ( _shooter.atPosition( ) )
            {
               retract();
            }
            elbowDown();
            break;
         case INTAKE:
            if ( _shooter.haveNote( ) )
            {
               _shooter.setState( MANIP_STATE.STOW, distance ); // does this need a delay?
               retract();
            }
            else
            {
               _shooter.setState( manip_state, distance ); // does this need a delay?
               extend();
            }
            elbowDown();
            break;
         case AMPLIFIER_TARGET:
            _shooter.setState( manip_state, distance ); // does this need a delay?
            extend();         // does this need to be true to reach the amp?
            elbowUp();
            break;
         case AMPLIFIER_SHOOT:
            if ( _shooter.haveNote( ) )
            {
               if ( _shooter.ready( ) )
               {
                  _shooter.setState( manip_state, distance );
               }
               else
               {
                  _shooter.setState( MANIP_STATE.AMPLIFIER_TARGET, distance );
               }
               extend();         // does this need to be true to reach the amp?
               elbowUp();
            }
            else
            {
               _shooter.setState( MANIP_STATE.STOW, distance ); // does this need a delay?
               retract();         // does this need to be true to reach the amp?
               elbowDown();
            }
            break;
         case SPEAKER_TARGET:
            _shooter.setState( manip_state, 0.0 ); // does this need a delay?
            retract();
            elbowDown();
            break;
         case SPEAKER_SHOOT:
            if ( _shooter.haveNote( ) )
            {
               if ( _shooter.ready( ) )
               {
                  _shooter.setState( manip_state, 0.0 );
               }
               else
               {
                  _shooter.setState( MANIP_STATE.SPEAKER_TARGET, distance );
               }
            }
            else
            {
               _shooter.setState( MANIP_STATE.STOW, distance ); // does this need a delay?
            }
            retract ();
            elbowDown();
            break;
      }
      _shooter.periodic( distance );
   }
   public void extend()
   {
      _extension0.set( Value.kReverse );
   }
   public void retract()
   {
      _extension0.set( Value.kForward );
   }
   public void elbowUp()
   {
      _elbow0.set( Value.kReverse );
      _elbow1.set( Value.kForward );
   }
   public void elbowDown()
   {
      _elbow0.set( Value.kForward );
      _elbow1.set( Value.kReverse );
   }
   public boolean atPosition()
   {
      return _shooter.atPosition();
   }
}