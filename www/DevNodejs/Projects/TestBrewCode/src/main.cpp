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
// Defines
#define NTP_OFFSET -3 * 60 * 60      // Em segundos
#define NTP_INTERVAL 60 * 1000       // Em milissegundos
#define NTP_ADDRESS "0.pool.ntp.org" // Url do site de serviço NTP

int16_t utc = -3; //UTC -3:00 Brazil
uint32_t currentMillis = 0;
uint32_t previousMillis = 0;
int hh = 0, mm = 0;

WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "a.st1.ntp.br", utc * 3600, 60000);

//Intervalo entre as checagens de novas mensagens
#define INTERVAL 1000

//Token do seu bot. Troque pela que o BotFather te mostrar
#define BOT_TOKEN "799913515:AAF8EnIyd5uFfmXCV6AwyDovtvHWhvxl3cE"

#define BAND 433E6 //you can set band here directly,e.g. 868E6,915E6
#define PABOOST true
BME280 bme280;

//Pino onde está o Relê
#define RELAY_PIN 5

byte celcius[8] = {
    B11000,
    B11000,
    B00111,
    B00100,
    B00100,
    B00100,
    B00111,
    B00000};

//Comandos aceitos
const String LIGHT_ON = "ligar a luz";
const String LIGHT_OFF = "desligar a luz";
const String CLIMATE = "clima";
const String STATS = "status";
const String START = "/start";

//Estado do relê
int relayStatus = HIGH;

//Troque pelo ssid e senha da sua rede WiFi
#define SSID "netvirtua_203"
#define PASSWORD "1073847000"

#define CHAT_ID "393741829"

// Declaração de variaveis globais
int counter = 0;
static float humidity;
static float temperature;
static String formattedDate;
static String formattedTime;
static String dayStamp;
static String timeStamp;
static String vDtProcess;
static String vDtInicio;
// int myDts[] = {"2019-01-02", "2019-01-03", "2019-01-04", "2019-01-05", "2019-01-06",""};

static int vQtdeDias;
static int vQtdeMudTemp;
static int vCountDias = 0;
static int vTempbrew;

static String dtAnterior;
static String dtAtual;

String ipStr;
int count = 0;

SSD1306 display(0x3c, 4, 15); //Cria e ajusta o Objeto display

//Preencha com o token que voce recebeu por email
char auth[] = "cf7a274ff29b4a61b50dcdd28c4516b6";

//Preenche com o nome e senha da sua rede Wifi
char ssid[] = "netvirtua_203";
char pass[] = "1073847000";

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

void setupWiFi()
{
  int i;
  Serial.println();
  Serial.print("Conectando a ");
  Serial.println(ssid);

  // Inicia a conexão passando as credenciais como parametros
  WiFi.begin(SSID, PASSWORD);

  // Aguarda a conexão
  while ((WiFi.status() != WL_CONNECTED) && i < 100)
  {
    i++;
    delay(500);
    Serial.print(".");
  }

  // Se a conexão foi bem sucedida
  if (WiFi.status() == WL_CONNECTED)
  {

    IPAddress ip = WiFi.localIP(); // Converte o IP

    // Monta a string com o numero IP para indicar no display
    ipStr = String(ip[0]) + '.' + String(ip[1]) + '.' + String(ip[2]) + '.' + String(ip[3]);
    Serial.println("");
    Serial.println("WiFi Conectado.");
    Serial.println("Endereço IP: ");
    Serial.println(WiFi.localIP());
    Serial.print("Máscara de rede: ");
    Serial.println(WiFi.subnetMask());
    Serial.print("Gateway: ");
    Serial.println(WiFi.gatewayIP());
  }
  else
  { // Se não conectou
    Serial.println("");
    Serial.println("WiFi Não conectado.");
  }
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
  display.setFont(ArialMT_Plain_24);
  display.setTextAlignment(TEXT_ALIGN_LEFT);
  display.flipScreenVertically(); //inverte verticalmente a tela (opcional)

  Serial.begin(9600);

  delay(3000);

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

  delay(3000);
  //Inicializa o WiFi e se conecta à rede
  setupWiFi();
  timeClient.begin(); // Inicia o serviço NTP
  // Setup a function to be called every second
}

void setData()
{

  delay(3000);
  Serial.println("configurando data");

  formattedTime = timeClient.getFormattedTime();
  Serial.print(formattedTime);

  formattedDate = timeClient.getFormattedDate();
  Serial.println(formattedDate);

  // Extract date
  int splitT = formattedDate.indexOf("T");
  dayStamp = formattedDate.substring(0, splitT);
  Serial.print("DATE: ");
  Serial.println(dayStamp);

  if (vCountDias == 0)
  {
    vDtInicio = String(dayStamp);
    Serial.println("passei aqui 1");
    Serial.println("DATA INICIO ::: " + vDtInicio);
  }
}

void vTempProcess2(String dt)
{

  Serial.println("Data <=> " + dt);

  Serial.println("Data Atual <=> " + dt);

  Serial.println("Data Inicio <=> " + vDtInicio);

  Serial.println("Data Final  <=> " + vDtInicio);

  Serial.println("Data Anterior <=> " + vDtInicio);

  Serial.println("**********************************************");

  dt = dtAtual;
  String vDia = dt.substring(8, 10);
  Serial.println("DIA <=> " + vDia);

  if (dayStamp == dtAtual)
  {
  }
  else
  {
    dtAnterior = dtAtual;
    vCountDias = vCountDias + 1;
  }
}

void loop()
{

  temperature = bme280.getTemperature();
  humidity = bme280.getHumidity();

  Serial.print("Temp: ");
  Serial.print(bme280.getTemperature());
  Serial.println("C");

  //get and print humidity data
  Serial.print("Humidity: ");
  Serial.print(bme280.getHumidity());
  Serial.println("%");

  String vTemp = "℃";

  Serial.print("vTempProcess2: " + String(dayStamp));

  //display.drawString(20, 0, "Temperatura");
  //display.drawString(20, 26, String(temperature)+" C");
  setData();

  vTempProcess2(String(dayStamp));

  // while (vCountDias <= 21)
  //{
  // do something repetitive 200 times
  switch (vCountDias)
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

  timeClient.update(); // Atualiza leitura de hora da rede

  //delay(180000); // wait for a second
  delay(5000);
}