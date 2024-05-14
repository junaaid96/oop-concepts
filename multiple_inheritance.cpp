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
