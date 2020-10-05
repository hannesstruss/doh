import os
import numpy as np
import skimage
from skimage import io, img_as_float
import matplotlib.pyplot as plt
from skimage.color import rgb2gray
from skimage import color
from skimage.filters import threshold_otsu as threshold
from skimage.measure import regionprops, label

if __name__ == "__main__":
    images_dir = "/Users/hannes/Desktop/doh-images/"
    images = os.listdir(images_dir)
    ambient_images = [images_dir + img for img in images if img.startswith("ambient-")]
    backlit_images = [images_dir + img for img in images if img.startswith("backlit-")]
    paired_images = list(zip(ambient_images, backlit_images))

    index = 182

    ambient, backlit = paired_images[index]

    backlit_img = io.imread(backlit)
    ambient_img = io.imread(ambient)
    fig, ax = plt.subplots(1, 2, figsize=(12, 8))
    ax[0].imshow(backlit_img)
    ax[1].imshow(ambient_img)
    plt.show()
