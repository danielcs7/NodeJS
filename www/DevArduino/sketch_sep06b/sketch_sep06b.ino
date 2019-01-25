
#include <RF24.h>

#define radioID 0

RF24 radio(D3,D8);


byte enderecos[][6] = {"1node","2node"};

int dadoDeEnvio, dadosRecebidos;

void setup() {
  Serial.begin(115200);
   pinMode(D4, OUTPUT);//Define o LED Onboard como saida.
   digitalWrite(D4, 1);//Apaga o LED.
  //radio.begin();
   #if radioId == 0
    Serial.print("RADIO-ID <=> 0");
  radio.openWritingPipe(enderecos[0]);
  radio.openReadingPipe(1,enderecos[1]);
  #else
  Serial.print("RADIO-ID <=> 1");
  //quando for o outro deve ser desse jeito
  radio.openWritingPipe(enderecos[1]);
  radio.openReadingPipe(1,enderecos[0]);*/
  #endif
  Serial.print("\n");
  //comando para iniciar a ouvir
  radio.startListening();
  radio.printDetails();
  
}

void loop() {
  // put your main code here, to run repeatedly:
   digitalWrite(D4, !digitalRead(D4));
   Serial.println("loop");
   dadoDeEnvio = 255;
   Serial.println("37");
    //comando para parar de ouvir
   radio.stopListening();
   Serial.println("40");
   radio.write( &dadoDeEnvio, sizeof(int));
    Serial.println("42");
   radio.startListening();
   Serial.println("44");
   if(radio.available()){
    Serial.print("Recebidos: ");
    radio.read(&dadosRecebidos, sizeof(int));

    Serial.println(dadosRecebidos);
    
   }

   delay(3000);
   
}
