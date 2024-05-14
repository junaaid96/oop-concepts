// Hybrid Inheritance is a combination of two or more types of inheritance.

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

class Mammal : public Animal
{
public:
    void feedMilk()
    {
        cout << "Feeding milk" << endl;
    }
};

class Bird : public Animal
{
public:
    void layEggs()
    {
        cout << "Laying eggs" << endl;
    }
};

class Bat : public Mammal, public Bird
{
};

int main()
{
    Bat obj;
    obj.feedMilk();
    obj.layEggs();
    obj.eat();
    return 0;
}

// In the above code, we have a base class Animal, two derived classes Mammal and Bird, and a class Bat that inherits from both Mammal and Bird classes. The Mammal class has a method feedMilk() and the Bird class has a method layEggs(). The Bat class inherits both the feedMilk() method from the Mammal class and the layEggs() method from the Bird class. When we create an object of the Bat class, it can access methods from both the Mammal and Bird classes. This is an example of hybrid inheritance.