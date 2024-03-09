package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class AllianceLandmarks
{
    private double        blueJoystickInversion = -1.0;
    private Translation2d blueSpeaker   = new Translation2d( 0.5,  5.55 );
    private Pose2d blueNearLeft         = new Pose2d( 2.5, 7.0,  new Rotation2d(0.0) );
    private Pose2d blueNearCenter       = new Pose2d( 2.5, 5.5,  new Rotation2d(0.0) );
    private Pose2d blueNearRight        = new Pose2d( 2.5, 4.25, new Rotation2d(0.0) );

    private Pose2d blueFarLeft          = new Pose2d( 7.84924, 7.6028, new Rotation2d(0.0) );
    private Pose2d blueFarLeftCenter    = new Pose2d( 7.84924, 5.9264, new Rotation2d(0.0) );
    private Pose2d blueFarCenter        = new Pose2d( 7.84924, 4.25,   new Rotation2d(0.0) );
    private Pose2d blueFarRightCenter   = new Pose2d( 7.84924, 2.5736, new Rotation2d(0.0) );
    private Pose2d blueFarRight         = new Pose2d( 7.84924, 0.8972, new Rotation2d(0.0) );

    private double        redJoystickInversion = 1.0;
    private Translation2d redSpeaker    = new Translation2d( 16.579342, 5.5 );
    private Pose2d redNearLeft          = new Pose2d( 14.579342, 4.25, new Rotation2d( Math.toRadians( 180.0 ) ) );
    private Pose2d redNearCenter        = new Pose2d( 14.579342, 5.5,  new Rotation2d( Math.toRadians( 180.0 ) ) );
    private Pose2d redNearRight         = new Pose2d( 14.579342, 7.0,  new Rotation2d( Math.toRadians( 180.0 ) ) );

    private Pose2d redFarLeft           = new Pose2d( 7.84924, 0.8972, new Rotation2d( Math.toRadians( 180.0 ) ) );
    private Pose2d redFarLeftCenter     = new Pose2d( 7.84924, 2.5736, new Rotation2d( Math.toRadians( 180.0 ) ) );
    private Pose2d redFarCenter         = new Pose2d( 7.84924, 4.25,   new Rotation2d( Math.toRadians( 180.0 ) ) );
    private Pose2d redFarRightCenter    = new Pose2d( 7.84924, 5.9264, new Rotation2d( Math.toRadians( 180.0 ) ) );
    private Pose2d redFarRight          = new Pose2d( 7.84924, 7.6028, new Rotation2d( Math.toRadians( 180.0 ) ) );

    public double        joystickInversion;
    public Translation2d speaker = blueSpeaker;

    public void newAlliance( Alliance alliance )
    {
        if ( alliance == Alliance.Blue )
        {
            joystickInversion = blueJoystickInversion;
            speaker = blueSpeaker;
        }
        else
        {
            joystickInversion = redJoystickInversion;
            speaker = redSpeaker;
        }
    }
}
