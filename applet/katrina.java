import processing.core.*; 
import processing.xml.*; 

import penner.easing.*; 
import processing.opengl.*; 
import peasy.*; 

import peasy.org.apache.commons.math.*; 
import peasy.*; 
import peasy.org.apache.commons.math.geometry.*; 

import java.applet.*; 
import java.awt.*; 
import java.awt.image.*; 
import java.awt.event.*; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class katrina extends PApplet {





/* Properties
___________________________________________________ */

PeasyCam cam;
ArrayList points;
boolean paused    = false;
boolean recording = false;
int time          = 0;
String timeStamp  = "";
int pointSpacing = 6;
int duration = 300;

/* Setup
___________________________________________________ */

public void setup()
{
   initSetup();
   
   createPoints();

   setupCamera(); 
}

/* Initial Setup
___________________________________________________ */

public void initSetup()
{
   size(1300, 800, OPENGL);
   frameRate( 24 );
   background(0);
   noStroke();
}

/* Create the agents
___________________________________________________ */

public void createPoints()
{    
   PImage theImage;
   theImage = loadImage("katrina_smaller.png");
   
   PImage particle = loadImage("particle_smaller2.png"); 
   
   int i; 
   points = new ArrayList();

   for(int row = 0; row < theImage.height; row ++ )
   {
     for( int col = 0; col < theImage.width; col ++ )
     {
        i  = row * theImage.width + col;
        
        int xPos = col * pointSpacing;
        xPos += theImage.width  * -(pointSpacing / 2);
        int yPos = row * pointSpacing;
        yPos += theImage.height * -(pointSpacing / 2);
        
        MovingPoint thePoint = new MovingPoint(xPos, yPos, 0, theImage.pixels[i], particle);            
        
        points.add(thePoint);
     }
  }
}

/* Setup PeasyCam
___________________________________________________ */

public void setupCamera()
{
   cam = new PeasyCam( this, 1000 );
   cam.setMinimumDistance( 0 );     
   cam.setMaximumDistance( 6000 );
}

/* Draw
___________________________________________________ */

public void draw()
{
  background(0);
  translate(0, -320, 0);
  rotateX(radians(-90));

  updatePoints();

  if( !paused && time < duration ) 
  {
     time ++;
  }
  
  if( recording ) 
  {
     saveFrame( timeStamp + "/frame-####.png" );
  }
}

/* Update agents
___________________________________________________ */

public void updatePoints()
{
   for( int i = points.size() - 1; i >= 0; i -- )
  {
    MovingPoint a = (MovingPoint) points.get(i);

    if(!paused) 
    {
       a.update(time, duration);
    }

    pushMatrix();

    a.drawPoint();
    
    popMatrix();
  }
}

/* Keypress methods
___________________________________________________ */

public void keyPressed()
{
  if( key == ' ' ) paused = paused ? false : true;
  if( key == 'Q' || key == 'q' ) exit();
  if( key == 'R' || key == 'r' )
  {
    if(recording)
    {
      recording = false;
    }
    else
    {
      timeStamp = timeStamp( false );
      recording = true;
    }
  }
  
  if( key == '`' ) saveFrame( timeStamp( true ) +".png" );
}

/* Timestamp function
___________________________________________________ */

public String timeStamp( boolean verbose )
{
  String timeStamp = "";
  if( !verbose ) timeStamp = year() + nf( month(), 2 ) + nf( day(), 2 ) +"-"+ nf( hour(), 2 ) + nf( minute(), 2 ) + nf( second(), 2 );
  if(  verbose ) timeStamp = year() + nf( month(), 2 ) + nf( day(), 2 ) +"-"+ nf( hour(), 2 ) + nf( minute(), 2 ) + nf( second(), 2 ) +"-"+ nf( round( time ), 5 );
  return timeStamp;
}


class MovingPoint
{
   /* Properties
   _______________________________________________ */ 
   
   int _redColor = 255;
   int _greenColor = 255;
   int _blueColor = 255;
   
   float _xPos = 0;
   float _yPos = 0;
   float _zPos = 0;
   float _zPosEnd = 0;
   float _zPosCur = 0;
   
   float _zRot = 0;
   float _zRotCur = 0;
   
   float _endPosMulti = 1;
   
   PImage _particle;
   
   /* Constructor
   _______________________________________________ */
   
   MovingPoint(float xPos, float yPos, float zPos, int pointColor, PImage particle)
   {
      _xPos = xPos;
      _yPos = yPos;
      _zPos = zPos;
      
      _redColor =     pointColor >> 16 & 0xFF;           
      _greenColor =   pointColor >>  8 & 0xFF;           
      _blueColor =    pointColor       & 0xFF;
      
      _zPosEnd = (_redColor + _greenColor + _blueColor) * _endPosMulti;
      
      _zRot = map(_zPosEnd, 0, 765, 0, 5);
      
      //_zPosEnd = _zPosEnd * 20;
      
      _particle = particle;
   }
   
   /* Update point values
   _______________________________________________ */

   public void update(int time, int duration)
   {
      _zPosCur = Quad.easeInOut(time, _zPos, _zPosEnd, duration);
      
      _zRotCur += _zRot;
   }
   
   /* Locate position and rotation
   _______________________________________________ */
   
   public void drawPoint()
   { 
      rotateZ( radians(_zRotCur));
      translate(_xPos, _yPos, _zPosCur);
      
      stroke(color(_redColor, _greenColor, _blueColor));
      
      point(0, 0);
      //image(_particle, 0, 0); 
   }
}

   static public void main(String args[]) {
      PApplet.main(new String[] { "--present", "--bgcolor=#666666", "--stop-color=#cccccc", "katrina" });
   }
}
