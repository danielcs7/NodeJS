/*
   Author : Daniel Carlos Santos
*/
#include "FS.h"
#include <TimeLib.h>
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <Wire.h>        //Biblioteca para manipulação do protocolo I2C
#include <DS3231.h>      //Biblioteca para manipulação do DS3231
#include <WiFiUdp.h>
#include <DHT.h>;
#include <DHT_U.h>

const char ssid[] = "iab-adm";  //  nome da rede
const char pass[] = "iab124578";     // senha da rede

//const char ssid[] = "iPhone de danielcarloss7";  //  nome da rede
//const char pass[] = "dadau171";     // senha da rede

// NTP Servers:
static const char ntpServerName[] = "br.pool.ntp.org";

const int timeZone = -3;
const String x = "'";
static String hr;
static String dt;
static String param1;
static String param2;
static String param3;
static float h;
static float t;

//variavel hadcode para o nome da stufa
//const String stufa = "stufa_blue1";
const String stufa = "4";

WiFiUDP Udp;
unsigned int localPort = 8888;

bool running = false;

void sendNTPpacket(IPAddress &address);

//ENTRADA PARA LM35
int outputpin = A0;
#define DHTPIN D3 // What digital pin we're connected to
#define DHTTYPE DHT22 // DHT 22, AM2302, AM2321 

DHT_Unified dht(DHTPIN, DHTTYPE);
uint32_t delayMS;

DS3231 rtc;              //Criação do objeto do tipo DS3231
RTCDateTime dataehora;   //Criação do objeto do tipo RTCDateTime

void formatFS(void) {
  SPIFFS.format();
}

void sendSensor()
{
  Serial.println("DHTxx Unified Sensor Example");
  
  sensor_t sensor;
  dht.temperature().getSensor(&sensor);
  //##AQUI PEGO OS DADOS TÉCNICOS DO SENSOR DE TEMPERATURA E UMIDADE
  //###############################################################################################
  Serial.println("------------------------------------");
  Serial.println("Temperature");
  Serial.print  ("Sensor:       "); Serial.println(sensor.name);
  Serial.print  ("Driver Ver:   "); Serial.println(sensor.version);
  Serial.print  ("Unique ID:    "); Serial.println(sensor.sensor_id);
  Serial.print  ("Max Value:    "); Serial.print(sensor.max_value); Serial.println(" *C");
  Serial.print  ("Min Value:    "); Serial.print(sensor.min_value); Serial.println(" *C");
  Serial.print  ("Resolution:   "); Serial.print(sensor.resolution); Serial.println(" *C");  
  Serial.println("------------------------------------");
  
  dht.humidity().getSensor(&sensor);
  Serial.println("------------------------------------");
  Serial.println("Humidity");
  Serial.print  ("Sensor:       "); Serial.println(sensor.name);
  Serial.print  ("Driver Ver:   "); Serial.println(sensor.version);
  Serial.print  ("Unique ID:    "); Serial.println(sensor.sensor_id);
  Serial.print  ("Max Value:    "); Serial.print(sensor.max_value); Serial.println("%");
  Serial.print  ("Min Value:    "); Serial.print(sensor.min_value); Serial.println("%");
  Serial.print  ("Resolution:   "); Serial.print(sensor.resolution); Serial.println("%");  
  Serial.println("------------------------------------");
  //###########################################################################################FIM
  
  //AJUSTE O ATRASO ENTRE AS LEITURAS DO SENSOR COM BASE NOS DETALHES DO SENSOR
  delayMS = sensor.min_delay / 1000;
 

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
  Serial.begin(115200);
  delay(250);
  rtc.begin();
  dht.begin();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.begin(ssid, pass);

  while (WiFi.status() != WL_CONNECTED) {
    delay(250);
    Serial.print(".");
    
  }

  Serial.println("DHTxx Unified Sensor Example");
  
  sensor_t sensor;
  dht.temperature().getSensor(&sensor);
  //##AQUI PEGO OS DADOS TÉCNICOS DO SENSOR DE TEMPERATURA E UMIDADE
  //###############################################################################################
  Serial.println("------------------------------------");
  Serial.println("Temperature");
  Serial.print  ("Sensor:       "); Serial.println(sensor.name);
  Serial.print  ("Driver Ver:   "); Serial.println(sensor.version);
  Serial.print  ("Unique ID:    "); Serial.println(sensor.sensor_id);
  Serial.print  ("Max Value:    "); Serial.print(sensor.max_value); Serial.println(" *C");
  Serial.print  ("Min Value:    "); Serial.print(sensor.min_value); Serial.println(" *C");
  Serial.print  ("Resolution:   "); Serial.print(sensor.resolution); Serial.println(" *C");  
  Serial.println("------------------------------------");
  
  dht.humidity().getSensor(&sensor);
  Serial.println("------------------------------------");
  Serial.println("Humidity");
  Serial.print  ("Sensor:       "); Serial.println(sensor.name);
  Serial.print  ("Driver Ver:   "); Serial.println(sensor.version);
  Serial.print  ("Unique ID:    "); Serial.println(sensor.sensor_id);
  Serial.print  ("Max Value:    "); Serial.print(sensor.max_value); Serial.println("%");
  Serial.print  ("Min Value:    "); Serial.print(sensor.min_value); Serial.println("%");
  Serial.print  ("Resolution:   "); Serial.print(sensor.resolution); Serial.println("%");  
  Serial.println("------------------------------------");
  //###########################################################################################FIM
  
  //AJUSTE O ATRASO ENTRE AS LEITURAS DO SENSOR COM BASE NOS DETALHES DO SENSOR
  delayMS = sensor.min_delay / 1000;

 

  formatFS();
  createFile();
  //Abre o sistema de arquivos (mount)
  openFS();
  //Cria o arquivo caso o mesmo não exista

  
  Udp.begin(localPort);
  setSyncProvider(getNtpTime);
  setSyncInterval(30000);

}

time_t prevDisplay = 0;


void loop()
{

  if (timeStatus() != timeNotSet) {
    if (now() != prevDisplay) { 
      prevDisplay = now();
      digitalClockDisplay();
    }
  }

  vDadosSensor();
  setValorHr();
  dtHora();
  dataehora = rtc.getDateTime();
  setValorHr();
  wSValida ();
  
  //tempo para o proximo insert
  //delay(180000); // 3minuto
  //delay(60000); // 1minuto
  delay(13000); //
  //  delay(20000);

}


void vDadosSensor(){
     delay(delayMS);
   
   
  // PEGA OS DADOS DE TEMPERATURA
  //###########################################################################################
  sensors_event_t event;  
  dht.temperature().getEvent(&event);
  if (isnan(event.temperature)) {
    Serial.println("Erro na leitura do dispositivo Temperatura");
  }
  else {
    Serial.print("Temperature: ");
    Serial.print(event.temperature);
     t = event.temperature;
    Serial.println(" *C");
  }
  //PEGA OS DADOS DE UMIDADE
  //###########################################################################################
  dht.humidity().getEvent(&event);
  if (isnan(event.relative_humidity)) {
    Serial.println("Erro na leitura do dispositivo Umidade");
  }
  else {
    Serial.print("Humidity: ");
    Serial.print(event.relative_humidity);
     h = event.relative_humidity;
    Serial.println("%");
  }
}


void setValorHr(){
    String dx = ((String)day());
    String mx = ((String)month());
    String yx = ((String)year());

    int Xpp = dx.toInt();
  
    String hhx = ((String)hour());
    String mix = ((String)minute());
    String ssx = ((String)second());

   
if (running == false){

Serial.println("287");
Serial.println(running);
Serial.println("*******");
Serial.println(yx);
Serial.println("***********");
    
  if(yx.toInt() == 2066 || yx.toInt() == 1970){

    Serial.println("Erro ao setar RTC") ;   
    running = false;
     digitalClockDisplay();
    
  }else{
    Serial.println("SETANDO VALORES DA DATA E HORA NA HORA DO START DO SISTEMA");
    rtc.setDateTime(yx.toInt(),mx.toInt(),dx.toInt(),hhx.toInt(),mix.toInt(),ssx.toInt());   //Configurando valores iniciais
  //rtc.setDateTime(, __TIME__);
  running = true;
  }
  
}
  
}


void vSetarHoraSistema(){

   String dx = ((String)day());
    String mx = ((String)month());
    String yx = ((String)year());

    int Xpp = dx.toInt();
  
    String hhx = ((String)hour());
    String mix = ((String)minute());
    String ssx = ((String)second());

   if(yx.toInt() == 2066 || yx.toInt() == 1970){

    Serial.println("Erro ao setar RTC") ;   
    running = false;
     digitalClockDisplay();
    
  }else{
    Serial.println("SETANDO VALORES DA DATA E HORA NA HORA DO START DO SISTEMA");
    rtc.setDateTime(yx.toInt(),mx.toInt(),dx.toInt(),hhx.toInt(),mix.toInt(),ssx.toInt());   //Configurando valores iniciais
  //rtc.setDateTime(, __TIME__);
  running = true;
  }
    
}

void dtHora() {

  setValorHr();

  dataehora = rtc.getDateTime();  //Atribuindo valores instantâneos de  //data e hora à instância dataehora

  String ano = ((String)dataehora.year);
  String mes = ((String)dataehora.month);
  String dia = ((String)dataehora.day);

  String h24 = ((String)dataehora.hour);
  String mit = ((String)dataehora.minute);
  String ss  = ((String)dataehora.second);

  dt =  ano + '-' + mes + '-' + dia;
  hr =  h24 + ':' + mit + ':' + ss;



    if(h24 == "15" & mit == "16"){
      reset_config();
    }
  

  //Serial.println("****************************************************");
  //Serial.println(dt);
  //Serial.println(hr);
//Serial.println("****************************************************");
}


void reset_config(void) {
Serial.println("*O ESP ira resetar agora");
ESP.reset();
}


void wS(String msg) {

  String yx = ((String)year());
  
  msg.replace("Log: ", "");
  String msg2 = msg;

  msg2.replace("Log: ", "");
  msg2.replace(" ", "");

  String x1 = msg2.substring(5);
  String x2 = msg2.substring(6, 7);


  HTTPClient http;  //Declara a classe HTTPClient
  //http://192.168.0.11/weather/insert_weather.php/?stufa=stufa&data=dt&temp=32.43&humid=80.32
  //****TESTE DE CASA
  //http.begin("http://192.168.0.11/weather/insert_weather.php/?stufa="+param1+"&data="+param2+"&hr="+param3+"&temp="+t+"&humid="+h+"");
  //***TESTE PRODUCAO

  if(yx.toInt() == 2066  || yx.toInt() == 1970){

    Serial.println("Erro ao setar RTC") ; 
     digitalClockDisplay();
     vSetarHoraSistema(); 
     
    running = false;
 
  }else{
     http.begin("http://arduino.moradaverdehf.com.br/api/tempmeasures/getsave/" + msg2 + "");
  }

  //http://arduino.moradaverdehf.com.br/api/tempmeasures/getsave/1/2017-11-10/20:45:52/12.45/932.12
  int httpCode = http.GET();
  //checa o retorno do codigo
  if (httpCode > 0) {

    String payload = http.getString(); // retorno
    Serial.print(msg2);
    Serial.print(" ");
    Serial.println(payload);

  }
  http.end();   //fecha conexao


}


void wSlog(String msg) {

  String yx = ((String)year());
  msg.replace("Log: ", "");
  String msg2 = msg;

  msg2.replace("Log: ", "");
  msg2.replace(" ", "");

  String x1 = msg2.substring(4);
  String x2 = msg2.substring(5, 34);

  

  x1.trim();

  HTTPClient http;  //Declara a classe HTTPClient
  //http://192.168.0.11/weather/insert_weather.php/?stufa=stufa&data=dt&temp=32.43&humid=80.32
  //****TESTE DE CASA
  //http.begin("http://192.168.0.11/weather/insert_weather.php/?stufa="+param1+"&data="+param2+"&hr="+param3+"&temp="+t+"&humid="+h+"");
  //***TESTE PRODUCAO

 if(yx.toInt() == 2066 || yx.toInt() == 1970){

    Serial.println("Erro ao setar RTC") ; 
     digitalClockDisplay();
    vSetarHoraSistema();  
    running = false;
 
  }else{
  http.begin("http://arduino.moradaverdehf.com.br/api/tempmeasures/getsave/" + x1 + "");
  }

  //http://arduino.moradaverdehf.com.br/api/tempmeasures/getsave/1/2017-11-10/20:45:52/12.45/932.12
  int httpCode = http.GET();
  //checa o retorno do codigo
  if (httpCode > 0) {

    String payload = http.getString(); // retorno
    Serial.print("LOG => ");
    Serial.println(payload);

  }
  http.end();   //fecha conexao


}



void wSValida () {

  dtHora();

  param2 = dt;
  param3 = hr;


  if (WiFi.status() != WL_CONNECTED) {

  

    String vlida = ((String)t);

    if (vlida == "nan") {
      Serial.println("Falha no sensor");
    } else {
      Serial.println("Gerando LOG!!!!");
      writeFile(stufa + '/' + dt + '/' + hr + '/' + t + '/' + h);
    }


  } else {

    dtHora();
    if (SPIFFS.exists("/log.txt") ) {
      Serial.println("Envia os dados do log");
      readFile();

    } else {

     

      String vlida = ((String)t);
      
      if (vlida == "nan") {
        Serial.println("Falha no sensor");
      } else {
        String rel = stufa + '/' + param2 + '/' + param3 + '/' + t + '/' + h;

        wS(rel);
      }
    }

  }


}


void readFile() {
  //Faz a leitura do arquivo
  dtHora();
  File rFile = SPIFFS.open("/log.txt", "r");
  //Dir dir = SPIFFS.open("/log.txt","r");
  Serial.println("Reading file...");
   while (rFile.available()) {
    String line = rFile.readStringUntil('\n');
    line.replace(" ", "");
    wSlog(line);

   Serial.println("\n ");
   Serial.println("Aguardando....\n");
    delay(2000);
   
  }
  rFile.close();
  formatFS();
  running = false;
  dtHora();
}

void digitalClockDisplay()
{

 Serial.println("\n ");
  Serial.println("################################################### ");

  Serial.println("Gravando dados no BD: ");
  Serial.print((int)t); Serial.print(" *C, "); 
  Serial.print((int)h); Serial.println(" %");
  
    String param1;  
    String param2;
    String param3;
 
    String dx = ((String)day());
    String mx = ((String)month());
    String yx = ((String)year());

    dt = yx+'-'+mx+'-'+dx;

    //String hr = "09:04:38";
    //**REF => https://ntp.br
    // horário de verão 2017/2018 se inicia as 0h de 15 de outubro de 2017, e vai até as 0h de 18 de fevereiro de 2018
    if(dt == "2018-02-19"){
      //Fim do horario de verao
    
    String hhx = ((String)hour());
    String mix = ((String)minute());
    String ssx = ((String)second());


    hr = hhx+':'+mix+':'+ssx;
      
    }else{

    int hh24 = hour();
   
    String hhx = ((String)hh24);
    String mix = ((String)minute());
    String ssx = ((String)second());

    hr = hhx+':'+mix+':'+ssx;
  
    }


 Serial.println(dt);
 Serial.println(hr);

    
 Serial.println("################################################### ");
    param2 = dt;
    param3 = hr;

 if(WiFi.status()== WL_CONNECTED){

  if(SPIFFS.exists("/log.txt")){ 
 
     Serial.println("existe log");
     readFile();
  
   }
 
   
   }else{
     wSValida ();
    Serial.println("################################################### ");
    
   
   }
 
  }
 



void printDigits(int digits)
{
  Serial.print(":");
  if (digits < 10)
    Serial.print('0');
  Serial.print(digits);
}


/*-------- NTP code ----------*/
const int NTP_PACKET_SIZE = 48; // NTP time is in the first 48 bytes of message
byte packetBuffer[NTP_PACKET_SIZE]; //buffer to hold incoming & outgoing packets

time_t getNtpTime()
{
  IPAddress ntpServerIP; // NTP server's ip address

  while (Udp.parsePacket() > 0) ; // discard any previously received packets
  Serial.println("Transmit NTP Request");
  // get a random server from the pool
  WiFi.hostByName(ntpServerName, ntpServerIP);
  Serial.print(ntpServerName);
  Serial.print(": ");
  Serial.println(ntpServerIP);
  sendNTPpacket(ntpServerIP);
  uint32_t beginWait = millis();
  while (millis() - beginWait < 1500) {
    int size = Udp.parsePacket();
    if (size >= NTP_PACKET_SIZE) {
      Serial.println("Receive NTP Response");
      Udp.read(packetBuffer, NTP_PACKET_SIZE);  // read packet into the buffer
      unsigned long secsSince1900;
      // convert four bytes starting at location 40 to a long integer
      secsSince1900 =  (unsigned long)packetBuffer[40] << 24;
      secsSince1900 |= (unsigned long)packetBuffer[41] << 16;
      secsSince1900 |= (unsigned long)packetBuffer[42] << 8;
      secsSince1900 |= (unsigned long)packetBuffer[43];
      return secsSince1900 - 2208988800UL + timeZone * SECS_PER_HOUR;
    }
  }
  Serial.println("No NTP Response :-(");
  return 0; // return 0 if unable to get the time
}

void sendNTPpacket(IPAddress &address)
{
  memset(packetBuffer, 0, NTP_PACKET_SIZE);
  packetBuffer[0] = 0b11100011;   // LI, Version, Mode
  packetBuffer[1] = 0;     // Stratum, or type of clock
  packetBuffer[2] = 6;     // Polling Interval
  packetBuffer[3] = 0xEC;  // Peer Clock Precision
  packetBuffer[12] = 49;
  packetBuffer[13] = 0x4E;
  packetBuffer[14] = 49;
  packetBuffer[15] = 52;
  Udp.beginPacket(address, 123); //NTP requests are to port 123
  Udp.write(packetBuffer, NTP_PACKET_SIZE);
  Udp.endPacket();
}
