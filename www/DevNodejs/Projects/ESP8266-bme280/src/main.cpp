#include <Arduino.h>
#include <NTPClient.h>
#include <WiFiUdp.h>

//https://github.com/Seeed-Studio/Grove_BME280
#include "Seeed_BME280.h"

static float humidity;
static float temperature;
int16_t utc = -3; //UTC -3:00 Brazil

WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "a.st1.ntp.br", utc * 3600, 60000);

BME280 bme280;

void setup()
{
  // put your setup code here, to run once:
  Serial.begin(9600);

    if (!bme280.init())
  {
    Serial.println("Device error BME280...!");
  }

}

void loop()
{
  // put your main code here, to run repeatedly:
    temperature = bme280.getTemperature();
  humidity = bme280.getHumidity();
  //get and print temperatures
  Serial.print("Temp: ");
  Serial.print(bme280.getTemperature());
  Serial.println("C");

  delay(30000);
}