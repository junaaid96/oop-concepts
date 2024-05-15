// A class is an abstract blueprint that defines the properties (attributes) and behaviors (methods) that objects of that type will have.

#include <bits/stdc++.h>
using namespace std;

class Car
{
public:
    string brand;
    string model;

    void startEngine()
    {
        cout << "Engine started!" << endl;
    }

    void stopEngine()
    {
        cout << "Engine stopped!" << endl;
    }
};

// In the above code, we have defined a class Car with two attributes brand and model, and a method startEngine(). The class Car is a blueprint for creating Car objects. The attributes brand and model define the properties of the Car objects, and the method startEngine() defines the behavior of the Car objects.