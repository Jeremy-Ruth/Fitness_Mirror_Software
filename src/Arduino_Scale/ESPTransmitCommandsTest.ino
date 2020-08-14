#include <SoftwareSerial.h>

// A simple loop routine to test the connection is working as expected
// This routine is only for ESP8266 chips. Most of the commands and configuration
// would need to be modified if another chip is used instead.

const byte rxPin = A0; // Wire this to Tx Pin of ESP8266
const byte txPin = A1; // Wire this to Rx Pin of ESP8266
String testVal = "";
int valLength = 0;
int finished = 0;

// We'll use a software serial interface to connect to ESP8266
SoftwareSerial ESP8266 (rxPin, txPin);

void setup() {
  Serial.begin(9600);
  ESP8266.begin(9600); 					// Change this to the baudrate used by ESP8266
  delay(1000); 							// Let the module self-initialize
  testVal = "Is anyone out there?";
}

// Some arguments in the loop will need to be changed to your specific setup. Especially
// the network name, port and, IP address will likely be system specific
void loop()
{
  if(!finished){
    ESP8266.println("AT+CWJAP=\"fitmirror\",\"ESP12345\"");
    delay(5000);
    ESP8266.println("AT+CIPSTART=\"TCP\",\"192.168.0.5\",1888");
    delay(3000);
    valLength=testVal.length();
    ESP8266.print("AT+CIPSEND=");
    ESP8266.println(valLength+2);
    delay(250);
    ESP8266.println(testVal);
    delay(250);
    ESP8266.println("AT+CIPSEND=6");
    delay(250);
    ESP8266.println("quit");
    delay(3000);
    finished++;
  }
}

