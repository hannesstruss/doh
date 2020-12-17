from flask import Flask, request, jsonify
from skimage import io

import analyze

app = Flask(__name__)
app.config['MAX_CONTENT_LENGTH'] = 32 * 1024 * 1024

# @app.before_request
# def log_request_info():
#     app.logger.debug("Body: %s", request.get_data())

@app.route("/")
def hello_world():
    return "Hi there Brozozo!"

@app.route("/analyze-images", methods=["POST"])
def analyze_images():
    result = analyze.analyze_images(
        backlit_img=io.imread(request.files["backlit"].stream),
        ambient_img=io.imread(request.files["ambient"].stream)
    )
    return jsonify(result)


