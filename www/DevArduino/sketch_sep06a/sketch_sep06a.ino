
#include <RF24.h>

#define radioID 1

RF24 radio(D3,D4);


byte enderecos[][6] = {"1node","2node"};

int dadoDeEnvio, dadosRecebidos;

void setup() {
  radio.printDetails();
  
}

void loop() {
  
}
