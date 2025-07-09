import logging

from fastapi import Depends, APIRouter, HTTPException
from fastapi.responses import PlainTextResponse
from urllib3 import HTTPResponse


router = APIRouter()


@router.get("/hello", response_class=PlainTextResponse)
def helloworld():

    try:
        return "Hello, World"
    except Exception as e:
        logging.error(f'Internal error: {str(e)}', exc_info=True)
        raise HTTPException(status_code=500, detail="Internal error")
