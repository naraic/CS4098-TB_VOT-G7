FROM python:3

#RUN apk update && \
#    apk add python3 python3-dev libffi-dev gcc make musl-dev py-pip mysql-client libxml2-dev libxslt-dev

RUN mkdir -p /opt/pilladvisor
COPY . /opt/pilladvisor
WORKDIR /opt/pilladvisor

RUN pip install -r app/requirements.txt

EXPOSE 8000

RUN chmod +x /opt/pilladvisor/docker-entry.sh

ENTRYPOINT ["/opt/pilladvisor/docker-entry.sh"]
