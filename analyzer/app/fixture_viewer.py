import os
import json
from io import BytesIO

from flask import Flask, request, Response, render_template_string, send_file
from werkzeug.wsgi import FileWrapper
import skimage
from skimage import draw
import imageio

from fixtures import FIXTURES
from analyze import analyze_images, CROP_X_CENTER

IMAGE_DIR = os.path.dirname(__file__) + "/test-images"

app = Flask(__name__)

TEMPLATE = """\
<!doctype html>
<html>
    <head>
        <title>Doh Analyzer Fixture Viewer</title>
        <style>
            img {
                max-height: 90vh;
            }
        </style>
    </head>
    <body>
        <h1>Fixtures</h1>
        {% for fixture in fixtures %}
            <p>{{ fixture[0] }}</p>
            <pre>{{ fixture[2] }}</pre>
            <p><img id="img-{{ fixture[0] }}"></p>
            <script type="text/javascript">
                (function() {
                    let baseUrl = "/image?name={{ fixture[0] }}";
                    let ambient = true;
                    let image = document.querySelector("#img-{{ fixture[0] }}");
                    image.src = baseUrl;
                    image.addEventListener("click", (event) => {
                        ambient = !ambient;
                        let fullUrl = baseUrl;
                        if (!ambient) {
                            fullUrl += "&ambient=false";
                        }
                        image.src = fullUrl;
                    });
                })();
            </script>
        {% endfor %}
    </body>
</html>
"""

@app.route("/")
def root():
    all_fixtures = [
        list(fixture) + [json.dumps(fixture[1], indent=2)]
        for fixture
        in FIXTURES
    ]
    return render_template_string(TEMPLATE, fixtures=all_fixtures)

@app.route("/image")
def image():
    name = request.args["name"]
    ambient_filename = "{}/{}-ambient.jpg".format(IMAGE_DIR, name)
    backlit_filename = "{}/{}-backlit.jpg".format(IMAGE_DIR, name)

    ambient_img = skimage.io.imread(ambient_filename)
    backlit_img = skimage.io.imread(backlit_filename)

    result = analyze_images(ambient_img=ambient_img, backlit_img=backlit_img)

    output_img = ambient_img if request.args.get("ambient", "true") == "true" else backlit_img

    annotate_image(output_img, result)

    buf = BytesIO()
    imageio.imwrite(buf, output_img, format="jpg")
    buf.seek(0)
    return Response(FileWrapper(buf), mimetype="image/jpeg", direct_passthrough=True)

def annotate_image(img, analyzer_result):
    if not analyzer_result["glass_present"]:
        return

    glass_data = analyzer_result["glass_data"]

    rubber_band_y = glass_data["rubber_band_y"]
    glass_bottom_y = glass_data["glass_bottom_y"]
    dough_level_y = glass_data["dough_level_y"]

    draw_horizontal_line(img, (0, 0, 0xFF), rubber_band_y)
    draw_horizontal_line(img, (0, 0xFF, 0), dough_level_y)
    draw_horizontal_line(img, (0xFF, 0, 0), glass_bottom_y)

def draw_horizontal_line(img, color, row):
    rr, cc = draw.rectangle(
        (row - 1, 0), 
        (row + 1, img.shape[1] - 1)
    )
    img[rr, cc] = color

if __name__ == "__main__":
    os.environ["FLASK_DEBUG"] = "true"
    app.run()
