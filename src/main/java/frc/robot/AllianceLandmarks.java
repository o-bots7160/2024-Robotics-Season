package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class AllianceLandmarks
{
    private Translation2d blueSpeaker = new Translation2d( 0.5,  5.55 );
    private Translation2d redSpeaker  = new Translation2d(  8.308975, 1.442593 );

    public Translation2d speaker = blueSpeaker;
    public void newAlliance( Alliance alliance )
    {
        if ( alliance == Alliance.Blue )
        {
            speaker = blueSpeaker;
        }
        else
        {
            speaker = redSpeaker;
        }
    }
}
