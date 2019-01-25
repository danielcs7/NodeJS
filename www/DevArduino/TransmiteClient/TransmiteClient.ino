//------------------------------------------------------------------------------------
// Libraries Needed For This Project
//------------------------------------------------------------------------------------
#include "FS.h"
#include <TimeLib.h>
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <Wire.h>        //Biblioteca para manipulação do protocolo I2C
#include <DS3231.h>      //Biblioteca para manipulação do DS3231
#include <WiFiUdp.h>
#include <DHT.h>;
//------------------------------------------------------------------------------------
// Defining I/O Pins
//------------------------------------------------------------------------------------
  #define       LED0      2         // WIFI Module LED
  #define       LED1      D0        // Indicate Connectivity With Client #1
  #define       BUTTON    D1        // Connectivity ReInitiate Button
  #define DHTPIN D3 // What digital pin we're connected to
  #define DHTTYPE DHT22 // DHT 22, AM2302, AM2321 
//------------------------------------------------------------------------------------
// BUTTON Variables
//------------------------------------------------------------------------------------
  int           ButtonState;
  int           LastButtonState   = LOW;
  int           LastDebounceTime  = 0;
  int           DebounceDelay     = 50;
  const String  ClientType       = "1";
  int           numBytes          =1;
  const int timeZone = -3;
  const String x = "'";
  static String hr;
  static String dt;
  static String param1;
  static String param2;
  static String param3;
  static String param4;
  static float h;
  static float t;
//------------------------------------------------------------------------------------
// LED Delay Variables
//------------------------------------------------------------------------------------
  int           LEDState          = LOW;
  unsigned long CurrMillis        = 0;
  unsigned long PrevMillis        = 0;
  unsigned long Interval          = 1000;
//------------------------------------------------------------------------------------
// WIFI Authentication Variables
//------------------------------------------------------------------------------------
/* This Client Is Going To Connect To A WIFI Server Access Point
 * So You Have To Specify Server WIFI SSID & Password In The Code Not Here
 * Please See The Function Below Name (WiFi.Begin)
 * If WIFI dont need Any password Then WiFi.begin("SSIDNAME")
 * If WIFI needs a Password Then WiFi.begin("SSIDNAME", "PASSWORD")
 */
  char*         ESPssid;            // Wifi Name
  char*         ESPpassword;        // Wifi Password
//------------------------------------------------------------------------------------
// WIFI Module Role & Port
//------------------------------------------------------------------------------------
// NTP Servers:
static const char ntpServerName[] = "br.pool.ntp.org";
//------------------------------------------------------------------------------------

/* This WIFI Module Code Works As A Client
 * That Will Connect To A Server WIFI Modul With (IP ADDress 192.168.4.1)
 */
  int             ESPServerPort  = 9001;
  IPAddress      ESPServer(192,168,4,1);
  WiFiClient      ESPClient;
  DHT dht(DHTPIN, DHTTYPE);
  DS3231 rtc;              //Criação do objeto do tipo DS3231
  RTCDateTime dataehora;   //Criação do objeto do tipo RTCDateTime

//====================================================================================

void sendNTPpacket(IPAddress &address);

void formatFS(void) {
  SPIFFS.format();
}

void sendSensor()
{
  float h = dht.readHumidity();
  float t = dht.readTemperature(); // or dht.readTemperature(true) for Fahrenheit

  if (isnan(h) || isnan(t)) {
    Serial.println("Failed to read from DHT sensor!");
    return;
  } else {
    writeFile("Temperatura" + (String)t);
  }

}

void createFile(void) {
  File wFile;

  //Cria o arquivo se ele não existir
  if (SPIFFS.exists("/log.txt")) {
    Serial.println("Arquivo ja existe!");
  } else {
    Serial.println("Criando o arquivo...");
    wFile = SPIFFS.open("/log.txt", "w+");

    //Verifica a criação do arquivo
    if (!wFile) {
      Serial.println("Erro ao criar arquivo!");
    } else {
      Serial.println("Arquivo criado com sucesso!");
    }
  }
  wFile.close();
}

void deleteFile(void) {
  //Remove o arquivo
  if (SPIFFS.remove("/log.txt")) {
    Serial.println("Erro ao remover arquivo!");
  } else {
    Serial.println("Arquivo removido com sucesso!");
  }
}

void writeFile(String msg) {

  //Abre o arquivo para adição (append)
  //Inclue sempre a escrita na ultima linha do arquivo
  File rFile = SPIFFS.open("/log.txt", "a+");

  if (!rFile) {
    Serial.println("Erro ao abrir arquivo!");
  } else {
    rFile.println("Log: " + msg);
    Serial.println(msg);
  }
  rFile.close();
}


void closeFS(void) {
  SPIFFS.end();
}

void openFS(void) {
  //Abre o sistema de arquivos
  if (!SPIFFS.begin()) {
    Serial.println("Erro ao abrir o sistema de arquivos");
  } else {
    Serial.println("Sistema de arquivos aberto com sucesso!");
  }
}


  void setup() 
  {
    // Setting The Serial Port ----------------------------------------------
    Serial.begin(115200);           // Computer Communication
    rtc.begin();
    dht.begin();
    
    // Setting The Mode Of Pins ---------------------------------------------
    pinMode(LED0, OUTPUT);          // WIFI OnBoard LED Light
    pinMode(LED1, OUTPUT);          // Indicator For Client #1 Connectivity
    pinMode(BUTTON, INPUT_PULLUP);  // Initiate Connectivity
    digitalWrite(LED0, !LOW);       // Turn WiFi LED Off
    
    // Print Message Of I/O Setting Progress --------------------------------
    Serial.println("\nI/O Pins Modes Set .... Done");

    // Starting To Connect --------------------------------------------------
    if(WiFi.status() == WL_CONNECTED)
    {
      WiFi.disconnect();
      WiFi.mode(WIFI_OFF);
      delay(50);
    }

    /* in this part it should load the ssid and password 
     * from eeprom they try to connect using them */
    
    WiFi.mode(WIFI_STA);                // To Avoid Broadcasting An SSID
    WiFi.begin("MasterMV", "");      // The SSID That We Want To Connect To

    // Printing Message For User That Connetion Is On Process ---------------
    Serial.println("!--- Connecting To " + WiFi.SSID() + " ---!");

    // WiFi Connectivity ----------------------------------------------------
    CheckWiFiConnectivity();        // Checking For Connection

    // Stop Blinking To Indicate Connected ----------------------------------
    digitalWrite(LED0, !HIGH);
    Serial.println("!-- Client Device Connected --!");

    // Printing IP Address --------------------------------------------------
    Serial.println("Connected To      : " + String(WiFi.SSID()));
    Serial.println("Signal Strenght   : " + String(WiFi.RSSI()) + " dBm");
    Serial.print  ("Server IP Address : ");
    Serial.println(ESPServer);
    Serial.print  ("Server Port Num   : ");
    Serial.println(ESPServerPort);
    // Printing MAC Address
    Serial.print  ("Device MC Address : ");
    Serial.println(String(WiFi.macAddress()));
    // Printing IP Address
    Serial.print  ("Device IP Address : ");
    Serial.println(WiFi.localIP());

     createFile();
  //Abre o sistema de arquivos (mount)
  openFS();
  //Cria o arquivo caso o mesmo não exista


    // Conecting The Device As A Client -------------------------------------
    ESPRequest();

   

  }

//====================================================================================
  time_t prevDisplay = 0;


  void loop()
  {
 dataehora = rtc.getDateTime();
  dtHora();

  float h = dht.readHumidity();
  float t = dht.readTemperature();
    ReadButton();
    ESPRequest();

    delay(80000);
  
  }

  void dtHora() {

 dataehora = rtc.getDateTime();  //Atribuindo valores instantâneos de  //data e hora à instância dataehora

  String ano = ((String)dataehora.year);
  String mes = ((String)dataehora.month);
  String dia = ((String)dataehora.day);

  String h24 = ((String)dataehora.hour);
  String mit = ((String)dataehora.minute);
  String ss  = ((String)dataehora.second);

  dt =  ano + '-' + mes + '-' + dia;
  hr =  h24 + ':' + mit + ':' + ss;

  h = dht.readHumidity();
  t = dht.readTemperature();

  param2 = dt;
  param3 = hr;


  param4 = ClientType + '/' + param2 + '/' + param3 + '/' + t + '/' + h;

  Serial.println(param4);


}

//====================================================================================

  void ReadButton()
  {
    // Reading The Button
    int reading = digitalRead(BUTTON);

    reading = 1;
    // If It Doest Match The Previous State
    if(reading != LastButtonState)
    {
      LastDebounceTime = millis();
    }

    // To Iliminate Debounce
    if((millis() - LastDebounceTime) > DebounceDelay)
    {
      if(reading != ButtonState)
      {
        ButtonState = reading;
        
        if(ButtonState == LOW)
        {
         
          Serial.println   ("<" + ClientType + "-" + LEDState + ">");
          ESPClient.println("<" + ClientType + "-" + LEDState + ">");
          ESPClient.flush();
        }
      }
    }

    // Last Button State Concidered
    LastButtonState = reading;
  }

//====================================================================================

  void CheckWiFiConnectivity()
  {
    while(WiFi.status() != WL_CONNECTED)
    {
      for(int i=0; i < 10; i++)
      {
        Serial.print(".");
      }
      Serial.println("");
    }
  }

  void ESPRequest()
  {
    // First Make Sure You Got Disconnected
    ESPClient.stop();

    // If Sucessfully Connected Send Connection Message
    if(ESPClient.connect(ESPServer, ESPServerPort))
    {

     
       Serial.println    ("<" + ClientType + "- CONNECTED>");
       Serial.println("<" + ClientType +"-"+param4+">");
      ESPClient.println ("<" + ClientType +"-"+param4+">");
      numBytes = numBytes+1;
      
    }
  }

//====================================================================================
