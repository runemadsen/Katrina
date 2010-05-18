import penner.easing.*;
import processing.opengl.*;
import peasy.*;

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

void setup()
{
   initSetup();
   
   createPoints();

   setupCamera(); 
}

/* Initial Setup
___________________________________________________ */

void initSetup()
{
   size(1300, 800, OPENGL);
   frameRate( 24 );
   background(0);
   noStroke();
}

/* Create the agents
___________________________________________________ */

void createPoints()
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

void setupCamera()
{
   cam = new PeasyCam( this, 1000 );
   cam.setMinimumDistance( 0 );     
   cam.setMaximumDistance( 6000 );
}

/* Draw
___________________________________________________ */

void draw()
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

void updatePoints()
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

void keyPressed()
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

String timeStamp( boolean verbose )
{
  String timeStamp = "";
  if( !verbose ) timeStamp = year() + nf( month(), 2 ) + nf( day(), 2 ) +"-"+ nf( hour(), 2 ) + nf( minute(), 2 ) + nf( second(), 2 );
  if(  verbose ) timeStamp = year() + nf( month(), 2 ) + nf( day(), 2 ) +"-"+ nf( hour(), 2 ) + nf( minute(), 2 ) + nf( second(), 2 ) +"-"+ nf( round( time ), 5 );
  return timeStamp;
}


