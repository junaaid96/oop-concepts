// Definition: A class is an abstract blueprint that defines the properties (attributes) and behaviors (methods) that objects of that type will have.

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
