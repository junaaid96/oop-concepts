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
