import os

import pytest
import skimage

import analyze
from fixtures import FIXTURES

IMAGE_DIR = os.path.dirname(__file__) + "/test-images"

def get_fixture_files(name):
    backlit = "{}/{}-backlit.jpg".format(IMAGE_DIR, name)
    ambient = "{}/{}-ambient.jpg".format(IMAGE_DIR, name)
    return backlit, ambient

@pytest.mark.parametrize("fixture_name,fixture", FIXTURES)
def test_analyzer(fixture_name, fixture):
    print("Testing {}".format(fixture_name))
    backlit, ambient = get_fixture_files(fixture_name)
    backlit_img = skimage.io.imread(backlit)
    ambient_img = skimage.io.imread(ambient)
    result = analyze.analyze_images(ambient_img, backlit_img)
    assert result["glass_data"] == fixture
