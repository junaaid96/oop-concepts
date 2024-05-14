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