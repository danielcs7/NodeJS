#include <DHT.h>
#include <ESP8266WiFi.h>


const char ssid[] = "iab-adm";  //  nome da rede
const char pass[] = "iab124578";     // senha da rede

#define DHTPIN D4         // What digital pin we're connected to
#define DHTTYPE DHT22   // DHT 22, AM2302, AM2321

DHT dht(DHTPIN, DHTTYPE);

void setup()
{
Serial.begin(115200);
Serial.setTimeout(2000);
delay(10);


 Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.begin(ssid, pass);

  while (WiFi.status() != WL_CONNECTED) {
    delay(250);
    Serial.print(".");
    
  }

   // Wait for serial to initialize.
  while(!Serial) { }
dht.begin();
  Serial.println("Device Started");
  Serial.println("-------------------------------------");
  Serial.println("Running DHT!");
  Serial.println("-------------------------------------");

  
}

int timeSinceLastRead = 0;

void loop()
{
 // Report every 2 seconds.
  if(timeSinceLastRead > 2000) {
    // Reading temperature or humidity takes about 250 milliseconds!
    // Sensor readings may also be up to 2 seconds 'old' (its a very slow sensor)
    float h = dht.readHumidity();
    // Read temperature as Celsius (the default)
    float t = dht.readTemperature();
   
    // Check if any reads failed and exit early (to try again).
    if (isnan(h) || isnan(t)) {
      Serial.println("Failed to read from DHT sensor!");
      timeSinceLastRead = 0;
      return;
    }

 
    Serial.print("Humidity: ");
    Serial.print(h);
    Serial.print(" %\t");
    Serial.print("Temperature: ");
    Serial.print(t);
    Serial.print(" *C ");

    Serial.println("\n");    
    
    timeSinceLastRead = 0;
  }
  delay(60000);

  timeSinceLastRead += 100;


}
