package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot
{
   private OpModeInterface teleop;
   private OpModeInterface auton;
   private OpModeInterface test;
   private RobotContainer  robot;

   @Override
   public void robotInit() 
   {
      robot = RobotContainer.getInstance();
      // m_chooser.setDefaultOption("Auton1CoOp", new Auton1CoOp());
      // m_chooser.addOption("Auton2LeftCS", new Auton2LeftCS());
      // m_chooser.addOption("Auton2RightCS", new Auton2RightCS());
      // m_chooser.addOption("AutonPose", new AutonPose()); 
      // m_chooser.addOption("Auton1", new Auton1()); 
      // m_chooser.addOption("AutonCone", new AutonCone()); 
      // m_chooser.addOption("Auton3Left", new Auton3Left() );
      // m_chooser.addOption("Auton3Right", new Auton3Right() );
      // m_chooser.addOption("AutonPrep", new AutonPrep() );
      // SmartDashboard.putData("Auto choices", m_chooser);
   }

   @Override
   public void robotPeriodic()
   {
      robot.periodic(); 
      //SmartDashboard.putData("Auto choices", m_chooser);
   }

   @Override
   public void autonomousInit()
   {
      auton = null; //m_chooser.getSelected();
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