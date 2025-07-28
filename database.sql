CREATE TABLE "player" (
  "id" integer PRIMARY KEY,
  "name" varchar(30),
  "email" varchar(40) UNIQUE,
  "password" varchar(30),
  "location" varchar(50)
);

CREATE TABLE "appointment" (
  "id" integer PRIMARY KEY,
  "date" date,
  "timestamp" timestamp,
  "location" varchar(50),
  "host_id" integer
);

CREATE TABLE "player_appointment" (
  "player_id" integer,
  "appointment_id" integer,
  PRIMARY KEY ("player_id", "appointment_id")
);

CREATE TABLE "game" (
  "id" integer PRIMARY KEY,
  "name" varchar(30),
  "description" text
);

CREATE TABLE "game_suggestion" (
  "id" integer PRIMARY KEY,
  "game_id" integer,
  "appointment_id" integer
);

CREATE TABLE "game_vote" (
  "player_id" integer,
  "game_suggestion_id" integer,
  "vote_value" bool,
  PRIMARY KEY ("player_id", "game_suggestion_id")
);

CREATE TABLE "evaluation" (
  "id" integer PRIMARY KEY,
  "player_id" integer,
  "appointment_id" integer,
  "host_evaluation" integer,
  "overall_evaluation" integer
);

CREATE TABLE "message" (
  "id" integer PRIMARY KEY,
  "message_from_player_id" integer,
  "appointment_id" integer,
  "timestamp" timestamp,
  "message_content" text
);

ALTER TABLE "appointment" ADD FOREIGN KEY ("host_id") REFERENCES "player" ("id");

ALTER TABLE "player_appointment" ADD FOREIGN KEY ("player_id") REFERENCES "player" ("id");

ALTER TABLE "player_appointment" ADD FOREIGN KEY ("appointment_id") REFERENCES "appointment" ("id");

ALTER TABLE "game_suggestion" ADD FOREIGN KEY ("game_id") REFERENCES "game" ("id");

ALTER TABLE "game_suggestion" ADD FOREIGN KEY ("appointment_id") REFERENCES "appointment" ("id");

ALTER TABLE "game_vote" ADD FOREIGN KEY ("player_id") REFERENCES "player" ("id");

ALTER TABLE "game_vote" ADD FOREIGN KEY ("game_suggestion_id") REFERENCES "game_suggestion" ("id");

ALTER TABLE "evaluation" ADD FOREIGN KEY ("player_id") REFERENCES "player" ("id");

ALTER TABLE "evaluation" ADD FOREIGN KEY ("appointment_id") REFERENCES "appointment" ("id");

ALTER TABLE "message" ADD FOREIGN KEY ("message_from_player_id") REFERENCES "player" ("id");

ALTER TABLE "message" ADD FOREIGN KEY ("appointment_id") REFERENCES "appointment" ("id");

-- player
INSERT INTO player (id, name, email, password, location) VALUES
  (1, 'Anna Schmidt', 'anna@example.com', 'pass123', 'Berlin'),
  (2, 'Ben Müller', 'ben@example.com', 'geheim', 'Hamburg'),
  (3, 'Clara Fischer', 'clara@example.com', 'pw456', 'München'),
  (4, 'David Weber', 'david@example.com', 'davidpw', 'Köln'),
  (5, 'Eva Braun', 'eva@example.com', 'evapw', 'Frankfurt'),
  (6, 'Felix König', 'felix@example.com', 'felixpw', 'Stuttgart');

-- appointment
INSERT INTO appointment (id, date, timestamp, location, host_id) VALUES
  (1, '2024-07-01', '2024-07-01 18:00:00', 'Berlin', 1),
  (2, '2024-07-15', '2024-07-15 19:00:00', 'Hamburg', 2),
  (3, '2024-08-05', '2024-08-05 17:30:00', 'München', 3),
  (4, '2024-08-20', '2024-08-20 20:00:00', 'Köln', 4),
  (5, '2024-09-01', '2024-09-01 19:00:00', 'Berlin', 1);

-- player_appointment
INSERT INTO player_appointment (player_id, appointment_id) VALUES
  (1, 1),
  (2, 1),
  (3, 1),
  (2, 2),
  (3, 2),
  (4, 2),
  (1, 3),
  (3, 3),
  (5, 3),
  (4, 4),
  (5, 4),
  (6, 4);

-- game
INSERT INTO game (id, name, description) VALUES
  (1, 'Codenames', 'Wortspiel für Teams'),
  (2, 'Carcassonne', 'Lege- und Strategiespiel'),
  (3, 'Azul', 'Fliesenlegespiel'),
  (4, '7 Wonders', 'Kartenspiel mit Zivilisationsaufbau'),
  (5, 'Just One', 'Kooperatives Partyspiel');

-- game_suggestion
INSERT INTO game_suggestion (id, game_id, appointment_id) VALUES
  (1, 1, 1),
  (2, 2, 1),
  (3, 3, 2),
  (4, 4, 3),
  (5, 5, 4);

-- game_vote
INSERT INTO game_vote (player_id, game_suggestion_id, vote_value) VALUES
  (1, 1, TRUE),
  (2, 1, FALSE),
  (3, 1, TRUE),
  (1, 2, FALSE),
  (2, 2, TRUE),
  (3, 3, TRUE),
  (4, 4, TRUE),
  (5, 4, FALSE),
  (6, 5, TRUE);

-- evaluation
INSERT INTO evaluation (id, player_id, appointment_id, host_evaluation, overall_evaluation) VALUES
  (1, 1, 1, 4, 5),
  (2, 2, 1, 5, 4),
  (3, 3, 1, 4, 3),
  (4, 2, 2, 5, 5),
  (5, 3, 2, 4, 4),
  (6, 4, 2, 3, 3);

-- message
INSERT INTO message (id, message_from_player_id, appointment_id, timestamp, message_content) VALUES
  (1, 1, 1, '2024-06-30 12:00:00', 'Freue mich auf morgen!'),
  (2, 2, 1, '2024-06-30 13:00:00', 'Ich bringe Snacks mit.'),
  (3, 3, 2, '2024-07-14 18:00:00', 'Wer kommt alles?'),
  (4, 4, 2, '2024-07-14 19:00:00', 'Ich kann etwas später kommen.'),
  (5, 5, 3, '2024-08-04 20:00:00', 'Brauchen wir noch Getränke?');
