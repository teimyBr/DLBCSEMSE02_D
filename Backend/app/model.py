import os
from datetime import date, datetime
from pydantic import BaseModel, Field
from typing import List, Optional
from sqlalchemy.ext.asyncio import create_async_engine, AsyncSession
from sqlalchemy.orm import sessionmaker, declarative_base
from sqlalchemy import Column, Integer, String, Boolean, Date, Text, ForeignKey, Numeric, TIMESTAMP, UniqueConstraint, select, insert, update

Base = declarative_base()

class Player(Base):
    __tablename__ = "player"
    id = Column(Integer, primary_key=True)
    name = Column(String(30))
    email = Column(String(40), unique=True)
    password = Column(String(30))
    location = Column(String(50))
    favourite_food_id = Column(Integer, ForeignKey("food_direction.id"))

class Appointment(Base):
    __tablename__ = "appointment"
    id = Column(Integer, primary_key=True)
    date = Column(Date)
    timestamp = Column(TIMESTAMP)
    location = Column(String(50))
    host_id = Column(Integer, ForeignKey("player.id"))

class Game(Base):
    __tablename__ = "game"
    id = Column(Integer, primary_key=True)
    name = Column(String(30))
    description = Column(Text)

class GameSuggestion(Base):
    __tablename__ = "game_suggestion"
    id = Column(Integer, primary_key=True)
    game_id = Column(Integer, ForeignKey("game.id"))
    appointment_id = Column(Integer, ForeignKey("appointment.id"))

class GameVote(Base):
    __tablename__ = "game_vote"
    player_id = Column(Integer, ForeignKey("player.id"), primary_key=True)
    game_suggestion_id = Column(Integer, ForeignKey("game_suggestion.id"), primary_key=True)
    vote_value = Column(Boolean)

class FoodDirection(Base):
    __tablename__ = "food_direction"
    id = Column(Integer, primary_key=True)
    designation = Column(String(30))

class FoodChoice(Base):
    __tablename__ = "food_choice"
    id = Column(Integer, primary_key=True)
    player_id = Column(Integer, ForeignKey("player.id"))
    appointment_id = Column(Integer, ForeignKey("appointment.id"))
    food_direction_id = Column(Integer, ForeignKey("food_direction.id"))

class Evaluation(Base):
    __tablename__ = "evaluation"
    id = Column(Integer, primary_key=True)
    player_id = Column(Integer, ForeignKey("player.id"))
    appointment_id = Column(Integer, ForeignKey("appointment.id"))
    meal_evaluation = Column(Integer)
    host_evaluation = Column(Integer)
    overall_evaluation = Column(Integer)

class Message(Base):
    __tablename__ = "message"
    id = Column(Integer, primary_key=True)
    message_from_player_id = Column(Integer, ForeignKey("player.id"))
    appointment_id = Column(Integer, ForeignKey("appointment.id"))
    timestamp = Column(TIMESTAMP)
    message_content = Column(Text)

# --- Pydantic Schemas ---

class PlayerCreate(BaseModel):
    name: str
    email: str
    password: str
    location: Optional[str] = None
    favourite_food_id: Optional[int] = None

class PlayerOut(BaseModel):
    id: int
    name: str
    email: str
    location: Optional[str]
    favourite_food_id: Optional[int]
    class Config:
        orm_mode = True

class AppointmentCreate(BaseModel):
    date: date
    timestamp: datetime
    location: str
    host_id: int

class AppointmentOut(BaseModel):
    id: int
    date: date
    timestamp: datetime
    location: str
    host_id: int
    class Config:
        orm_mode = True

class GameOut(BaseModel):
    id: int
    name: str
    description: Optional[str]
    class Config:
        orm_mode = True

class GameSuggestionCreate(BaseModel):
    game_id: int
    appointment_id: int

class GameSuggestionOut(BaseModel):
    id: int
    game_id: int
    appointment_id: int
    class Config:
        orm_mode = True

class GameVoteCreate(BaseModel):
    player_id: int
    game_suggestion_id: int
    vote_value: bool

class GameVoteOut(BaseModel):
    player_id: int
    game_suggestion_id: int
    vote_value: bool
    class Config:
        orm_mode = True

class FoodDirectionOut(BaseModel):
    id: int
    designation: str
    class Config:
        orm_mode = True

class FoodChoiceOut(BaseModel):
    id: int
    player_id: int
    appointment_id: int
    food_direction_id: int
    class Config:
        orm_mode = True

class EvaluationCreate(BaseModel):
    player_id: int
    appointment_id: int
    meal_evaluation: int
    host_evaluation: int
    overall_evaluation: int

class EvaluationOut(BaseModel):
    id: int
    player_id: int
    appointment_id: int
    meal_evaluation: int
    host_evaluation: int
    overall_evaluation: int
    class Config:
        orm_mode = True

class MessageCreate(BaseModel):
    message_from_player_id: int
    appointment_id: int
    timestamp: datetime
    message_content: str

class MessageOut(BaseModel):
    id: int
    message_from_player_id: int
    appointment_id: int
    timestamp: datetime
    message_content: str
    class Config:
        orm_mode = True
