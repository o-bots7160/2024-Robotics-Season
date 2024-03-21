package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class AllianceLandmarks
{
    private double        blueJoystickInversion = -1.0;
    private Translation2d blueSpeaker   = new Translation2d( 0.55,  5.55 );
    private Pose2d blueNearLeft         = new Pose2d( 2.5, 7.0,  new Rotation2d(0.0) );
    private Pose2d blueNearCenter       = new Pose2d( 2.5, 5.5,  new Rotation2d(0.0) );
    private Pose2d blueNearRight        = new Pose2d( 2.5, 4.25, new Rotation2d(0.0) );

    private Pose2d blueFarLeft          = new Pose2d( 7.84924, 7.6028, new Rotation2d(0.0) );
    private Pose2d blueFarLeftCenter    = new Pose2d( 7.84924, 5.9264, new Rotation2d(0.0) );
    private Pose2d blueFarCenter        = new Pose2d( 7.84924, 4.25,   new Rotation2d(0.0) );
    private Pose2d blueFarRightCenter   = new Pose2d( 7.84924, 2.5736, new Rotation2d(0.0) );
    private Pose2d blueFarRight         = new Pose2d( 7.84924, 0.8972, new Rotation2d(0.0) );

    private Pose2d blueSourceShoot      = new Pose2d( 2.5, 3.5, new Rotation2d(0.0) );
    private Pose2d blueBackPose         = new Pose2d( 3.5, 6.0, new Rotation2d(0.0) );

    private double        redJoystickInversion = 1.0;
    private Translation2d redSpeaker    = new Translation2d( 16.529342, 5.55 );
    private Pose2d redNearLeft          = new Pose2d( 14.579342, 7.0, new Rotation2d( Math.toRadians( 180.0 ) ) );
    private Pose2d redNearCenter        = new Pose2d( 14.579342, 5.5,  new Rotation2d( Math.toRadians( 180.0 ) ) );
    private Pose2d redNearRight         = new Pose2d( 14.579342, 4.25,  new Rotation2d( Math.toRadians( 180.0 ) ) );

    private Pose2d redFarLeft           = new Pose2d( 7.84924, 7.6028, new Rotation2d( Math.toRadians( 180.0 ) ) );
    private Pose2d redFarLeftCenter     = new Pose2d( 7.84924, 5.9264, new Rotation2d( Math.toRadians( 180.0 ) ) );
    private Pose2d redFarCenter         = new Pose2d( 7.84924, 4.25,   new Rotation2d( Math.toRadians( 180.0 ) ) );
    private Pose2d redFarRightCenter    = new Pose2d( 7.84924, 2.5736, new Rotation2d( Math.toRadians( 180.0 ) ) );
    private Pose2d redFarRight          = new Pose2d( 7.84924, 0.8972, new Rotation2d( Math.toRadians( 180.0 ) ) );
    
    private Pose2d redSourceShoot      = new Pose2d( 14.579342, 3.5, new Rotation2d(0.0) );
    private Pose2d redBackPose         = new Pose2d( 13.579342, 6.0, new Rotation2d(0.0) );

    public double        joystickInversion;
    public Translation2d speaker        = blueSpeaker;
    public Pose2d        nearLeft       = blueNearLeft;   
    public Pose2d        nearRight      = blueNearRight;
    public Pose2d        nearCenter     = blueNearCenter;
    public Pose2d        farLeft        = blueFarLeft;
    public Pose2d        farLeftCenter  = blueFarLeftCenter;
    public Pose2d        farCenter      = blueFarCenter;
    public Pose2d        farRightCenter = blueFarRightCenter;
    public Pose2d        farRight       = blueFarRight;
    public Pose2d        sourceShoot    = blueSourceShoot;
    public Pose2d        backPose       = blueBackPose;

    public void newAlliance( Alliance alliance )
    {
        if ( alliance == Alliance.Blue )
        {
            joystickInversion = blueJoystickInversion;
            speaker           = blueSpeaker;
            nearLeft          = blueNearLeft;
            nearRight         = blueNearRight;
            nearCenter        = blueNearCenter;
            farLeft           = blueFarLeft;
            farLeftCenter     = blueFarLeftCenter;
            farCenter         = blueFarCenter;
            farRightCenter    = blueFarRightCenter;
            farRight          = blueFarRight;
            sourceShoot       = blueSourceShoot;
            backPose          = blueBackPose;
        }
        else
        {
            joystickInversion = redJoystickInversion;
            speaker           = redSpeaker;
            nearLeft          = redNearLeft;
            nearRight         = redNearRight;
            nearCenter        = redNearCenter;
            farLeft           = redFarLeft;
            farLeftCenter     = redFarLeftCenter;
            farCenter         = redFarCenter;
            farRightCenter    = redFarRightCenter;
            farRight          = redFarRight;
            sourceShoot       = redSourceShoot;
            backPose          = redBackPose;
        }
        System.out.println( alliance );
    }
}
