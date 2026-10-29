# Python does not support traditional compile-time polymorphism, but you can achieve a similar effect using default arguments or keyword arguments.

# Example 1: Using default arguments
def add(a, b=0, c=0):
    return a + b + c


print(add(1))
print(add(1, 2))
print(add(1, 2, 3))

# Example 2: Using keyword arguments


def add(*args):
    return sum(args)


print(add(1))
print(add(1, 2))
print(add(1, 2, 3))
