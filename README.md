# DLBCSEMSE02_D
Aufgabenstellung 2: Board-Gamer-App

## App-Präsentation
[app_show2.webm](https://github.com/user-attachments/assets/0b3ba947-0b7c-439c-9b2e-bfc753778e30)


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
   authenticate a player through name and password

 * POST /register/
   register a new player

 * GET /player/{player_id}
   returns the player with the given id

 * GET /players
   returns all players

 * GET /isNextHost/{player_id}
   checks if the player with the given id is a potential next host

### APPOINTMENTS (SPIELEABENDE)

 * GET /appointments
   returns the last five appointments

 * POST /appointments/insert/
   adds a new appointment

 * POST /appointments/update/
   updates a appointment

### PLAYER-APPOINTMENT

 * GET /playerAppointments
   returns all player appointment links

 * POST /playerAppointment/insert/
   adds a new player appointment link

### SPIELE & VORSCHLÄGE

 * GET /games
   returns a list of all games

 * POST /game/insert/
   adds a new game

 * GET /gameSuggestions/{appointmentId}
   returns all game suggestion associated with the given appointment id

 * POST /gameSuggestions/insert/
   adds a list of game suggestions

### SPIEL-VOTES

 * POST /gameVotes/insert/
   adds a new game vote

 * POST /gameVotes/update/
   updates a game vote

 * GET /gameVotes/{appointmentId}/{playerId}
   returns all game votes a player made for a given appointment

 * GET /gameVotes/{appointmentId}
   returns all game votes for a given appointment

### EVALUATIONS

 * POST /evaluations/insert/
   adds a new evaluation

 * GET /evaluations/{appointmentId}
   returns all evaluation for a appointment

### NACHRICHTEN

 * POST /messages/insert/
   adds a new message

 * GET /messages/{appointmentId}
   returns all messages for a appointment

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

The Frontend is a Android Application. It has been developed using Android Studio.
Android Studio is a free software, so it is recommended to use it to work on the Frontend app

### Develop the App

To work on the project, just open the BoardGamer folder in the frontend folder as a project in Android Studio.
When first opening the project make sure to let the gradle sync run. Without a successful gradle sync development is not possible. After the sync was successful it should be possible to work and develop on the app as well as the tests


### Dependencies Frontend

2 Schritte sind hierfür nötig:
1. Dependency in der libs.version.toml datei hinzufügen. Das Pattern hierfür kann in den vorhandenen abgelesen werden
2. Danach in der build.gradle.kts datei von dem app module die Abhängigkeit in dem dependencies teil hinzufügen. Pattern kann wieder von vorhandenen inspiriert werden

### Build and run App

**Important** The app can only be tested if the following requirements are met:
1. The Backend needs to running an reachable in the network of the executing phone or emulator
2. The BackendAPI class needs to have the basicAddress set to the URL that represent the Backend in the network

Right now the BackendAPI is hard coded to the address that would represent local host of the running device on a emulator.

To run the app either a android phone or a emualtor is needed.
When android studio successfully finds the target device it will be shown in the UI. Afterwards just select the app build task & press the run button. 
After the build is finished it should be installed on the device and then launched.
If there are issue, it may be necessary to install the command line tools through the sdk manager offered by Android Studio.

### Run Tests

The tests can be run through Android Studio.
When you open a test class the UI will show run buttons next to test cases and also next to classes containing tests.
These will either run a test or *all* tests in a class.

At the moment the following two test files exist:
1. APITest: Integration test that check the connection to the Backend through the BackendAPI class
2. JsonConversionTests: Unit Test that check the conversion from and to JSON of the different models

**Note** The integrationstest for the Backend connection may fail. The state of the Backend is not reset after running a test. This means that while the first execution may be successfull, following executions may fail until the database is reset to the initial state by deleting the docker & the database volume.

**Important** The intergrationtest with the Backend can only be run when the Backend is running and reachable. The Address given to the creation of the BackendAPI class instance needs to represent the basic route in the local network of the executing device that connects to the Backend. Right now that route is hard coded to the localhost address that is valid, when the backend docker is running on the same device.
