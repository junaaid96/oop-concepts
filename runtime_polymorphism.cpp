// Run-Time Polymorphism is achieved through method overriding.
// Method overriding is a feature that allows us to have a same method in child class which is already present in the parent class.

#include <bits/stdc++.h>
using namespace std;

class Base
{
public:
    virtual void display()
    {
        cout << "Base display";
    }
};

class Derived : public Base
{
public:
    void display() override
    {
        cout << "Derived display";
    }
};

int main()
{
    Base *b;
    Derived d;
    b = &d;
    b->display();
    return 0;
}

// display() method is overridden in the Derived class. When we call display() method using the base class pointer pointing to the derived class object, the display() method of the derived class is called. This is called run-time polymorphism.

// virtual keyword is used in the base class method declaration. It is used to tell the compiler to perform dynamic linkage or late binding on the function. It tells the compiler that the function may be overridden in the derived class. The override keyword is used in the derived class method declaration. It is used to tell the compiler that the function is an override of a base class method. If the function is not an override of a base class method, the compiler will give an error.

