#include <Arduino.h>
#include <Keypad.h>

const byte n_rows = 4;
const byte n_cols = 4;

char keys[n_rows][n_cols] = {

    {'1', '2', '3', 'A'},

    {'4', '5', '6', 'B'},

    {'7', '8', '9', 'C'},

    {'*', '0', '#', 'D'}

};

byte colPins[n_rows] = {D3, D2, D1, D0};
byte rowPins[n_cols] = {D7, D6, D5, D4};

Keypad myKeypad = Keypad(makeKeymap(keys), rowPins, colPins, n_rows, n_cols);
long Num1, Num2, Number;
static char myKey, action;
boolean result = false;

void setup()
{
  // put your setup code here, to run once:
  Serial.begin(9600);
}

void DetectButtons()
{

  if (myKey == '*') //If cancel Button is pressed
  {
    Serial.println("Button Cancel");
    Number = Num1 = Num2 = 0;
    result = false;
  }

  if (myKey == '1') //If Button 1 is pressed
  {
    Serial.println("Button 1");
    if (Number == 0)
      Number = 1;
    else
      Number = (Number * 10) + 1; //Pressed twice
  }

  if (myKey == '4') //If Button 4 is pressed
  {
    Serial.println("Button 4");
    if (Number == 0)
      Number = 4;
    else
      Number = (Number * 10) + 4; //Pressed twice
  }

  if (myKey == '7') //If Button 7 is pressed
  {
    Serial.println("Button 7");
    if (Number == 0)
      Number = 7;
    else
      Number = (Number * 10) + 7; //Pressed twice
  }

  if (myKey == '0')
  {
    Serial.println("Button 0"); //Button 0 is Pressed
    if (Number == 0)
      Number = 0;
    else
      Number = (Number * 10) + 0; //Pressed twice
  }

  if (myKey == '2') //Button 2 is Pressed
  {
    Serial.println("Button 2");
    if (Number == 0)
      Number = 2;
    else
      Number = (Number * 10) + 2; //Pressed twice
  }

  if (myKey == '5')
  {
    Serial.println("Button 5");
    if (Number == 0)
      Number = 5;
    else
      Number = (Number * 10) + 5; //Pressed twice
  }

  if (myKey == '8')
  {
    Serial.println("Button 8");
    if (Number == 0)
      Number = 8;
    else
      Number = (Number * 10) + 8; //Pressed twice
  }

  if (myKey == '#')
  {
    Serial.println("Button Equal");
    Num2 = Number;
    result = true;
  }

  if (myKey == '3')
  {
    Serial.println("Button 3");
    if (Number == 0)
      Number = 3;
    else
      Number = (Number * 10) + 3; //Pressed twice
  }

  if (myKey == '6')
  {
    Serial.println("Button 6");
    if (Number == 0)
      Number = 6;
    else
      Number = (Number * 10) + 6; //Pressed twice
  }

  if (myKey == '9')
  {
    Serial.println("Button 9");
    if (Number == 0)
      Number = 9;
    else
      Number = (Number * 10) + 9; //Pressed twice
  }

  if (myKey == 'A' || myKey == 'B' || myKey == 'C' || myKey == 'D') //Detecting Buttons on Column 4
  {
    Num1 = Number;
    Number = 0;
    if (myKey == 'A')
    {
      Serial.println("Addition");
      action = '+';
    }
    if (myKey == 'B')
    {
      Serial.println("Subtraction");
      action = '-';
    }
    if (myKey == 'C')
    {
      Serial.println("Multiplication");
      action = '*';
    }
    if (myKey == 'D')
    {
      Serial.println("Devesion");
      action = '/';
    }

    delay(100);
  }
}

void loop()
{
  // put your main code here, to run repeatedly:
  myKey = myKeypad.getKey();

  if (myKey != NULL)
  {

    DetectButtons();
    Serial.print("Key pressed: ");
    Serial.println(myKey);
  }
}
