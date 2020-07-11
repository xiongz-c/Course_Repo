#include "FingerCount.h"
#include <Windows.h>
#include <opencv2/opencv.hpp>
#include "opencv2/imgproc.hpp"
#include "opencv2/highgui.hpp"

#define LIMIT_ANGLE_SUP 60
#define LIMIT_ANGLE_INF 5
#define BOUNDING_RECT_FINGER_SIZE_SCALING 0.3
#define BOUNDING_RECT_NEIGHBOR_DISTANCE_SCALING 0.05


using cv::Mat;
using cv::Rect;
using cv::Size;
using cv::Point;
using cv::Scalar;
using namespace std;

int win_control_cnt = 0, isZero = 0;

FingerCount::FingerCount(void) {
	color_blue = Scalar(255, 0, 0);
	color_green = Scalar(0, 255, 0);
	color_red = Scalar(0, 0, 255);
	color_black = Scalar(0, 0, 0);
	color_white = Scalar(255, 255, 255);
	color_yellow = Scalar(0, 255, 255);
	color_purple = Scalar(255, 0, 255);
}

Mat FingerCount::findFingersCount(Mat input_image, Mat frame) {
	Mat contours_image = Mat::zeros(input_image.size(), CV_8UC3);

	if (input_image.empty())
		return contours_image;

	if (input_image.channels() != 1)
		return contours_image;

	vector<vector<Point>> contours;
	vector<cv::Vec4i> hierarchy;

	findContours(input_image, contours, hierarchy, cv::RETR_EXTERNAL, cv::CHAIN_APPROX_NONE);

	if (contours.size() <= 0)
		return contours_image;

	int biggest_contour_index = -1;
	double biggest_area = 0.0;

	for (int i = 0; i < contours.size(); i++) {
		double area = contourArea(contours[i], false);
		if (area > biggest_area) {
			biggest_area = area;
			biggest_contour_index = i;
		}
	}

	if (biggest_contour_index < 0)
		return contours_image;

	vector<Point> hull_points;
	vector<int> hull_ints;

	convexHull(Mat(contours[biggest_contour_index]), hull_points, true);

	convexHull(Mat(contours[biggest_contour_index]), hull_ints, false);

	vector<cv::Vec4i> defects;
	if (hull_ints.size() > 3)
		convexityDefects(Mat(contours[biggest_contour_index]), hull_ints, defects);
	else
		return contours_image;

	Rect bounding_rectangle = boundingRect(Mat(hull_points));

	Point center_bounding_rect(
		(bounding_rectangle.tl().x + bounding_rectangle.br().x) / 2,
		(bounding_rectangle.tl().y + bounding_rectangle.br().y) / 2
	);

	vector<Point> start_points;
	vector<Point> far_points;

	for (int i = 0; i < defects.size(); i++) {
		start_points.push_back(contours[biggest_contour_index][defects[i].val[0]]);

		if (findPointsDistance(contours[biggest_contour_index][defects[i].val[2]], center_bounding_rect) < bounding_rectangle.height * BOUNDING_RECT_FINGER_SIZE_SCALING)
			far_points.push_back(contours[biggest_contour_index][defects[i].val[2]]);
	}

	vector<Point> filtered_start_points = compactOnNeighborhoodMedian(start_points, bounding_rectangle.height * BOUNDING_RECT_NEIGHBOR_DISTANCE_SCALING);
	vector<Point> filtered_far_points = compactOnNeighborhoodMedian(far_points, bounding_rectangle.height * BOUNDING_RECT_NEIGHBOR_DISTANCE_SCALING);

	vector<Point> filtered_finger_points;

	if (filtered_far_points.size() > 1) {
		vector<Point> finger_points;

		for (int i = 0; i < filtered_start_points.size(); i++) {
			vector<Point> closest_points = findClosestOnX(filtered_far_points, filtered_start_points[i]);

			if (isFinger(closest_points[0], filtered_start_points[i], closest_points[1], LIMIT_ANGLE_INF, LIMIT_ANGLE_SUP, center_bounding_rect, bounding_rectangle.height * BOUNDING_RECT_FINGER_SIZE_SCALING))
				finger_points.push_back(filtered_start_points[i]);
		}

		if (finger_points.size() > 0) {

			while (finger_points.size() > 5)
				finger_points.pop_back();

			for (int i = 0; i < finger_points.size() - 1; i++) {
				if (findPointsDistanceOnX(finger_points[i], finger_points[i + 1]) > bounding_rectangle.height * BOUNDING_RECT_NEIGHBOR_DISTANCE_SCALING * 1.5)
					filtered_finger_points.push_back(finger_points[i]);
			}

			if (finger_points.size() > 2) {
				if (findPointsDistanceOnX(finger_points[0], finger_points[finger_points.size() - 1]) > bounding_rectangle.height * BOUNDING_RECT_NEIGHBOR_DISTANCE_SCALING * 1.5)
					filtered_finger_points.push_back(finger_points[finger_points.size() - 1]);
			} else
				filtered_finger_points.push_back(finger_points[finger_points.size() - 1]);
		}
	}

	// we draw what found on the returned image 
	drawContours(contours_image, contours, biggest_contour_index, color_green, 2, 8, hierarchy);
	polylines(contours_image, hull_points, true, color_blue);
	rectangle(contours_image, bounding_rectangle.tl(), bounding_rectangle.br(), color_red, 2, 8, 0);
	circle(contours_image, center_bounding_rect, 5, color_purple, 2, 8);
	drawVectorPoints(contours_image, filtered_start_points, color_blue, true);
	drawVectorPoints(contours_image, filtered_far_points, color_red, true);
	drawVectorPoints(contours_image, filtered_finger_points, color_yellow, false);
	putText(contours_image, to_string(filtered_finger_points.size()), center_bounding_rect, cv::FONT_HERSHEY_PLAIN, 3, color_purple);

	drawContours(frame, contours, biggest_contour_index, color_green, 2, 8, hierarchy);
	circle(frame, center_bounding_rect, 5, color_purple, 2, 8);
	drawVectorPoints(frame, filtered_finger_points, color_yellow, false);
	putText(frame, to_string(filtered_finger_points.size()), center_bounding_rect, cv::FONT_HERSHEY_PLAIN, 3, color_purple);

	cout << " x:" << center_bounding_rect.x << " y:" << center_bounding_rect.y << endl;

	SetCursorPos(1920 - center_bounding_rect.x / (double)600 * 1920, center_bounding_rect.y / (double)400 * 1080);
	if (win_control_cnt == 100) {
		if (isZero > 50) {
			mouse_event(MOUSEEVENTF_LEFTDOWN, 0, 0, 0, 0);
		} else {
			mouse_event(MOUSEEVENTF_LEFTUP, 0, 0, 0, 0);
		}
		if (!filtered_finger_points.size())isZero++;
		else isZero--;
	} else {
		win_control_cnt++;
		if (!filtered_finger_points.size())isZero++;
	}
	cout << filtered_finger_points.size() << endl;
	return contours_image;
}

double FingerCount::findPointsDistance(Point a, Point b) {
	Point difference = a - b;
	return sqrt(difference.ddot(difference));
}

vector<Point> FingerCount::compactOnNeighborhoodMedian(vector<Point> points, double max_neighbor_distance) {
	vector<Point> median_points;

	if (points.size() == 0)
		return median_points;

	if (max_neighbor_distance <= 0)
		return median_points;

	Point reference = points[0];
	Point median = points[0];

	for (int i = 1; i < points.size(); i++) {
		if (findPointsDistance(reference, points[i]) > max_neighbor_distance) {

			median_points.push_back(median);

			reference = points[i];
			median = points[i];
		} else
			median = (points[i] + median) / 2;
	}

	median_points.push_back(median);

	return median_points;
}

double FingerCount::findAngle(Point a, Point b, Point c) {
	double ab = findPointsDistance(a, b);
	double bc = findPointsDistance(b, c);
	double ac = findPointsDistance(a, c);
	return acos((ab * ab + bc * bc - ac * ac) / (2 * ab * bc)) * 180 / CV_PI;
}

bool FingerCount::isFinger(Point a, Point b, Point c, double limit_angle_inf, double limit_angle_sup, Point palm_center, double min_distance_from_palm) {
	double angle = findAngle(a, b, c);
	if (angle > limit_angle_sup || angle < limit_angle_inf)
		return false;

	int delta_y_1 = b.y - a.y;
	int delta_y_2 = b.y - c.y;
	if (delta_y_1 > 0 && delta_y_2 > 0)
		return false;

	int delta_y_3 = palm_center.y - a.y;
	int delta_y_4 = palm_center.y - c.y;
	if (delta_y_3 < 0 && delta_y_4 < 0)
		return false;

	double distance_from_palm = findPointsDistance(b, palm_center);
	if (distance_from_palm < min_distance_from_palm)
		return false;

	double distance_from_palm_far_1 = findPointsDistance(a, palm_center);
	double distance_from_palm_far_2 = findPointsDistance(c, palm_center);
	if (distance_from_palm_far_1 < min_distance_from_palm / 4 || distance_from_palm_far_2 < min_distance_from_palm / 4)
		return false;

	return true;
}

vector<Point> FingerCount::findClosestOnX(vector<Point> points, Point pivot) {
	vector<Point> to_return(2);

	if (points.size() == 0)
		return to_return;

	double distance_x_1 = DBL_MAX;
	double distance_1 = DBL_MAX;
	double distance_x_2 = DBL_MAX;
	double distance_2 = DBL_MAX;
	int index_found = 0;

	for (int i = 0; i < points.size(); i++) {
		double distance_x = findPointsDistanceOnX(pivot, points[i]);
		double distance = findPointsDistance(pivot, points[i]);

		if (distance_x < distance_x_1 && distance_x != 0 && distance <= distance_1) {
			distance_x_1 = distance_x;
			distance_1 = distance;
			index_found = i;
		}
	}

	to_return[0] = points[index_found];

	for (int i = 0; i < points.size(); i++) {
		double distance_x = findPointsDistanceOnX(pivot, points[i]);
		double distance = findPointsDistance(pivot, points[i]);

		if (distance_x < distance_x_2 && distance_x != 0 && distance <= distance_2 && distance_x != distance_x_1) {
			distance_x_2 = distance_x;
			distance_2 = distance;
			index_found = i;
		}
	}

	to_return[1] = points[index_found];

	return to_return;
}

double FingerCount::findPointsDistanceOnX(Point a, Point b) {
	double to_return = 0.0;

	if (a.x > b.x)
		to_return = a.x - b.x;
	else
		to_return = b.x - a.x;

	return to_return;
}


static vector<vector<Point>> prepoints;

Mat star = cv::imread("2.png", cv::IMREAD_UNCHANGED);

void drawStar(Point fingerPoint, Mat star, Mat hand) {
	for (int x = 0; x < star.cols; x++) {
		if (x + fingerPoint.x >= hand.cols || x + fingerPoint.x < 0)
			break;
		for (int y = 0; y < star.rows; y++) {
			if (y + fingerPoint.y >= hand.rows || y + fingerPoint.y < 0)
				break;
			double op = (double)star.data[y * star.step + x * star.channels() + 3] / 255;
			if (op <= 0)
				continue;
			for (int z = 0; z < star.channels(); z++) {
				int index = (fingerPoint.y + y) * hand.step + (fingerPoint.x + x) * hand.channels() + z;
				unsigned char star_data = star.data[y * star.step + x * star.channels() + z];
				unsigned char hand_data = hand.data[index];
				hand.data[index] = star_data * op + hand_data * (1 - op);
			}
		}
	}
}


void FingerCount::drawVectorPoints(Mat image, vector<Point> points, Scalar color, bool with_numbers) {
	for (int i = 0; i < points.size(); i++) {
		try {
			drawStar(Point(points[i].x - 15, points[i].y - 15), star, image);
			if (with_numbers)
				putText(image, to_string(i), points[i], cv::FONT_HERSHEY_PLAIN, 3, color);
		} catch (exception e) {
			cout << e.what() << endl;
		}
	}
	if (prepoints.size() == 10) {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < prepoints[i].size(); j++) {
				try {
					drawStar(Point(prepoints[i][j].x - 15, prepoints[i][j].y - 15), star, image);
				} catch (exception e) {
					cout << e.what() << endl;
				}
			}
		}
		vector<vector<Point>>::iterator it = prepoints.begin();
		prepoints.erase(it);
		prepoints.push_back(points);
	} else {
		for (int i = 0; i < prepoints.size(); i++) {
			for (int j = 0; j < prepoints[i].size(); j++) {
				try {
					drawStar(Point(prepoints[i][j].x - 15, prepoints[i][j].y - 15), star, image);
				} catch (exception e) {
					cout << e.what() << endl;
				}
			}
		}
		prepoints.push_back(points);
	}
}