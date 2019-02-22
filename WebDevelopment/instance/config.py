import os

DEBUG = True

SECRET_KEY = 'DoNotUseInProduction'


dir_path = os.path.dirname(os.path.realpath(__file__))
sqlite_path = os.path.join(dir_path, "db.sqlite")

SQLALCHEMY_DATABASE_URI = 'sqlite:///' + sqlite_path

UPLOAD_FOLDER = os.environ.get('UPLOAD_FOLDER') or os.path.join(os.path.dirname(os.path.abspath(__file__)), 'uploads')

