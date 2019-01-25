#include <Arduino.h>

#include "Seeed_BME280.h"
#include <ESP8266WiFi.h>

static float h;
static float t;
static float humidity;
static float temperature;

BME280 bme280;

void setup()
{
  // put your setup code here, to run once:

  Serial.begin(9600);
  //**********************************************************************

  if (!bme280.init())
  {
    Serial.println("Device error BME280...!");
  }

  //**********************************************************************
  Serial.println();
}

void loop()
{
  // put your main code here, to run repeatedly:

  float pressure;

  temperature = bme280.getTemperature();
  humidity = bme280.getHumidity();
  //get and print temperatures
  Serial.print("Temp: ");
  Serial.print(bme280.getTemperature());
  Serial.println("C");

  //get and print atmospheric pressure data
  Serial.print("Pressure: ");
  Serial.print(pressure = bme280.getPressure());
  Serial.println("Pa");

  //get and print altitude data
  Serial.print("Altitude: ");
  Serial.print(bme280.calcAltitude(pressure));
  Serial.println("m");

  //get and print humidity data
  Serial.print("Humidity: ");
  Serial.print(bme280.getHumidity());
  Serial.println("%");

  delay(10000);
}