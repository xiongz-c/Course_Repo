import sys
from PyQt5 import QtWidgets
from PyQt5.QtWidgets import QApplication

from query import Ui_Form

if __name__ == "__main__":
    app = QApplication(sys.argv)
    widget = QtWidgets.QWidget()
    ui = Ui_Form()
    ui.setupUi(widget)
    widget.show()
    sys.exit(app.exec_())
