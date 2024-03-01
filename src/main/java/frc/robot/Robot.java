package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.AutonModes.Auton1NearCenter;
import frc.robot.AutonModes.Auton3FarAmp;
import frc.robot.AutonModes.Auton4FarSource;
import frc.robot.AutonModes.Auton4NearAmp;

public class Robot extends TimedRobot
{
   private OpModeInterface teleop;
   private OpModeInterface auton;
   private OpModeInterface test;
   private RobotContainer  robot;

   private final SendableChooser<OpModeInterface> m_chooser = new SendableChooser<>();

   @Override
   public void robotInit() 
   {
      robot = RobotContainer.getInstance();
      m_chooser.setDefaultOption("Auton1NearCenter", new Auton1NearCenter());
      m_chooser.addOption("Auton4FarAmp", new Auton4NearAmp());
      m_chooser.addOption("Auton3FarAmp", new Auton3FarAmp());
      m_chooser.addOption("Auton4FarSource", new Auton4FarSource());
      SmartDashboard.putData("Auto choices", m_chooser);
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
      teleop = new Teleop();
      teleop.Init();
   }

   @Override
   public void teleopPeriodic()
   {
      teleop.Periodic();
   }

   @Override
   public void disabledInit()
   {
      robot.disable();
   } 

   @Override
   public void disabledPeriodic()
   {
      robot.disable();
   } 

   public void testInit()
  {
    test = new TestPose2d();
    test.Init();
  }

  @Override
  public void testPeriodic()
  {
    test.Periodic();
  }
}