/*
TMRh20 2014
 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 version 2 as published by the Free Software Foundation.
 */

/** General Data Transfer Rate Test
 * This example demonstrates basic data transfer functionality with the 
 updated library. This example will display the transfer rates acheived using
 the slower form of high-speed transfer using blocking-writes.
 */


#include <SPI.h>
#include "RF24.h"

/*************  USER Configuration *****************************/
                                          // Hardware configuration
RF24 radio(9,10);                        // Set up nRF24L01 radio on SPI bus plus pins 7 & 8

/***************************************************************/

const uint64_t pipes[2] = { 0xABCDABCD71LL, 0x544d52687CLL };   // Radio pipe addresses for the 2 nodes to communicate.

byte data[32];                           //Data buffer for testing data transfer speeds

unsigned long counter, rxTimer;          //Counter and timer for keeping track transfer info
unsigned long startTime, stopTime;  
bool TX=1,RX=0,role=0;

void setup(void) {

  Serial.begin(115200);

  radio.begin();                           // Setup and configure rf radio
  radio.printDetails();                   // Dump the configuration of the rf unit for debugging
  
 
}

void loop(void){

   Serial.println(role);

   role = "TX";

   Serial.println(role);

  if(role == TX){
    
    delay(2000);
    
    Serial.println(F("Initiating Basic Data Transfer"));
    
    
    unsigned long cycles = 10000; //Change this to a higher or lower number. 

    Serial.println(cycles);
    
    startTime = millis();
    unsigned long pauseTime = millis();

    Serial.println("83");
            
    for(int i=0; i<cycles; i++){        //Loop through a number of cycles
      data[0] = i;                      //Change the first byte of the payload for identification
      
      if(!radio.writeFast(&data,32)){   //Write to the FIFO buffers        
         Serial.println("89");
        counter++;                      //Keep count of failed payloads
      }

     
    }

   
  }
}
