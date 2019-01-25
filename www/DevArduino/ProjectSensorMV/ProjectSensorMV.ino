#include <ESP8266WiFi.h>
#include "DHTesp.h"
#include <DS3232RTC.h> //Library Manager




#ifdef ESP32
#pragma message(THIS EXAMPLE IS FOR ESP8266 ONLY!)
#error Select ESP8266 board.
#endif

const char ssid[] = "iab-adm";  //  nome da rede
const char pass[] = "iab124578";     // senha da rede

//const char* ssid     = "iab-adm";
//const char* password = "iab124578";

static float h;
static float t;

DHTesp dht;

const int redLed = 13; //pino do led
const int greenLed = 0; //esse led acenderá toda vez que o ESP32 reiniciar

void setup() {
  
  //INICIA SERIAL
  Serial.begin(115200);
  delay(250);
  String thisBoard= ARDUINO_BOARD;
  Serial.println(thisBoard);

  //INICIA DO SENSOR DE TEMPERATURA E UMIDADE
  //******************************************************
   dht.setup(D4, DHTesp::DHT22);
  //******************************************************

  pinMode(redLed , OUTPUT);
    pinMode(greenLed , OUTPUT);  
  
  //INICIA DS3231 rtc
   setSyncProvider(RTC.get);   // the function to get the time from the RTC
    if(timeStatus() != timeSet)
        Serial.println("Unable to sync with the RTC");
    else
        Serial.println("RTC has set the system time");

  //******************************************************
  
  
  //CONEXAO COM INTERNET
  //******************************************************
    Serial.print("Conectando ... ");
    Serial.println(ssid);
    WiFi.begin(ssid, pass);

    WiFi.begin();
    //VERIFICA SE Á CONEXAO
    while(WiFi.status() != WL_CONNECTED){
      delay(250);
      Serial.print(">");
    }
  //******************************************************

    Serial.println("");
    Serial.println("WiFi connected");
    Serial.println("IP address: ");
    Serial.println(WiFi.localIP());
  //******************************************************
  
}

float sensorTemp(){
 
  float temperature = dht.getTemperature();
  return temperature;

 }

float sensorHumid(){
 
  float humidity = dht.getHumidity();
  return humidity;

 }


void loop() {

int i;

for (i=0;i<240000;i++){

  
        digitalWrite(greenLed, HIGH); //acende o led vermelho
        delay(500);//Espera 1 Segundo.
        digitalWrite(greenLed, LOW); //acende o led vermelho
        delay(500);//Espera 1 Segundo.
        Serial.print("count");
        Serial.println(i);

        if(i==120000){
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
          Serial.print("connecting to ");
         delay(dht.getMinimumSamplingPeriod());
         Serial.println("\n");
          
          h = sensorHumid();
          t = sensorTemp();
        
        
          
        
          // Verifica se o sensor esta respondend
           if (isnan(h) || isnan(t))
          {
            Serial.println("Falha ao ler dados do sensor DHT !!!");
            return;
          }
        
        
         // digital clock display of the time
            Serial.print(hour());
            printDigits(minute());
            printDigits(second());
            Serial.print(' ');
            Serial.print(day());
            Serial.print(' ');
            Serial.print(month());
            Serial.print(' ');
            Serial.print(year());
            Serial.println();
        
          // And use it
          Serial.print("Hour: ");   Serial.println(MyDateAndTime.Hour);
          Serial.print("Minute: "); Serial.println(MyDateAndTime.Minute);
          Serial.print("Second: "); Serial.println(MyDateAndTime.Second);
          Serial.print("Year: ");   Serial.println(MyDateAndTime.Year);
          Serial.print("Month: ");  Serial.println(MyDateAndTime.Month);
          Serial.print("Day: ");    Serial.println(MyDateAndTime.Day);
        
          Serial.print("End Of Program (RESET to run again)");
          while(1);
        
        
        /*
          dataehora = rtc.getDateTime();
         
          Serial.print(dataehora.year);   //Imprimindo o ano
          Serial.print("-");
          Serial.print(dataehora.month);  //Imprimindo o mês
          Serial.print("-");
          Serial.print(dataehora.day);    //Imprimindo o dia
          Serial.print(" ");
          Serial.print(dataehora.hour);   //Imprimindo a hora
          Serial.print(":");
          Serial.print(dataehora.minute); //Imprimindo os minutos
          Serial.print(":");
          Serial.print(dataehora.second); //Imprimindo os segundos  
          Serial.println("");
        
          */
          
        
          Serial.print(t);
          Serial.println("  : Temperatura");
          Serial.print(h);
          Serial.println("  : Umidade");
          Serial.println("****************************************************");
          
          







          
          Serial.println("ESP8266 Reniciando.......");
          ESP.restart(); //funcao correta
        }else{
          Serial.println("ESP8266 Ligado.......");
        }

          
 
}

void printDigits(int digits)
{
    // utility function for digital clock display: prints preceding colon and leading 0
    Serial.print(':');
    if(digits < 10)
        Serial.print('0');
    Serial.print(digits);
}
