# Python does not support traditional compile-time polymorphism, but you can achieve a similar effect using default arguments or keyword arguments.


def greet(name, message="Hello", punctuation="!"):
    print(f"{message}, {name}{punctuation}")


# Using default arguments
greet("Alice")  # Output: Hello, Alice!

# Overriding a default argument
greet("Bob", "Good morning")  # Output: Good morning, Bob!

# Using keyword arguments
greet(name="Carol", punctuation=".")  # Output: Hello, Carol.

# Using both default and keyword arguments
greet("David", punctuation="?")  # Output: Hello, David?

# In the above example, the greet function takes three arguments: name, message, and punctuation. The message and punctuation arguments have default values of "Hello" and "!", respectively. This means that if these arguments are not provided when calling the function, the default values will be used.
