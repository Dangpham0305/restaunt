import cv2
from facenet_pytorch import MTCNN, InceptionResnetV1, fixed_image_standardization
import torch
from torchvision import transforms
import numpy as np
from PIL import Image
import time
import argparse
from pathlib import Path
import os
frame_size = (640,480)
dir_path = os.path.dirname(os.path.realpath(__file__))
IMG_PATH = os.path.realpath(os.path.dirname(dir_path) + "\\faceimage")
DATA_PATH = os.path.realpath(os.path.dirname(dir_path) + "\\data")
def trans(img):
    transform = transforms.Compose([
        transforms.ToTensor(),
        fixed_image_standardization
    ])
    return transform(img)

def load_faceslist():
    if device == 'cpu':
        embeds = torch.load(DATA_PATH+'\\faceslistCPU.pth')
    else:
        embeds = torch.load(DATA_PATH+'\\faceslist.pth')
    names = np.load(DATA_PATH+'\\usernames.npy')
    return embeds, names

def inference(model, face, local_embeds, threshold = 3):
    embeds = []
    embeds.append(model(trans(face).to(device).unsqueeze(0)))
    detect_embeds = torch.cat(embeds) #[1,512]
    # print(detect_embeds.shape)
    #[1,512,1]                                      [1,512,n]
    norm_diff = detect_embeds.unsqueeze(-1) - torch.transpose(local_embeds, 0, 1).unsqueeze(0)
    norm_score = torch.sum(torch.pow(norm_diff, 2), dim=1) #(1,n),

    min_dist, embed_idx = torch.min(norm_score, dim = 1)

    if min_dist*power > threshold:
        return -1, -1
    else:
        return embed_idx, min_dist.double()

def extract_face(box, img, margin=20):
    face_size = 160
    img_size = frame_size
    margin = [
        margin * (box[2] - box[0]) / (face_size - margin),
        margin * (box[3] - box[1]) / (face_size - margin),
        ] #tạo margin bao quanh box cũ
    box = [
        int(max(box[0] - margin[0] / 2, 0)),
        int(max(box[1] - margin[1] / 2, 0)),
        int(min(box[2] + margin[0] / 2, img_size[0])),
        int(min(box[3] + margin[1] / 2, img_size[1])),
    ]
    img = img[box[1]:box[3], box[0]:box[2]]
    face = cv2.resize(img,(face_size, face_size), interpolation=cv2.INTER_AREA)
    face = Image.fromarray(face)
    return face

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("path")
    args = parser.parse_args()
    img = Path(args.path)

    prev_frame_time = 0
    new_frame_time = 0
    power = pow(10, 6)
    device = torch.device('cuda:0' if torch.cuda.is_available() else 'cpu')
    # print(device)

    model = InceptionResnetV1(
        classify=False,
        pretrained="casia-webface"
    ).to(device)
    model.eval()

    mtcnn = MTCNN(thresholds= [0.7, 0.7, 0.8] ,keep_all=True, device = device)
    similar_threshold = 0.35
    embeddings, names = load_faceslist()
    frame = cv2.imread(str(img))
    boxes, _ = mtcnn.detect(frame)
    if boxes is not None:
        for box in boxes:
            bbox = list(map(int,box.tolist()))
            face = extract_face(bbox, frame)
            idx, score = inference(model, face, embeddings)
            if idx != -1:
                score = torch.Tensor.cpu(score[0]).detach().numpy()*power
                if score < similar_threshold:
                    print(10)
                else:
                    print(0)
            else:
                print(0)
    else:
        print(0)
    cv2.destroyAllWindows()
    #+": {:.2f}".format(score)