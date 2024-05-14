# Inheritance allows creating a new class (derived or child class) based on an existing class (base or parent class).

# Single Inheritance occurs when a class (derived class) inherits from only one base class.


class Base:
    def display(self):
        print("Base display")


class Derived(Base):
    pass


obj = Derived()
obj.display()  # Base display

# In the above example, the Derived class inherits from the Base class. This is an example of Single Inheritance. The Derived class has access to the display method from the Base class.