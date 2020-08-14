#include <SoftwareSerial.h>
const byte rxPin = A1;                    // Wire this to Tx Pin of ESP8266
const byte txPin = A0;                    // Wire this to Rx Pin of ESP8266
SoftwareSerial ESP8266 (rxPin, txPin);    

///////////////////////////////////////////////////////////
///////////   Variables for routine operations  ///////////
///////////////////////////////////////////////////////////
int LDmask = 248;         // Mask for lower bits -> in Binary 11111000 
int HDmask = 3;           // Mask for higher bits -> in Binary 00000011
int scaleStart = 0;       // Flag to indicate the scale has started to display a weight
int trigStatus = 0;       // Temp val to store trigger condition
int digitFind = 0;        // Temp var to get which digit is on
int digitTemp = 0;        // Temp val to store the current combined port vals for 7-seg pins
int weightFinished = 0;   // Flag to indicate scale done showing the weight value
String finalWeight = "";
int weightLength = 0;     // Var to store the length of the weight result for network transfer 
int doOutput = 0;         // Flag to indicate when a weight value can be safely output
int newRead = 1;          // Flag to restrict intial debounce step to first on condition only
int cycleStarted = 0;     // Flag to track when a debounce cycle has begun
unsigned long tempMil = 0;
unsigned long tempMil2 = 0;

////////////////////////////////////////////////////////////
//////  Variables to manipulate the scale port values //////
////////////////////////////////////////////////////////////
// Variables to store the port read temporarily for further manipulation
int dPort = 0;              // Digital pins 0-7
int bPort = 0;              // Digital pins 8-13
int digitMask = 0;          // Temp var to store a port val while manipulating the contents

// Store if the given digit ever showed a weight value
int Dig1_count = 0;
int Dig2_count = 0;
int Dig3_count = 0;
int scanWeight_count = 0;   // counter to restrict number of times the get weight loop runs

// Store the temp 7-seg value. Is combination of the pin inputs from both ports
int Dig1_portVal = 0;
int Dig2_portVal = 0;
int Dig3_portVal = 0;


/////////////////////////////////////////////////////////////
///////////////    Trigger Variables    /////////////////////
/////////////////////////////////////////////////////////////
const int trig = 2;                 // Pin to use for trigger signal from scale OpAmp
int trigState = 0;                  // variables to handle debounce from
int lastTrigState = 0;              // noisy falling edge of triggers
unsigned long deBounceDelay = 750;  // Delay amount to ensure reading a settled trigger signal
unsigned long lastDeBounceTime = 0; // Variable to store how long ago the last debounce was detected
int personMoved = 1;                // variable to track if reset is needed due to movement etc.

/////////////////////////////////////////////////////////////
////////////////    Pin input layout    /////////////////////
/////////////////////////////////////////////////////////////
/*  
 *   Cathode side of the LED digit display is read from the main scale PCB
 *   Digit 1 is Arduino digital pin 10
 *   Digit 2 is Arduino digital pin 11
 *   Digit 3 is Arduino digital pin 12
 *   
 *   Anode side of the digit display
 *   Segment a = LED pin 13 /Arduino digital pin 3
 *   Segment b = LED pin 1  /Arduino digital pin 4
 *   Segment c = LED pin 5  /Arduino digital pin 5
 *   Segment d = LED pin 8  /Arduino digital pin 6
 *   Segment e = LED pin 10 /Arduino digital pin 7
 *   Segment f = LED pin 2  /Arduino digital pin 8
 *   Segment g = LED pin 3  /Arduino digital pin 9
*/

// Function prototypes
void scaleWAKE(void);
void scaleON(void);
void getWeight(void);
void outputWeight(void);
void scaleOFF(void);
void reInit(void);
String seg_to_dec(int segVal);



void setup() {
   Serial.begin(9600);
   delay(10000);            // Give time to connect the ESP when seperated from the circuit for testing etc.
   ESP8266.begin(9600);     // Change this to the baudrate used by ESP8266
 
}// END setup


void loop() {
  // Check if the trigger signal has gone high for first time this cycle and perform debouncing
  if(newRead == 1)
    scaleON();
  
  // Begin getting weight data after trigger goes high to low and stablizes.
  if(scaleStart == 1) {
    getWeight();
  }

  // Check to see if scale reset due to movement of person etc.
  if(weightFinished == 1 && personMoved == 1) {
    checkResult();
  }
 
  // Ensure scale has turned off before outputting and running reinitilization
  if(weightFinished == 1 && personMoved == 0) {
    scaleOFF();
  }

  // Output weight to device and reset all state and read variables
  if(doOutput == 1) {
    outputWeight(); 
    reInit(); 
    Serial.println();
  }

  lastTrigState = trigState;          // Store state of last trigger value for comparison
}//END loop


//*****************************************************************************
// Debounce routine when scale trigger goes high. Needed for noisy falling edge
//*****************************************************************************
void scaleON(void) { 
  trigState = digitalRead(trig);              // Read the current trigger status

  if(trigState == 1 && cycleStarted == 0) {   // If the trigger went high for the first time this cycle
    cycleStarted = 1;                         // set the flag to begin debouncing
  }
  if(cycleStarted == 1) {                     
    delay(42);                                // delay to prevent Arduino from scanning too fast for scale updates
    if(trigState != lastTrigState)            // If the trigger state has changed
      lastDeBounceTime = millis();            // get the time it occured
	  
    // If the time is large enough then the trigger has settled and the reading is valid
    if(((millis() - lastDeBounceTime) > deBounceDelay) && scaleStart == 0 && trigState == 0){
      scaleStart = 1;                         // Can begin reading the weight value
      newRead = 0;
    }
  }
  return;
}// END scaleON


//******************************************************
// Get weight value when the trigger goes high to low. 
// Means the weight is established
//******************************************************
void getWeight(void){
    dPort = PIND;                       // Get temp read of segments a-e
    bPort = PINB;                       // Get temp read of seg-f, seg-g, and digit Common Cathode 
    digitFind = bPort | -29;            // Determine which digit is currently on
    digitTemp = bPort & HDmask;         // Mask out unneeded bits. Leaves segments f and g
    digitTemp = digitTemp << 5;         // Shift bits to the left to make room for other segments
    digitMask = dPort & LDmask;         // Mask out uneeded bits. Leaves segments a - e
    digitMask = digitMask >> 3;         // Shift bits to the right to prepare to combine segment info
    digitTemp = digitTemp | digitMask;  // Combine the segment info in one variable for total BCD value
    
    if(digitFind == -5 && Dig1_count++ < 15)     // When digit 1 is detected get the value and update the count
    {
      delay(1);
      Dig1_portVal = digitTemp;
    }
    if(digitFind == -9 && Dig2_count++ < 10)     // Same for digit 2
    { 
      delay(1); 
      Dig2_portVal = digitTemp;
    }
    if(digitFind == -17 && Dig3_count++ < 5)     // and digit 3
    {
      delay(1); 
      Dig3_portVal = digitTemp;
    }
    trigState = digitalRead(trig);
    if(trigState == 1) {         
      delay(3);
      trigState = digitalRead(trig);
      if(trigState == 1) {
        scaleStart = 0;                   // Update flag to indicate no longer checking for weight
        weightFinished = 1;               // Update flag to indicate weight was obtained
        lastDeBounceTime = 0;             // Clear stored debounce time to prepare for OFF stage
        cycleStarted = 0;                 // and reset flag that detects next high cycle of scale trigger
      }
    }

    return;
}// END getWeight


//*****************************************************************************************
// Verifies the found weight value was valid. Needed for user movement or scale anomolies
// that would cause the scale to restart while determining the weight
//*****************************************************************************************
void checkResult(){
  // If the ones place is zero or empty check the other two places
  if((seg_to_dec(Dig1_portVal) == "")) {
    // If both the tens and hundreds place are 0 or empty then the scale reset or had an error while calculating
    if(((seg_to_dec(Dig2_portVal == "0")) || (seg_to_dec(Dig2_portVal == ""))) && ((seg_to_dec(Dig3_portVal == 63)) || (seg_to_dec(Dig3_portVal == ""))))
    {
      reInit();                     // Start over from the beginning if the scale reset
    }else
      personMoved = 0;              // else set the flag that the value was valid
  }else
    personMoved = 0;                // set the valid flag if only the ones place had to be checked
  return;
}// END checkResult


//*********************************************
// Once weight has been read output to device
//*********************************************
void outputWeight(void) {
  Serial.print("The determined scale value was: ");
  if(Dig3_count != 0) {
    finalWeight = seg_to_dec(Dig3_portVal);
  }
  if(Dig2_count != 0) {
    finalWeight += seg_to_dec(Dig2_portVal);
  }
  if(Dig1_count != 0) {
    finalWeight += seg_to_dec(Dig1_portVal);
  }
  if(finalWeight != "")
    Serial.println(finalWeight);
  if(finalWeight == "")
    Serial.println("No digits were identified for some reason!");

  // Many of the command arguments below will need to be modified to the specific
  // hardware setup being used. If not using an ESP chip most of this will likely need edited.
  ESP8266.println("AT+CWJAP=\"fitmirror\",\"ESP12345\"");
  delay(6000);
  ESP8266.println("AT+CIPSTART=\"TCP\",\"192.168.0.5\",1888");
  delay(3000);
  weightLength=finalWeight.length();
  ESP8266.print("AT+CIPSEND=");
  ESP8266.println(weightLength+2);
  delay(250);
  ESP8266.println(finalWeight);
  delay(250);
  ESP8266.println("AT+CIPSEND=6");
  delay(250);
  ESP8266.println("quit");
  
  return;
}// END outputWeight


//***********************************************************************************************
// Runs debounce at end of scale cycle prior to output and resetting all variables for next time
//***********************************************************************************************
void scaleOFF(void) {
  delay(42);                                // delay to ensure Arduino doesn't scan the trigger too fast
  trigState = digitalRead(trig);            // Get the current trigger state
  
  if(trigState == 1 && cycleStarted == 0) { // If first time went high this cycle set the flag
    cycleStarted = 1;                       // to begin debounce
  }
  if(cycleStarted == 1) {                   
    if(trigState != lastTrigState)          // If the trigger state has changed
      lastDeBounceTime = millis();          // get the time that it occured
	  
    // If the time passed is large enough then the trigger state is valid
    if(((millis() - lastDeBounceTime) > deBounceDelay) && scaleStart == 0 && trigState == 0)
      doOutput = 1;                         // Set the flag to output to the device
  }        
  return;
}// END scaleOFF


//******************************************************************
// Reset all variables to their original state for next scale read
//******************************************************************
void reInit(void) {
  scaleStart = 0;
  trigStatus = 0;
  digitFind = 0;
  digitTemp = 0;
  weightFinished = 0;
  finalWeight = "";
  doOutput = 0;
  newRead = 1;
  cycleStarted = 0;
  dPort = 0;
  bPort = 0;
  digitMask = 0;
  Dig1_count = 0;
  Dig2_count = 0;
  Dig3_count = 0;
  scanWeight_count = 0;
  Dig1_portVal = 0;
  Dig2_portVal = 0;
  Dig3_portVal = 0;
  lastTrigState = 0; 
  lastDeBounceTime = 0;
  personMoved = 1;
  return;
}// END reInit


//****************************************************************************
// Determine the value for a given digit and output as an element of a string
//****************************************************************************
String seg_to_dec(int segVal) {
  switch (segVal) {
    case 63:
      return "0";
    case 6:
      return "1";
    case 91:
      return "2";
    case 79:
      return "3";
    case 102:
      return "4";
    case 109:
      return "5";
    case 125:
      return "6";
    case 7:
      return "7";
    case 127:
      return "8";
    case 111:
      return "9";
    case 57:
      return "C";
    case 121:
      return "E";
    case 56:
      return "L";
    case 94:
      return "d";
    case 92:
      return "o";
    default:
      // If any unknown value occurs will return EE
    break;
  }
  return "EE";
}// END seg_to_dec

