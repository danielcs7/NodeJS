#include <Arduino.h>
#include <ESP8266WiFi.h>
#include <WiFiClientSecure.h>
#include <UniversalTelegramBot.h>
#include "Seeed_BME280.h"

// Initialize Wifi connection to the router
#define WIFI_SSID "netvirtua_203"
#define WIFI_PASSWORD "1073847000"
#define BOTtoken "711600176:AAFFnnroNszy8pQdLv36YzRMiZp8hKGbALo" // Seu token do telegram

#define RELAY_PIN D6

#define BOT_SCAN_MESSAGE_INTERVAL 1000 //Intervalo para obter novas mensagens
long lastTimeScan;                     // Ultima vez que buscou mensagem
bool ledStatus = false;                // Estado do LED
bool relayStatus = false;              // Estado do Relê
static float humidity;
static float temperature;

WiFiClientSecure client;
UniversalTelegramBot bot(BOTtoken, client);
#define PABOOST true
BME280 bme280;

// Trata as novas mensagens que chegaram
void handleNewMessages(int numNewMessages)
{
  // Serial.println("handleNewMessages");
  // Serial.println(String(numNewMessages));

  for (int i = 0; i < numNewMessages; i++)
  {
    String chat_id = String(bot.messages[i].chat_id);
    String text = bot.messages[i].text;

    // Pessoa que está enviando a mensagem
    String from_name = bot.messages[i].from_name;
    if (from_name == "")
      from_name = "Convidado";

    // Tratamento para cada tipo de comando a seguir.

    if (text == "/ledon")
    {
      //digitalWrite(LED_PIN, HIGH);   // turn the LED on (HIGH is the voltage level)
      ledStatus = true;
      bot.sendMessage(chat_id, "Led está ligado", "");
    }

    if (text == "/ledoff")
    {
      ledStatus = false;
      //digitalWrite(LED_PIN, LOW);    // turn the LED off (LOW is the voltage level)
      bot.sendMessage(chat_id, "Led está desligado", "");
    }

    if (text == "/relayon")
    {
      // digitalWrite(RELAY_PIN, HIGH);
      relayStatus = true;
      bot.sendMessage(chat_id, "Relê está ligado", "");
    }

    if (text == "/relayoff")
    {
      relayStatus = false;
      //digitalWrite(RELAY_PIN, LOW);
      bot.sendMessage(chat_id, "Relê está desligado", "");
    }

    if (text == "/status")
    {
      String message = "Led está ";
      if (ledStatus)
      {
        message += "ligado";
      }
      else
      {
        message += "desligado";
      }
      message += ". \n";
      message += "Relê está ";
      if (relayStatus)
      {
        message += "ligado";
      }
      else
      {
        message += "desligado";
      }
      message += ". \n";
      bot.sendMessage(chat_id, message, "Markdown");
    }

    if (text == "/env")
    {
      temperature = bme280.getTemperature();
      humidity = bme280.getHumidity();

      String message = "A temperatura é de " + String(temperature, 2) + " graus celsius.\n";
      message += "A umidade relativa do ar é de " + String(humidity, 2) + "%.\n\n";
      bot.sendMessage(chat_id, message, "Markdown");
    }

    // Cria teclado com as opções de comando
    if (text == "/options")
    {
      String keyboardJson = "[[\"/ledon\", \"/ledoff\"],[\"/relayon\", \"/relayoff\"],[\"/env\",\"/status\"],[\"/options\"]]";
      bot.sendMessageWithReplyKeyboard(chat_id, "Escolha uma das opções", "", keyboardJson, true);
    }

    // Comando de inicio de conversa no telegram
    if (text == "/start")
    {
      String welcome = from_name + ", bem vindo ao Bot da Casa Viebrantz.\n";
      welcome += "Para interagir com a casa, use um dos comandos a seguir.\n\n";
      welcome += "/ledon : para ligar o Led \n";
      welcome += "/ledoff : para desligar o Led \n";
      welcome += "/relayon : para ligar o Relê \n";
      welcome += "/relayoff : para desligar o Relê \n";
      welcome += "/env : saber a temperatura e umidade do ambiente \n";
      welcome += "/status : para saber o status dos sensores e atuadores \n";
      bot.sendMessage(chat_id, welcome, "Markdown");
    }
  }
}

void setupWifi()
{

  // attempt to connect to Wifi network:
  // Serial.print("Connecting Wifi: ");
  // Serial.println(ssid);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);

  while (WiFi.status() != WL_CONNECTED)
  {
    // Serial.print(".");
    delay(500);
  }

  // Serial.println("");
  // Serial.println("WiFi connected");
  // Serial.print("IP address: ");
  // Serial.println(WiFi.localIP());
}

void setupPins()
{
  //pinMode(LED_PIN, OUTPUT);
  pinMode(RELAY_PIN, OUTPUT);
  delay(10);
  //digitalWrite(LED_PIN, LOW);
  digitalWrite(RELAY_PIN, LOW);
}

void setup()
{
  // Serial.begin(115200);

  setupWifi();
  setupPins();

  lastTimeScan = millis();

  if (!bme280.init())
  {
    Serial.println("Device error BME280...!");
  }
}

void loop()
{
  if (millis() > lastTimeScan + BOT_SCAN_MESSAGE_INTERVAL)
  {
    // Serial.print("Checking messages - ");
    int numNewMessages = bot.getUpdates(bot.last_message_received + 1);
    // Serial.println(numNewMessages);
    while (numNewMessages)
    {
      // Serial.println("got response");
      handleNewMessages(numNewMessages);
      numNewMessages = bot.getUpdates(bot.last_message_received + 1);
    }

    lastTimeScan = millis();
  }
  yield();
  delay(10);
}
