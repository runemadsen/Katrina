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
   
   MovingPoint(float xPos, float yPos, float zPos, color pointColor, PImage particle)
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

   void update(int time, int duration)
   {
      _zPosCur = Quad.easeInOut(time, _zPos, _zPosEnd, duration);
      
      _zRotCur += _zRot;
   }
   
   /* Locate position and rotation
   _______________________________________________ */
   
   void drawPoint()
   { 
      rotateZ( radians(_zRotCur));
      translate(_xPos, _yPos, _zPosCur);
      
      stroke(color(_redColor, _greenColor, _blueColor));
      
      point(0, 0);
      //image(_particle, 0, 0); 
   }
}
