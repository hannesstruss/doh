FROM python:3.7-slim-buster

RUN pip install -U virtualenv \
  && apt-get update -qq \
  && apt-get install -y --no-install-recommends libgomp1 \
  && rm -rf /var/lib/apt/lists/*

RUN python -m virtualenv /venv

ENV MYPY=/venv/bin/python

WORKDIR /app

COPY ./pip.conf /etc/
ENV PIP_ONLY_BINARY=:all:
# numpy is required by scikit-image's setup.py and doesn't get resolved automatically:
RUN $MYPY -m pip install numpy --only-binary=:all:
COPY ./requirements.txt /tmp/requirements.txt
RUN $MYPY -m pip install -r /tmp/requirements.txt

COPY ./app /app

CMD /venv/bin/flask run --host 0.0.0.0 --port 8080
