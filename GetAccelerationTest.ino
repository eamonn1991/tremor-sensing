#include <Time.h>
#include <TimeLib.h>
  time_t t1;
  time_t t2;
  int duration;
  int acc;
  int tremoring;
  String timenow1;
  String timenow2;

  
void setup() {
  Serial.begin();  
  setTime(10,17,0,5,6,2017); 
  Bean.setAccelerationRange(4); 
  tremoring = 0;
  duration = 0;
  acc = 0;
  
}




void loop() {


  
  AccelerationReading acceleration = Bean.getAcceleration();
//  Serial.println(String((abs(acceleration.xAxis) + abs(acceleration.yAxis) + abs(acceleration.zAxis) ) / 3));

   if ((abs(acceleration.xAxis) + abs(acceleration.yAxis) + abs(acceleration.zAxis) ) / 3 > 350) {
//    Serial.println("tremoring");
    if(tremoring == 0){
    t1 = now();
    acc = (abs(acceleration.xAxis) + abs(acceleration.yAxis) + abs(acceleration.zAxis) ) / 3;
    timenow1 =  String(month(t1)) + "/" + String(day(t1)) + "/" +String(year(t1)) + "  " + String(hour(t1)) + ":" + String(minute(t1)) + ":" + String(second(t1));
//    Serial.println("1: " + timenow1);

//    Serial.println("tremor detected");
    tremoring = 1;
      }

    }
//    while(abs(acceleration.xAxis) > 450 || abs(acceleration.yAxis) > 450 || abs(acceleration.zAxis) > 450) {
//      acceleration = Bean.getAcceleration();
//      Serial.println(String(abs(acceleration.xAxis)) + String(abs(acceleration.yAxis)) + String(abs(acceleration.zAxis)));
//      }
      
      else{
//        Serial.println("tremor stopped" + String(tremoring));

        if (tremoring == 1){
          t2 = now();
            
//          timenow1 =  " Month: " + String(month(t1)) + " Day: " + String(day(t1)) + " Year: " +String(year(t1)) + " Hour: " + String(hour(t1)) + " Minute: " + String(minute(t1)) + " Second:" + String(second(t1));
//          timenow2 = " Month: " + String(month(t2)) + " Day: " + String(day(t2)) + " Year: " +  String(year(t2)) + " Hour: " + String(hour(t2)) + " Minute: " + String(minute(t2)) + " Second:" + String(second(t2));
    
          duration = 60 * (minute(t2) - minute(t1) - 1) + second(t2) + (60 - second(t1));
  //        Serial.println("2: " + timenow2);
          tremoring = 0; 
          String result = "\n Time: " + String(timenow1) + "\n Duration: " + String(duration) + "\n Acceleration: " + String(acc);
          Serial.println(result); 
          
          duration = 0;
          acc = 0;      
          }
      }      
      

    }
 
//  Bean.sleep(1000);




