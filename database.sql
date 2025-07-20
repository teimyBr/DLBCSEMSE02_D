CREATE TABLE "player" (
  "id" integer PRIMARY KEY,
  "name" varchar(30),
  "email" varchar(40) UNIQUE,
  "password" varchar(30),
  "location" varchar(50),
  "favourite_food_id" integer
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
  "meal_evaluation" integer,
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

CREATE TABLE "food_direction" (
  "id" integer PRIMARY KEY,
  "designation" varchar(30)
);

CREATE TABLE "food_choice" (
  "id" integer PRIMARY KEY,
  "player_id" integer,
  "appointment_id" integer,
  "food_direction_id" integer
);

CREATE TABLE "delivery_service" (
  "id" integer PRIMARY KEY,
  "service_name" varchar(30),
  "service_location" varchar(30),
  "food_direction_id" integer
);

CREATE TABLE "delivery_service_menu" (
  "id" integer PRIMARY KEY,
  "delivery_service_id" integer,
  "menu_item" varchar(30),
  "menu_price" numeric(5,2)
);

CREATE TABLE "food_order" (
  "id" integer PRIMARY KEY,
  "player_id" integer,
  "delivery_service_menu_id" integer,
  "appointment_id" integer
);

ALTER TABLE "player" ADD FOREIGN KEY ("favourite_food_id") REFERENCES "food_direction" ("id");

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

ALTER TABLE "food_choice" ADD FOREIGN KEY ("player_id") REFERENCES "player" ("id");

ALTER TABLE "food_choice" ADD FOREIGN KEY ("appointment_id") REFERENCES "appointment" ("id");

ALTER TABLE "food_choice" ADD FOREIGN KEY ("food_direction_id") REFERENCES "food_direction" ("id");

ALTER TABLE "delivery_service" ADD FOREIGN KEY ("food_direction_id") REFERENCES "food_direction" ("id");

ALTER TABLE "delivery_service_menu" ADD FOREIGN KEY ("delivery_service_id") REFERENCES "delivery_service" ("id");

ALTER TABLE "food_order" ADD FOREIGN KEY ("player_id") REFERENCES "player" ("id");

ALTER TABLE "food_order" ADD FOREIGN KEY ("delivery_service_menu_id") REFERENCES "delivery_service_menu" ("id");

ALTER TABLE "food_order" ADD FOREIGN KEY ("appointment_id") REFERENCES "appointment" ("id");

-- food_direction
INSERT INTO food_direction (id, designation) VALUES
  (1, 'Italienisch'),
  (2, 'Griechisch'),
  (3, 'Deutsch'),
  (4, 'Türkisch'),
  (5, 'Amerikanisch'),
  (6, 'Asiatisch'),
  (7, 'Egal');

-- player
INSERT INTO player (id, name, email, password, location, favourite_food_id) VALUES
  (1, 'Anna Schmidt', 'anna@example.com', 'pass123', 'Berlin', 1),
  (2, 'Ben Müller', 'ben@example.com', 'geheim', 'Hamburg', 2),
  (3, 'Clara Fischer', 'clara@example.com', 'pw456', 'München', 3),
  (4, 'David Weber', 'david@example.com', 'davidpw', 'Köln', 4),
  (5, 'Eva Braun', 'eva@example.com', 'evapw', 'Frankfurt', 5),
  (6, 'Felix König', 'felix@example.com', 'felixpw', 'Stuttgart', 6);

-- appointment
INSERT INTO appointment (id, date, timestamp, location, host_id) VALUES
  (1, '2024-07-01', '2024-07-01 18:00:00', 'Berlin', 1),
  (2, '2024-07-15', '2024-07-15 19:00:00', 'Hamburg', 2),
  (3, '2024-08-05', '2024-08-05 17:30:00', 'München', 3),
  (4, '2024-08-20', '2024-08-20 20:00:00', 'Köln', 4);

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
INSERT INTO evaluation (id, player_id, appointment_id, meal_evaluation, host_evaluation, overall_evaluation) VALUES
  (1, 1, 1, 5, 4, 5),
  (2, 2, 1, 4, 5, 4),
  (3, 3, 1, 3, 4, 3),
  (4, 2, 2, 5, 5, 5),
  (5, 3, 2, 4, 4, 4),
  (6, 4, 2, 3, 3, 3);

-- message
INSERT INTO message (id, message_from_player_id, appointment_id, timestamp, message_content) VALUES
  (1, 1, 1, '2024-06-30 12:00:00', 'Freue mich auf morgen!'),
  (2, 2, 1, '2024-06-30 13:00:00', 'Ich bringe Snacks mit.'),
  (3, 3, 2, '2024-07-14 18:00:00', 'Wer kommt alles?'),
  (4, 4, 2, '2024-07-14 19:00:00', 'Ich kann etwas später kommen.'),
  (5, 5, 3, '2024-08-04 20:00:00', 'Brauchen wir noch Getränke?');

-- food_choice
INSERT INTO food_choice (id, player_id, appointment_id, food_direction_id) VALUES
  (1, 1, 1, 1),
  (2, 2, 1, 2),
  (3, 3, 1, 3),
  (4, 4, 2, 4),
  (5, 2, 2, 5),
  (6, 5, 3, 6),
  (7, 1, 3, 7)

-- delivery_service
INSERT INTO delivery_service (id, service_name, service_location, food_direction_id) VALUES
  (1, 'Pizza Express', 'Berlin', 1),
  (2, 'Asia Wok', 'Hamburg', 6),
  (3, 'Burger House', 'München', 5);

-- delivery_service_menu
INSERT INTO delivery_service_menu (id, delivery_service_id, menu_item, menu_price) VALUES
  (1, 1, 'Margherita', 8.50),
  (2, 1, 'Salami', 9.00),
  (3, 2, 'Sushi Set', 12.00),
  (4, 3, 'Cheeseburger', 10.00);

-- food_order
INSERT INTO food_order (id, player_id, delivery_service_menu_id, appointment_id) VALUES
  (1, 1, 1, 1),
  (2, 2, 3, 1),
  (3, 3, 2, 1);
