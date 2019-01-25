//Envia mensagens de debug para o Serial Monitor
#define BLYNK_PRINT Serial

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

static int vQtdeDias;
static int vQtdeMudTemp;
static int vCountDias = 1;
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

void sendTelegramMessage()
{
  String message = "Temperatura Fermentação";
  message.concat("\n");

  message.concat("Data inicio do Processo ");
  message.concat(dayStamp);
  message.concat("\n");

  message.concat("Qtde de Dias: ");
  message.concat(vCountDias);
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

void setupWiFi()
{
  int i;
  Serial.println();
  Serial.print("Conectando a ");
  Serial.println(ssid);

  display.clear();
  display.setTextAlignment(TEXT_ALIGN_CENTER);
  display.setFont(ArialMT_Plain_16);

  display.drawString(54, 10, "Conectando a: ");
  display.drawString(57, 25, String(ssid));
  display.display();

  // Inicia a conexão passando as credenciais como parametros
  WiFi.begin(SSID, PASSWORD);

  // Aguarda a conexão
  while ((WiFi.status() != WL_CONNECTED) && i < 100)
  {
    i++;
    delay(500);
    Serial.print(".");
    display.drawString((3 + i * 2), 35, ".");
    display.display();
  }

  // Se a conexão foi bem sucedida
  if (WiFi.status() == WL_CONNECTED)
  {
    display.clear();
    display.setFont(ArialMT_Plain_16);
    display.drawString(57, 25, "Conectado!!");

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
    display.display();
  }
  else
  { // Se não conectou
    display.clear();
    display.setFont(ArialMT_Plain_10);
    display.drawString(3, 16, "WiFi nao conectado");
    ipStr = "NAO CONECTADO";
    Serial.println("");
    Serial.println("WiFi Não conectado.");
    display.display();
  }
  delay(1000);
}

String getClimateMessage()
{
  //Faz a leitura da temperatura e da umidade
  temperature = bme280.getTemperature();
  humidity = bme280.getHumidity();

  //Retorna uma string com os valores
  String message = "";
  message += "A temperatura é de " + String(temperature) + " °C e ";
  message += "a umidade é de " + String(humidity) + "%";
  return message;

  //Se não foi bem sucedido retorna um mensagem de erro
  // return "Erro ao ler temperatura e umidade";
}

boolean validateSender(String senderId)
{
  //Para cada id de usuário que pode interagir com este bot
  for (int i = 0; i < SENDER_ID_COUNT; i++)
  {
    //Se o id do remetente faz parte do array retornamos que é válido
    if (senderId == validSenderIds[i])
    {
      return true;
    }
  }
}

String getCommands()
{
  //String com a lista de mensagens que são válidas e explicação sobre o que faz
  String message = "Os comandos disponíveis são:\n\n";
  message += "<b>" + LIGHT_ON + "</b>: Para ligar a luz\n";
  message += "<b>" + LIGHT_OFF + "</b>: Para desligar a luz\n";
  message += "<b>" + CLIMATE + "</b>: Para verificar o clima\n";
  message += "<b>" + STATS + "</b>: Para verificar o estado da luz e a temperatura";
  return message;
}

void handleStart(String chatId, String fromName)
{
  //Mostra Olá e o nome do contato seguido das mensagens válidas
  String message = "<b>Olá " + fromName + ".</b>\n";
  message += getCommands();
  bot.sendMessage(chatId, message, "HTML");
}

void handleLightOn(String chatId)
{
  //Liga o relê e envia mensagem confirmando a operação
  relayStatus = LOW; //A lógica do nosso relê é invertida
  digitalWrite(RELAY_PIN, relayStatus);
  bot.sendMessage(chatId, "A luz está <b>acesa</b>", "HTML");
}

void handleLightOff(String chatId)
{
  //Desliga o relê e envia mensagem confirmando a operação
  relayStatus = HIGH; //A lógica do nosso relê é invertida
  digitalWrite(RELAY_PIN, relayStatus);
  bot.sendMessage(chatId, "A luz está <b>apagada</b>", "HTML");
}

void handleClimate(String chatId)
{
  //Envia mensagem com o valor da temperatura e da umidade
  bot.sendMessage(chatId, getClimateMessage(), "");
}

void handleStatus(String chatId)
{
  String message = "";

  //Verifica se o relê está ligado ou desligado e gera a mensagem de acordo
  if (relayStatus == LOW) //A lógica do nosso relê é invertida
  {
    message += "A luz está acesa\n";
  }
  else
  {
    message += "A luz está apagada\n";
  }

  //Adiciona à mensagem o valor da temperatura e umidade
  message += getClimateMessage();

  //Envia a mensagem para o contato
  bot.sendMessage(chatId, message, "");
}

void handleNotFound(String chatId)
{
  //Envia mensagem dizendo que o comando não foi encontrado e mostra opções de comando válidos
  String message = "Comando não encontrado\n";
  message += getCommands();
  bot.sendMessage(chatId, message, "HTML");
}

void handleNewMessages(int numNewMessages)
{
  for (int i = 0; i < numNewMessages; i++) //para cada mensagem nova
  {
    String chatId = String(bot.messages[i].chat_id);   //id do chat
    String senderId = String(bot.messages[i].from_id); //id do contato

    Serial.println("senderId: " + senderId); //mostra no monitor serial o id de quem mandou a mensagem

    boolean validSender = validateSender(senderId); //verifica se é o id de um remetente da lista de remetentes válidos

    if (!validSender) //se não for um remetente válido
    {
      bot.sendMessage(chatId, "Desculpe mas você não tem permissão", "HTML"); //envia mensagem que não possui permissão e retorna sem fazer mais nada
      continue;                                                               //continua para a próxima iteração do for (vai para próxima mensgem, não executa o código abaixo)
    }

    String text = bot.messages[i].text; //texto que chegou

    if (text.equalsIgnoreCase(START))
    {
      handleStart(chatId, bot.messages[i].from_name); //mostra as opções
    }
    else if (text.equalsIgnoreCase(LIGHT_ON))
    {
      handleLightOn(chatId); //liga o relê
    }
    else if (text.equalsIgnoreCase(LIGHT_OFF))
    {
      handleLightOff(chatId); //desliga o relê
    }
    else if (text.equalsIgnoreCase(CLIMATE))
    {
      handleClimate(chatId); //envia mensagem com a temperatura e umidade
    }
    else if (text.equalsIgnoreCase(STATS))
    {
      handleStatus(chatId); //envia mensagem com o estado do relê, temperatura e umidade
    }
    else
    {
      handleNotFound(chatId); //mostra mensagem que a opção não é válida e mostra as opções
    }
  } //for
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

void vTempProcess(String dt)
{

  Serial.println("Data <=> " + dt);

  Serial.println("Data Atual <=> " + dt);

  Serial.println("Data Anterior <=> " + dt);

  Serial.println("**********************************************");

  dt = dtAtual;

  if (dtAnterior == dtAtual)
  {
  }
  else
  {
    dtAnterior = dtAtual;
    vCountDias = vCountDias + 1;
  }
}

/**************************************************************************
  função que envia os dados para o display
**************************************************************************/
void displayData()
{
  formattedTime = timeClient.getFormattedTime();
  Serial.print(formattedTime);

  formattedDate = timeClient.getFormattedDate();
  Serial.println(formattedDate);

  // Extract date
  int splitT = formattedDate.indexOf("T");
  dayStamp = formattedDate.substring(0, splitT);
  Serial.print("DATE: ");
  Serial.println(dayStamp);

  Serial.print("  Temp: => ");
  Serial.print(temperature);
  Serial.print("  Hum => ");
  Serial.println(humidity);
  display.clear(); // Apaga o display
  display.setTextAlignment(TEXT_ALIGN_CENTER);
  display.setFont(ArialMT_Plain_16);
  display.drawString(58, 0, String(dayStamp));
  display.drawString(58, 16, String(formattedTime) + " h");

  timeClient.update();

  while (!timeClient.update())
  {
    timeClient.forceUpdate();
  }

  display.setTextAlignment(TEXT_ALIGN_CENTER);
  display.setFont(ArialMT_Plain_24);
  //display.drawString(52, 18, String(dayStamp) + ' ' + String(formattedTime));

  display.drawString(58, 33, String(temperature) + " C");

  //display.setFont(ArialMT_Plain_10);
  //display.drawString(46, 50, "IP: " + ipStr);

  display.display(); // Envia o buffer de dados para o display
  delay(10);
}

void showNewData()
{
  if (newData == true)
  {
    Serial.print("This just in ... ");
    Serial.println(receivedChars);
    Serial.println(receivedChars[ndx]);
    newData = false;
  }
}

void loop()
{

  //Tempo agora desde o boot
  uint32_t now = millis();

  //apaga o conteúdo do display

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

  display.clear();
  display.setTextAlignment(TEXT_ALIGN_CENTER);
  display.setFont(ArialMT_Plain_24);

  //display.drawString(20, 0, "Temperatura");
  //display.drawString(20, 26, String(temperature)+" C");

  vTempProcess(String(dayStamp));

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

  if (temperature >= (float)29.45 || temperature <= (float)29.40)
  {
    display.setTextAlignment(TEXT_ALIGN_CENTER);
    display.setFont(ArialMT_Plain_16);
    display.drawString(64, 0, "Correcao Temp...");
    display.drawString(64, 22, "ligado...");
    display.drawString(64, 40, String(temperature) + " C");
    lastCheckTime = now;
    int numNewMessages = bot.getUpdates(bot.last_message_received + 1);
    handleNewMessages(numNewMessages);
    sendTelegramMessage();
  }
  else
  {

    display.drawString(64, 22, String(temperature) + " C");
  }

  timeClient.update(); // Atualiza leitura de hora da rede
  displayData();       // indica no display

  if (count == 10)
  {
    sendSensor();
    displayData();
    count = 0;
  }
  //display.display(); //mostra o conteúdo na tela

  delay(180000); // wait for a second
  //delay(5000); // wait for a second
  //}
}