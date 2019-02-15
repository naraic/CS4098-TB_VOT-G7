#!/bin/sh

gunicorn --bind 0.0.0.0:8000 -w 17 "app:create_app()"
