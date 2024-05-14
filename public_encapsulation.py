# Encapsulation bundles data (attributes) and methods (functions) together within a class, restricting external access to certain attributes.

# Public Access means members of a class are accessible from outside the class.

class MyClass:
    def __init__(self):
        self.publicVar = 0


obj = MyClass()
obj.publicVar = 10
print(obj.publicVar)  # 10

# In the above example, the publicVar attribute is a public variable, accessible from outside the class. The value of the publicVar attribute can be set and accessed directly from an instance of the class. This demonstrates public access in encapsulation.
# Encapsulation is a fundamental concept in object-oriented programming that helps in organizing and structuring code by bundling data and methods within a class. It also helps in controlling access to attributes, allowing for better data protection and security. By using public, private, and protected access modifiers, developers can define the level of access to class members, providing flexibility and control over how data is accessed and modified.