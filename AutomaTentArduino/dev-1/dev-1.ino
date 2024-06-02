#include "DHT.h"
#include <Arduino.h>
#include <WiFi.h>
#include <WiFiMulti.h>
#include <HTTPClient.h>
#include <ArduinoJson.h>
#include "RGBLed.h"

#define PIN_RED 33    // GPIO23
#define PIN_GREEN 27  // GPIO22
#define PIN_BLUE 26   // GPIO21

#define rgbLed_TYPE COMMON_ANODE
RGBLed rgbLed(PIN_RED, PIN_GREEN, PIN_BLUE, rgbLed_TYPE);

#define DHTTYPE DHT22
DHT dht(14, DHTTYPE);

WiFiMulti wifiMulti;

const int relayPTCPin = 32;
const int relayFANPin = 25;
int ledActive = 0;

void setup() {
  Serial.begin(115200);
  Serial.println();
  Serial.println();
  Serial.println("Teste");
  pinMode(PIN_RED, OUTPUT);
  pinMode(PIN_GREEN, OUTPUT);
  pinMode(PIN_BLUE, OUTPUT);

  dht.begin();

  for (uint8_t t = 4; t > 0; t--) {
    Serial.printf("[SETUP] WAIT %d...\n", t);
    Serial.flush();
    delay(1000);
  }
  //change the name of the wifi and pass
  wifiMulti.addAP("MEO-DA8080", "f0b4e0df22");

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
    StaticJsonDocument<200> jsonDoc;
    jsonDoc["value"] = String(temperatura) + ":" + String(humidade);
    String requestBody;
    serializeJson(jsonDoc, requestBody);

    HTTPClient httpTemp;
    httpTemp.begin("http://192.168.1.87:8080/api/dev/updatestring/6");
    httpTemp.addHeader("Content-Type", "application/json");

    int httpResponseCode = httpTemp.PUT(requestBody);

    if (httpResponseCode > 0) {
      String response = httpTemp.getString();
    } else {
      Serial.println("Error on sending PUT request: " + String(httpResponseCode));
    }

    httpTemp.end();
    HTTPClient http;

    //change for the local IP to connect with androidstudio
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

          if (name == "FAN") {
            if (value == 1) {
              digitalWrite(relayFANPin, HIGH);
              Serial.println("Fan activated");
            } else {
              digitalWrite(relayFANPin, LOW);
            }
          } else if (name == "HEATER") {
            if (value == 1) {
              digitalWrite(relayPTCPin, HIGH);
              Serial.println("PTC activated");
            } else {
              digitalWrite(relayPTCPin, LOW);
            }
          } else if (name == "LED") {
            if (value == 1) {
              ledActive = 1;
            } else {
              rgbLed.setRGB(0, 0, 0);
              ledActive = 0;
            }
          } else if (name == "RGB_LED" && ledActive == 1) {
            String rgb = device["value_string"].as<String>();
            String rValue = getValue(rgb, ':', 0);
            String gValue = getValue(rgb, ':', 1);
            String bValue = getValue(rgb, ':', 2);
            rgbLed.setRGB(rValue.toInt(), gValue.toInt(), bValue.toInt());
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

String getValue(String data, char separator, int index) {
  int found = 0;
  int strIndex[] = { 0, -1 };
  int maxIndex = data.length() - 1;

  for (int i = 0; i <= maxIndex && found <= index; i++) {
    if (data.charAt(i) == separator || i == maxIndex) {
      found++;
      strIndex[0] = strIndex[1] + 1;
      strIndex[1] = (i == maxIndex) ? i + 1 : i;
    }
  }

  return found > index ? data.substring(strIndex[0], strIndex[1]) : "";
}