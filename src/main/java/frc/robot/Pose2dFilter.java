package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.util.DoubleCircularBuffer;
import edu.wpi.first.wpilibj.Timer;

//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Pose2dFilter
{
   final private int maxSamples = 50;

   class AverageBuffer extends DoubleCircularBuffer
   {
      private int    maxSize;
      private double sum;
      public AverageBuffer( int size )
      {
         super( size );
         maxSize = size;
      }
      @Override
      public void addFirst( double value )
      {
         if ( size() >= maxSize )
         {
            sum -= super.getFirst();
         }
         sum += value;
         super.addFirst( value );
      }
      @Override
      public void addLast( double value )
      {
         if ( size() >= maxSize )
         {
            sum -= super.getLast();
         }
         sum += value;
         super.addFirst( value );
      }
      @Override
      public void clear()
      {
         sum = 0.0;
         super.clear();
      }
      @Override
      public double removeFirst( )
      {
         double temp = 0.0;
         if ( size() >= maxSize )
         {
            temp = super.removeFirst( );
            sum -= temp;;
         }
         return temp;
      }
      @Override
      public double removeLast( )
      {
         double temp = 0.0;
         if ( size() >= maxSize )
         {
            temp = super.removeFirst();
            sum -= temp;
         }
         return temp;
      }
      @Override
      public void resize( int new_size )
      {
         maxSize = new_size;
         super.resize( new_size );
      }
      public double average( double value )
      {
         addLast( value );
         return sum / size();
      }
   }

   private AverageBuffer buffX  = new AverageBuffer( maxSamples );
   private AverageBuffer buffY  = new AverageBuffer( maxSamples );
   private AverageBuffer buffR  = new AverageBuffer( maxSamples );
   private AverageBuffer buffT  = new AverageBuffer( maxSamples );
   private AverageBuffer buffTd = new AverageBuffer( maxSamples - 1 );

   public  Pose2d resultPose;
   public  double resultTime;
   private double avgPeriod; 
   //
   //   Constructor prepare to receive poses collected from vision system
   //
   //
   public Pose2dFilter()
   {
      reset();
   }
   //
   //   Clear all sums and buffers
   //
   //
   public void reset()
   {
      buffX.clear();
      buffY.clear();
      buffR.clear();
      buffT.clear();
      buffTd.clear();
      resultPose = new Pose2d();
      resultTime = 0;
      avgPeriod  = 0;
   }
   //
   //   Add a pose to the averaging history and return true is we have enough
   //   samples for a good average and false otherwise
   //
   //   Also calculated for the callers information is:
   //      diffTime:  average time between readings
   //
   public boolean addData( Pose2d newSample, double timestamp )
   {
      double currentTime = Timer.getFPGATimestamp();
      // Clean out old data
      while ( buffT.size() > 0 && ( currentTime - buffT.getFirst() ) > 3000.0 )
      {
         buffX.removeFirst();
         buffY.removeFirst();
         buffR.removeFirst();
         buffT.removeFirst();
         buffTd.removeFirst();
      }
      resultPose  = new Pose2d( buffX.average( newSample.getX() ),
                                buffY.average( newSample.getY() ),
                                new Rotation2d( buffR.average( newSample.getRotation().getRadians() ) ) );
      avgPeriod   = buffTd.average( timestamp - buffT.getLast() );
      resultTime  = buffT.average(  timestamp );


      return ( buffX.size() < (long)( maxSamples / 2.0 ) && ( avgPeriod > 1000.0 ) );
   }
}