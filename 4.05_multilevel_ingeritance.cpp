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

// In the above code, we have a base class Animal, a derived class Dog that inherits from Animal, and a further derived class Puppy that inherits from Dog. The eat() method is defined in the Animal class and is inherited by the Dog and Puppy classes. The bark() method is defined in the Dog class and is inherited by the Puppy class. The weep() method is defined in the Puppy class. When we create an object of the Puppy class, it can access methods from the Animal, Dog, and Puppy classes. This is an example of multilevel inheritance.