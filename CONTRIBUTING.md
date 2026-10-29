# Contributing

Thanks for helping improve this OOP learning resource!

## Adding or improving a topic
1. Topics live in `topics/NN_topic_name/`. Each folder contains:
   - `README.md` — concept guide, common mistakes, **interview questions**, **exercises with solutions**.
   - `pom.xml` — a Maven module whose parent is the root `pom.xml` (add it to `<modules>` there).
   - `src/main/java/oop/<pkg>/<Name>.java` — Java 25 example with comments explaining each part (add a `main` method when a demo helps).
   - `src/test/java/oop/<pkg>/<Name>Test.java` — JUnit 5 tests for the Java example.
   - `<name>.cpp` — C++20 example; `assert`s in `main()` act as its tests.
2. Keep examples small, runnable and commented line by line where it helps a learner.
3. Prefer the standard library only (JUnit is the only Java dependency) so everything runs anywhere.
4. Link the new topic from the root `README.md` learning path.

## Style
- C++: C++20, standard headers only (no `<bits/stdc++.h>`), must compile with `-Wall -Wextra -Werror`
  on both GCC and Clang. Use smart pointers, `override`, `explicit`, `const` correctness.
- Java: Java 25, no compiler warnings under `-Xlint:all`; prefer records, sealed types, pattern matching,
  `final` fields and immutable collections; one top-level class per example file so `java File.java` runs it.

## Before opening a pull request
```bash
./scripts/run_all.sh        # builds all C++ examples, then runs ./mvnw verify
```
CI runs the same script with Temurin JDK 25 on Ubuntu (GCC, Clang) and macOS.

## Commit messages
Use short imperative messages, e.g. `Add visitor pattern example`.

By contributing you agree that your contributions are licensed under the MIT License.
