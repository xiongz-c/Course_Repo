# -*- coding: utf-8 -*-

from PyQt5 import QtCore, QtWidgets
from PyQt5.QtWidgets import QApplication, QMessageBox, QListWidget

import Utility
import search_API
import project_init


class Ui_Form(object):
    dictionary = {}
    doc_lens = {}
    bm25_dictionary = {}

    def __init__(self):
        self.dictionary, self.doc_lens, self.bm25_dictionary = project_init.init()

    def setupUi(self, Form):
        Form.setObjectName("Form")
        Form.resize(400, 300)
        self.pushButton = QtWidgets.QPushButton(Form)
        self.pushButton.setGeometry(QtCore.QRect(280, 20, 101, 23))
        self.pushButton.setObjectName("pushButton")
        self.pushButton.clicked.connect(self.query_vector)
        self.plainTextEdit = QtWidgets.QPlainTextEdit(Form)
        self.plainTextEdit.setGeometry(QtCore.QRect(110, 60, 161, 31))
        self.plainTextEdit.setObjectName("plainTextEdit")
        self.label = QtWidgets.QLabel(Form)
        self.label.setGeometry(QtCore.QRect(30, 60, 71, 31))
        self.label.setObjectName("label")
        self.label_2 = QtWidgets.QLabel(Form)
        self.label_2.setGeometry(QtCore.QRect(30, 30, 72, 15))
        self.label_2.setObjectName("label_2")
        self.comboBox = QtWidgets.QComboBox(Form)
        self.comboBox.setGeometry(QtCore.QRect(110, 30, 91, 22))
        self.comboBox.setObjectName("comboBox")
        self.comboBox.addItem("")
        self.pushButton_2 = QtWidgets.QPushButton(Form)
        self.pushButton_2.setGeometry(QtCore.QRect(280, 50, 101, 23))
        self.pushButton_2.setObjectName("pushButton_2")
        self.pushButton_2.clicked.connect(self.query_BM25)
        self.listWidget = QtWidgets.QListWidget(Form)
        self.listWidget.setGeometry(QtCore.QRect(20, 100, 361, 191))
        self.listWidget.setObjectName("listWidget")
        self.retranslateUi(Form)
        self.listWidget.itemClicked.connect(self.clicked_item_listener)
        QtCore.QMetaObject.connectSlotsByName(Form)

    def retranslateUi(self, Form):
        _translate = QtCore.QCoreApplication.translate
        Form.setWindowTitle(_translate("Form", "Form"))
        self.pushButton.setText(_translate("Form", "空间向量查询"))
        self.label.setText(_translate("Form", "民宿偏好"))
        self.label_2.setText(_translate("Form", "地区"))
        self.comboBox.setItemText(0, _translate("Form", "New York"))
        self.pushButton_2.setText(_translate("Form", "BM25查询"))
        __sortingEnabled = self.listWidget.isSortingEnabled()
        self.listWidget.setSortingEnabled(False)
        self.listWidget.setSortingEnabled(__sortingEnabled)

    def add_item(self, text):
        item = QtWidgets.QListWidgetItem()
        _translate = QtCore.QCoreApplication.translate
        item.setText(_translate("Form", 'Room ID:' + text))
        self.listWidget.addItem(item)

    def clicked_item_listener(self, item):
        QMessageBox.information(self.listWidget, "房源确认成功", "您选择的房源为: " + item.text())

    def same_query(self, res, wordList):
        # self.listWidget.disconnect()
        self.listWidget.clear()
        for item in res:
            self.add_item(item)
        # print('flag1---------------after addItem')
        QMessageBox.information(self.pushButton, "查询成功", "3-gram纠正后您的查询为：" + str(wordList))
        # print('flag1---------------after message box')

    def query_vector(self):
        query = self.plainTextEdit.toPlainText()
        wordList = Utility.get_token(query)
        for i in range(len(wordList)):
            wordList[i] = Utility.three_gram(wordList[i], self.dictionary)
        # print('flag1---------------before search')
        res = search_API.vector_space_search(query=wordList, doc_len=self.doc_lens, dic=self.dictionary)
        # print('flag2---------------after search')
        self.same_query(res, wordList)

    def query_BM25(self):
        query = self.plainTextEdit.toPlainText()
        wordList = Utility.get_token(query)
        for i in range(len(wordList)):
            wordList[i] = Utility.three_gram(wordList[i], self.bm25_dictionary)
        res = search_API.bm25(query=wordList, doc_len=self.doc_lens, dictionary=self.bm25_dictionary)
        self.same_query(res, wordList)
