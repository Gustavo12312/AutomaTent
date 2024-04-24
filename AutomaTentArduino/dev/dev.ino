#include <Arduino.h>
#include <WiFi.h>
#include <WiFiMulti.h>
#include <HTTPClient.h>
#include <ArduinoJson.h>

#define USE_SERIAL Serial

WiFiMulti wifiMulti;

// Define relay pin
const int relayPTCPin = 32;
const int relayFANPin = 25;

void setup() {
  USE_SERIAL.begin(115200);
  USE_SERIAL.println();
  USE_SERIAL.println();
  USE_SERIAL.println();

  for (uint8_t t = 4; t > 0; t--) {
    USE_SERIAL.printf("[SETUP] WAIT %d...\n", t);
    USE_SERIAL.flush();
    delay(1000);
  }

  wifiMulti.addAP("Visitors", "");
  pinMode(relayPTCPin, OUTPUT);
  pinMode(relayFANPin, OUTPUT);
}

void loop() {
  // wait for WiFi connection
  if ((wifiMulti.run() == WL_CONNECTED)) {
    HTTPClient http;

    USE_SERIAL.print("[HTTP] begin...\n");
    http.begin("http://10.72.125.120:8080/api/dev"); 

    USE_SERIAL.print("[HTTP] GET...\n");
    int httpCode = http.GET();

    if (httpCode > 0) {
      USE_SERIAL.printf("[HTTP] GET... code: %d\n", httpCode);

      if (httpCode == HTTP_CODE_OK) {
        String payload = http.getString();
        USE_SERIAL.println(payload);

        // Parse JSON
        DynamicJsonDocument doc(1024);
        deserializeJson(doc, payload);

        // Extract value from JSON
        for (JsonObject device : doc.as<JsonArray>()) {
          String name = device["name"].as<String>();
          int value = device["value"].as<int>();

        

          if (name == "FAN" && value == 1) {
            // Turn on relay to activate the fan
           digitalWrite(relayFANPin, HIGH);
            USE_SERIAL.println("Fan activated");
          } else if (name == "HEATER" && value == 1) {
            // Turn on relay to activate the fan
            digitalWrite(relayPTCPin, HIGH);
            USE_SERIAL.println("PTC activated");
            // Turn off relay
            
          } else {
            // Turn off relay
            digitalWrite(relayFANPin, LOW);
            digitalWrite(relayPTCPin, LOW);
          }
        }
      }
    } else {
      USE_SERIAL.printf("[HTTP] GET... failed, error: %s\n", http.errorToString(httpCode).c_str());
    }
    http.end();
  }
  delay(1000);
}
