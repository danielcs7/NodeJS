#include <Arduino.h>
#include <NTPClient.h>
#include <WiFiUdp.h>
#include <ESP8266WiFi.h>
//https://github.com/Seeed-Studio/Grove_BME280
#include "Seeed_BME280.h"
#include <UniversalTelegramBot.h>
#include <Wire.h>              // responsável pela comunicação com a interface i2c
#include <LiquidCrystal_I2C.h> // responsável pela comunicação com o display LCD

const char *ssid = "netvirtua_203";
const char *password = "1073847000";
#define CHAT_ID "393741829"

static String formattedDate;
static String dayStamp;
String timeStamp;
static float humidity;
static float temperature;
static float vTempbrew;
static int dias_decorrido = 1; // Variavel para armazenar os dias decorridos desde o começo da incubação
byte horas = 0;                // Variavel para contar as horas desde o começo da incubação
byte minuto = 0;               // Variavel para contar os minutos desde o começo da incubação
unsigned long controleDoTempo; // Variavel utilizada para tira a difernça ente o tempo decorrido
String ipStr;

static int vCountMsg = 0;
static int vQtdeDias;
static int vQtdeMudTemp;
static int vCountDias = 1;
static int vIniciaProcess = 0;
static String dtAnterior;
static String dtAtual;

int16_t utc = -3; //UTC -3:00 Brazil

WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "a.st1.ntp.br", utc * 3600, 60000);

//Intervalo entre as checagens de novas mensagens
#define INTERVAL 1000

//long previousMillisAmarelo = 0;  //VARIÁVEL QUE CONTROLA O TEMPO DO LED AMARELO
long previousMillisTemp = 0;
long tempoInicial = 0;

//long tempLedInterval = 60000; //VARIÁVEL QUE REGISTRA O INTERVALO DO LED AMARELO (2000ms = 2 SEGUNDOS)
long tempLedInterval = 10000;

//Token do seu bot. Troque pela que o BotFather te mostrar
#define BOT_TOKEN "799913515:AAF8EnIyd5uFfmXCV6AwyDovtvHWhvxl3cE"
//Cliente para conexões seguras
WiFiClientSecure client;
//Objeto com os métodos para comunicarmos pelo Telegram
UniversalTelegramBot bot(BOT_TOKEN, client);
//Tempo em que foi feita a última checagem
uint32_t lastCheckTime = 0;

//Quantidade de usuários que podem interagir com o bot
#define SENDER_ID_COUNT 2
//Ids dos usuários que podem interagir com o bot.
//É possível verificar seu id pelo monitor serial ao enviar uma mensagem para o bot
String validSenderIds[SENDER_ID_COUNT] = {"393741829"};

BME280 bme280;
LiquidCrystal_I2C lcd(0x27, 16, 2);

void sendTelegramMessage()
{
  String message = "      ************* {BrewCode} ************* ";
  message.concat("\n");
  message.concat("\n");

  String vEstilo = "PILSEN";
  String vRedEstilo = "Estilo : ";
  String msgEstilo = vRedEstilo + ' ' + vEstilo;

  message.concat("***************************");
  message.concat("\n");

  message.concat(msgEstilo);
  message.concat("\n");

  message.concat("***************************");
  message.concat("\n");

  message.concat("Inicio do Processo ");
  message.concat(dayStamp);
  message.concat("\n");

  message.concat("***************************");
  message.concat("\n");

  message.concat("Qtde de Dias: ");
  message.concat(dias_decorrido);
  message.concat("\n");

  message.concat("***************************");
  message.concat("\n");

  message.concat("Tempo -> ");
  String msgBrewCode = String(horas) + ':' + String(minuto);
  String msgSensorTemp = String(horas) + ':' + String(minuto) + ' min';

  Serial.println("msgBrewCode" + msgBrewCode);
  Serial.println("msgSensorTemp" + msgSensorTemp);

  //Serial.println("Qtde de Tempo: Horas " + horas + ' : ' + minuto + ' min');
  message.concat(msgBrewCode);
  //message.concat("Horas " + horas);
  //message.concat(minuto + " Minutos");
  message.concat("\n");
  message.concat("\n");

  message.concat("***************************");
  message.concat("\n");

  message.concat("Temperatura Ext:   ");
  message.concat(temperature);
  message.concat("\n");

  message.concat("Temperatura Int:   ");
  message.concat(vTempbrew);
  message.concat("\n");

  message.concat("***************************");
  message.concat("\n");

  if (bot.sendMessage(CHAT_ID, message, "Markdown"))
  {
    Serial.println("Mensagem Telegram enviada com sucesso");
  }
  //telegramSensorPressedFlag = false;
}

void sendSensor()
{

  temperature = bme280.getTemperature();
  humidity = bme280.getHumidity();

  //Serial.print("Temp: ");
  //Serial.print(bme280.getTemperature());
  //Serial.println("C");

  if (isnan(humidity || isnan(temperature)))
  {
    Serial.println("Failed to read from DHT sensor!");
    return;
  }
}

void setData()
{

  while (!timeClient.update())
  {
    timeClient.forceUpdate();
  }

  // The formattedDate comes with the following format:
  // 2018-05-28T16:00:13Z
  // We need to extract date and time
  formattedDate = timeClient.getFormattedDate();
  Serial.println(formattedDate);

  // Extract date
  int splitT = formattedDate.indexOf("T");
  dayStamp = formattedDate.substring(0, splitT);
  Serial.print("DATE: ");
  Serial.println(dayStamp);
  // Extract time
  timeStamp = formattedDate.substring(splitT + 1, formattedDate.length() - 1);
  Serial.print("HOUR: ");
  Serial.println(timeStamp);
}

void catchMinute()
{

  if (vIniciaProcess == 0)
  {
    //minuto++;
    vIniciaProcess == 1;
  }

  if ((millis() - controleDoTempo) > 60000)
  { // Verivicado se ja se passaram 1 minuto

    Serial.println("Passou : " + String(minuto) + " min");

    minuto++; // Minuto recebe minuto mais 1
  }
}

void catchHours()
{

  if (minuto > 59)
  { // Verificado se ja se passaram 59 Minutos
    Serial.println("Passou Horas: " + String(minuto) + " min");
    horas++;    // Horas recebe Horas mais 1
    minuto = 0; // Minutos recebe 0
  }
}

void Relogio()
{

  if (vIniciaProcess == 0)
  {
    //minuto++;
  }

  if ((millis() - controleDoTempo) > 60000)
  { // Verivicado se ja se passaram 1 minuto

    //Serial.println("Passou : " + String(minuto) + " min");

    minuto++; // Minuto recebe minuto mais 1

    if (minuto > 59)
    { // Verificado se ja se passaram 59 Minutos
      Serial.println("Passou : " + String(minuto) + " min");
      horas++;    // Horas recebe Horas mais 1
      minuto = 0; // Minutos recebe 0
      if (horas >= 24)
      { // Verifica se ja se passaram 23 Horas

        dias_decorrido = dias_decorrido + 1; // Dias recebe dias mais 1

        Serial.println("Quantidade Dias :" + String(dias_decorrido));

        Serial.println("Passou hors :" + String(horas) + " hrs");
        //horas = 0;        // horas recebe 0
      }
    }
    //controleDoTempo = millis(); // Inicia a contagem do tempo novamente
  }
}

void vPettier()
{

  Serial.println("Pelttier..");
  Serial.println("***************");
  Serial.println("Temperature: " + String(temperature));
  Serial.println("Temperature int: " + String(vTempbrew));

  Serial.println("***************");

  if (temperature == vTempbrew)
  {
    digitalWrite(D3, HIGH);
    Serial.println("Espera 1 minuto");
  }
  if (temperature > vTempbrew)
  {
    //aciona o
    Serial.println("Acionado");
    digitalWrite(D3, LOW);
    }
}

void vValidDayTemp()
{

  switch (dias_decorrido)
  {
  case 1:
    //do something when var equals 1vQtdeMudTemp
    vQtdeMudTemp = 12;
    vTempbrew = vQtdeMudTemp;
    break;
  case 2:
    vQtdeMudTemp = 12;
    vTempbrew = vQtdeMudTemp;
    break;
  case 3:
    vQtdeMudTemp = 12;
    vTempbrew = vQtdeMudTemp;
    break;
  case 4:
    vQtdeMudTemp = 12;
    vTempbrew = vQtdeMudTemp;
    break;
  case 5:
    vQtdeMudTemp = 12;
    vTempbrew = vQtdeMudTemp;
    break;
  case 6:
    vQtdeMudTemp = 12;
    vTempbrew = vQtdeMudTemp;
    break;
  case 7:
    vQtdeMudTemp = 12;
    vTempbrew = vQtdeMudTemp;
    break;
  case 8:
    vQtdeMudTemp = 14;
    vTempbrew = vQtdeMudTemp;
    break;
  case 9:
    vQtdeMudTemp = 16;
    vTempbrew = vQtdeMudTemp;
    break;
  case 10:
    vQtdeMudTemp = 18;
    vTempbrew = vQtdeMudTemp;
    break;
  case 11:
    vQtdeMudTemp = 18;
    vTempbrew = vQtdeMudTemp;
    break;
  case 12:
    vQtdeMudTemp = 18;
    vTempbrew = vQtdeMudTemp;
    break;
  case 13:
    vQtdeMudTemp = 18;
    vTempbrew = vQtdeMudTemp;
    break;
  case 14:
    vQtdeMudTemp = 18;
    vTempbrew = vQtdeMudTemp;
    break;
  case 15:
    vQtdeMudTemp = 18;
    vTempbrew = vQtdeMudTemp;
    break;
  case 16:
    vQtdeMudTemp = 18;
    vTempbrew = vQtdeMudTemp;
    break;
  case 17:
    vQtdeMudTemp = 18;
    vTempbrew = vQtdeMudTemp;
    break;
  case 18:
    vQtdeMudTemp = 5;
    vTempbrew = vQtdeMudTemp;
    break;
  case 19:
    vQtdeMudTemp = 5;
    vTempbrew = vQtdeMudTemp;
    break;
  case 20:
    vQtdeMudTemp = 5;
    vTempbrew = vQtdeMudTemp;
    break;
  case 21:
    vQtdeMudTemp = 5;
    vTempbrew = vQtdeMudTemp;
    break;

  default:
    // if nothing else matches, do the default
    // default is optional
    break;
  }
}

void setup()
{
  // put your setup code here, to run once:
  Serial.begin(9600);

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected");

  if (!bme280.init())
  {
    Serial.println("Device error BME280...!");
  }

  //-------------------AQUI DEFINE A LUZ DE LED PARA VERIFICAR SE ESTA TUDO FUNCIONANDO
  pinMode(D4, OUTPUT); //Define o LED Onboard como saida.
  digitalWrite(D4, 1); //Apaga o LED.

  digitalWrite(D3, HIGH); //GPIO13
  pinMode(D3, OUTPUT);    // Pin connected to the Relay

  //------------------------------------------------------------------------------------
}

void loop()
{
  digitalWrite(D4, !digitalRead(D4));
  delay(200);
  uint32_t now = millis();

  //Serial.println("Inicia...");
  digitalWrite(D4, !digitalRead(D4));
  delay(200);
  //Relogio();
  sendSensor();

  vValidDayTemp();
  vPettier();
  catchHours();
  catchMinute();

  if (vIniciaProcess == 0)
  {
    digitalWrite(D4, !digitalRead(D4));
    delay(200);

    Serial.println("Inicia processo...");

    setData();

    sendSensor();
    vIniciaProcess = 1;
  }
  else
  {

    digitalWrite(D4, !digitalRead(D4));
    delay(200);

    if (vCountMsg >= 1)
    {
      digitalWrite(D4, !digitalRead(D4));
      delay(200);

      setData();

      Serial.println("Segunda processo...");
      sendSensor();
      //Relogio();

      lastCheckTime = now;
      int numNewMessages = bot.getUpdates(bot.last_message_received + 1);
      Serial.println("linha 219");

      sendTelegramMessage();

      Serial.println("linha 221");
      digitalWrite(D4, !digitalRead(D4));
      delay(200);

      Serial.println("***********************************************");
      vCountMsg = 0;
    }

    digitalWrite(D4, !digitalRead(D4));
    delay(200);

    vCountMsg++;
    Serial.println("------------------------------------------------");
    Serial.println("Contagem de msg " + String(vCountMsg));
  }

  Serial.println("Passou qtos Minutos : " + String(minuto) + " min");
  Serial.println("Passou qtos Horas : " + String(horas) + " Horas");
  Serial.println("Passou qtos Dias : " + String(dias_decorrido) + " dias");

  Serial.println(vTempbrew);

  digitalWrite(D4, !digitalRead(D4));
  delay(200);

  delay(60000);

  digitalWrite(D4, !digitalRead(D4));
  delay(100);
  digitalWrite(D4, !digitalRead(D4));
  delay(200);
}