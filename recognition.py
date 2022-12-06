import os
import cv2
import numpy as np
import pandas as pd
from matplotlib import pyplot as plt

def _hog(image):
    block_size = 16
    cell_size = 8
    dx = np.array([[0, 0, 0], [-1, 0, 1], [0, 0, 0]])
    dy = dx.T

    # tinh bien do gx, gy
    gx = cv2.filter2D(image, -1, dx)
    gy = cv2.filter2D(image, -1, dy)

    # Tinh bien do, bien do goc
    gs = np.sqrt(np.square(gx) + np.square(gy))
    phis = np.arctan(gy / (gx + 1e-6))
    phis[gx == 0] = np.pi / 2
    argmax_g = gs.argmax(axis=2)

    # lấy ra g, phi mà tại đó g max
    g = np.take_along_axis(gs, argmax_g[..., None], axis=1)[..., 0]
    phi = np.take_along_axis(phis, argmax_g[..., None], axis=1)[..., 0]

    histogram = []
    for x in range(0, g.shape[0] - block_size + 1, cell_size):
        for y in range(0, g.shape[1] - block_size + 1, cell_size):

            # Tính các block
            block_his = []
            for i in range(x, x + block_size - cell_size + 1, cell_size):
                for j in range(y, y + block_size - cell_size + 1, cell_size):

                    # Lấy các giá trị g trong cell
                    g_in_square = g[i:i + cell_size, j:j + cell_size]
                    # Lấy các giá trị phi trong cell
                    phi_in_square = phi[i:i + cell_size, j:j + cell_size]
                    # Khởi tạo 9 thùng
                    bins = np.zeros(9)

                    for u in range(0, g_in_square.shape[0]):
                        for v in range(0, g_in_square.shape[1]):
                            g_pixel = g_in_square[u, v]

                            # Chuyển từ radian sang độ
                            phi_pixel = phi_in_square[u, v] * 180 / np.pi

                            bin_index = int(phi_pixel // 20)
                            a = bin_index * 20
                            b = (bin_index + 1) * 20

                            value_1 = (phi_pixel - a) / 20 * g_pixel
                            value_2 = (b - phi_pixel) / 20 * g_pixel

                            bins[bin_index] += value_2
                            bins[(bin_index + 1) % 9] += value_1

                    block_his.append(bins)


            block_his = np.array(block_his).reshape((-1, 1))
            block_his /= np.linalg.norm(block_his) + 1e-6
            histogram.append(block_his)

    histogram = np.array(histogram).reshape((-1, 1))
    return histogram
def _extract_object(image):
    gray = cv2.cvtColor(image, cv2.COLOR_RGB2GRAY)

    image_cpy = cv2.threshold(gray, 60, 255, cv2.THRESH_BINARY | cv2.THRESH_OTSU)[1]

    image_cpy = cv2.bitwise_not(image_cpy)
    # Cut just the object out
    sums = image_cpy.sum(axis=0)
    t = np.where(sums != 0)
    x1, x2 = t[0][0], t[0][-1]
    sums = image_cpy.sum(axis=1)
    t = np.where(sums != 0)
    y1, y2 = t[0][0], t[0][-1]

    return image[y1:y2 + 1, x1:x2 + 1]
def _get_color_mean(image):
    hist = []
    for i in range(3):
        hist.append(cv2.calcHist([image], [i], None, [256], [0, 256]))
    return np.array(hist).reshape((-1, 1))

def _pad_resize(image, width=None, height=None, inter=cv2.INTER_AREA):
    # initialize the dimensions of the image to be resized and
    # grab the image size
    dim = None
    (h, w) = image.shape[:2]

    # if both the width and height are None, then return the
    # original image
    if width is None and height is None:
        return image

    # check to see if the width is None
    if width is None:
        # calculate the ratio of the height and construct the
        # dimensions
        r = height / float(h)
        dim = (int(w * r), height)

    # otherwise, the height is None
    else:
        # calculate the ratio of the width and construct the
        # dimensions
        r = width / float(w)
        dim = (width, int(h * r))

    # resize the image
    resized = cv2.resize(image, dim, interpolation=inter)
    # return the resized image
    return resized

def extract_features(image, shape, name="", rotate=False):
    assert image.shape[-1] == 3, "Expected 3 channels, got %d" % image.shape[-1]


    image = _extract_object(image)

    # Lấy lược đồ màu
    obj_color_mean = _get_color_mean(image)

    # Resize về cùng một cỡ
    if image.shape[0] >= image.shape[1]:
        image = _pad_resize(image, shape[0], None)
        y1 = 0
        y2 = shape[0]
        x1 = image.shape[0] // 2 - shape[0] // 2
        x2 = x1 + shape[0]
        image = image[x1:x2, y1:y2]

    else:
        image = _pad_resize(image, None, shape[1])
        x1 = 0
        x2 = shape[0]
        y1 = image.shape[1] // 2 - shape[1] // 2
        y2 = y1 + shape[1]
        image = image[x1:x2, y1:y2]

    if rotate:
        feature = []
        for i in range(3):
            image = cv2.rotate(image, cv2.ROTATE_90_CLOCKWISE)
            ft = (_hog(image), obj_color_mean)
            feature.append(ft)

    else:
        feature = [(_hog(image), obj_color_mean)]
    return feature

def readfile():
    path = '/content/drive/MyDrive/CSDLĐPT/dataset/data.csv'
    data_folder = "/content/drive/MyDrive/CSDLĐPT/dataset/Datamoi"
    shape = (224, 224)
    data = []
    cols = None
    for name in os.listdir(data_folder):
        img_path = os.path.join(data_folder, name)
        img = cv2.imread(img_path)
        print(img_path)
        features = extract_features(img, shape, img_path)

        for feature in features:
            hog, color_mean = feature
            if cols is None:
                cols = ['name']
                cols += ['color_' + str(i) for i in range(color_mean.shape[0])]
                cols += ['hog_' + str(i) for i in range(hog.shape[0])]

            row = np.vstack((name, color_mean, hog))

            data.append(row)

    df = pd.DataFrame(np.array(data).reshape((len(data), -1)), columns=cols)
    df.to_csv(path)

def read():
    df = pd.read_csv('/content/drive/MyDrive/CSDLĐPT/dataset/data.csv')
    df.head()

def get():

    img_path = '/content/drive/MyDrive/CSDLĐPT/dataset/Test/273647091_5602622106431464_6317128233559635531_n.jpg'
    img = cv2.imread(img_path)
    x = _get_color_mean(img)
    print(x.shape)
    plt.imshow(img)
    plt.show()
    #  print(_get_color_mean(img))
    # shape = (224,224)
    # feature = extract_features(img,shape, rotate = True);
    # print(feature[1][1].shape)

def cosine_similarity(ft1, ft2):
    return - (ft1 * ft2).sum() / (np.linalg.norm(ft1) * np.linalg.norm(ft2) + 1e-6)

def euclidean_distance(ft1, ft2):
    return np.linalg.norm(ft1 - ft2)
def reading():
    test_images = '/content/drive/MyDrive/CSDLĐPT/dataset/Test/blue_people_0581242a61fb44639e936aa5da2c0496.jpg'

    shape = (224, 224)

    test_img = cv2.imread(test_images)
    # test_img = cv2.cvtColor(test_img, cv2.COLOR_BGR2RGB)
    ft1 = extract_features(test_img, shape, rotate=True)

    df = pd.read_csv('/content/drive/MyDrive/CSDLĐPT/dataset/data.csv')
    images = np.array(os.listdir('/content/drive/MyDrive/CSDLĐPT/dataset/Datamoi'))
    print(len(images))
    dsts = np.zeros(len(images), dtype=np.float32)
    print(dsts.shape)
    i = 0
    for i, row in df.iterrows():
        color_features = np.array(row[2:770]).reshape((-1, 1))
        hog_features = np.array(row[770:]).reshape((-1, 1))
        dsts[i] = 10
        for j in range(3):
            dst_1 = cosine_similarity(hog_features, ft1[j][0])
            dst_2 = cosine_similarity(color_features, ft1[j][1])
            dsts[i] = min(dst_1 + dst_2, dsts[i])

    top_ten = images[dsts.argsort()][:10]
    rows = 6
    columns = 2
    fig = plt.figure(figsize=(10, 7))

    for i, image in enumerate(top_ten):
        fig.add_subplot(rows, columns, i + 1)
        img = cv2.imread(os.path.join('/content/drive/MyDrive/CSDLĐPT/dataset/Datamoi', image))
        # img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
        img = cv2.resize(img, (shape[1], shape[0]))
        plt.imshow(img)
        plt.axis('off')
        plt.title(image)

    print(top_ten)
    plt.show()

def main():
    process();

if __name__ == '__main__':
    main()
  