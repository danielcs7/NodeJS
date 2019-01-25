#include <ESP8266WiFi.h>
#include <WiFiUdp.h>



WiFiUDP Udp;

byte serdata=0;
byte fromserver=0;

unsigned int localUdpPort = 4210;  // local port to listen on
char incomingPacket[255];  // buffer for incoming packets
char  replyPacket[] = "CARLOS ";  // a reply string to send back


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
    Udp.begin(81);


}


void loop()
{

  
   digitalWrite(D4, !digitalRead(D4));


   const int port=4211;
   int i = 0;
   udpsend();

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

    // send back a reply, to the IP address and port we got the packet from
    Udp.beginPacket(Udp.remoteIP(), Udp.remotePort());
    Udp.write(replyPacket);
    Udp.endPacket();
  }
  
   delay(3000);
}

void udpsend()
  {
    const char ip[]="192.168.10.226";
  Udp.beginPacket(ip,81);
  Udp.write(replyPacket);
  Udp.endPacket();
  delay(1000);
  return;
  }
