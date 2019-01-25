//Envia mensagens de debug para o Serial Monitor
#define BLYNK_PRINT Serial

//Carrega as bibliotecas do ESP32 e Blynk
#include <WiFi.h>
#include <WiFiClient.h>
#include <BlynkSimpleEsp32.h>
#include <SPI.h>
#include <LoRa.h>
#include <Wire.h>
#include <SSD1306.h>
#include "Seeed_BME280.h"

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

#define BAND 433E6 //you can set band here directly,e.g. 868E6,915E6
#define PABOOST true
BME280 bme280;

int counter = 0;
static float humidity;
static float temperature;
BlynkTimer timer;

SSD1306 display(0x3c, 4, 15); //Cria e ajusta o Objeto display

//Preencha com o token que voce recebeu por email
char auth[] = "cf7a274ff29b4a61b50dcdd28c4516b6";

//Preenche com o nome e senha da sua rede Wifi
char ssid[] = "netvirtua_203";
char pass[] = "1073847000";

void sendSensor()
{

  temperature = bme280.getTemperature();
  humidity = bme280.getHumidity();

  if (isnan(humidity || isnan(temperature)))
  {
    Serial.println("Failed to read from DHT sensor!");
    return;
  }
  // You can send any value at any time.
  // Please don't send more that 10 values per second.
  Blynk.virtualWrite(V5, humidity);
  Blynk.virtualWrite(V6, temperature);
}

void setup()
{
  //O estado do GPIO16 é utilizado para controlar o display OLED
  Blynk.begin(auth, ssid, pass);
  pinMode(16, OUTPUT);
  //Reseta as configurações do display OLED
  digitalWrite(16, LOW);
  //Para o OLED permanecer ligado, o GPIO16 deve permanecer HIGH
  //Deve estar em HIGH antes de chamar o display.init() e fazer as demais configurações,
  //não inverta a ordem
  digitalWrite(16, HIGH);

  display.init();
  display.flipScreenVertically();
  display.setFont(ArialMT_Plain_24);
  display.setTextAlignment(TEXT_ALIGN_LEFT);
  display.flipScreenVertically(); //inverte verticalmente a tela (opcional)

  Serial.begin(115200);
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

  // Setup a function to be called every second
  timer.setInterval(1000L, sendSensor);
}

void loop()
{
  //apaga o conteúdo do display
  Blynk.run();
  timer.run();
  temperature = bme280.getTemperature();
  humidity = bme280.getHumidity();

  String vTemp = "℃";

  display.clear();
  display.setTextAlignment(TEXT_ALIGN_CENTER);
  display.setFont(ArialMT_Plain_24);

  //display.drawString(20, 0, "Temperatura");
  //display.drawString(20, 26, String(temperature)+" C");

  if (temperature >= (float)29.45 || temperature <= (float)29.40)
  {
    display.setTextAlignment(TEXT_ALIGN_CENTER);
    display.setFont(ArialMT_Plain_16);
    display.drawString(64, 0, "Correcao Temp...");
    display.drawString(64, 22, "ligado...");
    display.drawString(64, 40, String(temperature) + " C");
  }
  else
  {

    display.drawString(64, 22, String(temperature) + " C");
  }

  display.display(); //mostra o conteúdo na tela

  delay(1000); // wait for a second
}