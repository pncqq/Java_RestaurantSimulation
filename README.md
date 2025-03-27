# 🍽 Java_RestaurantSimulation

Symulacja restauracji z wykorzystaniem **wielowątkowości** oraz rozbudowanej logiki zarządzania zamówieniami, personelem i menu. Projekt w języku **Java**, przygotowany w ramach zajęć akademickich.

## 📂 Zawartość repozytorium

- `src/` – główny kod źródłowy aplikacji
- `menu.txt` – przykładowe menu wczytywane z pliku
- `RestaurantSimulation.iml` – plik konfiguracyjny IntelliJ
- `.idea/` – pliki środowiska IntelliJ IDEA

## ⚙️ Technologie

- Java 17+
- Programowanie obiektowe (OOP)
- Programowanie współbieżne (multithreading)

## 🪜 Funkcjonalności

- Składanie zamówień:
  - na miejscu (dla stolika)
  - na wynos (z obsługą dostawy)
- Kolejkowanie i priorytetyzacja zamówień
- Obsługa opóźnień i przeterminowań
- Edycja menu (dodawanie/usuwanie pozycji)
- Zapisywanie i ładowanie menu z pliku
- Baza danych pracowników:
  - przypisywanie ról
  - dodawanie/usuwanie pracowników
- System napiwków i dziennych przychodów
- Obsługa zamknięcia/otwarcia restauracji
- Modyfikacja czasu realizacji zamówień w zależności od liczby kucharzy

## 🚀 Jak uruchomić

1. Otwórz projekt w IntelliJ IDEA lub innym IDE wspierającym Maven/Java.
2. Uruchom klasę główną (np. `Main.java` w katalogu `src/`).
3. Upewnij się, że plik `menu.txt` znajduje się w katalogu głównym projektu.

## 👨‍💼 Autor
**Filip Michalski**  
Projekt zrealizowany w ramach kursu z programowania w języku Java. 
Prezentuje praktyczne zastosowanie wielowątkowości oraz organizacji systemu zarządzania w symulowanym środowisku restauracyjnym.
