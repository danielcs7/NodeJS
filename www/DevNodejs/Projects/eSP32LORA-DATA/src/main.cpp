/*********
  Rui Santos
  Complete project details at https://randomnerdtutorials.com
  Based on the NTP Client library example
*********/
#include <Arduino.h>
//Carrega as bibliotecas do ESP32 e Blynk
#include <WiFi.h>
#include <WiFiClient.h>
#include <SPI.h>
#include <LoRa.h>
#include <Wire.h>
#include <SSD1306.h>
#include <WiFiClientSecure.h>
#include <UniversalTelegramBot.h>
#include "Seeed_BME280.h"
#include <NTPClient.h>

// Pin definetion of WIFI LoRa 32
// HelTec AutoMation 2017 support@heltec.cn
#define SCK 5   // GPIO5  -- SX127x's SCK
#define MISO 19 // GPIO19 -- SX127x's MISO
#define MOSI 27 // GPIO27 -- SX127x's MOSI
#define SS 18   // GPIO18 -- SX127x's CS
#define RST 14  // GPIO14 -- SX127x's RESET
#define DI0 26  // GPIO26 -- SX127x's IRQ(Interrupt Request)
#define posX 21
#define posY 0
#define intervalo 500

BME280 bme280;

// Replace with your network credentials
const char *ssid = "netvirtua_203";
const char *password = "1073847000";

int16_t utc = -3; //UTC -3:00 Brazil

// Define NTP Client to get time
//WiFiUDP ntpUDP;
//NTPClient timeClient(ntpUDP);
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "a.st1.ntp.br", utc * 3600, 60000);

// Variables to save date and time
String formattedDate;
String formattedTime;

static String dayStamp;
static String vDtAnterior;
static String vDtAtual;
String timeStamp;
int vCountprocess = 0;
int vCountBrewCode = 0;

static float humidity;
static float temperature;
String ipStr;

SSD1306 display(0x3c, 4, 15); //Cria e ajusta o Objeto display

void sendSensor()
{

  temperature = bme280.getTemperature();
  humidity = bme280.getHumidity();

  if (isnan(humidity || isnan(temperature)))
  {
    Serial.println("Failed to read from DHT sensor!");
    return;
  }
}

/**************************************************************************
  função que envia os dados para o display
**************************************************************************/
/*void displayData()
{
  formattedTime = timeClient.getFormattedTime();
  Serial.print(formattedTime);

  Serial.print("  Temp: => ");
  Serial.print(temperature);
  Serial.print("  Hum => ");
  Serial.println(humidity);
  display.clear(); // Apaga o display
  display.setFont(ArialMT_Plain_10);
  display.drawString(3, 0, "T:");
  display.drawString(14, 0, String(temperature));
  display.drawString(44, 0, "oC");
  display.drawString(75, 0, "H:");
  display.drawString(87, 0, String(humidity));
  display.drawString(117, 0, "%");

  timeClient.update();

  while (!timeClient.update())
  {
    timeClient.forceUpdate();
  }

  //formattedDate = timeClient.getFormattedDate();
  //SSerial.println(formattedDate);

  // Extract date
  int splitT = formattedDate.indexOf("T");
  dayStamp = formattedDate.substring(0, splitT);
  Serial.print("DATE: ");
  Serial.println(dayStamp);

  display.setTextAlignment(TEXT_ALIGN_CENTER);
  display.setFont(ArialMT_Plain_24);
  display.drawString(45, 18, String(dayStamp));

  display.setFont(ArialMT_Plain_10);
  display.drawString(46, 50, "IP: " + ipStr);

  display.display(); // Envia o buffer de dados para o display
  delay(10);
}
*/
void setDate()
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

  formattedTime = timeClient.getFormattedTime();
  Serial.print(formattedTime);

  // Extract date
  int splitT = formattedDate.indexOf("T");
  dayStamp = formattedDate.substring(0, splitT);
  Serial.print("DATE: ");
  Serial.println(dayStamp);
  // Extract time
  //timeStamp = formattedDate.substring(splitT + 1, formattedDate.length() - 1);
  Serial.print("HOUR: ");
  Serial.println(formattedTime);
  delay(1000);
}

void setup()
{

  //O estado do GPIO16 é utilizado para controlar o display OLED

  pinMode(16, OUTPUT);
  //Reseta as configurações do display OLED
  digitalWrite(16, LOW);
  //Para o OLED permanecer ligado, o GPIO16 deve permanecer HIGH
  //Deve estar em HIGH antes de chamar o display.init() e fazer as demais configurações,
  //não inverta a ordem
  digitalWrite(16, HIGH);

  display.init();
  display.flipScreenVertically();
  display.setTextAlignment(TEXT_ALIGN_LEFT);
  display.setFont(ArialMT_Plain_16);
  display.flipScreenVertically(); //inverte verticalmente a tela (opcional)

  // Initialize Serial Monitor
  Serial.begin(9600);
  Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.print(".");
  }
  // Print local IP address and start web server
  Serial.println("");
  Serial.println("WiFi connected.");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());

  while (!Serial)
    ;
  Serial.println("LoRa Sender");

  SPI.begin(SCK, MISO, MOSI, SS);
  LoRa.setPins(SS, RST, DI0);
  if (!LoRa.begin(433E6))
  {
    Serial.println("Starting LoRa failed!");
    while (1)
      ;
  }

  if (!bme280.init())
  {
    Serial.println("Device error BME280...!");
  }

  // Initialize a NTPClient to get time
  timeClient.begin();
  // Set offset time in seconds to adjust for your timezone, for example:
  // GMT +1 = 3600
  // GMT +8 = 28800
  // GMT -1 = -3600
  // GMT 0 = 0
  timeClient.setTimeOffset(3600);
}

void vWriteDt()
{

  vDtAtual = dayStamp;

  display.setTextAlignment(TEXT_ALIGN_CENTER);
  display.setFont(ArialMT_Plain_16);
  display.drawString(64, 0, "Data : " + String(vDtAtual));

  Serial.println("DATA ATUAL <=> " + vDtAtual);
  if (vDtAtual == vDtAnterior)
  {
    vCountBrewCode = 1;
    Serial.println("DATA IGUAL... ");
    display.drawString(64, 22, "data igual...");
  }
  else
  {
    Serial.println("DATA DIFERENTE <=> " + vDtAtual);
    vDtAnterior = vDtAtual;
    vCountBrewCode = vCountBrewCode + 1;
    display.drawString(64, 40, "Qtde :" + String(vCountBrewCode));

    Serial.println("QTDE PROCESS <=> " + vCountBrewCode);
  }
  display.display();
  delay(1000);
}

void vProcess()
{

  if (vCountprocess == 0)
  {

    vDtAnterior = vDtAtual;
  }
  else
  {

    vCountprocess = 1;
  }
}

void loop()
{

  Serial.println("Loop...");
  vProcess();
  delay(1000);
  Serial.println("Setando a data!!");
  setDate();
  Serial.println("Data correta!!");
  delay(1000);
  Serial.println("*************************************");
  setDate();
  vWriteDt();
  //displayData(); // indica no display

  delay(180000);
}