#include <Arduino.h>
#include <WiFi.h>
#include <WiFiMulti.h>
#include <HTTPClient.h>
#include <ArduinoJson.h>

#define USE_SERIAL Serial

WiFiMulti wifiMulti;

// Define relay pin
const int relayPTCPin = 2;
const int relayFANPin = 2;

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
    http.begin("http://10.72.107.112:8080/api/dev"); 

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
        int value = doc["value"];
        String name = doc["name"];


        // Check if value is 1
        if (value == 1 && name == "LED") {
          // Turn on relay to activate the fan
          digitalWrite(relayPTCPin, HIGH);
          digitalWrite(relayFANPin, HIGH);
          USE_SERIAL.println("Fan and PTC activated");
        } else {
          // Turn off relay
          digitalWrite(relayPTCPin, LOW);
          digitalWrite(relayFANPin, LOW);
          USE_SERIAL.println("Fan and PTC deactivated");
        }

         if (value == 1 && name == "Temperature Control") {
          // Turn on relay to activate the fan
          digitalWrite(relayPTCPin, HIGH);
          digitalWrite(relayFANPin, HIGH);
          USE_SERIAL.println("Fan and PTC activated");
        } else {
          // Turn off relay
          digitalWrite(relayPTCPin, LOW);
          digitalWrite(relayFANPin, LOW);
          USE_SERIAL.println("Fan and PTC deactivated");
        }

         if (value == 1 && name == "Reagroup Button") {
          // Turn on relay to activate the fan
          digitalWrite(relayPTCPin, HIGH);
          digitalWrite(relayFANPin, HIGH);
          USE_SERIAL.println("Fan and PTC activated");
        } else {
          // Turn off relay
          digitalWrite(relayPTCPin, LOW);
          digitalWrite(relayFANPin, LOW);
          USE_SERIAL.println("Fan and PTC deactivated");
        }
      }
    } else {
      USE_SERIAL.printf("[HTTP] GET... failed, error: %s\n", http.errorToString(httpCode).c_str());
    }
    http.end();
  }
  delay(5000);
}
