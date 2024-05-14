// Inheritance allows creating a new class (derived or child class) based on an existing class (base or parent class).

// Single Inheritance occurs when a class (derived class) inherits from only one base class.

#include <bits/stdc++.h>
using namespace std;

class Base
{
public:
    void display()
    {
        cout << "Base display";
    }
};

class Derived : public Base
{
};

int main()
{
    Derived obj;
    obj.display(); // Base display
    return 0;
}