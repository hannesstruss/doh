import json

from flask import Flask, request
from skimage import io

import analyze

app = Flask(__name__)

@app.route("/")
def hello_world():
    return "Hi there Brozozo!"

@app.route("/analyze-images", methods=["POST"])
def analyze_images():
    result = analyze.analyze_images(
        backlit_img=io.imread(request.files["backlit"].stream),
        ambient_img=io.imread(request.files["ambient"].stream)
    )
    return json.dumps(result)


