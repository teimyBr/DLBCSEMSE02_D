# DLBCSEMSE02_D
Aufgabenstellung 2: Board-Gamer-App



## ER-Diagramm der Datenbankstruktur

<img width="1809" height="1353" alt="ER-Diagramm" src="https://github.com/user-attachments/assets/9719f04e-05af-4056-af2c-1aff4c560ee2" />

## Using Swagger UI

Acess after starting docker compose.

```
http://localhost:8000/docs 
```


## BOARDGAME FASTAPI – API DOKUMENTATION


Diese API verwaltet Spieler, Spieleabende, Spielvorschläge, Abstimmungen, Essenswünsche, Bewertungen und Nachrichten für eine Brettspielgruppe.


### AUTHENTIFIZIERUNG & REGISTRIERUNG

 * POST /authenticate/{name}/{password}
   Authentifiziert einen Spieler anhand Name und Passwort.
   Antwort: Spieler-Objekt oder Fehler.

 * POST /register/
   Registriert einen neuen Spieler.
   Body:```json
  {
    "name": "string",
    "email": "string",
    "password": "string",
    "location": "string",
    "favourite_food_id": 1
  }```
    Antwort:```json
  { "id": 1 }```
    oder```json
  { "id": -1 }```

### APPOINTMENTS (SPIELEABENDE)

 * GET /appointments
   Gibt die letzten 5 Termine zurück.

 * POST /appointments/insert/
   Fügt einen neuen Termin hinzu.
   Body:```json
  {
    "date": "YYYY-MM-DD",
    "timestamp": "YYYY-MM-DDTHH:MM:SS",
    "location": "string",
    "host_id": 1
  }```
    Antwort:```json
  { "id": 1 }```
   * POST /appointments/update/
   Aktualisiert einen bestehenden Termin.
   Body: wie oben, zusätzlich "id": 1
   Antwort:```json
  { "success": true }```


### SPIELE & VORSCHLÄGE

 * GET /games
   Listet alle Spiele.

 * GET /gameSuggestions/{appointmentId}
   Listet alle Spielvorschläge für einen Termin.

 * POST /gameSuggestions/insert/
   Fügt Spielvorschläge für einen Termin hinzu.
   Body:```json
  [
    { "game_id": 1, "appointment_id": 1 },
    { "game_id": 2, "appointment_id": 1 }
  ]```
    Antwort:```json
  { "success": true }```


### SPIEL-VOTES

 * POST /gameVotes/insert/
   Fügt eine neue Spiel-Abstimmung hinzu.
   Body:```json
  {
    "player_id": 1,
    "game_suggestion_id": 1,
    "vote_value": true
  }```
    Antwort:```json
  { "id": 1 }```
   * POST /gameVotes/update/
   Aktualisiert eine bestehende Abstimmung.
   Body: wie oben
   Antwort:```json
  { "success": true }```
   * GET /gameVotes/{appointmentId}/{playerId}
   Gibt alle Votes eines Spielers für einen Termin zurück.

 * GET /gameVotes/{appointmentId}
   Gibt alle Votes für einen Termin zurück.

### FOOD DIRECTIONS & CHOICES

 * GET /foodDirections
   Listet alle Essensrichtungen.

 * POST /foodChoices/insert/{appointmentId}/{playerId}/{foodDirectionId}
   Fügt eine Essenswahl hinzu.
   Antwort:```json
  { "id": 1 }```
   * GET /foodChoices/{appointmentId}
   Listet alle Essenswahlen für einen Termin.

 * GET /foodChoices/{appointmentId}/{playerId}
   Gibt die Essenswahl eines Spielers für einen Termin zurück.

### EVALUATIONS

 * POST /evaluations/insert/
   Fügt eine Bewertung hinzu.
   Body:```json
  {
    "player_id": 1,
    "appointment_id": 1,
    "meal_evaluation": 5,
    "host_evaluation": 4,
    "overall_evaluation": 5
  }```
    Antwort:```json
  { "id": 1 }```
   * GET /evaluations/{appointmentId}
   Listet alle Bewertungen für einen Termin.

### NACHRICHTEN

 * POST /messages/insert/
   Fügt eine Nachricht hinzu.
   Body:```json
  {
    "message_from_player_id": 1,
    "appointment_id": 1,
    "timestamp": "YYYY-MM-DDTHH:MM:SS",
    "message_content": "string"
  }```
    Antwort:```json
  { "id": 1 }```
   * GET /messages/{appointmentId}
   Listet alle Nachrichten zu einem Termin.


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

