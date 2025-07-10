# DLBCSEMSE02_D
Aufgabenstellung 2: Board-Gamer-App



##ER-Diagramm der Datenbankstruktur

<img width="1809" height="1353" alt="ER-Diagramm" src="https://github.com/user-attachments/assets/9719f04e-05af-4056-af2c-1aff4c560ee2" />

## Development

### docker compose

Start postgres instance via docker compose

```bash
docker compose up
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

