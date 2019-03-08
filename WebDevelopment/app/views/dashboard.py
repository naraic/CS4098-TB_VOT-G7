from flask import Blueprint
from flask import render_template, request, redirect, url_for, jsonify, flash, session, send_file, safe_join

from app.models import *

dash = Blueprint('dash', __name__)


@dash.route('/at_risk_patients')
def at_risk_patients():
    videos = Video.query.filter_by(manual_review_decision="did_not_take_pill")
    return render_template('dash/at_risk_patients.html', videos=videos)


@dash.route('/flagged_videos')
def dash_main():
    videos = Video.query.filter_by(manual_review_decision=None)
    return render_template('dash/flagged_videos.html', videos=videos)