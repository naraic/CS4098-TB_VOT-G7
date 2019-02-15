import os

DEBUG = True

SECRET_KEY = 'DoNotUseInProduction'

SQLALCHEMY_DATABASE_URI = "sqlite://pilladvisor.db"

UPLOAD_FOLDER = os.environ.get('UPLOAD_FOLDER') or os.path.join(os.path.dirname(os.path.abspath(__file__)), 'uploads')

