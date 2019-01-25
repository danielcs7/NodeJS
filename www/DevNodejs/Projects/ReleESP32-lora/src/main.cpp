#include <Arduino.h>

//Pino ligado no botão
int btn1 = 34;
//Pinos que são ligados no módulo de relés
int rele1 = 22;
int rele2 = 23;
//variável para guardar o estado do segundo relé
boolean rele2_Ativo = false;

//A função setup() é executada uma vez antes do loop
void setup()
{
  //Seta os pinos dos botões como entrada
  pinMode(btn1, INPUT);
  //Seta os pinos dos relés como saída
  pinMode(rele1, OUTPUT);
  pinMode(rele2, OUTPUT);
  //Abre a porta serial, definindo a taxa de dados para 9600 bps
  Serial.begin(9600);
}

//A função loop() é executada após o setup e é repetida continuamente
void loop()
{
  Serial.println("Ligamos o relé 1");
  digitalWrite(rele1, HIGH);
  delay(10000);
  //Desligamos o relé 1
  digitalWrite(rele1, LOW);
  delay(10000);
}