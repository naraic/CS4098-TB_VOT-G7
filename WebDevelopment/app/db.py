import sqlite3

from flask import current_app, g
from flask_sqlalchemy import SQLAlchemy

db = SQLAlchemy()

def init_db():
    from .models import Team, Solves, User
    db.create_all()

    create_sample_data()

    #db.session.commit()

def create_sample_data():
    pass

def init_app(app):
    """Register database functions with the Flask app. This is called by
    the application factory.
    """
    app.teardown_appcontext(close_db)
    db.init_app(app)
