# DLBCSEMSE02_D
Aufgabenstellung 2: Board-Gamer-App

## UI-Konzept
Link zum Testen des Prototyps: https://www.figma.com/proto/Q4iVmpl5158N9qSRTyhGiQ/Untitled?node-id=6-1290&p=f&t=iT2xymim2e7K3vGC-1&scaling=scale-down&content-scaling=fixed&page-id=0%3A1&starting-point-node-id=6%3A1290

## Startseite, Treffen erstellen und Spielebibliothek
<img width="360" height="1112" alt="Startseite" src="https://github.com/user-attachments/assets/eae29cc8-2567-435a-b4fa-bae3403ccfc3" />
<img width="360" height="800" alt="Spielebibliothek" src="https://github.com/user-attachments/assets/04fec6e4-4f99-4fd2-a6b4-44118ddc0e3d" />
<img width="360" height="800" alt="Neues Treffen" src="https://github.com/user-attachments/assets/a96d5f24-a017-491f-8d42-45eba92841aa" />

## Infos zum Event, weitere Spiele vorschlagen und optionale Nachricht hinterlassen
<img width="360" height="800" alt="Infos" src="https://github.com/user-attachments/assets/5e69d378-4683-4660-9fae-55e6599c7076" />
<img width="360" height="1143" alt="Weiteres Spiel vorschlagen" src="https://github.com/user-attachments/assets/57c6b6af-fbdc-4277-86a5-3832bd377236" />
<img width="360" height="1032" alt="Nachricht schreiben" src="https://github.com/user-attachments/assets/3fad927b-ffcd-4643-b6a0-2dc3099edad0" />

## Eigenes Profil
<img width="360" height="800" alt="Dein Profil" src="https://github.com/user-attachments/assets/aecb35f8-571d-4bed-b71f-a27f6c41f291" />

## Am Event teilnehmen
<img width="360" height="1224" alt="Teilnehmen" src="https://github.com/user-attachments/assets/db27fad1-a0a2-4f83-be0c-5563defc7b7d" />

## Event bewerten
<img width="360" height="826" alt="Abend bewerten" src="https://github.com/user-attachments/assets/d9ae81fa-2817-4bba-a12c-518dc6f57dd2" />

## ER-Diagramm der Datenbankstruktur

<img width="1060" height="900" alt="ER-Diagramm Projekt" src="https://github.com/user-attachments/assets/6fdb1be0-e799-4681-a0b9-6aae89b43ef8" />

## C4 Diagramm

![BoardGamerApp](/Docs/c4.svg)

## Using Swagger UI

Acess after starting docker compose.

```
http://localhost:8000/docs 
```

## API ENDPUNKTE – KURZBESCHREIBUNG

### AUTHENTIFIZIERUNG & REGISTRIERUNG

 * POST /authenticate/{name}/{password}
   Authentifiziert einen Spieler mit Name und Passwort.

 * POST /register/
   Registriert einen neuen Spieler.

 * GET /player/{player_id}
   Gibt die Daten eines Spielers zurück.

 * GET /players
   Listet alle Spieler auf.

 * GET /isNextHost/{player_id}
   Prüft, ob der Spieler als nächster Gastgeber an der Reihe ist.

### APPOINTMENTS (SPIELEABENDE)

 * GET /appointments
   Gibt die letzten 5 Termine zurück.

 * POST /appointments/insert/
   Fügt einen neuen Termin hinzu.

 * POST /appointments/update/
   Aktualisiert einen bestehenden Termin.

### PLAYER-APPOINTMENT

 * GET /playerAppointments
   Listet alle player_appointments auf.

 * POST /playerAppointment/insert/
   Fügt einen neuen player_appointment hinzu.

### SPIELE & VORSCHLÄGE

 * GET /games
   Listet alle Spiele auf.

 * POST /game/insert/
   Fügt ein neues Spiel hinzu.

 * GET /gameSuggestions/{appointmentId}
   Gibt alle Spielvorschläge für einen Termin zurück.

 * POST /gameSuggestions/insert/
   Fügt Spielvorschläge für einen Termin hinzu.

### SPIEL-VOTES

 * POST /gameVotes/insert/
   Fügt eine neue Spiel-Abstimmung hinzu.

 * POST /gameVotes/update/
   Aktualisiert eine bestehende Abstimmung.

 * GET /gameVotes/{appointmentId}/{playerId}
   Gibt alle Votes eines Spielers für einen Termin zurück.

 * GET /gameVotes/{appointmentId}
   Gibt alle Votes für einen Termin zurück.

### EVALUATIONS

 * POST /evaluations/insert/
   Fügt eine Bewertung hinzu.

 * GET /evaluations/{appointmentId}
   Listet alle Bewertungen für einen Termin auf.

### NACHRICHTEN

 * POST /messages/insert/
   Fügt eine Nachricht zu einem Termin hinzu.

 * GET /messages/{appointmentId}
   Listet alle Nachrichten zu einem Termin auf.

## Development

### docker compose

Start postgres and backend instance via docker compose

```bash
docker compose up --build
```

### Prepare venv environment for Backend

```bash
cd Backend
./prepare.sh
```

### Update requirements for Backend

Add new dependencies to `./dev_requirements.txt` and run:

```bash
cd Backend
./prepare.sh
```

### Run Backend as python code

```bash
cd Backend
./run.sh
```

### Prepare test venv environment for backend

```bash
cd Backend
./prepare_test.sh
```

### Update test requirements for backend

Add new dependencies to `./dev_test_requirements.txt` and run:

```bash
cd Backend
./prepare_test.sh
```

### Run tests for backend

```bash
cd Backend
./run_tests.sh
```

## Work with the Frontend

Das Frontend ist eine Android App. Daher wird empfohlen mit Android Studio zu arbeiten. 
Dies ist eine kostenlose IDE auf Basis von Jetbrains IntelliJ welche speziel für die Entwicklung von Android Apps gedacht ist.

### Entwicklung an der App

Einfach den BroadGamer Ordner im Frontend Ordner in Android Studio öffnen als Projekt
Danach einmal warten bis der gradle sync durchgelaufen ist & danach sollte alles einsatz bereit sein um die App zu bauen & auch die Unit Test auszuführen.

### Dependencies Frontend

2 Schritte sind hierfür nötig:
1. Dependency in der libs.version.toml datei hinzufügen. Das Pattern hierfür kann in den vorhandenen abgelesen werden
2. Danach in der build.gradle.kts datei von dem app module die Abhängigkeit in dem dependencies teil hinzufügen. Pattern kann wieder von vorhandenen abgeguckt werden

### App bauen und laufen lassen

Entweder ein Android smartphone anschließen welches erlaubt über adb apk's zu installieren oder einen emulator in android studio aufsetzen
Danach über den Run Knopf in Android Studio bauen lassen & auf dem Gerät/Emulator installieren
Die App sollte sich dann automatisch öffnen wenn der build & install prozess durchgelaufen ist.

### Unit Tests bauen

Einfach die Unit Test datei in Android Studio öffnen
Diese hat dann in der UI eine Art Button um einzelne Test fälle oder auch eine ganze Klasse auszuführen
Unabhängig davon kann auch die Gradle Task: testDebugUnitTest oder testReleaseUnitTest ausgeführt werden. Diese lässt *alle* Unit Test laufen
