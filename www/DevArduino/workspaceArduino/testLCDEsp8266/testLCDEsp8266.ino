/*********
  Rui Santos
  Complete project details at https://randomnerdtutorials.com  
*********/
#include <RF24Network.h>
#include <RF24.h>
#include <SPI.h>
#include <LiquidCrystal_I2C.h>

// Armazena os dados recebidos
int recebidos[1];

RF24 radio(D3, D4); // CE, CSN
byte guest=0;
RF24Network network(radio);      // Network uses that radio
const uint16_t this_node = 00;    // Address of our node in Octal format ( 04,031, etc)
const uint16_t other_node = 01;   // Address of the other node in Octal format

// set the LCD number of columns and rows
int lcdColumns = 16;
int lcdRows = 2;

// set LCD address, number of columns and rows
// if you don't know your display address, run an I2C scanner sketch
LiquidCrystal_I2C lcd(0x27, lcdColumns, lcdRows);  

void setup(){
  // initialize LCD
  Serial.begin(115200);
  lcd.init();
  // turn on LCD backlight                      
  lcd.backlight();
  Serial.println("RF24Network/examples/helloworld_rx/");
 
  SPI.begin();
  radio.begin();
  network.begin(/*channel*/ 90, /*node address*/ this_node);
}

void loop(){
  // set cursor to first column, first row
  Serial.println("entrei loop");
  network.update();                  // Check the network regularly
  lcd.setCursor(0, 0);
  lcd.print("RECEIVER");

  lcd.setCursor(0, 1);
  lcd.print("Status: "+radio.available());
  Serial.println("Radio : "+radio.available());
  
  delay(3000);
  lcd.clear();
  
  if (radio.available()){
   while ( network.available() ) {     // Is there anything ready for us?
     /*char text[32];
     radio.read(&text, sizeof(text));

     Serial.println("Reslt : "||text);
     lcd.setCursor(0, 0);
     lcd.print(text);
     delay(1000);
     lcd.clear(); 
     delay(1000);
     */   
    RF24NetworkHeader header;        // If so, grab it and print it out
    static  char pesan[32];
    network.read(header,&pesan,sizeof(pesan));
    Serial.print("Received packet #");
    Serial.println(pesan);
   
     lcd.setCursor(0, 0);
     lcd.print(pesan);
     delay(1000);
     lcd.clear(); 
     delay(1000);
   guest++;
if(guest>2){
  guest=1;
}
  }


     
  }else{
    lcd.setCursor(0, 0);
  // print message
  lcd.print("Aguardando Dados...");
  delay(1000);
  lcd.clear(); 
  delay(1000);
  }
  
}
