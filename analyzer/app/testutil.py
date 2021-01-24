import sys
import json
import os
from pathlib import Path

import requests

API_HOST = "http://doh.2e3.de:8080"
IMAGE_DIR = os.path.dirname(__file__) + "/test-images"

def download_image(relative_path, name, name_suffix):
    url = API_HOST + relative_path
    path = Path(relative_path)
    local_file_name = "{}/{}-{}.{}".format(IMAGE_DIR, name, name_suffix, path.suffix.lstrip("."))

    sys.stdout.write("{} ➔ {}".format(url, local_file_name))

    r = requests.get(url)

    with open(local_file_name, 'wb') as f:
        f.write(r.content)

    sys.stdout.write(".\n")


if __name__ == "__main__":
    status_id = sys.argv[1]
    status_name = sys.argv[2]
    status_url = "{}/doughstatuses/{}".format(API_HOST, status_id)
    print("Fetching {}".format(status_url))
    status = requests.get(status_url).json()
    
    print(json.dumps(status, indent=2))
    print("\nDownloading images...")

    download_image(status["backlitImagePath"], status_name, "backlit")
    download_image(status["ambientImagePath"], status_name, "ambient")

    print("Done \\o/")
