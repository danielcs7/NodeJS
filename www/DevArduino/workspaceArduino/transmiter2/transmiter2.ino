
/** Arduino Wireless Communication Tutorial
*      Transmitter Code
*                
* by Smart Technology,  <a href="https://makesmarttech.blogspot.com/" rel="nofollow"> https://makesmarttech.blogspot.com/
</a>
* 
* Library: TMRh20/RF24,<a href="https://tmrh20.github.io/RF24" rel="nofollow">https://tmrh20.github.io/RF24</a>

*/
#include <SPI.h> 
#include <nRF24L01.h>
#include <RF24.h>
#define button 7
RF24 radio(4, 15); // CE, CSN
const byte address[6] = "00001";
boolean buttonState = 0;
char text[] = "Hello Sanki I'm Here !!";
void setup() {
  pinMode(button, INPUT);
  radio.begin();
  radio.openWritingPipe(address);
  radio.setPALevel(RF24_PA_MAX);
  radio.stopListening();
}
void loop() {
delay(5);
    radio.stopListening();
    radio.write(&text, sizeof(text));
  }
