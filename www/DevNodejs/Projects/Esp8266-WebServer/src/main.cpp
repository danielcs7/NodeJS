#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>

ESP8266WebServer server(80);

void handleRoot()
{
  // HTML da pagina principal
  String html = "<html><head><title>Exemplo 2</title>";
  html += "<style>body { background-color: #cccccc; ";
  html += "font-family: Arial, Helvetica, Sans-Serif; ";
  html += "Color: #000088; }</style>";
  html += "</head><body>";
  html += "<h1>Exemplo 2 - Fazendo POST</h1>";
  html += "<p>Pagina Principal</p>";
  html += "<p><a href=/form>Carregar Formulario</a></p>";
  html += "</body></html>";
  // Enviando HTML para o servidor
  server.send(200, "text/html", html);
}

void formularioChamada()
{
  // HTML do formulario
  String html = "<html><head><title>Exemplo 2</title>";
  html += "<style>body { background-color: #cccccc; ";
  html += "font-family: Arial, Helvetica, Sans-Serif; ";
  html += "Color: #000088; }</style>";
  html += "</head><body>";
  html += "<h1>Exemplo 2 - Fazendo POST</h1>";
  html += "<p>Formulario de Envio</p>";
  html += "<form method='POST' action='/form'>";
  html += "<p><input type=text name=txtEnviar /> ";
  html += "<input type=submit name=botao value=Enviar /></p>";
  html += "</form>";
  html += "<p><a href=/>Pagina Principal</a></p>";
  html += "</body></html>";
  // Enviando HTML para o servidor
  server.send(200, "text/html", html);
}

void formularioEnviado()
{
  // HTML do formulario
  String html = "<html><head><title>Exemplo 2</title>";
  html += "<style>body { background-color: #cccccc; ";
  html += "font-family: Arial, Helvetica, Sans-Serif; ";
  html += "Color: #000088; }</style>";
  html += "</head><body>";
  html += "<h1>Exemplo 2 - Fazendo POST</h1>";
  html += "<p>Formulario de Envio</p>";

  html += "<p>";
  if (server.hasArg("txtEnviar"))
  {
    html += "<b>Valor digitado: </b>";
    html += server.arg("txtEnviar");
  }
  else
  {
    html += "<b>Argumento invalido!</b>";
  }
  html += "</p>";

  html += "<form method='GET' action='/form'>";
  html += "<p><input name=button2 type=submit value=Voltar /></p>";
  html += "</form>";
  html += "<p><a href=/>Pagina Principal</a></p>";
  html += "</body></html>";
  // Enviando HTML para o servidor
  server.send(200, "text/html", html);
}

void setup()
{
  // Iniciando Serial
  Serial.begin(9600);

  // Iniciando WiFi
  WiFi.begin("netvirtua_203", "1073847000");

  IPAddress subnet(255, 255, 255, 0);
  WiFi.config(IPAddress(192, 168, 0, 125),
              IPAddress(192, 168, 0, 1), subnet);

  // Aguardando conectar na rede
  Serial.println("");
  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.print('.');
  }

  // Mostrando IP
  Serial.println("");
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());

  // Atribuindo urls para funções
  // Quando não especificado método, uma função trata todos
  server.on("/", handleRoot);

  // Chamada do método GET
  server.on("/form", HTTP_GET, formularioChamada);
  // Método POST
  server.on("/form", HTTP_POST, formularioEnviado);

  // Iniciando servidor
  server.begin();

  // Apenas informando que servidor iniciou
  Serial.println("HTTP server started");
}

void loop()
{
  // No loop só precisa dessa função
  server.handleClient();
}
