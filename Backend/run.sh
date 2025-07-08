#!/usr/bin/env bash

venv_activated=""

if [ -z "${VIRTUAL_ENV}" ]; then
    if [ ! -d venv ]; then
        echo "No venv detected, create one with ./prepare.sh. Exiting ..."
        exit 1
    fi
    echo "Did not detect an active virtual env - activating ..."
    . venv/bin/activate
    venv_activated="true"
fi

export PYTHONPATH=.

uvicorn app:app --reload --host 0.0.0.0

if [ ${venv_activated} ];then
    echo "Deactivating virtual env ..."
    deactivate
fi

exit 0
