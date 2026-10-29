# Abstraction hides the implementation details and exposes only relevant features to the user.

from abc import ABC, abstractmethod


class Shape(ABC):
    @abstractmethod
    def area(self):
        pass

    @abstractmethod
    def perimeter(self):
        pass


class Circle(Shape):
    def __init__(self, radius):
        self.radius = radius

    def area(self):
        return 3.14 * self.radius * self.radius

    def perimeter(self):
        return 2 * 3.14 * self.radius


class Rectangle(Shape):
    def __init__(self, length, breadth):
        self.length = length
        self.breadth = breadth

    def area(self):
        return self.length * self.breadth

    def perimeter(self):
        return 2 * (self.length + self.breadth)


circle = Circle(7)

print("Area of circle:", circle.area())
print("Perimeter of circle:", circle.perimeter())

rectangle = Rectangle(5, 10)

print("Area of rectangle:", rectangle.area())
print("Perimeter of rectangle:", rectangle.perimeter())

# In the above example, the Shape class is an abstract class that defines two abstract methods: area and perimeter. The Circle and Rectangle classes inherit from the Shape class and implement these abstract methods. The abstract methods enforce that any subclass of the Shape class must implement these methods. This allows us to define a common interface for different shapes while hiding the implementation details of each shape.
