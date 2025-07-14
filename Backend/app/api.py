import logging
import os
from typing import List, Optional
from datetime import datetime

from fastapi import FastAPI, APIRouter,  HTTPException, Depends
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse
from pydantic import BaseModel
from sqlalchemy.ext.asyncio import create_async_engine, AsyncSession
from sqlalchemy.orm import sessionmaker, declarative_base
from sqlalchemy import (
    Column, Integer, String, Boolean, Date, Text, ForeignKey, Numeric, TIMESTAMP, select, insert, update
)
from sqlalchemy import func


from . import model

DATABASE_URL = (
    f"postgresql+asyncpg://{os.getenv('POSTGRES_USER')}:{os.getenv('POSTGRES_PASSWORD')}"
    f"@{os.getenv('POSTGRES_HOST')}:{os.getenv('POSTGRES_PORT')}/{os.getenv('POSTGRES_DB')}"
)

engine = create_async_engine(DATABASE_URL, echo=False)
async_session = sessionmaker(engine, expire_on_commit=False, class_=AsyncSession)
Base = declarative_base()


router = APIRouter()

# --- Dependency ---
async def get_session():
    async with async_session() as session:
        yield session

# --- API Endpoints ---

@router.post("/authenticate/{name}/{password}", response_model=Optional[model.PlayerOut])
async def authenticate(name: str, password: str, session: AsyncSession = Depends(get_session)):
    logging.info(f"Authenticating user: {name}")
    result = await session.execute(
        select(model.Player).where(model.Player.name == name, model.Player.password == password)
    )
    player = result.scalar_one_or_none()
    if player:
        logging.info(f"Authentication successful for user: {name}")
        return player
    logging.warning(f"Authentication failed for user: {name}")
    raise HTTPException(status_code=401, detail="Invalid credentials")

@router.post("/register/", response_model=dict)
async def register(player: model.PlayerCreate, session: AsyncSession = Depends(get_session)):
    logging.info(f"Registering user with email: {player.email}")
    result = await session.execute(select(model.Player).where(model.Player.email == player.email))
    if result.scalar_one_or_none():
        logging.warning(f"Registration failed: Email already exists ({player.email})")
        return {"id": -1}
    # Nächste freie ID suchen
    max_id_result = await session.execute(select(func.max(model.Player.id)))
    max_id = max_id_result.scalar() or 0
    new_id = max_id + 1
    new_player = model.Player(
        id=new_id,
        name=player.name,
        email=player.email,
        password=player.password,
        location=player.location,
        favourite_food_id=player.favourite_food_id
    )
    session.add(new_player)
    await session.commit()
    await session.refresh(new_player)
    logging.info(f"User registered successfully: {player.email} (ID: {new_id})")
    return {"id": new_player.id}

@router.get("/appointments", response_model=List[model.AppointmentOut])
async def getAppointments(session: AsyncSession = Depends(get_session)):
    logging.info("Fetching latest 5 appointments")
    result = await session.execute(select(model.Appointment).order_by(model.Appointment.date.desc()).limit(5))
    return result.scalars().all()

@router.post("/appointments/insert/", response_model=dict)
async def addAppointment(appointment: model.AppointmentCreate, session: AsyncSession = Depends(get_session)):
    logging.info("Inserting new appointment")
    ts = appointment.timestamp
    if ts.tzinfo is not None:
        ts = ts.astimezone().replace(tzinfo=None)

    # Nächste freie ID bestimmen
    result = await session.execute(select(func.max(model.Appointment.id)))
    max_id = result.scalar()
    next_id = (max_id or 0) + 1

    new_appointment = model.Appointment(
        id=next_id,  # <--- ID explizit setzen
        date=appointment.date,
        timestamp=ts,
        location=appointment.location,
        host_id=appointment.host_id
    )
    session.add(new_appointment)
    await session.commit()
    await session.refresh(new_appointment)
    logging.info(f"Inserted appointment with ID: {new_appointment.id}")
    return {"id": new_appointment.id}

@router.post("/appointments/update/", response_model=dict)
async def updateAppointment(appointment: model.AppointmentOut, session: AsyncSession = Depends(get_session)):
    logging.info(f"Updating appointment with ID: {appointment.id}")
    result = await session.execute(select(model.Appointment).where(model.Appointment.id == appointment.id))
    db_appointment = result.scalar_one_or_none()
    if not db_appointment:
        logging.warning(f"Appointment with ID {appointment.id} not found")
        return {"success": False}
    # timestamp auf naive Zeit konvertieren, falls nötig
    data = appointment.dict()
    ts = data.get("timestamp")
    if ts and ts.tzinfo is not None:
        data["timestamp"] = ts.astimezone().replace(tzinfo=None)
    for key, value in data.items():
        setattr(db_appointment, key, value)
    await session.commit()
    logging.info(f"Appointment with ID {appointment.id} updated successfully")
    return {"success": True}

@router.get("/gameSuggestions/{appointmentId}", response_model=List[model.GameSuggestionOut])
async def getGameSuggestions(appointmentId: int, session: AsyncSession = Depends(get_session)):
    logging.info(f"Fetching game suggestions for appointment ID: {appointmentId}")
    result = await session.execute(select(model.GameSuggestion).where(model.GameSuggestion.appointment_id == appointmentId))
    return result.scalars().all()

@router.post("/gameSuggestions/insert/", response_model=dict)
async def addGameSuggestions(suggestions: List[model.GameSuggestionCreate], session: AsyncSession = Depends(get_session)):
    logging.info(f"Inserting {len(suggestions)} game suggestions")
    # Aktuelle maximale ID abfragen
    result = await session.execute(select(func.max(model.GameSuggestion.id)))
    max_id = result.scalar()
    next_id = (max_id or 0) + 1

    objs = []
    for i, sugg in enumerate(suggestions):
        obj = model.GameSuggestion(id=next_id + i, **sugg.dict())
        objs.append(obj)

    session.add_all(objs)
    await session.commit()
    logging.info("Game suggestions inserted successfully")
    return {"success": True}

@router.get("/games", response_model=List[model.GameOut])
async def getGames(session: AsyncSession = Depends(get_session)):
    logging.info(f"Fetching games")
    result = await session.execute(select(model.Game))
    return result.scalars().all()

@router.post("/gameVotes/insert/", response_model=dict)
async def addGameVote(vote: model.GameVoteCreate, session: AsyncSession = Depends(get_session)):
    logging.info(f"Inserting game vote")
    new_vote = model.GameVote(**vote.dict())
    session.add(new_vote)
    try:
        await session.commit()
        logging.info("Game vote inserted successfully")
        return {"id": 1}
    except Exception:
        await session.rollback()
        logging.warning(f"Game vote inserted unsuccessfully")
        return {"id": -1}

@router.post("/gameVotes/update/", response_model=dict)
async def updateGameVote(vote: model.GameVoteCreate, session: AsyncSession = Depends(get_session)):
    logging.info(f"Update game vote")
    result = await session.execute(
        select(model.GameVote).where(
            model.GameVote.player_id == vote.player_id,
            model.GameVote.game_suggestion_id == vote.game_suggestion_id
        )
    )
    db_vote = result.scalar_one_or_none()
    if not db_vote:
        logging.warning(f"Game vote updated unsuccessfully")
        return {"success": False}
    db_vote.vote_value = vote.vote_value
    await session.commit()
    logging.info("Game vote updated successfully")
    return {"success": True}

@router.get("/gameVotes/{appointmentId}/{playerId}", response_model=List[model.GameVoteOut])
async def getGameVotesForPlayer(appointmentId: int, playerId: int, session: AsyncSession = Depends(get_session)):
    # Get all game_suggestion_ids for appointment
    logging.info("Get all game suggestion ids for appointment: {appointmentId} and player: {playerId}")
    result = await session.execute(
        select(model.GameSuggestion.id).where(model.GameSuggestion.appointment_id == appointmentId)
    )
    suggestion_ids = [row[0] for row in result.all()]
    if not suggestion_ids:
        logging.warning(f"No game suggestion found")
        return []
    result = await session.execute(
        select(model.GameVote).where(
            model.GameVote.player_id == playerId,
            model.GameVote.game_suggestion_id.in_(suggestion_ids)
        )
    )
    logging.info("Returned game suggestion ids for appointment: {appointmentId} and player: {playerId}")
    return result.scalars().all()

@router.get("/gameVotes/{appointmentId}", response_model=List[model.GameVoteOut])
async def getGameVotes(appointmentId: int, session: AsyncSession = Depends(get_session)):
    logging.info("Get all game suggestion ids for appointment: {appointmentId}")
    result = await session.execute(
        select(model.GameSuggestion.id).where(model.GameSuggestion.appointment_id == appointmentId)
    )
    suggestion_ids = [row[0] for row in result.all()]
    if not suggestion_ids:
        logging.warning(f"No game suggestion found")
        return []
    result = await session.execute(
        select(model.GameVote).where(model.GameVote.game_suggestion_id.in_(suggestion_ids))
    )
    logging.info("Returned game suggestion ids for appointment: {appointmentId}")
    return result.scalars().all()

@router.get("/foodDirections", response_model=List[model.FoodDirectionOut])
async def getFoodDirections(session: AsyncSession = Depends(get_session)):
    logging.info("Return all food directions")
    result = await session.execute(select(model.FoodDirection))
    return result.scalars().all()

@router.post("/foodChoices/insert/{appointmentId}/{playerId}/{foodDirectionId}", response_model=dict)
async def addFoodChoice(appointmentId: int, playerId: int, foodDirectionId: int, session: AsyncSession = Depends(get_session)):
    logging.info("Insert food choices")
    # Nächste freie ID bestimmen
    result = await session.execute(select(func.max(model.FoodChoice.id)))
    max_id = result.scalar()
    next_id = (max_id or 0) + 1

    new_choice = model.FoodChoice(
        id=next_id,
        appointment_id=appointmentId,
        player_id=playerId,
        food_direction_id=foodDirectionId
    )
    session.add(new_choice)
    try:
        await session.commit()
        await session.refresh(new_choice)
        logging.info(f"Insert food choices successfully: {new_choice.id}")
        return {"id": new_choice.id}
    except Exception:
        await session.rollback()
        logging.warning("Insert food choices failed")
        return {"id": -1}

@router.get("/foodChoices/{appointmentId}", response_model=List[model.FoodChoiceOut])
async def getFoodChoices(appointmentId: int, session: AsyncSession = Depends(get_session)):
    logging.info("Get food choices for appointment: {appointmentId}")
    result = await session.execute(select(model.FoodChoice).where(model.FoodChoice.appointment_id == appointmentId))
    return result.scalars().all()

@router.get("/foodChoices/{appointmentId}/{playerId}", response_model=Optional[model.FoodChoiceOut])
async def getFoodChoice(appointmentId: int, playerId: int, session: AsyncSession = Depends(get_session)):
    logging.info("Get food choices for appointment: {appointmentId} and player: {playerId}")
    result = await session.execute(
        select(model.FoodChoice).where(
            model.FoodChoice.appointment_id == appointmentId,
            model.FoodChoice.player_id == playerId
        )
    )
    return result.scalar_one_or_none()

@router.post("/evaluations/insert/", response_model=dict)
async def addEvaluation(evaluation: model.EvaluationCreate, session: AsyncSession = Depends(get_session)):
    logging.info("Insert evaluations")
    # Nächste freie ID bestimmen
    result = await session.execute(select(func.max(model.Evaluation.id)))
    max_id = result.scalar()
    next_id = (max_id or 0) + 1

    new_eval = model.Evaluation(id=next_id, **evaluation.dict())
    session.add(new_eval)
    await session.commit()
    await session.refresh(new_eval)
    return {"id": new_eval.id}

@router.get("/evaluations/{appointmentId}", response_model=List[model.EvaluationOut])
async def getEvaluations(appointmentId: int, session: AsyncSession = Depends(get_session)):
    logging.info("Get evaluations for appointment: {appointmentId}")
    result = await session.execute(select(model.Evaluation).where(model.Evaluation.appointment_id == appointmentId))
    return result.scalars().all()

@router.post("/messages/insert/", response_model=dict)
async def addMessage(message: model.MessageCreate, session: AsyncSession = Depends(get_session)):
    logging.info("Insert messages")
    data = message.dict()
    ts = data.get("timestamp")
    if ts and ts.tzinfo is not None:
        data["timestamp"] = ts.astimezone().replace(tzinfo=None)
    
    # Nächste freie ID bestimmen
    result = await session.execute(select(func.max(model.Message.id)))
    max_id = result.scalar()
    next_id = (max_id or 0) + 1

    new_msg = model.Message(id=next_id, **data)
    session.add(new_msg)
    await session.commit()
    await session.refresh(new_msg)
    return {"id": new_msg.id}

@router.get("/messages/{appointmentId}", response_model=List[model.MessageOut])
async def getMessages(appointmentId: int, session: AsyncSession = Depends(get_session)):
    logging.info("Get messages for appointment: {appointmentId}")
    result = await session.execute(select(model.Message).where(model.Message.appointment_id == appointmentId))
    return result.scalars().all()
