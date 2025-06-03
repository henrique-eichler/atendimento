#!/bin/bash

docker kill nginx-ssl

docker build -t nginx-ssl-proxy .

docker run -d --rm \
  --name nginx-ssl \
  --network host \
  nginx-ssl-proxy
