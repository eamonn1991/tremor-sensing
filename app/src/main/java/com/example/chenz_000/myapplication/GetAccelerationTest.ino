#include <Time.h>
#include <TimeLib.h>

bool tremoring = 0;

void setup() {
 
  Serial.begin();  
  setTime(11,17,0,12,5,2017); 
  
  Bean.setAccelerationRange(16);
  Bean.enableMotionEvent(ANY_MOTION_EVENT);

  tremoring = 0;
  
}




void loop() {

 
  AccelerationReading acceleration = Bean.getAcceleration();
  
  String stringToPrint = String();
  String alert = String();
  int duration = 0;
  

  time_t t1 = now();

  String timenow1 =  " Month: " + String(month(t1)) + " Day: " + String(day(t1)) + " Year: " +String(year(t1)) + " Hour: " + String(hour(t1)) + " Minute: " + String(minute(t1)) + " Second:" + String(second(t1));
//  int timenum1 = timenow1.toInt();
//  int timenum2 = timenow2.toInt();
  
  stringToPrint = stringToPrint + "X: " + acceleration.xAxis + "\tY: " + acceleration.yAxis + "\tZ: " + acceleration.zAxis;
//  Serial.println(stringToPrint);
  int acc = 0;
   if (abs(acceleration.xAxis) > 420 || abs(acceleration.yAxis) > 420 || abs(acceleration.zAxis) > 420) {
    tremoring = 1;
    acc = sqrt(abs(acceleration.xAxis) * abs(acceleration.xAxis) + abs(acceleration.yAxis) * abs(acceleration.yAxis) + abs(acceleration.zAxis) * abs(acceleration.zAxis)); }
    
    if (abs(acceleration.xAxis) < 350 && abs(acceleration.yAxis) < 350 && abs(acceleration.zAxis) < 350 && tremoring == 1) {
        time_t t2 = now();
        
      tremoring = 0;
      String timenow2 = " Month: " + String(month(t2)) + " Day: " + String(day(t2)) + " Year: " +  String(year(t2)) + " Hour: " + String(hour(t2)) + " Minute: " + String(minute(t2)) + " Second:" + String(second(t2));

      duration = 60 * (minute(t2) - minute(t1) - 1) + second(t2) + (60 - second(t1));
//      Serial.println(duration);

      String result = "\n Time started: " + String(timenow1) + "\n Time ended: " + String(timenow2) + "\n Duration: " + String(duration) + "\n Acceleration: " + String(acc);
        
      Serial.println(result);
//      Serial.println(" Time ended: " + timenow2);    
//      Serial.println(" Duration: " + duration);
//      Serial.println(" Acceleration: " + acc);
        
        
      

    }
  }
//  Bean.sleep(1000);




