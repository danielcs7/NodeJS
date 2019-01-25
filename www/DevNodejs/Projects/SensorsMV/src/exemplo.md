# Documentação dos Sensores Morada Verde

Essa documentação visa entender melhor o código, para futuramente dar manutenção.

## Sections

- [Headers](#headers)
- [Quotes](#quotes)

## Links das bibliotecas para o projeto

<!--
    Links
    https://github.com/beegee-tokyo/DHTesp
    https://randomnerdtutorials.com/esp32-data-logging-temperature-to-microsd-card/
    https://github.com/Seeed-Studio/Grove_BME280
    https://github.com/Makuna/Rtc
    WiFi Configuration Magic ( https://github.com/zhouhan0126/WIFIMANAGER-ESP32 ) >> https://github.com/tzapu/WiFiManager (ORIGINAL)
-->

[Bibioteca DHT para DHT22](https://github.com/beegee-tokyo/DHTesp)

[Biblioteca NTPClient.h para Relógio](https://randomnerdtutorials.com/esp32-data-loggin/home/iab/Documents/PlatformIO/Projects/SensorsMV/img/WifiManager.jpgature-to-microsd-card/)

[Biblioteca para o sensor de temperatura BME280](https://github.com/Seeed-Studio/Grove_BME280)

### Aqui Defino a Rede caso não user o WiFiManager/home/iab/Documents/PlatformIO/Projects/SensorsMV/img/WifiManager.jpg

```js
const char *ssid     = "SUT";
const char *password = "iab124578";
```

Hoje o sistema utiliza o BME280, pois a precisão é bem melhor que o DHT22.
Utilize também o Visual Studio Code para compilar o projeto e codificar.

O Fluxo do sistema:

O Sistema ler o sensor BkME280 que devolve a temperatura, umidade e pressão. Mas só estamos utilizando a temperatura e umidade do sensor.
O sistema valida se os dados estão zerados, se estiverem ele renicia o nodemcu.
Se os dados estiverem ok ele envia fia GET para o servidor que foi desenvolvido.

Se a internet cair, o sistema vai computando os valores de 3 em 3 minutos para o arquivo , depois que a internet voltar o sistema ler o arquivo e envia todos os dados.

Se o sistema, estiver utilizando o WiFiManager e cair a energia, ele consegue ver o que foi gravado em memória e se conecta a internet novamente, caso isso não ocorra, é só ir com o celular perto do sensor , selecionar a rede wifi, que irá aparecer em rede Wifi com o nome "ESP06" o "06" é o numero do sensor para enviar os dados!
è só conectar no "ESP06" e colocar os dados para conectar a Rede SUT
<img src="/img/WifiManager.jpg" />
