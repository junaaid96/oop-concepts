# Encapsulation bundles data (attributes) and methods (functions) together within a class, restricting external access to certain attributes.

# Public Access means members of a class are accessible from outside the class.

class MyClass:
    def __init__(self):
        self.publicVar = 0


obj = MyClass()
obj.publicVar = 10
print(obj.publicVar)  # 10
