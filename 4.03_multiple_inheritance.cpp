// Multiple Inheritance occurs when a class inherits from more than one base class.

#include <bits/stdc++.h>
using namespace std;

class Mammal
{
public:
    void feedMilk()
    {
        cout << "Feeding milk" << endl;
    }
};

class Bird
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
    return 0;
}

// In the above code, we have two base classes Mammal and Bird, and a class Bat that inherits from both Mammal and Bird classes. The Mammal class has a method feedMilk() and the Bird class has a method layEggs(). The Bat class inherits both the feedMilk() method from the Mammal class and the layEggs() method from the Bird class. When we create an object of the Bat class, it can access methods from both the Mammal and Bird classes.