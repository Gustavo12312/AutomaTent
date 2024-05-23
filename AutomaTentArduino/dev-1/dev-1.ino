#include "DHT.h"
#include <Arduino.h>
#include <WiFi.h>
#include <WiFiMulti.h>
#include <HTTPClient.h>
#include <ArduinoJson.h>

#define PIN_RED    27 // GPIO23
#define PIN_GREEN  26 // GPIO22
#define PIN_BLUE   33 // GPIO21
#define DHTTYPE DHT22
DHT dht(14, DHTTYPE);

WiFiMulti wifiMulti;

const int relayPTCPin = 32;
const int relayFANPin = 25;

void setup() {
  Serial.begin(115200);
  Serial.println();
  Serial.println();
  Serial.println("Teste");
  pinMode(PIN_RED,   OUTPUT);
  pinMode(PIN_GREEN, OUTPUT);
  pinMode(PIN_BLUE,  OUTPUT);

  dht.begin();

  for (uint8_t t = 4; t > 0; t--) {
    Serial.printf("[SETUP] WAIT %d...\n", t);
    Serial.flush();
    delay(1000);
  }

  wifiMulti.addAP("Visitors", " ");

  pinMode(relayPTCPin, OUTPUT);
  pinMode(relayFANPin, OUTPUT);
}

void loop() {
  delay(2000);

  float humidade = dht.readHumidity();
  float temperatura = dht.readTemperature();

  if (isnan(humidade) || isnan(temperatura)) {
    Serial.println("Erro ao tentar ler Temperatura e Humidade.");
    return;
  }

  Serial.println("---------------------------------------------");
  Serial.print("Humidade: ");
  Serial.print(humidade);
  Serial.print("% ");
  Serial.print("Temperatura: ");
  Serial.print(temperatura);
  Serial.println("C");
  Serial.println("---------------------------------------------");
  Serial.println(" ");

  if ((wifiMulti.run() == WL_CONNECTED)) {
    HTTPClient http;

    Serial.print("[HTTP] begin...\n");
    http.begin("http://192.168.1.87:8080/api/dev");

    Serial.print("[HTTP] GET...\n");
    int httpCode = http.GET();

    if (httpCode > 0) {
      Serial.printf("[HTTP] GET... code: %d\n", httpCode);

      if (httpCode == HTTP_CODE_OK) {
        String payload = http.getString();
        Serial.println(payload);

        DynamicJsonDocument doc(1024);
        deserializeJson(doc, payload);

        for (JsonObject device : doc.as<JsonArray>()) {
          String name = device["name"].as<String>();
          int value = device["value"].as<int>();

          if (name == "FAN" && value == 1) {
            digitalWrite(relayFANPin, HIGH);
            Serial.println("Fan activated");
          } else if (name == "HEATER" && value == 1) {
            digitalWrite(relayPTCPin, HIGH);
            Serial.println("PTC activated");
          } else if (name == "LED" && value == 1) {
            analogWrite(PIN_RED,   255);
            analogWrite(PIN_GREEN, 0);
            analogWrite(PIN_BLUE,  0);
          }else {
            digitalWrite(relayFANPin, LOW);
            digitalWrite(relayPTCPin, LOW);
            analogWrite(PIN_RED,   0);
            analogWrite(PIN_GREEN, 0);
            analogWrite(PIN_BLUE,  0);
          }
        }
      }
    } else {
      Serial.printf("[HTTP] GET... failed, error: %s\n", http.errorToString(httpCode).c_str());
    }
    http.end();
  }
  delay(1000);
}
