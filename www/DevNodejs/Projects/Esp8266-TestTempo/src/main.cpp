#include <Arduino.h>

#include <ESP8266WiFi.h>

const char *ssid = "netvirtua_203";
const char *password = "1073847000";

static String formattedDate;
static String dayStamp;
String timeStamp;
static float humidity;
static float temperature;
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

void setup()
{
  // put your setup c
  Serial.begin(9600);

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected");

  //-------------------AQUI DEFINE A LUZ DE LED PARA VERIFICAR SE ESTA TUDO FUNCIONANDO
  pinMode(D4, OUTPUT); //Define o LED Onboard como saida.
  digitalWrite(D4, 1); //Apaga o LED.
}

void Relogio()
{

  if (vIniciaProcess == 0)
  {
    minuto++;
  }

  if ((millis() - controleDoTempo) > 60000)
  { // Verivicado se ja se passaram 1 minuto

    Serial.println("Passou : " + String(minuto) + " min");

    minuto++; // Minuto recebe minuto mais 1

    if (minuto > 59)
    { // Verificado se ja se passaram 59 Minutos
      Serial.println("Passou : " + String(minuto) + " min");
      horas++;    // Horas recebe Horas mais 1
      minuto = 0; // Minutos recebe 0
      if (horas >= 1)
      { // Verifica se ja se passaram 23 Horas

        dias_decorrido = dias_decorrido + 1; // Dias recebe dias mais 1

        Serial.println("Quantidade Dias :" + String(dias_decorrido));

        Serial.println("Passou hors :" + String(horas) + " hrs");
        Serial.println("Quantidade Dias :" + String(dias_decorrido));
        //horas = 0;        // horas recebe 0
      }
    }
    //controleDoTempo = millis(); // Inicia a contagem do tempo novamente
  }
}

void loop()
{
  // put your main code here, to run repeatedly:
  uint32_t now = millis();

  //Serial.println("Inicia...");

  if (vIniciaProcess == 0)
  {
    vIniciaProcess = 1;
  }
  else
  {

    Relogio();
  }

  delay(1000);
}