#include <ESP8266WiFi.h>
#include <WiFiUdp.h>



WiFiUDP Udp;

byte serdata=0;
byte fromserver=0;


static String resp[255];
unsigned int localUdpPort = 4210;  // local port to listen on
char incomingPacket[255];  // buffer for incoming packets
char  replyPacket[255] = "DANIEL ";  // a reply string to send back



void setup()
{
  Serial.begin(115200);
  Serial.println();

  pinMode(D4, OUTPUT);//Define o LED Onboard como saida.
  digitalWrite(D4, 1);//Apaga o LED.

  WiFi.mode(WIFI_STA);
  WiFi.begin("iab-adm","iab124578");

  Serial.println();
    Serial.println();
    Serial.print("Wait for WiFi... ");
    
  while(WiFi.status() != WL_CONNECTED) 
  {
    Serial.print(".");
        delay(500);
  }
     Serial.println("");
    Serial.println("WiFi connected");
    Serial.println("IP address: ");
    Serial.println(WiFi.localIP());   
    Udp.begin(4211);


}


void loop()
{

  
   digitalWrite(D4, !digitalRead(D4));


   const int port=4211;
   int i = 0;
  
  //Serial.println("RESP=>"+resp);
  int packetSize = Udp.parsePacket();
  if (packetSize)
  {
    // receive incoming UDP packets
    Serial.println("Receiver");
    Serial.printf("Received %d bytes from %s, port %d\n", packetSize, Udp.remoteIP().toString().c_str(), Udp.remotePort());
    int len = Udp.read(incomingPacket, 255);
    if (len > 0)
    {
      incomingPacket[len] = 0;
    }
    Serial.printf("UDP packet contents: %s\n", incomingPacket);
    digitalWrite(D4, !digitalRead(D4));
    // send back a reply, to the IP address and port we got the packet from
    const char ip[]="192.168.10.226";
   // Udp.beginPacket(ip,4211);
   // Udp.write(incomingPacket);
   // Udp.endPacket();
   
//    resp = incomingPacket;
//    Serial.println("RESP 80 =>"+resp);
   

    delay(1000);
    digitalWrite(D4, !digitalRead(D4));
  }

   udpsend();
   delay(1000);
}

void udpsend()
  {
  Serial.println("incomingPacket 93 =>"||incomingPacket);
  const char ip[]="192.168.10.155";
  Udp.beginPacket(ip,4210);
  Udp.write(incomingPacket);
  Udp.endPacket();
  delay(2000);
  return;
  }
