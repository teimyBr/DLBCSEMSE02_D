#!/usr/bin/env bash

test_venv_activated=""

if [ -z "${VIRTUAL_ENV}" ]; then
    if [ ! -d test_venv ]; then
        echo "No test_venv detected, create one with ./prepare_test.sh. Exiting ..."
        exit 1
    fi
    echo "Did not detect an active virtual env - activating ..."
    . test_venv/bin/activate
    test_venv_activated="true"
fi

python -m pytest -vv tests -s

if [ ${test_venv_activated} ];then
    echo "Deactivating virtual env ..."
    deactivate
fi

exit 0
