// Multilevel Inheritance involves a hierarchy where a derived class inherits from a base class which in turn is a derived class of another base class.

#include <bits/stdc++.h>
using namespace std;

class Animal
{
public:
    void eat()
    {
        cout << "Eating" << endl;
    }
};

class Dog : public Animal
{
public:
    void bark()
    {
        cout << "Woof!" << endl;
    }
};

class Puppy : public Dog
{
public:
    void weep()
    {
        cout << "Weeping" << endl;
    }
};

int main()
{
    Puppy obj;
    obj.eat();
    obj.bark();
    obj.weep();
    return 0;
}