import os
import numpy as np
import skimage
from skimage import io, img_as_float
import matplotlib.pyplot as plt
from skimage.color import rgb2gray
from skimage import color
from skimage.filters import threshold_otsu as threshold
from skimage.measure import regionprops, label

# Minimum for dough, Yen for rubber band

def crop(img):
    return img[170:1000, 960:1065]

def thresh_img(name, plot_row):
    img = rgb2gray(io.imread(name))
    img = crop(img)
    thresh_min = threshold(img)
    binary_img = img < thresh_min

    label_img = label(binary_img)
    regions = regionprops(label_img)
    plot_row[0].imshow(img, cmap=plt.cm.gray)
    plot_row[1].hist(img.ravel(), bins=256)
    plot_row[2].imshow(binary_img, cmap=plt.cm.gray)

    br = plot_row[2]
    br.imshow(binary_img, cmap=plt.cm.gray)
    for props in regions:
        minr, minc, maxr, maxc = props.bbox
        bx = (minc, maxc, maxc, minc, minc)
        by = (minr, minr, maxr, maxr, minr)

        area = (maxc - minc) * (maxr - minr)

        if area >= 100 * 100:
            br.plot(bx, by, '-b', linewidth=2.5)


fig, ax = plt.subplots(3, 3, figsize=(12, 8))

thresh_img("high.jpg", ax[0])
thresh_img("low.jpg", ax[1])

color_img = crop(io.imread("high.jpg"))
ax[2, 0].imshow(color_img)
hsv_img = color.rgb2hsv(color_img)
hue_img = hsv_img[:, :, 0]
sat_img = hsv_img[:, :, 1]
binary_img = (hue_img > 0.5) & (hue_img < 0.6) & (sat_img > 0.2)
ax[2, 1].imshow(binary_img, cmap='gray')
label_img = label(binary_img)
regions = regionprops(label_img)
br = ax[2, 2]
br.imshow(binary_img, cmap='gray')
for props in regions:
    minr, minc, maxr, maxc = props.bbox
    bx = (minc, maxc, maxc, minc, minc)
    by = (minr, minr, maxr, maxr, minr)
    br.plot(bx, by, '-b', linewidth=2.5)

#plt.hist(img.ravel(), bins=256, range=(0.0, 1.0), fc='k', ec='k')
plt.show()


