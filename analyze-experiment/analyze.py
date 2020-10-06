import os
import numpy as np
import skimage
from skimage import io, img_as_float
import matplotlib.pyplot as plt
from skimage.color import rgb2gray
from skimage import color
from skimage.filters import threshold_otsu as threshold
from skimage.measure import regionprops, label

CROP_X1 = 790
CROP_X2 = 850
CROP_Y1 = 190
CROP_Y2 = 950
GLASS_BOTTOM_Y = 935 - CROP_Y1

def crop(img):
    return img[CROP_Y1:CROP_Y2, CROP_X1:CROP_X2]

def find_rubber_band_y(img):
    hsv_img = color.rgb2hsv(img)
    hue_img = hsv_img[:, :, 0]
    sat_img = hsv_img[:, :, 1]
    binary_img = (hue_img > 0.45) & (hue_img < 0.65) & (sat_img > 0.2)

    regions = regionprops(label(binary_img))
    best_region_y, score = None, 0.0
    for props in regions:
        minr, minc, maxr, maxc = props.bbox
        width = 


    return None, binary_img

if __name__ == "__main__":
    images_dir = "/Users/hannes/Desktop/doh-images/"
    images = os.listdir(images_dir)
    ambient_images = [images_dir + img for img in images if img.startswith("ambient-")]
    backlit_images = [images_dir + img for img in images if img.startswith("backlit-")]
    paired_images = list(zip(ambient_images, backlit_images))

    index = 182

    ambient, backlit = paired_images[index]

    backlit_img = crop(io.imread(backlit))
    ambient_img = crop(io.imread(ambient))
    fig, ax = plt.subplots(1, 2, figsize=(12, 8))
    ax[0].imshow(backlit_img)
    ax[1].imshow(find_rubber_band_y(ambient_img)[1], cmap="gray")
    plt.show()
