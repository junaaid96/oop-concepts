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

// In the above code, we have a class Animal with public data members name and age. These data members are accessible from outside the class. We create an object obj of the Animal class and set the name and age values. We can access and modify the name and age data members directly using the object obj. This is an example of public access in encapsulation, where the data members are accessible from outside the class. Public access allows external code to interact with the class and its data members directly. It is important to note that public access can lead to data integrity issues if not used carefully. It is recommended to use private access for data members and provide public member functions to access and modify the data in a controlled manner. This helps in maintaining data integrity and encapsulation in OOP.