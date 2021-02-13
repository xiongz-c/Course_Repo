import argparse
import json
import pickle
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.model_selection import train_test_split
from sklearn.svm import *

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('-t', '--train_file', type=str, default='./train.json')

    args = parser.parse_args()
    train_file = args.train_file
    with open(train_file) as f:
        train_data = json.load(f)
    # score = 0
    # while score < 0.91:
    X_data, Y_data = [], []
    for data in train_data:
        X_data.append(data['data'])
        Y_data.append(data['label'])
    # X_train, X_test, Y_train, Y_test = train_test_split(X_data, Y_data, test_size=0.05)
    vec = TfidfVectorizer(ngram_range=(1, 2),
                            lowercase=False)
    vec_model = vec.fit(X_data)
    X_data = vec.fit_transform(X_data)
    cls = LinearSVC(C=0.81, multi_class='crammer_singer', class_weight='balanced' )
    cls_model = cls.fit(X_data, Y_data)

    with open("model", "wb") as f:
        pickle.dump([vec_model, cls_model], f)

        # y_pred = cls.predict(vec.transform(X_test))
        # score = 0
        # for i in range(len(y_pred)):
        #     if y_pred[i] == Y_test[i]:
        #         score += 1
        # score /= len(y_pred)
        # print(score)
