# DLBCSEMSE02_D
Aufgabenstellung 2: Board-Gamer-App

UI-Konzept
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