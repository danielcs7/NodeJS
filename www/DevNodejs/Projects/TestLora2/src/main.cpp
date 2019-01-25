#include <SPI.h>
#include <LoRa.h>
#include <Wire.h>
#include <SSD1306.h>

//Deixe esta linha descomentada para compilar o Master
//Comente ou remova para compilar o Slave
//#define MASTER

#define RST 14  // GPIO14 RESET
#define DI00 26 // GPIO26 IRQ(Interrupt Request)

#define BAND 433E6 //Frequência do radio - exemplo : 433E6, 868E6, 915E6

//Constante para informar ao Slave que queremos os dados
const String GETDATA = "get";
//Constante que o Slave retorna junto com os dados para o Master
const String SETDATA = "set";

//Estrutura com os dados do sensor
typedef struct
{
  float temperature;
  float pressure;
  float humidity;
} Data;

//Variável para controlar o display
SSD1306 display(0x3c, 4, 15);
void setupDisplay()
{
  //O estado do GPIO16 é utilizado para controlar o display OLED
  pinMode(16, OUTPUT);
  //Reseta as configurações do display OLED
  digitalWrite(16, LOW);
  //Para o OLED permanecer ligado, o GPIO16 deve permanecer HIGH
  //Deve estar em HIGH antes de chamar o display.init() e fazer as demais configurações,
  //não inverta a ordem
  digitalWrite(16, HIGH);

  //Configurações do display
  display.init();
  display.flipScreenVertically();
  display.setFont(ArialMT_Plain_16);
  display.setTextAlignment(TEXT_ALIGN_LEFT);
}
//Configurações iniciais do LoRa
void setupLoRa()
{
  //Inicializa a comunicação
  SPI.begin(SCK, MISO, MOSI, SS);
  LoRa.setPins(SS, RST, DI00);

  //Inicializa o LoRa

  //Ativa o crc
  LoRa.enableCrc();
  //Ativa o recebimento de pacotes
  LoRa.receive();
}
//Compila apenas se MASTER estiver definido no arquivo principal
#ifdef MASTER

//#include <PubSubClient.h>
#include <WiFi.h>

//Substitua pelo SSID da sua rede
#define SSID "netvirtua_203"

//Substitua pela senha da sua rede
#define PASSWORD "1073847000"

//Server MQTT que iremos utlizar
#define MQTT_SERVER "quickstart.messaging.internetofthings.ibmcloud.com"

//Nome do tópico que devemos enviar os dados
//para que eles apareçam nos gráficos
#define TOPIC_NAME "iot-2/evt/status/fmt/json"

//ID que usaremos para conectar
//QUICK_START deve permanecer como está
const String QUICK_START = "d:quickstart:arduino:";

//No DEVICE_ID você deve mudar para um id único
//Aqui nesse exemplo utilizamos o MAC Address
//do dispositivo que estamos utilizando
//Servirá como identificação no site
//https://quickstart.internetofthings.ibmcloud.com
const String DEVICE_ID = "241ab91e0fa0";

//Concatemos o id do quickstart com o id do nosso
//dispositivo
const String CLIENT_ID = QUICK_START + DEVICE_ID;

//Cliente WiFi que o MQTT irá utilizar para se conectar
WiFiClient wifiClient;

//Cliente MQTT, passamos a url do server, a porta
//e o cliente WiFi
//PubSubClient client(MQTT_SERVER, 1883, wifiClient);

//Intervalo entre os envios
#define INTERVAL 500

//Tempo do último envio
long lastSendTime = 0;

//Onde ficam os dados que chegam do outro dispositivo LoRa
Data data;

void setup()
{
  Serial.begin(115200);

  //Conectamos à rede WiFi
  setupWiFi();

  //Chama a configuração inicial do display
  setupDisplay();
  //Chama a configuração inicial do LoRa
  setupLoRa();

  display.clear();
  display.drawString(0, 0, "Master");
  display.display();

  //connectMQTTServer();
}

//Função responsável por conectar ao server MQTT
/*void connectMQTTServer() {
  Serial.println("Connecting to MQTT Server...");
  //Se conecta ao id que definimos
  if (client.connect(CLIENT_ID.c_str())) {
    //Se a conexão foi bem sucedida
    Serial.println("connected");
  } else {
    //Se ocorreu algum erro
    Serial.print("error = ");
    Serial.println(client.state());
  }
}
*/
//Função responsável por conectar à rede WiFi
void setupWiFi()
{
  Serial.println();
  Serial.print("Connecting to ");
  Serial.print(SSID);

  //Manda o esp se conectar à rede através
  //do ssid e senha
  WiFi.begin(SSID, PASSWORD);

  //Espera até que a conexão com a rede seja estabelecida
  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.print(".");
  }

  //Se chegou aqui é porque conectou
  Serial.println("");
  Serial.println("WiFi connected");
}

void loop()
{
  //Se passou o tempo definido em INTERVAL desde o último envio
  if (millis() - lastSendTime > INTERVAL)
  {
    //Marcamos o tempo que ocorreu o último envio
    lastSendTime = millis();
    //Envia o pacote para informar ao Slave que queremos receber os dados
    send();
  }

  //Verificamos se há pacotes para recebermos
  receive();
}

void send()
{
  //Inicializa o pacote
  LoRa.beginPacket();
  //Envia o que está contido em "GETDATA"
  LoRa.print(GETDATA);
  //Finaliza e envia o pacote
  LoRa.endPacket();
}

void receive()
{
  //Tentamos ler o pacote
  int packetSize = LoRa.parsePacket();

  //Verificamos se o pacote tem o tamanho mínimo de caracteres que esperamos
  if (packetSize > SETDATA.length())
  {
    String received = "";
    //Armazena os dados do pacote em uma string
    for (int i = 0; i < SETDATA.length(); i++)
    {
      received += (char)LoRa.read();
    }

    //Se o cabeçalho é o que esperamos
    if (received.equals(SETDATA))
    {
      //Fazemos a leitura dos dados
      LoRa.readBytes((uint8_t *)&data, sizeof(data));
      //Mostramos os dados no display
      showData();

      Serial.print("Publish message: ");
      //Criamos o json que enviaremos para o server mqtt
      String msg = createJsonString();
      Serial.println(msg);
      //Publicamos no tópico onde o servidor espera para receber
      //e gerar o gráfico
      //client.publish(TOPIC_NAME, msg.c_str());
    }
  }
}

void showData()
{
  //Tempo que demorou para o Master criar o pacote, enviar o pacote,
  //o Slave receber, fazer a leitura, criar um novo pacote, enviá-lo
  //e o Master receber e ler
  String waiting = String(millis() - lastSendTime);
  //Mostra no display os dados e o tempo que a operação demorou
  display.clear();
  display.drawString(0, 0, String(data.temperature) + " C");
  display.drawString(0, 16, String(data.pressure) + " Pa");
  display.drawString(0, 32, String(data.humidity) + "%");
  display.drawString(0, 48, waiting + " ms");
  display.display();
}

//Função responsável por criar
//um Json com os dados lidos
String createJsonString()
{
  String json = "{";
  json += "\"d\": {";
  json += "\"temperature\":";
  json += String(data.temperature);
  json += ",";
  json += "\"humidity\":";
  json += String(data.humidity);
  json += ",";
  json += "\"pressure\":";
  json += String(data.pressure);
  json += "}";
  json += "}";
  return json;
}

#endif

//Compila apenas se MASTER não estiver definido no arquivo principal
#ifndef MASTER

//Contador que irá servir como o dados que o Slave irá enviar
int count = 0;

void setup()
{
  Serial.begin(115200);
  //Chama a configuração inicial do display
  setupDisplay();
  //Chama a configuração inicial do LoRa
  setupLoRa();
  display.clear();
  display.drawString(0, 0, "Slave esperando...");
  display.display();
}

//Função onde se faz a leitura dos dados que queira enviar
//Poderia ser o valor lido por algum sensor por exemplo
//Aqui vamos enviar apenas um contador para testes
//mas você pode alterar a função para fazer a leitura de algum sensor
String readData()
{
  return String(count++);
}

void loop()
{
  //Tenta ler o pacote
  int packetSize = LoRa.parsePacket();

  //Verifica se o pacote possui a quantidade de caracteres que esperamos
  if (packetSize == GETDATA.length())
  {
    String received = "";

    //Armazena os dados do pacote em uma string
    while (LoRa.available())
    {
      received += (char)LoRa.read();
    }

    if (received.equals(GETDATA))
    {
      //Simula a leitura dos dados
      String data = readData();
      Serial.println("Criando pacote para envio");
      //Cria o pacote para envio
      LoRa.beginPacket();
      LoRa.print(SETDATA + data);
      //Finaliza e envia o pacote
      LoRa.endPacket();
      //Mostra no display
      display.clear();
      display.drawString(0, 0, "Enviou: " + String(data));
      display.display();
    }
  }
}

#endif