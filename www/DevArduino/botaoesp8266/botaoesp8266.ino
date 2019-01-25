 /** Código 2 **/
// Elaborado por Tony Emerson Marim em 18/12/2016.
/** mecatronizando@gmail.com **/
/** Liga LED**/
//Constantes
int botao = 4;

//Varável de estado do botão
int estadobotao = 0;

void setup() {
 Serial.begin(115200);
  //Define o pino 15 como entrada
  pinMode(botao, INPUT);
}
 
void loop() {

  //Atribuindo resultado para o estado do botão
  estadobotao = digitalRead(botao);

  Serial.println(estadobotao);

  //Lógica de funcionament0
  if (estadobotao == HIGH)// Botão recebe 1
  {
    Serial.println("<< 1 >>");
  }else{
    Serial.println("<< 2 >>");
  }


   delay(3000);
}
