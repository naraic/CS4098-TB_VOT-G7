import os
import hashlib
import subprocess
import json

from app.models import *
from app import db

from flask import current_app as app



def upload_file_helper(file):
    filename = file.filename

    if len(filename) <= 0:
        return False

    md5hash = hashlib.md5(os.urandom(64)).hexdigest()

    upload_folder = os.path.join(os.path.normpath(app.root_path), app.config['UPLOAD_FOLDER'])
    if not os.path.exists(os.path.join(upload_folder, md5hash)):
        os.makedirs(os.path.join(upload_folder, md5hash))

    file.save(os.path.join(upload_folder, md5hash, filename))
    db_f = Video(md5hash + '/' + filename)
    db.session.add(db_f)
    db.session.commit()
    return db_f


def evaluate_file(file):
    evaluator_script_path = os.path.join(os.path.normpath(os.path.join(app.root_path, "../")), "evaluator/evaluate.py")
    upload_folder = os.path.join(app.root_path, app.config['UPLOAD_FOLDER'])
    file_path = os.path.join(upload_folder, file.path)
    output = subprocess.check_output(['python3', evaluator_script_path, '-src', file_path])
    evaluation = json.loads(output.decode('ascii'))
    file.confidence = evaluation["face_confidence"]
    db.session.add(file)
    db.session.commit()
    #os.system(cmd)