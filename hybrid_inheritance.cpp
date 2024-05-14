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