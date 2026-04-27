#include <algorithm>
#include <cctype>
#include <fstream>
#include <iostream>
#include <sstream>
#include <string>
#include <vector>

using namespace std;

// This struct holds one course from the input file
struct Course {
    string courseNumber;
    string courseTitle;
    vector<string> prerequisites;
};

// Basic BST node
struct Node {
    Course course;
    Node* left;
    Node* right;

    Node(const Course& aCourse) {
        course = aCourse;
        left = nullptr;
        right = nullptr;
    }
};

// Trim off extra spaces from the front and back of text
string trim(const string& text) {
    size_t start = 0;
    size_t end = text.length();

    while (start < end && isspace(static_cast<unsigned char>(text[start]))) {
        start++;
    }

    while (end > start && isspace(static_cast<unsigned char>(text[end - 1]))) {
        end--;
    }

    return text.substr(start, end - start);
}

// I used this so course searches are not affected by lowercase vs uppercase input
string toUpperCase(string text) {
    for (char& ch : text) {
        ch = static_cast<char>(toupper(static_cast<unsigned char>(ch)));
    }
    return text;
}

// Split one CSV line into parts using commas
vector<string> splitLine(const string& line) {
    vector<string> parts;
    string part;
    stringstream ss(line);

    while (getline(ss, part, ',')) {
        parts.push_back(trim(part));
    }

    return parts;
}

class BinarySearchTree {
private:
    Node* root;

    void addNode(Node* node, const Course& course) {
        if (course.courseNumber < node->course.courseNumber) {
            if (node->left == nullptr) {
                node->left = new Node(course);
            } else {
                addNode(node->left, course);
            }
        } else {
            if (node->right == nullptr) {
                node->right = new Node(course);
            } else {
                addNode(node->right, course);
            }
        }
    }

    // In-order traversal prints the courses in alphanumeric order
    void inOrder(Node* node) const {
        if (node == nullptr) {
            return;
        }

        inOrder(node->left);
        cout << node->course.courseNumber << ", " << node->course.courseTitle << endl;
        inOrder(node->right);
    }

    void destroyTree(Node* node) {
        if (node == nullptr) {
            return;
        }

        destroyTree(node->left);
        destroyTree(node->right);
        delete node;
    }

public:
    BinarySearchTree() {
        root = nullptr;
    }

    ~BinarySearchTree() {
        destroyTree(root);
    }

    void clear() {
        destroyTree(root);
        root = nullptr;
    }

    void insert(const Course& course) {
        if (root == nullptr) {
            root = new Node(course);
        } else {
            addNode(root, course);
        }
    }

    // Search the tree by course number
    Course* search(const string& courseNumber) const {
        Node* current = root;
        string target = toUpperCase(courseNumber);

        while (current != nullptr) {
            string currentNumber = toUpperCase(current->course.courseNumber);

            if (target == currentNumber) {
                return &(current->course);
            }

            if (target < currentNumber) {
                current = current->left;
            } else {
                current = current->right;
            }
        }

        return nullptr;
    }

    void printCourseList() const {
        inOrder(root);
    }

    bool empty() const {
        return root == nullptr;
    }
};

// Read the file and load all courses into the BST
bool loadCoursesFromFile(const string& fileName, BinarySearchTree& courseTree) {
    ifstream file(fileName);

    if (!file.is_open()) {
        cout << "Could not open file." << endl;
        return false;
    }

    courseTree.clear();

    vector<Course> loadedCourses;
    string line;

    while (getline(file, line)) {
        line = trim(line);

        if (line.empty()) {
            continue;
        }

        vector<string> parts = splitLine(line);

        // Every line should at least have a course number and title
        if (parts.size() < 2) {
            cout << "Error: invalid file format." << endl;
            courseTree.clear();
            return false;
        }

        Course course;
        course.courseNumber = toUpperCase(parts[0]);
        course.courseTitle = parts[1];

        // Any remaining values on the line are prerequisites
        for (size_t i = 2; i < parts.size(); i++) {
            if (!parts[i].empty()) {
                course.prerequisites.push_back(toUpperCase(parts[i]));
            }
        }

        loadedCourses.push_back(course);
    }

    file.close();

    for (size_t i = 0; i < loadedCourses.size(); i++) {
        courseTree.insert(loadedCourses[i]);
    }

    // This checks that every prerequisite actually exists as a course in the file
    for (size_t i = 0; i < loadedCourses.size(); i++) {
        for (size_t j = 0; j < loadedCourses[i].prerequisites.size(); j++) {
            if (courseTree.search(loadedCourses[i].prerequisites[j]) == nullptr) {
                cout << "Error: prerequisite " << loadedCourses[i].prerequisites[j]
                     << " was not found in the file." << endl;
                courseTree.clear();
                return false;
            }
        }
    }

    return true;
}

// Print one course and also print the prerequisite titles if found
void printSingleCourse(BinarySearchTree& courseTree, const string& courseNumber) {
    Course* course = courseTree.search(courseNumber);

    if (course == nullptr) {
        cout << "Course not found." << endl;
        return;
    }

    cout << course->courseNumber << ", " << course->courseTitle << endl;

    if (course->prerequisites.empty()) {
        cout << "Prerequisites: None" << endl;
        return;
    }

    cout << "Prerequisites: ";
    for (size_t i = 0; i < course->prerequisites.size(); i++) {
        Course* prereqCourse = courseTree.search(course->prerequisites[i]);

        if (prereqCourse != nullptr) {
            cout << prereqCourse->courseNumber << ", " << prereqCourse->courseTitle;
        } else {
            cout << course->prerequisites[i];
        }

        if (i < course->prerequisites.size() - 1) {
            cout << "; ";
        }
    }
    cout << endl;
}

int main() {
    BinarySearchTree courseTree;
    bool dataLoaded = false;
    int choice = 0;

    cout << "Welcome to the course planner." << endl << endl;

    while (choice != 9) {
        cout << "  1. Load Data Structure." << endl;
        cout << "  2. Print Course List." << endl;
        cout << "  3. Print Course." << endl;
        cout << "  9. Exit" << endl << endl;
        cout << "What would you like to do? ";

        cin >> choice;

        if (cin.fail()) {
            cin.clear();
            cin.ignore(1000, '\n');
            cout << "That is not a valid option." << endl << endl;
            continue;
        }

        switch (choice) {
        case 1: {
            string fileName;
            cout << "What file would you like to load? ";
            cin >> fileName;

            if (loadCoursesFromFile(fileName, courseTree)) {
                dataLoaded = true;
                cout << "Data loaded successfully." << endl;
            } else {
                dataLoaded = false;
            }

            cout << endl;
            break;
        }

        case 2:
            if (!dataLoaded) {
                cout << "Please load the data structure first." << endl << endl;
            } else {
                cout << "Here is a sample schedule:" << endl << endl;
                courseTree.printCourseList();
                cout << endl;
            }
            break;

        case 3:
            if (!dataLoaded) {
                cout << "Please load the data structure first." << endl << endl;
            } else {
                string courseNumber;
                cout << "What course do you want to know about? ";
                cin >> courseNumber;
                courseNumber = toUpperCase(courseNumber);
                printSingleCourse(courseTree, courseNumber);
                cout << endl;
            }
            break;

        case 9:
            cout << "Thank you for using the course planner!" << endl;
            break;

        default:
            cout << choice << " is not a valid option." << endl << endl;
            break;
        }
    }

    return 0;
}