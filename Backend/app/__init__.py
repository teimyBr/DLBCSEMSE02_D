import logging
import os
from typing import List, Optional

from fastapi import FastAPI, HTTPException, Depends
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse
from pydantic import BaseModel
from sqlalchemy.ext.asyncio import create_async_engine, AsyncSession
from sqlalchemy.orm import sessionmaker, declarative_base
from sqlalchemy import (
    Column, Integer, String, Boolean, Date, Text, ForeignKey, Numeric, TIMESTAMP, select, insert, update
)

from . import api
from . import model

app = FastAPI()

log_level = os.environ.get('LOG_LEVEL')

if os.environ.get('ENABLE_JSON_LOGGING', 'false') == 'true':
    import json_logging
    import json_logging.util

    logging.basicConfig(
        level=log_level if log_level != None else logging.INFO
    )
    json_logging.init_fastapi(enable_json=True)
    if log_level == 'DEBUG':
        json_logging.init_request_instrument(app)
    json_logging.config_root_logger()
    logging.getLogger('uvicorn').propagate = False
else:
    logging.basicConfig(
        level=log_level if log_level != None else logging.DEBUG,
        format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
        datefmt='%Y-%m-%d %H:%M:%S',
    )

app.add_middleware(
    CORSMiddleware,
    allow_origins=['*'],
    allow_methods=['*'],
    allow_headers=['*'],
    allow_credentials=True
)

app.include_router(api.router)
