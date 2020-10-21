#!/bin/bash

cd "$(dirname "$0")"
pipenv run python analyze.py $@
