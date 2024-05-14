# Definition: A class is an abstract blueprint that defines the properties (attributes) and behaviors (methods) that objects of that type will have.

class Car:
    def __init__(self, brand, model):
        self.brand = brand
        self.model = model

    def start_engine(self):
        print("Engine started!")

