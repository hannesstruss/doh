import os
import skimage
from skimage import io
import matplotlib.pyplot as plt
from skimage.color import rgb2gray
from skimage.filters import threshold_otsu as threshold
from skimage.measure import regionprops, label

# Minimum for dough, Yen for rubber band


def thresh_img(name, plot_row):
    img = rgb2gray(io.imread(name))
    img = img[170:1000, 1015:1065]
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


fig, ax = plt.subplots(2, 3, figsize=(8, 8))

thresh_img("high.jpg", ax[0])
thresh_img("low.jpg", ax[1])

#plt.hist(img.ravel(), bins=256, range=(0.0, 1.0), fc='k', ec='k')
plt.show()


