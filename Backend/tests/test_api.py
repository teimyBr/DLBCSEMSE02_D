import pytest
from fastapi import HTTPException
from unittest.mock import AsyncMock, MagicMock
import datetime

from app import authenticate, register, getAppointments, addAppointment, updateAppointment
from app import getGameSuggestions, addGameSuggestions, getGames, addGameVote, updateGameVote
from app import getGameVotesForPlayer, getGameVotes, getFoodDirections, addFoodChoice, getFoodChoices
from app import getFoodChoice, addEvaluation, getEvaluations, addMessage, getMessages
from app import model


@pytest.mark.asyncio
async def test_authenticate_success(mocker):
    # Arrange
    name = "testuser"
    password = "testpass"
    fake_player = model.Player(name=name, password=password)
    
    # Mock das Session-Objekt und das execute-Ergebnis
    mock_session = MagicMock()
    mock_result = MagicMock()
    mock_result.scalar_one_or_none = AsyncMock(return_value=fake_player)
    mock_session.execute = AsyncMock(return_value=mock_result)
    
    # Act
    result = await authenticate(name, password, session=mock_session)
    
    # Assert
    assert result == fake_player

@pytest.mark.asyncio
async def test_authenticate_failure(mocker):
    # Arrange
    name = "testuser"
    password = "wrongpass"
    
    mock_session = MagicMock()
    mock_result = MagicMock()
    mock_result.scalar_one_or_none = AsyncMock(return_value=None)
    mock_session.execute = AsyncMock(return_value=mock_result)
    
    # Act & Assert
    with pytest.raises(HTTPException) as exc_info:
        await authenticate(name, password, session=mock_session)
    assert exc_info.value.status_code == 401
    assert exc_info.value.detail == "Invalid credentials"

@pytest.mark.asyncio
async def test_register_email_exists(mocker):
    # Arrange
    player_data = model.PlayerCreate(
        name="Test",
        email="test@example.com",
        password="secret",
        location="Earth",
        favourite_food_id=1
    )
    mock_session = MagicMock()
    # simulate: email already exists
    mock_result = MagicMock()
    mock_result.scalar_one_or_none = AsyncMock(return_value=True)
    mock_session.execute = AsyncMock(return_value=mock_result)

    # Act
    result = await register(player_data, session=mock_session)

    # Assert
    assert result == {"id": -1}

@pytest.mark.asyncio
async def test_register_success(mocker):
    # Arrange
    player_data = model.PlayerCreate(
        name="Test",
        email="test@example.com",
        password="secret",
        location="Earth",
        favourite_food_id=1
    )
    mock_session = MagicMock()

    # simulate: email does not exist
    mock_result_email = MagicMock()
    mock_result_email.scalar_one_or_none = AsyncMock(return_value=None)

    # simulate: max id is 5
    mock_result_max_id = MagicMock()
    mock_result_max_id.scalar = MagicMock(return_value=5)

    # session.execute returns different results depending on call
    mock_session.execute = AsyncMock(side_effect=[mock_result_email, mock_result_max_id])

    # simulate: new player
    new_player = model.Player(
        id=6,
        name=player_data.name,
        email=player_data.email,
        password=player_data.password,
        location=player_data.location,
        favourite_food_id=player_data.favourite_food_id
    )

    # session.refresh sets the id (simulate)
    async def refresh_side_effect(obj):
        obj.id = 6
    mock_session.refresh = AsyncMock(side_effect=refresh_side_effect)
    mock_session.add = MagicMock()
    mock_session.commit = AsyncMock()

    # Act
    result = await register(player_data, session=mock_session)

    # Assert
    assert result == {"id": 6}
    mock_session.add.assert_called_once()
    mock_session.commit.assert_awaited_once()
    mock_session.refresh.assert_awaited_once()

@pytest.mark.asyncio
async def test_get_appointments(mocker):
    # Arrange
    mock_session = MagicMock()
    mock_result = MagicMock()
    appointments = [model.Appointment(id=i, date=None, timestamp=None, location="loc", host_id=1) for i in range(5)]
    mock_result.scalars.return_value.all.return_value = appointments
    mock_session.execute = AsyncMock(return_value=mock_result)

    # Act
    result = await getAppointments(session=mock_session)

    # Assert
    assert result == appointments
    mock_session.execute.assert_awaited_once()

@pytest.mark.asyncio
async def test_add_appointment(mocker):
    # Arrange
    appointment_data = model.AppointmentCreate(
        date=datetime.date(2024, 1, 1),
        timestamp=datetime.datetime(2024, 1, 1, 12, 0, 0, tzinfo=None),
        location="Test Location",
        host_id=42
    )
    mock_session = MagicMock()

    # simulate: max id is 5
    mock_result_max_id = MagicMock()
    mock_result_max_id.scalar.return_value = 5
    mock_session.execute = AsyncMock(return_value=mock_result_max_id)

    # simulate: new appointment
    new_appointment = model.Appointment(
        id=6,
        date=appointment_data.date,
        timestamp=appointment_data.timestamp,
        location=appointment_data.location,
        host_id=appointment_data.host_id
    )

    # session.refresh sets the id (simulate)
    async def refresh_side_effect(obj):
        obj.id = 6
    mock_session.refresh = AsyncMock(side_effect=refresh_side_effect)
    mock_session.add = MagicMock()
    mock_session.commit = AsyncMock()

    # Act
    result = await addAppointment(appointment_data, session=mock_session)

    # Assert
    assert result == {"id": 6}
    mock_session.add.assert_called_once()
    mock_session.commit.assert_awaited_once()
    mock_session.refresh.assert_awaited_once()

@pytest.mark.asyncio
async def test_update_appointment_success(mocker):
    # Arrange
    appointment_data = model.AppointmentOut(
        id=1,
        date=datetime.date(2024, 1, 1),
        timestamp=datetime.datetime(2024, 1, 1, 12, 0, 0, tzinfo=None),
        location="Test Location",
        host_id=42
    )
    mock_session = MagicMock()
    mock_result = MagicMock()
    db_appointment = MagicMock()
    mock_result.scalar_one_or_none = AsyncMock(return_value=db_appointment)
    mock_session.execute = AsyncMock(return_value=mock_result)
    mock_session.commit = AsyncMock()

    # Act
    result = await updateAppointment(appointment_data, session=mock_session)

    # Assert
    assert result == {"success": True}
    mock_session.commit.assert_awaited_once()

@pytest.mark.asyncio
async def test_update_appointment_not_found(mocker):
    # Arrange
    appointment_data = model.AppointmentOut(
        id=999,
        date=datetime.date(2024, 1, 1),
        timestamp=datetime.datetime(2024, 1, 1, 12, 0, 0, tzinfo=None),
        location="Test Location",
        host_id=42
    )
    mock_session = MagicMock()
    mock_result = MagicMock()
    mock_result.scalar_one_or_none = AsyncMock(return_value=None)
    mock_session.execute = AsyncMock(return_value=mock_result)

    # Act
    result = await updateAppointment(appointment_data, session=mock_session)

    # Assert
    assert result == {"success": False}

@pytest.mark.asyncio
async def test_get_game_suggestions(mocker):
    # Arrange
    mock_session = MagicMock()
    mock_result = MagicMock()
    suggestions = [
        model.GameSuggestion(id=1, appointment_id=42, game_id=5, suggester_id=7),
        model.GameSuggestion(id=2, appointment_id=42, game_id=6, suggester_id=8),
    ]
    mock_result.scalars.return_value.all.return_value = suggestions
    mock_session.execute = AsyncMock(return_value=mock_result)

    # Act
    result = await getGameSuggestions(appointmentId=42, session=mock_session)

    # Assert
    assert result == suggestions
    mock_session.execute.assert_awaited_once()

@pytest.mark.asyncio
async def test_add_game_suggestions(mocker):
    # Arrange
    suggestion_data = [
        model.GameSuggestionCreate(appointment_id=42, game_id=5, suggester_id=7),
        model.GameSuggestionCreate(appointment_id=42, game_id=6, suggester_id=8),
    ]
    mock_session = MagicMock()

    # simulate: max id is 10
    mock_result_max_id = MagicMock()
    mock_result_max_id.scalar.return_value = 10
    mock_session.execute = AsyncMock(return_value=mock_result_max_id)
    mock_session.add_all = MagicMock()
    mock_session.commit = AsyncMock()

    # Act
    result = await addGameSuggestions(suggestion_data, session=mock_session)

    # Assert
    assert result == {"success": True}
    mock_session.add_all.assert_called_once()
    mock_session.commit.assert_awaited_once()
    # Prüfe, dass die IDs korrekt gesetzt wurden
    added_objs = mock_session.add_all.call_args[0][0]
    assert [obj.id for obj in added_objs] == [11, 12]

@pytest.mark.asyncio
async def test_get_games(mocker):
    # Arrange
    mock_session = MagicMock()
    mock_result = MagicMock()
    games = [
        model.Game(id=1, name="Chess"),
        model.Game(id=2, name="Monopoly"),
    ]
    mock_result.scalars.return_value.all.return_value = games
    mock_session.execute = AsyncMock(return_value=mock_result)

    # Act
    result = await getGames(session=mock_session)

    # Assert
    assert result == games
    mock_session.execute.assert_awaited_once()

@pytest.mark.asyncio
async def test_add_game_vote_success(mocker):
    vote_data = model.GameVoteCreate(player_id=1, game_suggestion_id=2, vote_value=1)
    mock_session = MagicMock()
    mock_session.add = MagicMock()
    mock_session.commit = AsyncMock(return_value=None)

    result = await addGameVote(vote_data, session=mock_session)

    assert result == {"id": 1}
    mock_session.add.assert_called_once()
    mock_session.commit.assert_awaited_once()

@pytest.mark.asyncio
async def test_add_game_vote_failure(mocker):
    vote_data = model.GameVoteCreate(player_id=1, game_suggestion_id=2, vote_value=1)
    mock_session = MagicMock()
    mock_session.add = MagicMock()
    mock_session.commit = AsyncMock(side_effect=Exception("fail"))
    mock_session.rollback = AsyncMock()

    result = await addGameVote(vote_data, session=mock_session)

    assert result == {"id": -1}
    mock_session.rollback.assert_awaited_once()

@pytest.mark.asyncio
async def test_update_game_vote_success(mocker):
    vote_data = model.GameVoteCreate(player_id=1, game_suggestion_id=2, vote_value=1)
    mock_session = MagicMock()
    mock_vote = MagicMock()
    mock_result = MagicMock()
    mock_result.scalar_one_or_none = AsyncMock(return_value=mock_vote)
    mock_session.execute = AsyncMock(return_value=mock_result)
    mock_session.commit = AsyncMock()

    result = await updateGameVote(vote_data, session=mock_session)

    assert result == {"success": True}
    assert mock_vote.vote_value == 1
    mock_session.commit.assert_awaited_once()

@pytest.mark.asyncio
async def test_update_game_vote_not_found(mocker):
    vote_data = model.GameVoteCreate(player_id=1, game_suggestion_id=2, vote_value=1)
    mock_session = MagicMock()
    mock_result = MagicMock()
    mock_result.scalar_one_or_none = AsyncMock(return_value=None)
    mock_session.execute = AsyncMock(return_value=mock_result)

    result = await updateGameVote(vote_data, session=mock_session)

    assert result == {"success": False}

@pytest.mark.asyncio
async def test_get_game_votes_for_player_with_suggestions(mocker):
    mock_session = MagicMock()
    # First execute: returns suggestion ids
    mock_result_suggestions = MagicMock()
    mock_result_suggestions.all.return_value = [(10,), (11,)]
    # Second execute: returns votes
    mock_result_votes = MagicMock()
    votes = [
        model.GameVote(id=1, player_id=1, game_suggestion_id=10, vote_value=1),
        model.GameVote(id=2, player_id=1, game_suggestion_id=11, vote_value=0),
    ]
    mock_result_votes.scalars.return_value.all.return_value = votes
    mock_session.execute = AsyncMock(side_effect=[mock_result_suggestions, mock_result_votes])

    result = await getGameVotesForPlayer(appointmentId=5, playerId=1, session=mock_session)

    assert result == votes

@pytest.mark.asyncio
async def test_get_game_votes_for_player_no_suggestions(mocker):
    mock_session = MagicMock()
    mock_result_suggestions = MagicMock()
    mock_result_suggestions.all.return_value = []
    mock_session.execute = AsyncMock(return_value=mock_result_suggestions)

    result = await getGameVotesForPlayer(appointmentId=5, playerId=1, session=mock_session)

    assert result == []

@pytest.mark.asyncio
async def test_get_game_votes_with_suggestions(mocker):
    mock_session = MagicMock()
    # First execute: returns suggestion ids
    mock_result_suggestions = MagicMock()
    mock_result_suggestions.all.return_value = [(10,), (11,)]
    # Second execute: returns votes
    mock_result_votes = MagicMock()
    votes = [
        model.GameVote(id=1, player_id=1, game_suggestion_id=10, vote_value=1),
        model.GameVote(id=2, player_id=2, game_suggestion_id=11, vote_value=0),
    ]
    mock_result_votes.scalars.return_value.all.return_value = votes
    mock_session.execute = AsyncMock(side_effect=[mock_result_suggestions, mock_result_votes])

    result = await getGameVotes(appointmentId=5, session=mock_session)

    assert result == votes

@pytest.mark.asyncio
async def test_get_game_votes_no_suggestions(mocker):
    mock_session = MagicMock()
    mock_result_suggestions = MagicMock()
    mock_result_suggestions.all.return_value = []
    mock_session.execute = AsyncMock(return_value=mock_result_suggestions)

    result = await getGameVotes(appointmentId=5, session=mock_session)

    assert result == []

@pytest.mark.asyncio
async def test_get_food_directions(mocker):
    mock_session = MagicMock()
    mock_result = MagicMock()
    directions = [
        model.FoodDirection(id=1, name="Vegan"),
        model.FoodDirection(id=2, name="Vegetarian"),
    ]
    mock_result.scalars.return_value.all.return_value = directions
    mock_session.execute = AsyncMock(return_value=mock_result)

    result = await getFoodDirections(session=mock_session)

    assert result == directions
    mock_session.execute.assert_awaited_once()

@pytest.mark.asyncio
async def test_add_food_choice_success(mocker):
    mock_session = MagicMock()
    # simulate: max id is 5
    mock_result_max_id = MagicMock()
    mock_result_max_id.scalar.return_value = 5
    mock_session.execute = AsyncMock(return_value=mock_result_max_id)
    mock_session.add = MagicMock()
    mock_session.commit = AsyncMock()
    mock_session.refresh = AsyncMock()

    # simulate: new choice
    new_choice = model.FoodChoice(
        id=6,
        appointment_id=1,
        player_id=2,
        food_direction_id=3
    )
    # refresh sets the id
    async def refresh_side_effect(obj):
        obj.id = 6
    mock_session.refresh = AsyncMock(side_effect=refresh_side_effect)

    result = await addFoodChoice(1, 2, 3, session=mock_session)

    assert result == {"id": 6}
    mock_session.add.assert_called_once()
    mock_session.commit.assert_awaited_once()
    mock_session.refresh.assert_awaited_once()

@pytest.mark.asyncio
async def test_add_food_choice_failure(mocker):
    mock_session = MagicMock()
    mock_result_max_id = MagicMock()
    mock_result_max_id.scalar.return_value = 5
    mock_session.execute = AsyncMock(return_value=mock_result_max_id)
    mock_session.add = MagicMock()
    mock_session.commit = AsyncMock(side_effect=Exception("fail"))
    mock_session.refresh = AsyncMock()
    mock_session.rollback = AsyncMock()

    result = await addFoodChoice(1, 2, 3, session=mock_session)

    assert result == {"id": -1}
    mock_session.rollback.assert_awaited_once()

@pytest.mark.asyncio
async def test_get_food_choices(mocker):
    mock_session = MagicMock()
    mock_result = MagicMock()
    choices = [
        model.FoodChoice(id=1, appointment_id=1, player_id=2, food_direction_id=3),
        model.FoodChoice(id=2, appointment_id=1, player_id=3, food_direction_id=4),
    ]
    mock_result.scalars.return_value.all.return_value = choices
    mock_session.execute = AsyncMock(return_value=mock_result)

    result = await getFoodChoices(appointmentId=1, session=mock_session)

    assert result == choices
    mock_session.execute.assert_awaited_once()

@pytest.mark.asyncio
async def test_get_food_choice_found(mocker):
    mock_session = MagicMock()
    mock_result = MagicMock()
    choice = model.FoodChoice(id=1, appointment_id=1, player_id=2, food_direction_id=3)
    mock_result.scalar_one_or_none = AsyncMock(return_value=choice)
    mock_session.execute = AsyncMock(return_value=mock_result)

    result = await getFoodChoice(appointmentId=1, playerId=2, session=mock_session)

    assert result == choice
    mock_session.execute.assert_awaited_once()

@pytest.mark.asyncio
async def test_get_food_choice_not_found(mocker):
    mock_session = MagicMock()
    mock_result = MagicMock()
    mock_result.scalar_one_or_none = AsyncMock(return_value=None)
    mock_session.execute = AsyncMock(return_value=mock_result)

    result = await getFoodChoice(appointmentId=1, playerId=2, session=mock_session)

    assert result is None
    mock_session.execute.assert_awaited_once()

@pytest.mark.asyncio
async def test_add_evaluation(mocker):
    eval_data = model.EvaluationCreate(appointment_id=1, player_id=2, rating=5, comment="Great!")
    mock_session = MagicMock()
    # simulate: max id is 7
    mock_result_max_id = MagicMock()
    mock_result_max_id.scalar.return_value = 7
    mock_session.execute = AsyncMock(return_value=mock_result_max_id)
    mock_session.add = MagicMock()
    mock_session.commit = AsyncMock()
    # simulate: new evaluation
    new_eval = model.Evaluation(id=8, **eval_data.dict())
    async def refresh_side_effect(obj):
        obj.id = 8
    mock_session.refresh = AsyncMock(side_effect=refresh_side_effect)

    result = await addEvaluation(eval_data, session=mock_session)

    assert result == {"id": 8}
    mock_session.add.assert_called_once()
    mock_session.commit.assert_awaited_once()
    mock_session.refresh.assert_awaited_once()

@pytest.mark.asyncio
async def test_get_evaluations(mocker):
    mock_session = MagicMock()
    mock_result = MagicMock()
    evaluations = [
        model.Evaluation(id=1, appointment_id=1, player_id=2, rating=5, comment="Great!"),
        model.Evaluation(id=2, appointment_id=1, player_id=3, rating=4, comment="Good"),
    ]
    mock_result.scalars.return_value.all.return_value = evaluations
    mock_session.execute = AsyncMock(return_value=mock_result)

    result = await getEvaluations(appointmentId=1, session=mock_session)

    assert result == evaluations
    mock_session.execute.assert_awaited_once()

@pytest.mark.asyncio
async def test_add_message(mocker):
    msg_data = model.MessageCreate(
        appointment_id=1,
        player_id=2,
        content="Hello!",
        timestamp=datetime.datetime(2024, 1, 1, 12, 0, 0, tzinfo=None)
    )
    mock_session = MagicMock()
    # simulate: max id is 3
    mock_result_max_id = MagicMock()
    mock_result_max_id.scalar.return_value = 3
    mock_session.execute = AsyncMock(return_value=mock_result_max_id)
    mock_session.add = MagicMock()
    mock_session.commit = AsyncMock()
    # simulate: new message
    new_msg = model.Message(id=4, **msg_data.dict())
    async def refresh_side_effect(obj):
        obj.id = 4
    mock_session.refresh = AsyncMock(side_effect=refresh_side_effect)

    result = await addMessage(msg_data, session=mock_session)

    assert result == {"id": 4}
    mock_session.add.assert_called_once()
    mock_session.commit.assert_awaited_once()
    mock_session.refresh.assert_awaited_once()

@pytest.mark.asyncio
async def test_get_messages(mocker):
    mock_session = MagicMock()
    mock_result = MagicMock()
    messages = [
        model.Message(id=1, appointment_id=1, player_id=2, content="Hi", timestamp=None),
        model.Message(id=2, appointment_id=1, player_id=3, content="Hello", timestamp=None),
    ]
    mock_result.scalars.return_value.all.return_value = messages
    mock_session.execute = AsyncMock(return_value=mock_result)

    result = await getMessages(appointmentId=1, session=mock_session)

    assert result == messages
    mock_session.execute.assert_awaited_once()
