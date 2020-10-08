import os
import numpy as np
import skimage
from skimage import io, img_as_float, draw
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
        width = maxc - minc
        height = maxr - minr
        new_score = width / float(height)
        if new_score > score:
            score = new_score
            best_region_y = int(minr + ((maxr - minr) / 2.0))

    return best_region_y

def find_dough_level_y(img):
    gray_img = rgb2gray(img)
    thresh_min = threshold(gray_img)
    binary_img = gray_img < thresh_min

    label_img = label(binary_img)
    regions = regionprops(label_img)

    best_region_y, biggest_area = None, 0
    for props in regions:
        minr, minx, maxr, maxc = props.bbox
        area = props.bbox_area
        if area < biggest_area:
            continue

        biggest_area = area
        best_region_y = minr

    return best_region_y, binary_img

def add_horizontal_marker(img, row, color):
    rr, cc = draw.rectangle(
        (row - 1, 0), 
        (row + 1, img.shape[1] - 1)
    )
    img[rr, cc] = color

def is_glass_present(img):
    bottom_spot = img[880:905, 770:830]
    value_img = color.rgb2hsv(bottom_spot)[:, :, 2]
    binary_img = value_img > 0.5
    raveled = binary_img.ravel()
    result = sum(raveled) < len(raveled) / 2
    return result, bottom_spot

if __name__ == "__main__":
    images_dir = "/Users/hannes/Desktop/doh-images/"
    images = sorted(os.listdir(images_dir))
    ambient_images = [images_dir + img for img in images if img.startswith("ambient-")]
    backlit_images = [images_dir + img for img in images if img.startswith("backlit-")]
    paired_images = list(zip(ambient_images, backlit_images))

    # Interesting indices:
    # 136: Empty glass
    # 207: Empty glass

    index = 0

    while True:
        user_input = input("> ")

        if user_input == "n":
            index += 1
        elif user_input == "p":
            index -= 1
        elif user_input == "exit":
            break
        else:
            index = int(user_input)

        ambient, backlit = paired_images[index]


        backlit_uncropped = io.imread(backlit)
        backlit_img = crop(backlit_uncropped)
        ambient_uncropped = io.imread(ambient)
        ambient_img = crop(ambient_uncropped)
        
        glass_present, glass_spot = is_glass_present(backlit_uncropped)

        print("Index: {} (Glass: {}), {} {}".format(
            index, 
            glass_present,
            ambient, 
            backlit
        ))

        fig, ax = plt.subplots(2, 3, figsize=(12, 8))
        ax[0][0].imshow(ambient_uncropped)
        ax[0][1].imshow(backlit_uncropped)
        ax[0][2].imshow(glass_spot)

        ax[1][0].imshow(backlit_img)

        rubber_band_row = find_rubber_band_y(ambient_img)
        dough_level_row, dough_level_img = find_dough_level_y(backlit_img)

        ax[1][1].imshow(dough_level_img, cmap="gray")

        marked_img = ambient_img.copy()

        if dough_level_row:
            add_horizontal_marker(marked_img, dough_level_row, (0, 255, 0))

        if rubber_band_row:
            add_horizontal_marker(marked_img, rubber_band_row, (255, 0, 0))

        add_horizontal_marker(marked_img, GLASS_BOTTOM_Y, (0, 0, 255))

        ax[1][2].imshow(marked_img, cmap="gray")
        plt.show()
