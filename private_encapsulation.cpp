// Encapsulation bundles data (attributes) and methods (functions) together within a class, restricting external access to certain attributes.

// Private Access means members of a class are not accessible from outside the class. Only the class and friends can access private members. but we can set and get the value of private members using public member functions.

#include <bits/stdc++.h>
using namespace std;

class MyClass
{
private:
    int privateVar;

public:
    void setPrivateVar(int x)
    {
        privateVar = x;
    }
    int getPrivateVar()
    {
        return privateVar;
    }
};

int main()
{
    MyClass obj;
    obj.setPrivateVar(42);
    cout << obj.getPrivateVar() << endl; // 42
    // obj.privateVar = 42; // Error: 'int MyClass::privateVar' is private within this context
    return 0;
}

// In the above code, we have a class MyClass with a private data member privateVar. The privateVar member is not accessible from outside the class. We have defined public member functions setPrivateVar() and getPrivateVar() to set and get the value of the privateVar member. These member functions can be used to access and modify the privateVar member from outside the class. This is an example of encapsulation, where the data (privateVar) and methods (setPrivateVar() and getPrivateVar()) are bundled together within the class MyClass. The privateVar member is encapsulated within the class and can only be accessed through the public member functions. This helps in controlling access to the data and enforcing data integrity. Encapsulation is a fundamental concept in object-oriented programming and helps in building robust and maintainable code.
