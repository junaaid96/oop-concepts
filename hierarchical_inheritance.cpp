// Hierarchical Inheritance occurs when multiple classes inherit from a single base class.

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

class Cat : public Animal
{
public:
    void meow()
    {
        cout << "Meow!" << endl;
    }
};

int main()
{
    Dog dog;
    dog.eat();
    dog.bark();

    Cat cat;
    cat.eat();
    cat.meow();

    return 0;
}