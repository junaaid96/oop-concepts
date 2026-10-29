# A class is an abstract blueprint that defines the properties (attributes) and behaviors (methods) that objects of that type will have.

class Car:
    def __init__(self, brand, model):
        self.brand = brand
        self.model = model

    def start_engine(self):
        print("Engine started!")

    def stop_engine(self):
        print("Engine stopped!")

    def drive(self):
        print("Driving...")

# In the above example, the Car class is a blueprint for creating Car objects. It has attributes like brand and model, and methods like start_engine, stop_engine, and drive.