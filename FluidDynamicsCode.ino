int SensorOne = A0;
int SensorTwo = A1;

double out1;
double out2;

double val1;
double val2;

void setup() {
  Serial.begin(9600);
  while(!Serial){
    ;
  }
 // delay(1000);
}

void loop() {
  
  while(millis() <= 60000){
    out1 = analogRead(SensorOne);
    out2 = analogRead(SensorTwo);
  
    val1 = out1*(5.0/1023.0);
    val2 = out2*(5.0/1023.0);
    //delay(1000);
    Serial.print(millis());
    Serial.print(",");
    Serial.print(val1);
    Serial.print(",");
    Serial.print(val2);
    Serial.println("\n");
  }

}
