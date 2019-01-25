// Programa : NRF24L01 Receptor - Servo motor
//NRF24L01Receptor - Servo motor
// Autor : Arduino e Cia

#include <SPI.h>
#include "nRF24L01.h"
#include "RF24.h"
#include <Servo.h> 

// Armazena os dados recebidos
int recebidos[1];

// Inicializa o NRF24L01 nos pinos 9 (CE) e 53 (CS) do Arduino Mega
RF24 radio(D4,D8);

Servo myservo;

// Define o endereco para comunicacao entre os modulos
const uint64_t pipe = 0xE13CBAF433LL;

// Define o pino do led
int LED1 = 5;

void setup()
{
  // Servo conectado ao pino 4
  myservo.attach(4);
  // Define o pino do led como saida
  pinMode(LED1, OUTPUT);
  // Inicializa a serial
  Serial.begin(115200);
  // Inicializa a comunicacao do NRF24L01
  radio.begin();
  // Entra em modo de recepcao
  radio.openReadingPipe(1,pipe);
  radio.startListening();
  // Mensagem inicial
  Serial.println("Aguardando dados...");
}

void loop()
{
  // Verifica sinal de radio
  if (radio.available())
  {
    bool done = false;    
    while (!done)
    {
      //done = 
      radio.read(recebidos, 1);
      Serial.println("Recebido : ");    
      Serial.println(recebidos[0]);
      
      // Se recebeu 1, movimenta o servo para a esquerda
      if (recebidos[0] == 1)
      {
        delay(10);
        Serial.println(" -> Girando motor para a esquerda");
        myservo.write(1);
      }
      
      // Se recebeu 2, movimenta o servo para a direita
      if (recebidos[0] == 2)
      {
        delay(10);
        Serial.println(" -> Girando motor para a direita");
        myservo.write(160);
      }

      // Se recebeu 3, acende o led
      if (recebidos[0] == 3)
      {
        delay(10);
        Serial.println(" -> Acende led");
        digitalWrite(LED1, HIGH);
      }
      else
      {
        digitalWrite(LED1, LOW);
      }
      delay(8000);
    }
  }
}
