#!/usr/bin/env bash

venv_activated=""

deactivate_venv(){
    if [ ${venv_activated} ];then
        echo "Deactivating virtual env ..."
        deactivate
    fi
}

if [ -z "${VIRTUAL_ENV}" ]; then
    if [ ! -d venv ]; then
        echo "No venv detected, e.g. create one ..."
        python3 -m venv venv
    fi
    echo "Did not detect an active virtual env - activating ..."
    . venv/bin/activate
    venv_activated="true"
fi

pip install -U pip wheel setuptools
if [ -e dev_requirements.txt ]; then
    pip install -r dev_requirements.txt
    grep extra-index-url dev_requirements.txt > requirements.txt
    grep trusted-host dev_requirements.txt >> requirements.txt
    pip freeze >> requirements.txt
    # Remove pkg-resources as it can cause problems on dockerbuild
    #(see https://stackoverflow.com/questions/40670602/could-not-find-a-version-that-satisfies-the-requirement-pkg-resources-0-0-0)
    sed -i '/resources==0.0.0/d' requirements.txt
    deactivate_venv
    exit 0
else
	echo "No dev_requirements.txt found! Exiting ..."
    deactivate_venv
    exit 1
fi