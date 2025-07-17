#!/usr/bin/env bash

test_venv_activated=""

deactivate_test_venv(){
    if [ ${test_venv_activated} ];then
        echo "Deactivating virtual env ..."
        deactivate
    fi
}

if [ -z "${VIRTUAL_ENV}" ]; then
    if [ ! -d test_venv ]; then
        echo "No test_venv detected, e.g. create one ..."
        python3 -m venv test_venv
    fi
    echo "Did not detect an active virtual env - activating ..."
    . test_venv/bin/activate
    test_venv_activated="true"
fi

pip install -U pip wheel setuptools
if [ -e dev_requirements.txt -a -e dev_test_requirements.txt ]; then
    pip install -r dev_test_requirements.txt
    pip freeze > test_requirements.txt
    # Remove pkg-resources as it can cause problems on dockerbuild
    #(see https://stackoverflow.com/questions/40670602/could-not-find-a-version-that-satisfies-the-requirement-pkg-resources-0-0-0)
    sed -i '/resources==0.0.0/d' test_requirements.txt

	pip install -r dev_requirements.txt
    deactivate_test_venv
    exit 0
else
    echo "No dev_requirements.txt or dev_test_requirements.txt found! Exiting ..."
    deactivate_test_venv
    exit 1
fi