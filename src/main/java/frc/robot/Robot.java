package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.util.PixelFormat;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.AutonModes.Auton1NearCenter;
import frc.robot.AutonModes.Auton3FarAmp;
import frc.robot.AutonModes.Auton3NearAmp;
import frc.robot.AutonModes.Auton4FarSource;
import frc.robot.AutonModes.Auton4NearAmp;

public class Robot extends TimedRobot
{
   private OpModeInterface teleop;
   private OpModeInterface auton;
   private OpModeInterface test;
   private RobotContainer  robot;

   private final SendableChooser<OpModeInterface> m_chooser = new SendableChooser<>();
   private final SendableChooser<Alliance> m_alliance = new SendableChooser<>();
   private UsbCamera camera;

   @Override
   public void robotInit() 
   {
      robot = RobotContainer.getInstance();
      m_chooser.setDefaultOption("Auton1NearCenter", new Auton1NearCenter());
      m_chooser.addOption("Auton4NearAmp", new Auton4NearAmp());
      m_chooser.addOption("Auton3FarAmp", new Auton3FarAmp());
      m_chooser.addOption("Auton4FarSource", new Auton4FarSource());
      m_chooser.addOption("Auton3NearAmp", new Auton3NearAmp());
      SmartDashboard.putData("Auto choices", m_chooser);
      m_alliance.setDefaultOption("Blue", Alliance.Blue);
      m_alliance.addOption("Red", Alliance.Red);
      SmartDashboard.putData("Alliance", m_alliance);

      camera = CameraServer.startAutomaticCapture();
      camera.setVideoMode( PixelFormat.kMJPEG, 240, 180, 8 );
   }

   @Override
   public void robotPeriodic()
   {
      robot.periodic(); 
      SmartDashboard.putData("Auto choices", m_chooser);
   }

   @Override
   public void autonomousInit()
   {
      robot.opmodeInit( m_alliance.getSelected());
      auton = m_chooser.getSelected();
      //System.out.println("Auto selected: " + auton.toString());

      auton.Init();
      //_LED.Init();
   }

   @Override
   public void autonomousPeriodic()
   {
      auton.Periodic();
   }

   @Override
   public void teleopInit()
   {
      robot.opmodeInit( m_alliance.getSelected() );
      teleop = new Teleop();
      teleop.Init();
      robot.leds.init();
   }

   @Override
   public void teleopPeriodic()
   {
      teleop.Periodic();
      robot.leds.periodic();
   }

   @Override
   public void disabledInit()
   {
      robot.disable();
      robot.leds.disable();
   } 

   @Override
   public void disabledPeriodic()
   {
      robot.disable();
   } 

   public void testInit()
  {
    robot.opmodeInit( m_alliance.getSelected() );
    test = new TestPose2d();
    test.Init();
    robot.leds.init();
  }

  @Override
  public void testPeriodic()
  {
    test.Periodic();
    robot.leds.periodic();
  }
}