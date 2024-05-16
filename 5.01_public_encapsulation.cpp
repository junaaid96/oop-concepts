// Encapsulation bundles data (attributes) and methods (functions) together within a class, restricting external access to certain attributes.

// Public Access means members of a class are accessible from outside the class.

#include <bits/stdc++.h>
using namespace std;

class Animal
{
public:
    string name;
    int age;
};

int main()
{
    Animal obj;
    obj.name = "Lion";
    obj.age = 5;
    cout << "Name: " << obj.name << endl;
    cout << "Age: " << obj.age << endl;
    return 0;
}

// In the above code, we have a class Animal with public data members name and age. These data members are accessible from outside the class. We create an object obj of the Animal class and set the name and age values. We can access and modify the name and age data members directly using the object obj.