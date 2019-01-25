#include "DHTesp.h"
#include <NTPClient.h>
#include <ESP8266WiFi.h>
#include <WiFiUdp.h>
#include <Wire.h>
#include <RtcDS3231.h>
#include <ESP8266HTTPClient.h>

//******************** DADOS INTERNET **********************
const char *ssid     = "iab-adm";
const char *password = "iab124578";
//**********************************************************

//******************** DEFINI QUAL PLACA IRA USAR *******************
#ifdef ESP32
#pragma message(THIS EXAMPLE IS FOR ESP8266 ONLY!)
#error Select ESP8266 board.
#endif
//********************************************************************


//******************** VARIAVEIS ****************************

    const String x = "'";
    
    const char* dtStmp;
    const char* tmStmp;
    
    static String hr;
    static String dt;
    static String param1;
    static String param2;
    static String param3;
    static String formattedDate;
    static String dayStamp;
    static String timeStamp;
    
    //AQUI DEFINI QUAL ESTUFA IRA FICAR O SENSOR
    const String stufa = "9";
    
    static float h;
    static float t;
    static float humidity;
    static float temperature;

    int16_t utc = -3; //UTC -3:00 Brazil
    uint32_t currentMillis = 0;
    uint32_t previousMillis = 0;
    int hh=0,mm=0;
    

//************************************************************


  //Declara a classe
  DHTesp dht;
  WiFiUDP ntpUDP;
  NTPClient timeClient(ntpUDP, "a.st1.ntp.br", utc*3600, 60000);
  RtcDS3231<TwoWire> Rtc(Wire);


void setup(){
  
      Serial.begin(115200);
     //**********************************************************************
 
      Serial.println();

      WiFi.begin(ssid, password);
 
      while ( WiFi.status() != WL_CONNECTED ) {
        delay ( 500 );
        Serial.print ( "." );
      }

      //************* INICIAR MODULOS *****************
      
      Rtc.Begin();
      timeClient.begin();
      timeClient.update();
      //Esse está correto
      //dht.setup(D5, DHTesp::DHT22); // Connect DHT sensor to GPIO 17   
      dht.setup(D3, DHTesp::DHT22); // Connect DHT sensor to GPIO 17   


      //-------------------AQUI DEFINE A LUZ DE LED PARA VERIFICAR SE ESTA TUDO FUNCIONANDO
      pinMode(D4, OUTPUT);//Define o LED Onboard como saida.
      digitalWrite(D4, 1);//Apaga o LED.
      //------------------------------------------------------------------------------------

      
      //*****************************************************************************************

  
      String thisBoard= ARDUINO_BOARD;
      Serial.println(thisBoard);

   
  }

void setRTC(String dt, String hr){

     dt.replace("-", "/");
 

     //AQUI CONVERTE A VARIAVEL STRING <=> CONST CHAR*
      dtStmp = dt.c_str();
      tmStmp = hr.c_str();

      Serial.println("Configurando RTC");

      Serial.println("Data <=> "+dt);
      Serial.println("Hora <=> "+hr);
  
      //AQUI ESTOU PASSANDO OS VALORES DE DATA E HORA PARA O MODULO RTC
      RtcDateTime compiled = RtcDateTime(dtStmp, tmStmp);

      if (!Rtc.IsDateTimeValid()) 
        {
            Serial.println("RTC Relógio está desatualizado!!!");
            Rtc.SetDateTime(compiled);
            return;
        }
    
        RtcDateTime now = Rtc.GetDateTime();

        //RESET RELOGIO
        Rtc.Enable32kHzPin(false);
        Rtc.SetSquareWavePin(DS3231SquareWavePin_ModeNone);
        Serial.println("Relógio ativado\n");
      
    }


void dtHours(){
      
      timeClient.update();
        
        while(!timeClient.update()) {
          timeClient.forceUpdate();
        }

        Serial.println(timeClient.getFormattedTime());
  
        Serial.println("*****************************************************");
        formattedDate = timeClient.getFormattedDate();
        Serial.println(formattedDate);
      
        // Extract date
        int splitT = formattedDate.indexOf("T");
        dayStamp = formattedDate.substring(0, splitT);
        Serial.print("DATE: ");
        Serial.println(dayStamp);
        // Extract time
        timeStamp = formattedDate.substring(splitT+1, formattedDate.length()-1);
        Serial.print("HOUR: ");
        Serial.println(timeStamp);
        Serial.println("*****************************************************");
      
        setRTC(dayStamp, timeStamp);
        
 
}

void getDtHoraRTC(){

          RtcDateTime now = Rtc.GetDateTime();
          //Print RTC time to Serial Monitor
          Serial.print("Datexxxxxxxxxxxxx:");
          Serial.print(now.Year(), DEC);
          Serial.print('/');
          Serial.print(now.Month(), DEC);
          Serial.print('/');
          Serial.print(now.Day(), DEC);
          Serial.print(" Time:");
          Serial.print(now.Hour(), DEC);
          Serial.print(':');
          Serial.print(now.Minute(), DEC);
          Serial.print(':');
          Serial.print(now.Second(), DEC);
          Serial.println("\n");
          delay(1000); // one second


            String ano = ((String)now.Year());
            String mes = ((String)now.Month());
            String dia = ((String)now.Day());

            String h24 = ((String)now.Hour());
            String mit = ((String)now.Minute());
            String ss  = ((String)now.Minute());

            dt =  ano + '-' + mes + '-' + dia;
            hr =  h24 + ':' + mit + ':' + ss;


            Serial.println("¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨");
            Serial.println(dt);
            Serial.println(hr);
            Serial.println("¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨");

  

}

void wS(String msg){

    HTTPClient http;

    Serial.println("Dados para envio < == > "+msg);
    
    http.begin("http://arduino.moradaverdehf.com.br/api/tempmeasures/getsave/" + msg + "");

    Serial.println("<++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++> ");

    //http://arduino.moradaverdehf.com.br/api/tempmeasures/getsave/1/2017-11-10/20:45:52/12.45/932.12
    int httpCode = http.GET();
     //checa o retorno do codigo
    if (httpCode > 0) {

    String payload = http.getString(); // retorno
    Serial.print(msg);
    Serial.print(" ");
    Serial.println(payload);

     }
    http.end();   //fecha conexao

  
}


void wSValidInformation(){


   //PEGA TODAS AS INFORMAÇÕES E MANDA PARA O WEBSERVICE
      String msgSensorTemp;

      //  dt =  ano + '-' + mes + '-' + dia;
      ////http://arduino.moradaverdehf.com.br/api/tempmeasures/getsave/1/2017-11-10/20:45:52/12.45/932.12


     
      
      msgSensorTemp = stufa + '/'+dayStamp+ '/'+timeStamp+'/'+temperature+'/'+humidity;

      Serial.println("Preparando dados para envio < == > "+msgSensorTemp);

      wS(msgSensorTemp);
         
 

  
  
}

void loop()
{

  
      int i;
      
      
      for (i=0;i<300;i++){
      
              digitalWrite(D4, !digitalRead(D4));
              delay(500); 
              digitalWrite(D4, !digitalRead(D4));
              delay(500);
              Serial.print("⁕");
              
              if(i==140){
                Serial.print("\n");
                
                digitalWrite(D4, !digitalRead(D4));
                //delay(dht.getMinimumSamplingPeriod());
               
               
                //**************************************************************************************
                delay(dht.getMinimumSamplingPeriod());
      
      
          
      
                humidity = dht.getHumidity();
                temperature = dht.getTemperature();
      
      
                if(dht.getStatusString() == "OK"){
                Serial.println(dht.getStatusString());  
                }else{
                  
                }
      
                dtHours();
                getDtHoraRTC();
                Serial.print("UMIDADE     < == > ");
                Serial.print(humidity,1);
                Serial.print("%");
                Serial.println("\n");
      
                Serial.print("TEMPERATURA < == > ");
                Serial.print(temperature,1);
                Serial.print("℃");
                
                Serial.println("\n");
                Serial.println(" ");
                Serial.println("************************************************************");
                Serial.println("ESP8266 Reniciando.......");
                digitalWrite(D4, !digitalRead(D4));


                wSValidInformation();
                
                ESP.restart(); //funcao correta
      
                Serial.println("...............................................................................");
                }else{
                // Serial.println("ESP8266 Ligado.......");
                }
                
              }

}
