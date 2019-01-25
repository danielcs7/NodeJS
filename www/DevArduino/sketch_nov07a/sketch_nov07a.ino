#include <ESP8266WiFi.h>
#include <PubSubClient.h> // https://github.com/knolleary/pubsubclient/releases/tag/v2.3

//-------- Customise these values -----------
const char* ssid = "netvirtua_203";
const char* password = "1073847000";

#define ORG "3puyj3"
#define DEVICE_TYPE "greenhouse11"
#define DEVICE_ID "00011"
#define TOKEN "?f?73TkHf!n4KQ-aIh"
//-------- Customise the above values --------

char server[] = ORG ".messaging.internetofthings.ibmcloud.com";
char topic[] = "iot-2/evt/status/fmt/json";
char authMethod[] = "use-token-auth";
char token[] = TOKEN;
char clientId[] = "d:" ORG ":" DEVICE_TYPE ":" DEVICE_ID;

WiFiClient wifiClient;
PubSubClient client(server, 1883, NULL, wifiClient);

void setup() {
 Serial.begin(115200);
 Serial.println();

 Serial.print("Connecting to "); Serial.print(ssid);
 WiFi.begin(ssid, password);
 while (WiFi.status() != WL_CONNECTED) {
 delay(500);
 Serial.print(".");
 } 
 Serial.println("");


 Serial.print("WiFi connected, IP address: "); Serial.println(WiFi.localIP());
}

int counter = 0;

void loop() {

 if (!client.connected()) {
 Serial.print("Reconnecting client to ");
 Serial.println(server);
 while (!client.connect(clientId, authMethod, token)) {
 Serial.print(".");
 delay(500);
 }
 Serial.println();
 }

 String vCheck = "2018-11-18";
 String vHours = "13:48";
 String vTemperature = "23.45";
 String vHumidty = "67.45";

// //http://arduino.moradaverdehf.com.br/api/tempmeasures/getsave/1/2017-11-10/20:45:52/12.45/932.12
 String payload = "{\"d\":{\"Estufa\":\"11\"";
payload += ",\"checkDate\":";
 payload += vCheck;

payload += ",\"hours\":";
 payload += vHours;

payload += ",\"temperature\":";
 payload += vTemperature;

payload += ",\"humidity\":";
payload += vHumidty;
 
 payload += "}}";
 
 Serial.print("Sending payload: ");
 Serial.println(payload);
 
 if (client.publish(topic, (char*) payload.c_str())) {
 Serial.println("Publish ok");
 } else {
 Serial.println("Publish failed");
 }

 delay(120000);
}
