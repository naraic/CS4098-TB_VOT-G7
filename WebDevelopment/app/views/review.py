from flask import Blueprint
from flask import render_template, request, redirect, url_for, jsonify, flash, session, send_file, safe_join

import os

from flask import current_app as app
from app.models import *

from app import db

review = Blueprint('review', __name__)


@review.route('/playvideo/<id>')
def home(id):
    video = Video.query.get(id)
    return render_template('review/review.html', video=video)

@review.route('/did_not_take_pill/<id>')
def not_take_pill(id):
    video = Video.query.get(id)
    video.manual_review_decision = "did_not_take_pill"
    db.session.add(video)
    db.session.commit()
    return redirect(url_for('dash.dash_main'))

@review.route('/took_pill/<id>')
def took_pill(id):
    video = Video.query.get(id)
    video.manual_review_decision = "took_pill"
    db.session.add(video)
    db.session.commit()
    return redirect(url_for('dash.dash_main'))

@review.route('/files/<path:file_id>')
def serve_file(file_id):
    upload_folder = os.path.join(app.root_path, app.config['UPLOAD_FOLDER'])
    return send_file(safe_join(upload_folder, file_id))