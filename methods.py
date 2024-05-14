# Methods are functions defined within a class. They perform actions related to the class.
# Methods are defined using the def keyword, followed by the method name and parentheses. The method can take arguments, which are specified within the parentheses. The method body is indented and contains the code that defines the behavior of the method.
# Methods can access and modify the attributes of the class using the self keyword. The self keyword refers to the current instance of the class and allows methods to access and modify the instance's attributes.

class Animal:
    # Define a method called eat
    def eat(self):
        print("Eating")