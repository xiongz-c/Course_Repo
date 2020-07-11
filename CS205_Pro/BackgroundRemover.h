#pragma once
#include<opencv2/opencv.hpp>

using cv::Mat;
using cv::Rect;
using cv::Size;
using cv::Point;
using cv::Scalar;
using namespace std;

class BackgroundRemover {
public:
	BackgroundRemover(void);
	void calibrate(cv::Mat input);
	cv::Mat getForeground(cv::Mat input);

private:
	cv::Mat background;
	bool calibrated = false;

	cv::Mat getForegroundMask(cv::Mat input);
	void removeBackground(cv::Mat input, cv::Mat background);
};