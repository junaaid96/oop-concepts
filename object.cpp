// An object is an instance of a class. It represents a specific entity with its own unique attributes and behaviors.

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
};

int main()
{
    Car myCar;
    myCar.brand = "Toyota";
    myCar.model = "Camry";
    myCar.startEngine();

    return 0;
};

// In the above code, we have a class Car with attributes brand and model, and a method startEngine(). We create an object myCar of the Car class and set its brand and model attributes to "Toyota" and "Camry" respectively. We then call the startEngine() method on the myCar object, which prints "Engine started!". This is an example of using objects in C++ to represent real-world entities and perform actions on them. Objects allow us to model and interact with entities in a structured and organized manner.