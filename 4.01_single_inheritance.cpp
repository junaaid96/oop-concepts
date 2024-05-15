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

// In the above code, we have a base class Base and a derived class Derived that inherits from the Base class. The Base class has a method display(). The Derived class inherits the display() method from the Base class. When we create an object of the Derived class, it can access the display() method from the Base class. This is an example of single inheritance. Single Inheritance is the simplest form of inheritance and is used to create a hierarchy of classes. It allows for code reusability and helps in organizing the classes in a logical hierarchy.