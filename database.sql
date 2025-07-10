CREATE TABLE "player" (
  "id" integer PRIMARY KEY,
  "name" varchar(30),
  "email" varchar(40) UNIQUE,
  "password" varchar(30),
  "password_repeat" varchar(30),
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
  "player_id" integer,
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

ALTER TABLE "game_suggestion" ADD FOREIGN KEY ("player_id") REFERENCES "player" ("id");

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
