// *********************************************************** TRANSMISSOR **
#include <SPI.h>
#include <nRF24L01.h>
#include <RF24.h>
 
int comunica[1];

RF24 radio(D3,D8);

const uint64_t pipe = 0x88E8F0F0E1LL;

void setup(void)
{ Serial.begin(115200);
 // radio.begin();
  radio.setPALevel(RF24_PA_LOW);
  radio.openWritingPipe(pipe);
  pinMode(3, OUTPUT);  
  comunica[0]=0;
  delay(100);
  radio.printDetails();  
  }


void loop(void){
  
    comunica[0]=123;
    radio.write( comunica, sizeof(comunica) );
    digitalWrite(3, HIGH);

ESP.restart();
  delay(500);
}
