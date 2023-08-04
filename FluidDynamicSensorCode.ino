/*
 Fluid Dynamics Sensors

 Reads conductivity from sensors, of which the first one is connected to A0 and the second sensor is connected A1.
 Each sensor is connected to 5V and GND through a 1K ressistor

 Two relays connected pin 7 and 9 are controlled by the Arduino to control the pumps on the machine.

 The Arduino will read the data from this sensor and send it back to the computer through the serial port with the millis() value.
 A Java Program will monitor this Serial Port and graph the data as well as save it in a excel file.

 Steps:
  1. Relay for pumps turn on ( run while loop for 2 minutes as of now)
  2. Data collecting 
  3. wait 20 sec ( water running) 
  4. Second relay engaged for 3 seconds 
  5. Second relay disengaged for 20 sec 
    Repeat process 4-5 X 5 (provide 4 sets of data) 1st data will be discarded 
  6.First relay disengaged 
  7. End program 
*/

//connect sensord to A0 and A1
const int sensorOne = A0;
const int sensorTwo = A2;

//variable to store the value read data from sensors
double val = 0;  
double valTwo = 0;
double out;
double outTwo;

//connect relays to digital pin 7 and 9
const int relayOne = 7;  
const int relayTwo = 9;

void setup() {
  //Begin serial at baud rate of 9600 and wait for it to begin
  Serial.begin(9600);
  while(!Serial){;}

  //set both relays as outputs
  pinMode(relayOne, OUTPUT);
  pinMode(relayTwo, OUTPUT);

  //make sure both relays are off at the beggenning of the program
  digitalWrite(relayOne, HIGH);
  digitalWrite(relayTwo, HIGH);
}

void beginReadingFor(int forTimeMillis){
  while(millis() <= forTimeMillis){
    //turn on first relay
    digitalWrite(relayOne, LOW);

    //read analog data from sensors
    val = analogRead(sensorOne);
    valTwo = analogRead(sensorTwo);

    //convert analog data to voltage measurements
    out = val*(5.0/1023.0);
    outTwo = valTwo*(5.0/1023.0);

    //wait 20 sec
    if(millis() > 20000){
      //turn on second relay
      digitalWrite(relayTwo, LOW);
    }

    if(millis() > 23000){
      //turn off second relay
      digitalWrite(relayTwo, HIGH);
    }
    //...?
  }
}

void loop() {
  
  while (millis() <= 60000) { //running for 1 minutes 

    //turn on first relay
    digitalWrite(relayOne, LOW);
    val = analogRead(analogPin); // read the input sensors
    valTwo = analogRead(sensorTwo);

    out = val / (204.6); //blue line
    outTwo = valTwo / (204.6); //orange line
 
        // Turning on second relay 
        
        digitalWrite(relayTwo, HIGH);
        delay(2000);
        digitalWrite(relayTwo, LOW);
        
      
     

    
    //uncomment to send time to java program
    /*Serial.print(millis());
      Serial.print("\t");
      Serial.print(millis());
      Serial.print("\t");
    */

    Serial.print(out);
    Serial.print("\t");
    Serial.println(outTwo);


  }


  digitalWrite(relayOne, HIGH);
 
  }
