// Compile-Time Polymorphism is achieved through method overloading or operator overloading.
// Method overloading is a feature that allows us to have more than one function with the same name in the same scope. The function is overloaded by its signature (the return type, number of arguments, and the data type of the arguments).

#include <bits/stdc++.h>
using namespace std;

class CompileTimePoly
{
public:
    void display(int i)
    {
        cout << "Displaying int: " << i;
    }
    void display(double d)
    {
        cout << "Displaying double: " << d;
    }
};

int main()
{
    CompileTimePoly c;
    c.display(5);
    cout << endl;
    c.display(5.5);
    return 0;
}

// In the above code, we have two display() methods in the CompileTimePoly class. One takes an integer argument and the other takes a double argument. When we call the display() method with an integer argument, the display() method that takes an integer argument is called. When we call the display() method with a double argument, the display() method that takes a double argument is called. This is called compile-time polymorphism. The function to be called is determined at compile time based on the arguments passed to the function.