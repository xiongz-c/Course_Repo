#include <opencv2/opencv.hpp>
#include "BackgroundRemover.h"
#include "SkinDetector.h"
#include "FingerCount.h"


using cv::Mat;
using cv::Rect;
using cv::Size;
using cv::Point;
using cv::Scalar;
using cv::VideoCapture;
using namespace std;

int main() {
	VideoCapture videoCapture(0);
	videoCapture.set(cv::CAP_PROP_SETTINGS, 1);

	if (!videoCapture.isOpened()) {
		cout << "Can't find camera!" << endl;
		return -1;
	}

	Mat frame, frameOut, handMask, foreground, fingerCountDebug;
	BackgroundRemover backgroundRemover;
	SkinDetector skinDetector;
	FingerCount fingerCount;

	while (true) {
		videoCapture >> frame;
		frameOut = frame.clone();

		skinDetector.drawSkinColorSampler(frameOut);
		foreground = backgroundRemover.getForeground(frame);
		handMask = skinDetector.getSkinMask(foreground);
		fingerCountDebug = fingerCount.findFingersCount(handMask, frameOut);

		imshow("output", frameOut);
		imshow("foreground", foreground);
		imshow("handMask", handMask);
		imshow("handDetection", fingerCountDebug);

		int key = cv::waitKey(1);

		if (key == 27) // esc
			break;
		else if (key == 98) // b
			backgroundRemover.calibrate(frame);
		else if (key == 115) // s
			skinDetector.calibrate(frame);
	}

	return 0;
}