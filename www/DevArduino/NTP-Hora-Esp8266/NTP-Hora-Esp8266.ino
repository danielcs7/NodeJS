
#include <NTPClient.h>
#include <ESP8266WiFi.h>
#include <WiFiUdp.h>

//char ssid[] = "iab-adm";  //  your network SSID (name)
//char pass[] = "iab124578";       // your network password

const char *ssid     = "iab-adm";
const char *password = "iab124578";

WiFiUDP ntpUDP;
 
int16_t utc = -3; //UTC -3:00 Brazil
uint32_t currentMillis = 0;
uint32_t previousMillis = 0;
 
NTPClient timeClient(ntpUDP, "a.st1.ntp.br", utc*3600, 60000);

 // Variables to save date and time
static String formattedDate;
static String dayStamp;
static String timeStamp;

void setup(){
  
  Serial.begin(115200);
 
  WiFi.begin(ssid, password);
 
  while ( WiFi.status() != WL_CONNECTED ) {
    delay ( 500 );
    Serial.print ( "." );
  }
 
  timeClient.begin();
  timeClient.update();

  // Print local IP address and start web server
  Serial.println("");
  Serial.println("WiFi connected.");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}
 

void dtHours(){
  timeClient.update();
  while(!timeClient.update()) {
    timeClient.forceUpdate();
  }

  Serial.println(timeClient.getFormattedTime());
  
  Serial.println("*****************************************************");
  formattedDate = timeClient.getFormattedDate();
  Serial.println(formattedDate);

  // Extract date
  int splitT = formattedDate.indexOf("T");
  dayStamp = formattedDate.substring(0, splitT);
  Serial.print("DATE: ");
  Serial.println(dayStamp);
  // Extract time
  timeStamp = formattedDate.substring(splitT+1, formattedDate.length()-1);
  Serial.print("HOUR: ");
  Serial.println(timeStamp);
  Serial.println("*****************************************************");
 
}
 
void loop() {
  //Chama a verificacao de tempo
  //checkOST();
    dtHours();
 
  

  delay(1000);
}
