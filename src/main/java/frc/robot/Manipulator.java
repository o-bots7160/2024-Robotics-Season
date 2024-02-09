package frc.robot;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;

//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Manipulator
{
   private Shooter  _shooter    = new Shooter( );
   private Solenoid _extension0 = new Solenoid( PneumaticsModuleType.REVPH, 0 );
   private Solenoid _extension1 = new Solenoid( PneumaticsModuleType.REVPH, 1 );
   private Solenoid _elbow0     = new Solenoid( PneumaticsModuleType.REVPH, 2 );
   private Solenoid _elbow1     = new Solenoid( PneumaticsModuleType.REVPH, 3 );

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

   public Manipulator( )
   {
      disable( );
   }

   public void disable( ) 
   {
      _shooter.disable(       );
      extend          ( false );
      elbowUp         ( false );
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
               extend( false );
            }
            elbowUp( false );
            break;
         case INTAKE:
            if ( _shooter.haveNote( ) )
            {
               _shooter.setState( MANIP_STATE.STOW, distance ); // does this need a delay?
               extend( false );
            }
            else
            {
               _shooter.setState( manip_state, distance ); // does this need a delay?
               extend( true );
            }
            elbowUp( false );
            break;
         case AMPLIFIER_TARGET:
            _shooter.setState( manip_state, distance ); // does this need a delay?
            extend ( false );         // does this need to be true to reach the amp?
            elbowUp( true  );
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
               extend ( false );         // does this need to be true to reach the amp?
               elbowUp( true  );
            }
            else
            {
               _shooter.setState( MANIP_STATE.STOW, distance ); // does this need a delay?
               extend ( false );         // does this need to be true to reach the amp?
               elbowUp( false );
            }
            break;
         case SPEAKER_TARGET:
            _shooter.setState( manip_state, 0.0 ); // does this need a delay?
            extend ( false );
            elbowUp( false );
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
            extend ( false );
            elbowUp( false );
            break;
      }
      _shooter.periodic( distance );
   }
   public void extend( boolean value )
   {
      _extension0.set( value );
      _extension1.set( value );
   }
   public void elbowUp( boolean value )
   {
      _elbow0.set( value );
      _elbow1.set( value );
   }
   public boolean atPosition()
   {
      return _shooter.atPosition();
   }
}