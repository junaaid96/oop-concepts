// Methods are functions defined within a class. They perform actions related to the class. In C++, methods are defined within the class definition using the `class` keyword. They can be public, private, or protected, depending on the access level required. Methods can access and modify the data members of the class. this pointer is used to refer to the current object within a method.

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

// we have a class Animal with a public method eat(). The method eat() is defined within the class definition and performs the action of eating. It is accessible to all objects of the Animal class.

class Person
{
private:
    string name;

public:
    Person(string name)
    {
        this->name = name;
    }

    void display()
    {
        cout << "Name: " << this->name << endl;
    }
};

int main()
{
    Animal animal;
    animal.eat();

    Person person("Alice");
    person.display();

    return 0;
}

// we have a class Person with a private data member name and a public method display(). The constructor of the Person class takes a name parameter and assigns it to the name data member using the this pointer. The display() method uses the this pointer to access the name data member and display it. The this pointer refers to the current object within a method and is used to access the data members and methods of the object. It is a keyword in C++ and is automatically available within class methods.