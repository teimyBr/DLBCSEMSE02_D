# DLBCSEMSE02_D
Aufgabenstellung 2: Board-Gamer-App



## ER-Diagramm der Datenbankstruktur

<img width="1809" height="1353" alt="ER-Diagramm" src="https://github.com/user-attachments/assets/9719f04e-05af-4056-af2c-1aff4c560ee2" />

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


### APPOINTMENTS (SPIELEABENDE)

 * GET /appointments
   Gibt die letzten 5 Termine zurück.

 * POST /appointments/insert/
   Fügt einen neuen Termin hinzu.

 * POST /appointments/update/
   Aktualisiert einen bestehenden Termin.


### SPIELE & VORSCHLÄGE

 * GET /games
   Listet alle Spiele auf.

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


### FOOD DIRECTIONS & CHOICES

 * GET /foodDirections
   Listet alle Essensrichtungen auf.

 * POST /foodChoices/insert/{appointmentId}/{playerId}/{foodDirectionId}
   Fügt eine Essenswahl für einen Termin hinzu.

 * GET /foodChoices/{appointmentId}
   Listet alle Essenswahlen für einen Termin auf.

 * GET /foodChoices/{appointmentId}/{playerId}
   Gibt die Essenswahl eines Spielers für einen Termin zurück.


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

