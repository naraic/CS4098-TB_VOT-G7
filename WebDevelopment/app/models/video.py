from app import db

import datetime

class Video(db.Model):
    __tablename__ = 'videos'
    id = db.Column(db.Integer, primary_key=True)

    path = db.Column(db.Text)
    upload_date = db.Column(db.DateTime, default=datetime.datetime.utcnow)

    manual_review_decision = db.Column(db.Text)
    confidence = db.Column(db.Text)

    def __init__(self, path):
        self.path = path