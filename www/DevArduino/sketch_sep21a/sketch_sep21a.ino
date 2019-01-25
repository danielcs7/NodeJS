/*              êîä ¹1 äëÿ ïåðåäàò÷èêà transmitter
 ****************************
 óðîê îò Äìèòðèÿ Îñèïîâà. http://www.youtube.com/user/d36073?feature=watch
 v.01 NRF24L01 if Arduino1 button press-Arduino2 LED on
 Version 0.1 2013/07/14
 
 ïîäðîáíóþ âèäåî èíñòðóêöèþ âûëîæó çäåñü
https://www.youtube.com/watch?v=aJVSrGwZs2s
 
 êîä ¹2 äëÿ ïðè¸ìíèêà 
v.01 receiver NRF24L01 if Arduino ¹1 button press-Arduino ¹2 LED on
http://yadi.sk/d/ZDAQhQFB6pHM8



 êîä ¹1 äëÿ ïåðåäàò÷èêà 
 
 
 
 íà Arduino ¹1 íàæèìàåì êíîïêó, ÏÎÊÀ óäåðæèâàåì êíîïêó íàæàòîé, 
 íà Arduino ¹2 âÊëþ÷àåòñÿ (ãîðèò) ñâåòîäèîä, 
 
 NRF24L01+ 2.4GHz Antenna Wireless Transceiver Module For Microcontr
 
 Arduino1 NRF24L01 Serial send. - Arduino2 led. LOW HIGH
 https://www.youtube.com/watch?v=aHgxXXRwtOE&noredirect=1
 
 Arduino Ðàäèîìîäóëü nRF24L01 -2.4GHz RF24 Libraries. test
 https://www.youtube.com/watch?v=B6LHfwisgUQ
 
 http://forum.arduino.cc/index.php?topic=138663.0
 */

// ýòî ñêà÷àííàÿ áèáëèîòåêà
//https://github.com/maniacbug/RF24
//https://github.com/maniacbug/RF24/archive/master.zip
//http://yadi.sk/d/ZvMq19fB6lgPs

#include <SPI.h>
//#include "nRF24L01.h"
#include "RF24.h"


int msg[1];

// Set up nRF24L01 radio on SPI bus plus pins 9 & 10 
//Êîíòàêòû îò ðàäèîìîäóëÿ NRF24L01 ïîäêëþ÷àåì ê ïèíàìíàì -> Arduino

//SCK  -> 13
//MISO -> 12
//MOSI -> 11
//CSN  -> 10
//CE   -> 9

RF24 radio(D3,D8);


const uint64_t pipe = 0xE8E8F0F0E1LL; // àäðåñ êàíàëà  ïåðåäà÷è

//êíîïêè ïîäêëþ÷åíû ê ýòèì ïèíàì
int buttonPin1 = 2;
int buttonPin2 = 3;

void setup(void){ 
  radio.begin();
  radio.openWritingPipe(pipe); // Îòêðûâàåì êàíàë ïåðåäà÷è
} 
void loop(void){
  //ïîêà êíîïêà (buttonPin1)íàæàòà îòïðàâëÿåì ïàêåò (111)â Arduino ¹2
  if (digitalRead(buttonPin1) == HIGH){ 
    msg[0] = 111; 
    radio.write(msg, 1);
  }
  if (digitalRead(buttonPin2) == HIGH){ 
    msg[0] = 112; 
    radio.write(msg, 1);
  }
}
