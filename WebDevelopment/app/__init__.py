import os

from flask import Flask
from flask import render_template, request, redirect, url_for, jsonify, flash, session, send_file, safe_join
from flask_session import Session
from flask_caching import Cache
from flask_wtf.csrf import CSRFProtect
from itsdangerous import TimedSerializer
from werkzeug.security import check_password_hash
from .db import db, init_db
import requests

from app.models import *
import redis




def create_app(test_config=None):
    """Create instance of the application"""
    app = Flask(__name__, instance_relative_config=True)
    app.config.from_mapping(
        # secret key which should be overridden by instance config
        SECRET_KEY='notverysecret',
        # by default, store an SQLite database in the instance folder
        # DATABASE=os.path.join(app.instance_path, 'db.sqlite'),
    )

    if test_config is None:
        # when not testing, load config from instance config
        app.config.from_pyfile('config.py', silent=True)
    else:
        # load the passed test config
        app.config.update(test_config)

    try:
        os.makedirs(app.instance_path)
    except OSError:
        pass    
    

    @app.route('/playvideo')
    def home():
        return render_template('home.html')

    @app.route('/files/<path:file_id>')
    def serve_file(file_id):
        upload_folder = os.path.join(app.root_path, app.config['UPLOAD_FOLDER'])
        return send_file(safe_join(upload_folder, file_id))


    db.init_app(app)

    sess = Session()
    sess.init_app(app)

    csrf = CSRFProtect()
    csrf.init_app(app)

    return app


# # Required to allow the app to be picked up by Flask-SocketIO
# if __name__ == "__main__":
#     app = create_app()
#     socketio = SocketIO(app)
#     socketio.run(app)
