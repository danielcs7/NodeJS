/*#define ledPin 13
// #define ledPin D7



void setup() 
{
  pinMode(ledPin, OUTPUT);
}

void loop() 
{
  digitalWrite(ledPin, HIGH);   
  delay(1000);              
  digitalWrite(ledPin, LOW);    
  delay(1000);             
}
*/


const int ledRed = 13;
const int ledGren = 2;
const int button = 4;
int temp = 0;


void setup(){
  Serial.begin(115200);
  pinMode(ledRed, OUTPUT);
  pinMode(ledGren, OUTPUT);
  pinMode(button, INPUT);
}

void loop(){

temp = digitalRead(button);

Serial.println("Botao <=-=> "+temp);

if(temp == HIGH){
  
  digitalWrite(ledRed,HIGH);
  Serial.println("Led ON");
  delay(2000);
  digitalWrite(ledRed,LOW);
}else{
 
  digitalWrite(ledGren,HIGH);
  Serial.println("Led OFF");
  delay(2000);
  digitalWrite(ledGren,LOW);
}
 

   
}
