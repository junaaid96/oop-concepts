# Encapsulation bundles data (attributes) and methods (functions) together within a class, restricting external access to certain attributes.

# Private Access means members of a class are not accessible from outside the class. In Python, private members are denoted by double underscore (__) before the member name. but we can set and get the private variable using setter and getter methods.

class MyClass:
    def __init__(self):
        self.__privateVar = 0

    def setVar(self, privateVar):
        self.__privateVar = privateVar

    def getVar(self):
        return self.__privateVar


obj = MyClass()
obj.setVar(10)
print(obj.getVar())

# print(obj.__privateVar) # AttributeError: 'MyClass' object has no attribute '__privateVar'
# This is because __privateVar is a private variable and cannot be accessed from outside the class.
