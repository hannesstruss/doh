FROM python:3.9-slim-buster

RUN pip install -U virtualenv \
  && apt-get update -qq \
  && apt-get install -y --no-install-recommends libgomp1 \
  && rm -rf /var/lib/apt/lists/*

RUN mkdir /tmp/wheels

ENV SCIKIT_WHL_ARM=/tmp/wheels/scikit_image-0.17.2-cp39-cp39-linux_aarch64.whl
COPY ./scikit_image-0.17.2-cp39-cp39-linux_aarch64.whl $SCIKIT_WHL_ARM

ENV SCIKIT_WHL_AMD=/tmp/wheels/scikit_image-0.17.2-cp39-cp39-linux_x86_64.whl
COPY ./scikit_image-0.17.2-cp39-cp39-linux_x86_64.whl $SCIKIT_WHL_AMD

ENV MATPLOTLIB_WHL_ARM=/tmp/wheels/matplotlib-3.3.3-cp39-cp39-linux_aarch64.whl
COPY ./matplotlib-3.3.3-cp39-cp39-linux_aarch64.whl $MATPLOTLIB_WHL_ARM

ENV MARKUPSAFE_WHL_ARM=/tmp/wheels/MarkupSafe-1.1.1-cp39-cp39-linux_aarch64.whl
COPY ./MarkupSafe-1.1.1-cp39-cp39-linux_aarch64.whl $MARKUPSAFE_WHL_ARM

ENV MARKUPSAFE_WHL_AMD=/tmp/wheels/MarkupSafe-1.1.1-cp39-cp39-linux_x86_64.whl
COPY ./MarkupSafe-1.1.1-cp39-cp39-linux_x86_64.whl $MARKUPSAFE_WHL_AMD

RUN python -m virtualenv /venv

ENV MYPY=/venv/bin/python

WORKDIR /app

# numpy is required by scikit-image's setup.py and doesn't get resolved automatically:
RUN $MYPY -m pip install numpy --only-binary=:all:

ENV PIP_ONLY_BINARY=:all:
RUN if [ $(uname -m) = "aarch64" ]; then $MYPY -m pip install \
  $MATPLOTLIB_WHL_ARM \
  $SCIKIT_WHL_ARM \
  $MARKUPSAFE_WHL_ARM; \
  else echo "Not installing ARM wheels"; fi
RUN if [ $(uname -m) = "x86_64" ]; then $MYPY -m pip install \
  $SCIKIT_WHL_AMD \
  $MARKUPSAFE_WHL_AMD; \
  else echo "Not installing x86_64 wheels"; fi
RUN rm -rf /tmp/wheels

COPY ./requirements.txt /tmp/requirements.txt
RUN $MYPY -m pip install -r /tmp/requirements.txt

COPY ./app /app
