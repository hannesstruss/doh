FROM python:3.9-slim-buster

RUN pip install -U pipenv \
  && apt-get update -qq \
  && apt-get install -y libgomp1

RUN mkdir /tmp/wheels

ENV SCIKIT_WHL_ARM=/tmp/wheels/scikit_image-0.17.2-cp39-cp39-linux_aarch64.whl
COPY ./scikit_image-0.17.2-cp39-cp39-linux_aarch64.whl $SCIKIT_WHL_ARM

ENV SCIKIT_WHL_AMD=/tmp/wheels/scikit_image-0.17.2-cp39-cp39-linux_x86_64.whl
COPY ./scikit_image-0.17.2-cp39-cp39-linux_x86_64.whl $SCIKIT_WHL_AMD

ENV MATPLOTLIB_WHL_ARM=/tmp/wheels/matplotlib-3.3.3-cp39-cp39-linux_aarch64.whl
COPY ./matplotlib-3.3.3-cp39-cp39-linux_aarch64.whl $MATPLOTLIB_WHL_ARM

COPY Pipfile Pipfile.lock /app/

WORKDIR /app

# numpy is required by scikit-image's setup.py and doesn't get resolved automatically:
RUN pipenv run pip install numpy --only-binary=:all:

ENV PIP_ONLY_BINARY=:all:
RUN if [ $(uname -m) = "aarch64" ]; then pipenv run pip install $MATPLOTLIB_WHL_ARM $SCIKIT_WHL_ARM; else echo "Not installing ARM wheels"; fi
RUN if [ $(uname -m) = "x86_64" ]; then pipenv run pip install $SCIKIT_WHL_AMD; else echo "Not installing x86_64 skimage wheel"; fi
# RUN pipenv install
# RUN rm -rf /tmp/wheels

COPY ./app/* /app/
