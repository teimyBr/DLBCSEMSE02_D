# DLBCSEMSE02_D
Aufgabenstellung 2: Board-Gamer-App



##ER-Diagramm der Datenbankstruktur

![ER-Diagramm](https://github.com/user-attachments/assets/ed9f37de-7966-47d2-93a8-ae8a63f4440c)

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

