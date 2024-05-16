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

// In the above code, we have a base class Animal and two derived classes Dog and Cat. Both Dog and Cat classes inherit from the Animal class. The eat() method is defined in the Animal class and is inherited by both Dog and Cat classes. The bark() method is defined in the Dog class, and the meow() method is defined in the Cat class. When we create objects of the Dog and Cat classes, they can access the eat() method from the Animal class and their own specific methods bark() and meow().