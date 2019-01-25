#include <Ticker.h>
#include <ESP8266WiFi.h>

Ticker secondTick;

const int redLed = 13; //pino do led
const int greenLed = 2; //esse led acenderá toda vez que o ESP32 reiniciar


void setup() {

    Serial.begin(115200);
   pinMode(redLed , OUTPUT);
    pinMode(greenLed , OUTPUT);  
    
   
    
}

void loop() {

    
    int i;

    for (i=0;i<20;i++){
        digitalWrite(greenLed, HIGH); //acende o led vermelho
        delay(500);//Espera 1 Segundo.
        digitalWrite(greenLed, LOW); //acende o led vermelho
        delay(500);//Espera 1 Segundo.
        Serial.print("count");
        Serial.println(i);

        if(i==6){
          Serial.println(ESP.getVcc());
          Serial.println(ESP.getFreeHeap()); //returns the free heap size.
          Serial.println(ESP.getChipId());// returns the ESP8266 chip ID as a 32-bit integer.
          Serial.println("************************************************************");
          Serial.println("Resetting ESP");
          digitalWrite(greenLed, LOW); //acende o led vermelho
          digitalWrite(redLed, HIGH); //acende o led vermelho
          delay(5000);//Espera 1 Segundo.
          digitalWrite(redLed, LOW); //acende o led vermelho

          //Serial.println("ESP8266 Dormindo.......");
          //ESP.deepSleep(1 * 60000000);//Dorme por 1 Minuto (Deep-Sleep em Micro segundos).
          Serial.println("************************************************************");
          Serial.println("ESP8266 Reniciando.......");
           ESP.restart(); //funcao correta
          
          
        }
    }


    

      
    
    
}
