#include <Arduino.h>

//Programa: Modulo Max7219 FC-16 com ESP8266 NodeMCU
//Autor: Arduino e Cia

#include <FC16.h>

//Conexao do pino CS
const int csPin = D4;
//Numero de displays que estamos usando
const int numDisp = 4;
//Tempo do scroll em milisegundos
const int scrollDelay = 250;

FC16 display = FC16(csPin, numDisp);

void writeDisplay()
{

  //Apaga o display
  display.clearDisplay();
  //Texto a ser exibido no display
  display.setText("\x10 10 \x11");
}

void setup()
{
  //Inicializa o display
  display.begin();
  //Intensidade / Brilho
  display.setIntensity(8);
  //Apaga o display
  //display.clearDisplay();

  //writeDisplay();
}

void loop()
{

  writeDisplay();
  //Chama a rotina de scroll
  //display.update();

  //Aguarda o tempo definid

  delay(120000);
}