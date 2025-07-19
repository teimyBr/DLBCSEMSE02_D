FROM python:3.12-alpine AS base
ENV PYTHONUNBUFFERED=1

FROM base AS builder
RUN apk add --no-cache --no-check-certificate build-base &&\
    mkdir /install
WORKDIR /install
COPY requirements.txt ./requirements.txt
RUN pip install -U pip setuptools wheel && pip install --prefix=/install --no-warn-script-location -r ./requirements.txt

FROM builder AS test
COPY --from=builder /install /usr/local
COPY app /src/app
COPY tests /src/tests
WORKDIR /src
COPY test_requirements.txt ./test_requirements.txt
RUN pip install --prefix=/usr/local --no-warn-script-location -r ./test_requirements.txt
RUN python3 -m pytest tests

FROM base
COPY --from=builder /install /usr/local
COPY app/ /src/app/

ENV ENABLE_JSON_LOGGING=true
ENV LOG_LEVEL=INFO
ENV PYTHONPATH=/src
CMD ["uvicorn", "app:app", "--no-access-log", "--host" ,"0.0.0.0"]
