/*********
  Rui Santos
  Complete project details at http://randomnerdtutorials.com  
*********/

// Load Wi-Fi library
#include <ESP8266WiFi.h>
#include <UniversalTelegramBot.h>
#include <WiFiClientSecure.h>
#include "Seeed_BME280.h"

//Intervalo entre as checagens de novas mensagens
#define INTERVAL 1000

//Token do seu bot. Troque pela que o BotFather te mostrar
#define BOT_TOKEN "799913515:AAF8EnIyd5uFfmXCV6AwyDovtvHWhvxl3cE"
#define CHAT_ID "393741829"

//Cliente para conexões seguras
WiFiClientSecure client;
//Objeto com os métodos para comunicarmos pelo Telegram
UniversalTelegramBot bot(BOT_TOKEN, client);
//Tempo em que foi feita a última checagem

//Quantidade de usuários que podem interagir com o bot
#define SENDER_ID_COUNT 2
//Ids dos usuários que podem interagir com o bot.
//É possível verificar seu id pelo monitor serial ao enviar uma mensagem para o bot
String validSenderIds[SENDER_ID_COUNT] = {"393741829"};

// Replace with your network credentials
const char *ssid = "netvirtua_203";
const char *password = "1073847000";

static String formattedDate;
static String dayStamp;
static String timeStamp;

static float humidity;
static float temperature;

int dias_decorrido = 1;        // Variavel para armazenar os dias decorridos desde o começo da incubação
byte horas = 0;                // Variavel para contar as horas desde o começo da incubação
byte minuto = 0;               // Variavel para contar os minutos desde o começo da incubação
unsigned long controleDoTempo; // Variavel utilizada para tira a difernça ente o tempo decorrido
// Fim controle de Incubação
const long interval = 60000; // interval at which to blink (milliseconds)
// The value will quickly become too large for an int to store
unsigned long previousMillis = 0; // will store last time LED was updated
long start;

unsigned long startTime = millis();
unsigned long whenToStop = startTime + 60000;

void sendTelegramMessage()
{
  String message = " ************* {BrewCode} ************* ";
  message.concat("\n");

  message.concat(" Fermentação e Maturação ");
  message.concat("\n");

  message.concat(" Inicio do Processo ");
  message.concat(dayStamp);
  message.concat("\n");

  message.concat(" Qtde de Dias: ");
  message.concat(dias_decorrido);
  message.concat("\n");
  message.concat(" TEMPERATURA: ");
  message.concat(temperature);
  message.concat("\n");

  if (bot.sendMessage(CHAT_ID, message, "Markdown"))
  {
    Serial.println("Mensagem Telegram enviada com sucesso");
  }
  //telegramSensorPressedFlag = false;
}

void Relogio()
{
  Serial.println("controleDoTempo " + controleDoTempo);

  if ((millis() - controleDoTempo) > 60000)
  { // Verivicado se ja se passaram 1 minuto
    Serial.println("Passou 1 minuto: " + String(minuto));
    minuto++; // Minuto recebe minuto mais 1
    if (minuto > 59)
    { // Verificado se ja se passaram 59 Minutos
      Serial.println("Passou 59 minuto: " + String(minuto));
      horas++;    // Horas recebe Horas mais 1
      minuto = 0; // Minutos recebe 0
      Serial.println("Horas: " + String(horas));
      if (horas >= 23)
      { // Verifica se ja se passaram 23 Horas
        Serial.println("Passou 23 Horas: " + String(horas));
        dias_decorrido++; // Dias recebe dias mais 1
        horas = 0;        // horas recebe 0
      }
    }
    Serial.println("INICIA CONTAGEM DO TEMPO...");
    controleDoTempo = millis(); // Inicia a contagem do tempo novamente
  }
}

void setWifi()
{
  int i;

  Serial.print("Connecting to ");
  Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.begin(ssid, password);
  while ((WiFi.status() != WL_CONNECTED) && i < 100)
  {
    i++;
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected.");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

void setup()
{
  Serial.begin(9600);
  // Connect to Wi-Fi network with SSID and password
  // Print local IP address and start web server
  // Print local IP address and start web server
  setWifi();
}

void loop()
{
  Serial.println("ENTROU LOOP");

  Relogio();
  sendTelegramMessage();

  delay(180000);
  Serial.println("SAIU LOOP");
}