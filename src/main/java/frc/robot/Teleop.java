package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Manipulator.MANIP_STATE;

public class Teleop implements OpModeInterface
{
   private RobotContainer robot;
   private UI UI = new UI();

   Joystick Joystick = new Joystick(0); // Joystick
   Joystick Buttons1 = new Joystick(1); // Button Board
   Joystick Buttons2 = new Joystick(2); // Button Board
   double x, y, hx, hy;
   double stageAngle;

   public Teleop()
   {
      robot = RobotContainer.getInstance();
   }
   public void Init()
   {
      // zero gyro? maybe not... only in Robot.init?
      robot.setManual( false );
   }
   public void Periodic()
   {
      x  = Joystick.getRawAxis(5);
      x  = robot.landmarks.joystickInversion * Math.pow( x, 3.0 ) * robot.driveBase.getMaximumVelocity()/4.0;
      y  = Joystick.getRawAxis(4);
      y  = robot.landmarks.joystickInversion * Math.pow( y, 3.0 ) * robot.driveBase.getMaximumVelocity()/4.0;
      hx = -Joystick.getRawAxis(0);
      hx = Math.pow( hx, 3.0 ) * robot.driveBase.getMaximumVelocity();
      hy = -Joystick.getRawAxis(1);
      hy = Math.pow( hy, 3.0 ) * robot.driveBase.getMaximumVelocity();
      double turboPower = 1.0-Joystick.getRawAxis(2)+0.1;
      double slowPower  = 3.0;
  
      if ( UI.speakerTarget() || UI.speakerShoot() )
      {
         robot.driveBase.driveFacing( x, y, robot.landmarks.speaker );
      }
      else if ( Joystick.getPOV() == 270 )
      {
         robot.driveBase.driveHeading( x, y, Math.PI/2.0 );
      }
      else if ( Joystick.getPOV() == 0 )
      {
         robot.driveBase.driveHeading( x, y, 0.0 );
      }
      else if ( Joystick.getPOV() == 90 )
      {
         robot.driveBase.driveHeading( x, y, -Math.PI/2.0 );
      }
      else if ( Joystick.getPOV() == 180 )
      {
         robot.driveBase.driveFacing( x, y, robot.landmarks.speaker );
      }
      else if( Joystick.getRawButton(7))
      {
         switch(robot.id)
         {
            case 11:
            stageAngle = Math.toRadians(300.0);
            break;

            case 12:
            stageAngle = Math.toRadians(60.0);
            break;

            case 13:
            stageAngle = Math.toRadians(180.0);
            break;

            case 14:
            stageAngle = Math.toRadians(0.0);
            break;

            case 15:
            stageAngle = Math.toRadians(120.0);
            break;

            case 16:
            stageAngle = Math.toRadians(240.0);
            break;

            default:
            stageAngle = 0;
            break;

         }
         robot.driveBase.driveHeading( x, y, stageAngle );
      }
      else if ( UI.lockActive() )
      {
         robot.driveBase.stopDrive();
      }
      else if ( Joystick.getRawAxis(2) > 0.1 )
      {
         robot.driveBase.drive( x/turboPower, y/turboPower, hx*8 );
      }
      else if ( Joystick.getRawButton(5))
      {
         robot.driveBase.drive( x/slowPower, y/slowPower, hx/slowPower);
      }
      else
      {
         robot.driveBase.drive( x, y, hx );//x, hy );
      }
      // if (Buttons1.getRawButtonPressed(2)) // Travel
      // {
      //    robot.shooter.setState(MANIP_STATE.STOW, 0.0);
      // }
      // if (Joystick.getRawAxis(3) <= 0.25)
      // {
      //    robot.shooter.setState(MANIP_STATE.STOW, 0.0);
      // }
      // else if (Joystick.getRawAxis(3) > 0.25) // Intake
      // {
      //    robot.shooter.setState( MANIP_STATE.INTAKE, 0.0 );
      // }
      // if (Buttons1.getRawButtonPressed(3)) // Shoot
      // {
      //    robot.shooter.setState( MANIP_STATE.SPEAKER_TARGET, robot.target_distance );
      // }
      // else if (Joystick.getRawButtonPressed(1))
      // {
      //    robot.shooter.setState( MANIP_STATE.SPEAKER_SHOOT, robot.target_distance );
      // }
      // else if (!Buttons1.getRawButton(4)) // Limelight Off
      // {
      //    if (Buttons1.getRawButton(5)) // Rotate Up
      //    {
      //       robot.shooter.manualAngle(-0.45);
      //    }
      //    else if (Buttons1.getRawButton(6)) // Rotate Down
      //    {
      //       robot.shooter.manualAngle(1.2);
      //    }
      //    else
      //    {
      //       robot.shooter.manualAngle(0.0);
      //    }
      // }
      // else if (Buttons2.getRawButtonPressed(4)) // Amp
      // {
      //    robot.shooter.setState( MANIP_STATE.AMPLIFIER_TARGET, 0.0 );
      // }
      // if ( Buttons1.getRawButton(4)) // Limelight On
      // {
      //    robot.setManual( false );
      // }
      // else
      // {
      //    robot.setManual( true );
      // }
      // if (Buttons1.getRawButton(11)) // Left Climb Up
      // {
      //    robot.climber.leftExtend();
      // }
      // else if (Buttons1.getRawButton(7)) // Left Climb Down
      // {
      //    robot.climber.leftRetract();
      // }
      // else
      // {
      //    robot.climber.leftStop();
      // }
      // if (Buttons2.getRawButton(6)) // Right Climb Up
      // {
      //    robot.climber.rightExtend();
      // }
      // else if (Buttons2.getRawButton(5)) // Right Climb Down
      // {
      //    robot.climber.rightRetract();
      // }
      // else
      // {
      //    robot.climber.rightStop();
      // }

      if( UI.intakeActive() ) // Intake
      {
         robot.shooter.setState( MANIP_STATE.INTAKE, 0.0 );
      }
      else if( UI.speakerShoot() ) // Speaker Shoot
      {
         robot.shooter.setState( MANIP_STATE.SPEAKER_SHOOT, robot.target_distance );
      }
      else if( UI.speakerTarget() ) // Speaker Target
      {
         robot.shooter.setState( MANIP_STATE.SPEAKER_TARGET, robot.target_distance );
      }
      else if ( UI.ampTarget() ) // Amp
      {
         robot.shooter.setState( MANIP_STATE.AMPLIFIER_TARGET, 0.0 );
      }
      else if ( UI.ampShoot() )
      {
         robot.shooter.setState( MANIP_STATE.AMPLIFIER_SHOOT, 0.0 );
      }
      else if( UI.stowActive() ) // Stow
      {
         robot.shooter.setState( MANIP_STATE.STOW, 0.0);
      }
      else
      {
         robot.shooter.setState( MANIP_STATE.STOW,  0.0 );
      }
      
      // Left Climber
      if( UI.leftClimbExtend() ) // Left Climb Extend
      {
         robot.climber.leftExtend();
      }
      else if( UI.leftClimbRetract() ) // Left Climb Retract
      {
         robot.climber.leftRetract();
      }
      else
      {
         robot.climber.leftStop();
      }


      // Right Climber
      if( UI.rightClimbExtend() ) // Right Climb Extend
      {
         robot.climber.rightExtend();
      }
      else if( UI.rightClimbRetract() ) // Right Climb Retract
      {
         robot.climber.rightRetract();
      }
      else
      {
         robot.climber.rightStop();
      }


      // Manual Angle Control
      if( UI.manualRotUp() ) // Rotation Up
      {
         robot.shooter.manualAngle(-0.45);
      }
      else if( UI.manualRotDown() ) // Rotation Down
      {
         robot.shooter.manualAngle(1.2);
      }
      else
      {
         robot.shooter.manualAngle(0.0);
      }

      // Limelight
      if( UI.limelightActive() ) // Limelight On
      {
         robot.setManual( false );
      }
      else // Limelight Off
      {
         robot.setManual( true );
         if( UI.manualRotUp() ) // Rotation Up
         {
            robot.shooter.manualAngle(-0.45);
         }
         else if( UI.manualRotDown() ) // Rotation Down
         {
            robot.shooter.manualAngle(1.2);
         }
         else
         {
            robot.shooter.manualAngle(0.0);
         }
      }
      


      // else
      // {
      //    robot.shooter.setState( MANIP_STATE.STOW, 0.0 );
      // }
      // if (Joystick.getRawButton(3))
      // {
      //    robot.shooter.shooterSetVelocity( 0.50 );
      // }
      // else if (Joystick.getRawButton(1))
      // {
      //    robot.shooter.shooterSetVelocity(0.75);
      // }
      // else
      // {
      //    robot.shooter.shooterSetVelocity( 0.0 );
      // }
   }
   public void testPeriodic()
   {
   }
}
