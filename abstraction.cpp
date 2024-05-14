// Abstraction hides the implementation details and exposes only relevant features to the user.

#include <bits/stdc++.h>
using namespace std;

class Shape
{
public:
    virtual void draw() = 0;
};

class Circle : public Shape
{
public:
    void draw() override
    {
        cout << "Drawing Circle" << endl;
    }
};

class Rectangle : public Shape
{
public:
    void draw() override
    {
        cout << "Drawing Rectangle" << endl;
    }
};

int main()
{
    Circle c;
    Rectangle r;
    c.draw();
    r.draw();
    return 0;
}

// In the above code, we have a base class Shape with a pure virtual function draw(). The Circle and Rectangle classes inherit from the Shape class and implement the draw() method. The draw() method is an abstract method in the Shape class, and it is overridden in the Circle and Rectangle classes. When we create objects of the Circle and Rectangle classes, we can call the draw() method to draw the respective shapes. This is an example of abstraction, where the implementation details of drawing the shapes are hidden from the user, and only the relevant feature (draw) is exposed.