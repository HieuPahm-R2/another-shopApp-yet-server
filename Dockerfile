FROM ubuntu:latest
LABEL authors="OS"

ENTRYPOINT ["top", "-b"]