from fastapi import FastAPI

app = FastAPI()

@app.post("/authenticate/{name}/{password}")
def authenticate(name: str, password: str):
    # TODO implement, most likely going to return the player
    return

@app.post("/register/{jsonPlayer}")
def register(jsonPlayer: str): # maybe the type can be improved into something more json specific
    #TODO implement, add new player to db, payload is a json or something representing a player, return id in db or -1 for fail
    return

@app.get("/appointments")
def getAppointments():
    # TODO implement, rückgabe list der appointments, vergangene potentiell auf 5 verringern
    return

@app.post("/appointments/insert/{jsonApointment}")
def addAppointment(jsonApointment: str): # maybe the type can be improved into something more json specific
    #TODO implement, try to add the json appointment to the db, return id in db or -1 for fail
    return

@app.post("/appointments/update/{jsonAppointment}")
def updateAppointment(jsonAppointment: str): 
    #TODO implement, update and return bool indicating succes, may also be better to combine with above method
    return

@app.get("/gameSuggestions/{appointmentId}")
def getGameSuggestions(appointmentId: int):
    # TODO implement, return list of game suggestions for the appointment
    return

@app.post("/gameSuggestions/insert/{jsonGameSuggestions}")
def addGameSuggestions(jsonGameSuggestions: str): # maybe the type can be improved into something more json specific
    # TODO implement, parse list of game suggestions and return a bool indicating success
    return

@app.get("/games")
def getGames():
    # TODO implement, return list of games in db
    return

@app.post("/gameVotes/insert/{jsonGameVote}")
def addGameVote(jsonGameVote: str): # maybe the type can be improved into something more json specific
    # TODO implement, add vote to db, return id of db or -1 for fail
    return

@app.post("/gameVotes/update/{jsonGameVote}")
def updateGameVote(jsonGameVote: str): # maybe the type can be improved into something more json specific
    # TODO implement, update vote in db return bool indicating success
    # could maybe also be combined with the method above and we "detect" 
    # if we want to update or insert by checking the given id
    return

@app.get("/gameVotes/{appointmentId}/{playerId}")
def getGameVotes(appointmentId: int, playerId: int):
    # TODO implement, return list of game votes the player made for the appointment
    return

@app.get("/gameVotes/{appointmentId}")
def getGameVotes(appointmentId: int):
    # TODO implement, returns a list of game votes for an appointment
    return

@app.get("/foodDirections")
def getFoodDirections():
    # TODO implement, return list of food directions
    return

@app.post("/foodChoices/insert/{appointmentId}/{playerId}/{foodDirectionId}")
def addFoodChoice(appointmentId: int, playerId: int, foodDirectionId: int):
    # TODO implement, add to db and return id to indicate success or -1
    return

@app.get("/foodChoices/{appointmentId}")
def getFoodChoices(appointmentId: int):
    # TODO implement, get list of food choices for an appointment
    return

@app.get("/foodChoices/{appointmentId}/{playerId}")
def getFoodChoice(appointmentId: int, playerId: int):
    # TODO implement, get food choice the player made for the appointment
    return

@app.post("/evaluations/insert/{jsonEvaluation}")
def addEvaluation(jsonEvaluation: str): # maybe the type can be improved into something more json specific
    # TODO implement, parse json put into db and return id
    return

@app.get("/evaluations/{appointmentId}")
def getEvaluations(appointmentId: int):
    # TODO implement, get Evaluations for appointment
    return

@app.post("/messages/insert/{jsonMessage}")
def addMessage(jsonMessage: str): # maybe the type can be improved into something more json specific
    # TODO implement, add message to db
    return

@app.get("/messages/{appointmentId}")
def getMessages(appointmentId: int):
    # TODO implement, get messages for appointment
    return