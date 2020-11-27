#!/usr/bin/env python

import os
import sys
import argparse
import json
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

argparser = argparse.ArgumentParser(description="Read sourdough starter status.")
subparsers = argparser.add_subparsers(help="The task you want to get done.", dest="cmd")

analyze_parser = subparsers.add_parser("analyze", help="Analyze the given images.")
analyze_parser.add_argument("--backlit", dest="backlit", help="Path to the backlit input image.")
analyze_parser.add_argument("--ambient", dest="ambient", help="Path to the ambient input image.")

debug_parser = subparsers.add_parser("debug", help="Launch the matplotlib debugger.")
debug_parser.add_argument("--backlit", dest="backlit", help="Path to the backlit input image.")
debug_parser.add_argument("--ambient", dest="ambient", help="Path to the ambient input image.")

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
    # This area on the cardboard stand is either covered by
    # the glass with starter or lit brightly by the backlight:
    bottom_spot = img[880:905, 770:830]
    value_img = color.rgb2hsv(bottom_spot)[:, :, 2]
    
    # Consider glass present if half of pixels have HSV value > 0.5:
    binary_img = value_img > 0.5
    raveled = binary_img.ravel()
    result = sum(raveled) < len(raveled) / 2

    return bool(result), bottom_spot

def analyze_images(ambient_img, backlit_img):
    backlit_uncropped = backlit_img
    backlit_img = crop(backlit_uncropped)
    ambient_img = crop(ambient_img)

    glass_present, glass_bottom = is_glass_present(backlit_uncropped)

    if glass_present:
        glass_data = {
            "rubber_band_y": find_rubber_band_y(ambient_img) + CROP_Y1,
            "glass_bottom_y": GLASS_BOTTOM_Y + CROP_Y1,
            "dough_level_y": find_dough_level_y(backlit_img)[0] + CROP_Y1
        }
    else:
        glass_data = None

    return {
        "glass_present": glass_present,
        "glass_data": glass_data
    }

def plot_debug(ambient_path, backlit_path):
    ambient, backlit = ambient_path, backlit_path

    backlit_uncropped = io.imread(backlit)
    backlit_img = crop(backlit_uncropped)
    ambient_uncropped = io.imread(ambient)
    ambient_img = crop(ambient_uncropped)
    
    glass_present, glass_spot = is_glass_present(backlit_uncropped)

    print("Glass present: {}, {} {}".format(
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

if __name__ == "__main__":
    args = argparser.parse_args()
    
    if args.cmd == "analyze":
        result = analyze_images(
            backlit_img=io.imread(args.backlit),
            ambient_img=io.imread(args.ambient)
        )
        json.dump(result, sys.stdout, sort_keys=True, indent=2)
        sys.stdout.write("\n")
    elif args.cmd == "debug":
        plot_debug(ambient_path=args.ambient, backlit_path=args.backlit)
    else:
        raise Exception("Invalid command")
