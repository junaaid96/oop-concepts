# An object is an instance of a class. It represents a specific entity with its own unique attributes and behaviors.

class Car:
    def __init__(self, brand, model):
        self.brand = brand
        self.model = model

    def start_engine(self):
        print("Engine started!")


my_car = Car(brand="Toyota", model="Camry")
my_car.start_engine()

