////////////////////////////////////////////////////////////////////////////////////////
////    Projeto: Rede ESP utilizando a IDE do Arduino.                              ////
////                                                                                ////
////    Testes Realizados com ESP8266 - Model 12.                                   ////
////                                                                                ////
////    Comunicacao Entre ESP8266 e PC via UDP.                                     ////
////                                                                                ////
////    Comunicacao Entre "A ESP8266", "B ESP8266" "C PC" via UDP.                  ////
////                                                                                ////
////    Autor: Leonardo Hilgemberg Lopes - leonardohilgemberg@gmail.com             ////
////    Data: 10/05/2017.                                                           ////
////                                                                                ////
////////////////////////////////////////////////////////////////////////////////////////
////                                                                                ////
//// Colaboradores e agradecimentos:                                                ////
//// https://github.com/esp8266/Arduino/blob/master/doc/esp8266wifi/udp-examples.md ////
////                                                                                ////                                  ////
////    Grande ajuda do: Jose Gustavo Abreu Murta.                                  ////
////    O mundo precisa de mais pessoas como o Jose, pois sempre me ajuda onde      ////
////    preciso                                                                     ////
////                                                                                ////
////    Obrigado Lab De Garagem.                                                    ////
////////////////////////////////////////////////////////////////////////////////////////
////    Observacoes:                                                                ////
////    aplicativo para teste no PC: "Packet Sender"                                ////
////                                                                                ////
////    Projeto Realizado no Atom PlatformIO                                        ////
////                                                                                ////
////    Caso utilze um projeto mais complexo, deixe seu Access point com o DHCP     ////
////    Desabilitado.                                                               ////
////                                                                                ////
////    Verifique se o IP que estamos utilizando esta livre caso conecte em um      ////
////    Roteador                                                                    ////
////                                                                                ////
////    Projeto nao testado utilizando um ESP como Access Point ainda.              ////
////                                                                                ////
////////////////////////////////////////////////////////////////////////////////////////
/*
    Para testar com dois ESP:
        1 - definir o seu ssid e senha do roteador.
        2 - Definir pinos do seu botao e led do seu ESP.
        3 - Para ESP 1 - Mude a linha 54, coloque igual a 0
        5 - pegue outro ESP.
        4 - Para ESP 2 - Mude a linha 54, coloque igual a 1
        6.0 - Pressione o botao no ESP_1, o led do ESP_2 deve ligar.
        6.1 - Pressione o botao no ESP_1, o led do ESP_2 deve desligar.
        6.2 - e vice-versa

    Para testar com o PC:
        1 - Conecte seu pc na mesma rede dos ESP.
        2 - atravez do programa - "Packet Sender" ou IO-ninja
            selecione a porta e o IP do ESP e envie a palavra
            "hello", o ESP deve responder: "OK, msg recebida..."...

    Boa sorte meus amigos Arduineiros, espero que o codigo
    tenha ficado facil de entender..
*/

//  Libraries.
#include <ESP8266WiFi.h>    // Biblioteca para utilizacao de dados Wifi
#include <WiFiUdp.h>        // Biblioteca para utilizacao de dados UDP

//  Escolha aqui quais os enderecos padroes deseja utilizar.
//  verifica se algum endereco foi definido.
#ifndef ADDRESS_ESP_NETWORK
    #define ADDRESS_ESP_NETWORK     1   // pode ser 0, 1, 2... voce pode adicionar mais...
#endif

#if ADDRESS_ESP_NETWORK==0
    IPAddress ip(192, 168, 25, 191);    //  IP desta placa ESP
    byte IP_ESP_1[] = { 192, 168, 25, 192 };    // responde para este endereco
    byte IP_ESP_2[] = { 192, 168, 25, 193 };    // responde para este endereco
#elif ADDRESS_ESP_NETWORK==1
    IPAddress ip(192, 168, 25, 192);    //  IP desta placa ESP
    byte IP_ESP_1[] = { 192, 168, 25, 191 };    // responde para este endereco
    byte IP_ESP_2[] = { 192, 168, 25, 193 };    // responde para este endereco
#elif ADDRESS_ESP_NETWORK==2
    // coloque aqui o IP que desejar.
    /*
        IPAddress ip(192, 168, 25, 193);    //  IP desta placa ESP
        byte IP_ESP_1[] = { 192, 168, 25, 191 };    // responde para este endereco
        byte IP_ESP_2[] = { 192, 168, 25, 192 };    // responde para este endereco
    */
#endif

//  Your AccessPoint SSID and password.
const char* ssid        = "iab-adm";   // nome da sua rede.
const char* password    = "iab124578";   // Senha da sua rede.

// NETWORK: Static IP details...

IPAddress gateway(192, 168, 4, 1); //  gateway padrao da sua rede.
IPAddress subnet(255, 255, 255, 0); //  Mascara de rede, geralmente esta e a padrao.

WiFiUDP Udp;    // Instanciamos a utilizacao do UDP;

unsigned int localUdpPort = 4210;   // Porta local para trafego de dados.
char incomingPacket[255];           // buffer para recebimento de pacotes.
char  replyPacekt[] = "OK, msg recebida...";         // msg de resposta quando recebe algo

char  testPacekt[] = "hello";

// IPAddress IPOutroESP(192, 196, 25, 192);  //  IPAddress e um typedef da linha a cima
char sendPacektToIP[] = "led"; // quando pressiona o botao envia este dado para o IP Acima.
char msgToggleLed[] = "led";    // Msg que liga o LED e Desliga.

#define PIN_LED     12
#define PIN_BUTTON  16

bool    statusLed   = false;
String  msgRecebidaConvertida = "";

// declara todas as funcoes utilizadas.
void connectToAccessPoint(void);
void trataPacoteRecebido(void);
void enviaPacote(unsigned int _pin_button);
void recebePacote(void);

void setup(){

    Serial.begin(115200);

    pinMode(PIN_LED, OUTPUT);
    pinMode(PIN_BUTTON, INPUT);

    connectToAccessPoint();

}

void loop(){

    trataPacoteRecebido();
    enviaPacote(PIN_BUTTON);
    recebePacote();
}

// conecta ao roteador.
void connectToAccessPoint(void){

    Serial.println();
    Serial.printf("Connecting to %s ", ssid);

    // Static IP Setup Info Here...
    WiFi.config(ip, gateway, subnet);

    WiFi.begin(ssid, password);

    while (WiFi.status() != WL_CONNECTED)
    {
        delay(500);
        Serial.print(".");
    }
    Serial.println(" connected");

    Udp.begin(localUdpPort);
    Serial.printf("Escutando IP: %s, UDP port: %d\n", WiFi.localIP().toString().c_str(), localUdpPort);
}

// esta funcao recebe os pacotes UDP.
void recebePacote(void){

    int packetSize = Udp.parsePacket(); // pega dados do analisador de pacotes.

    //  Se receber algum pacote.
    if(packetSize)
    {
        msgRecebidaConvertida = ""; // sempre zera a mensagem anterior.

        // recebe pacotes UDP de entrada
        Serial.printf("Received %d bytes from %s, port %d\n", packetSize, Udp.remoteIP().toString().c_str(), Udp.remotePort());

        int len = Udp.read(incomingPacket, 255);
        if(len > 0)
        {
            incomingPacket[len] = 0;
        }

        msgRecebidaConvertida.concat(incomingPacket);

        Serial.println("Msg Recebida e convertida p/ String: ");
        Serial.print(msgRecebidaConvertida);
        Serial.println();

        //VERIFICA SE HÁ DADOS
        int checkSender;

        checkSender = sizeof(testPacekt) ;

        Serial.println("CHECK ENVIO DE DADOS... "+checkSender);

        //if(msgRecebidaConvertida == testPacekt)
        if(checkSender > 0)
        {
            Serial.printf("Msg %s Interpretada.",testPacekt);
            // Responde "replyPacekt" para quem nos chamou.
            Serial.printf("UDP packet contents: %s\n", incomingPacket);
            // enviar de volta uma resposta, para o endereço IP e porta temos o pacote de
            Udp.beginPacket(Udp.remoteIP(), Udp.remotePort());
            Udp.write(replyPacekt);
            Udp.endPacket();
        }
    }
}

//  o nome da funcao ja fala tudo rsrs
void trataPacoteRecebido(void){

    if(msgRecebidaConvertida == msgToggleLed)
    {
        msgRecebidaConvertida = ""; // zera a variavel pra nao ficar repetindo aqui.

        // avisa que a msg foi entendida
        Serial.println("Msg Interpretada.");

        //  faz o toggle do led.
        if(statusLed == false)
        {
            Serial.println("Ligando Led");
            digitalWrite(PIN_LED, HIGH);
            statusLed = true;
        }else{
            Serial.println("desligando Led");
            digitalWrite(PIN_LED, LOW);
            statusLed = false;
        }
    }
}

//  variaveis para controle do bounce do botao.
bool _statusButton = false;
unsigned long _oldTimeButton = 0;
#define BOUNCE_TIME 200

// trata o bounce do botao e envia o pacote para os esp.
void enviaPacote(unsigned int _pin_button){

    if(digitalRead(_pin_button) == 1)
    {
        if((millis()-_oldTimeButton)>BOUNCE_TIME)
        {
            _oldTimeButton = millis();

            if(_statusButton == false)
            {
                _statusButton = true;
                Serial.println("Enviando Pacote...");

                // envia dados para o ESP_1
                Udp.beginPacket(IP_ESP_1, localUdpPort);    // beginPacket(IP,Porta)
                Udp.write(sendPacektToIP);  // mensagem a ser enviada.
                Udp.endPacket();    // finaliza a comunicacao anterior.

                // envia dados para o ESP_2
                Udp.beginPacket(IP_ESP_2, localUdpPort);    // beginPacket(IP,Porta)
                Udp.write(sendPacektToIP);  // mensagem a ser enviada.
                Udp.endPacket();    // finaliza a comunicacao anterior
            }
        }
    }else{
        if(true)
            _statusButton = false;
    }
}
