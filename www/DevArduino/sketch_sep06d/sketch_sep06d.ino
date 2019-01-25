#include <RF24.h>
#include <nRF24L01.h>
#include <RF24_config.h>
#define radioID 1
#include <LiquidCrystal_I2C.h>

RF24 radio(D3,D8);


byte enderecos[][6] = {"1node","2node"};

int dadoDeEnvio, dadosRecebidos;
// set the LCD number of columns and rows
int lcdColumns = 16;
int lcdRows = 2;

// if you don't know your display address, run an I2C scanner sketch
LiquidCrystal_I2C lcd(0x27, lcdColumns, lcdRows);  

void setup() {
  Serial.begin(115200);
   lcd.init();
  // turn on LCD backlight                      
  lcd.backlight();
   pinMode(D4, OUTPUT);//Define o LED Onboard como saida.
   digitalWrite(D4, 1);//Apaga o LED.
   //comando para iniciar a ouvir
   
   radio.setChannel(108);  // Above most Wifi Channels
   radio.setDataRate(RF24_250KBPS); // Fast enough.. Better range
   radio.setPALevel(RF24_PA_MIN);
   radio.setCRCLength (RF24_CRC_16); 
   radio.powerDown();

  
 
  Serial.print("RADIO-ID <=> 1");
  //quando for o outro deve ser desse jeito
  radio.startListening();
  radio.openReadingPipe(1,enderecos[0]);
  Serial.print("\n");
  
  radio.printDetails();
  
}

void loop() {
  // put your main code here, to run repeatedly:
   digitalWrite(D4, !digitalRead(D4));
   Serial.println("loop");
   dadoDeEnvio = 255;
   Serial.println("37");
    //comando para parar de ouvir
    
   
   Serial.println("40");
   bool ddos;
   bool ddos2;
    bool xpto;
   xpto = radio.isAckPayloadAvailable();
   Serial.print("XPTO "+xpto);
   radio.rxFifoFull();
   radio.stopListening();
   radio.read(&dadosRecebidos, sizeof(int));
   Serial.println("Retorno : "+ddos);
   radio.getChannel();
   Serial.println("Canal : "+radio.getChannel());
   Serial.println("Canal : "+radio.getDataRate());
   Serial.println("Canal : "+radio.getPayloadSize());
   Serial.println("42");
   radio.startListening();
   Serial.println("44");

   lcd.setCursor(0, 0);
   lcd.print("RECEIVER");

   lcd.setCursor(0, 1);
   lcd.print("Status: "+radio.available());
    
   if(radio.available()){
    Serial.print("Recebidos: ");
    radio.read(&dadosRecebidos, sizeof(int));
    lcd.setCursor(0, 1);
    lcd.print("Status: "+radio.available());
    Serial.println("Radio : "+radio.available());
    Serial.println(dadosRecebidos);
    
   }else{
     lcd.setCursor(0, 1);
    lcd.print("Nao ha dados...");
   }

   delay(3000);
    lcd.clear();
}
