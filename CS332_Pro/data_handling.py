import os
import csv


if __name__ == '__main__':
    for root, dirs, files in os.walk('./data/'):
        for file in files:
            with open('./data/' + file, 'r', encoding='utf-8') as f:
                reader = csv.reader(f)
                data = list(reader)
                for info in data[1:]:
                    # print('./out/' + str(info[0]))
                    with open('./out/' + str(info[0]), 'a', encoding='utf-8')as f2:
                        f2.write(info[5] + '\n')
