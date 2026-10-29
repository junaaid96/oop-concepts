# Run-Time Polymorphism is achieved through method overriding.
# Method overriding is a feature that allows the derived class to provide another implementation of the method that is already defined in its parent class. This enables the derived class to customize or completely replace the behavior of that method as needed.

class Base:
    def display(self):
        print("Base display")


class Derived(Base):
    def display(self):
        print("Derived display")


d = Derived()
d.display()  # Derived display

b = Base()
b.display()  # Base display

# In the above example, the display() method is overridden in the Derived class. When an instance of the Derived class calls the display() method, the overridden method in the Derived class is executed. When an instance of the Base class calls the display() method, the method in the Base class is executed.
