import argparse
import json
import pickle
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.model_selection import train_test_split
from sklearn.svm import *

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('-m', '--model', type=str, default='./model')
    parser.add_argument('-i', '--test_data', type=str, default='./testdataexample.json')
    args = parser.parse_args()
    model = args.model
    test_data = args.test_data

    with open(test_data, 'r') as f:
        target = json.loads(f.read())

    with open(model, 'rb') as f:
        my_model = pickle.load(f)
    vec_model = my_model[0]
    cls = my_model[1]
    vec = TfidfVectorizer(ngram_range=(1, 2),
                          lowercase=False,
                          vocabulary=vec_model.vocabulary_)
    y_pred = cls.predict(vec.fit_transform(target))

    with open('output.txt', 'w') as f:
        for y in y_pred:
            f.write(str(int(y)) + '\n')
