FROM python:3.9-slim-buster

RUN pip install -U pipenv \
  && apt-get update -qq \
  && apt-get install -y libgomp1

ENV SCIKIT_WHL_ARM=/tmp/scikit_image-0.17.2-cp39-cp39-linux_aarch64.whl
ENV SCIKIT_WHL_AMD=/tmp/scikit_image-0.17.2-cp39-cp39-linux_x86_64.whl
COPY ./scikit_image-0.17.2-cp39-cp39-linux_aarch64.whl $SCIKIT_WHL_ARM
COPY ./scikit_image-0.17.2-cp39-cp39-linux_x86_64.whl $SCIKIT_WHL_AMD

COPY Pipfile Pipfile.lock /app/

WORKDIR /app

# numpy is required by scikit-image's setup.py and doesn't get resolved automatically:
RUN pipenv run pip install numpy --only-binary=:all:

ENV PIP_ONLY_BINARY=:all:
RUN if [ $(uname -m) = "aarch64" ]; then pipenv install $SCIKIT_WHL_ARM; else echo "Not installing ARM skimage wheel"; fi
RUN if [ $(uname -m) = "x86_64" ]; then pipenv install $SCIKIT_WHL_AMD; else echo "Not installing x86_64 skimage wheel"; fi
RUN pipenv install
RUN rm $SCIKIT_WHL_ARM $SCIKIT_WHL_AMD

COPY ./app/* /app/
