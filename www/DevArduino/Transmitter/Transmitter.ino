/*
 Copyright (C) 2011 J. Coliz <maniacbug@ymail.com>

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 version 2 as published by the Free Software Foundation.
 */

#include <SPI.h>
#include "nRF24L01.h"
#include "RF24.h"


//
// Hardware configuration
//

// Set up nRF24L01 radio on SPI bus plus pins 9 & 10 

RF24 radio(D4,D8);

//
// Topology
//

// Radio pipe addresses for the 2 nodes to communicate.
const uint64_t address = 0xB00B1E5000LL;
char buffer[10];
//
// Role management
//
// Set up role.  This sketch uses the same software for all the nodes
// in this system.  Doing so greatly simplifies testing.  
//

// The various roles supported by this sketch
typedef enum { role_ping_out = 1, role_pong_back } role_e;

// The debug-friendly names of those roles
const char* role_friendly_name[] = { "invalid", "Ping out", "Pong back"};

// The role of the current running sketch
role_e role = role_pong_back;

void setup(void)
{
  //
  // Print preamble
  //

  Serial.begin(115200);
    Serial.println("****** Nadajnik ******");
    pinMode(D4, OUTPUT);//Define o LED Onboard como saida.
    digitalWrite(D4, 1);//Apaga o LED.

   
    // radio.begin();

    // optionally, increase the delay between retries & # of retries
    radio.setRetries(15,15);

    // optionally, reduce the payload size.  seems to
    // improve reliability
    radio.setPayloadSize(8);

    radio.openWritingPipe(address);
    radio.stopListening();
    radio.printDetails();
}

void loop(void)
{
  //
  // Ping out role.  Repeatedly send the current time
  //

  Serial.println("Loop");
  digitalWrite(D4, !digitalRead(D4));

  int numberToSend = 9;
  Serial.println("Sending packet...");
  if (!radio.write("BW", sizeof("BW"))) {
    Serial.println("Sending FAILED.");
  }
  else 
  {
    Serial.println("Packet sent!");
  }

  delay(1000);
}
