# Contributing

Thanks for helping improve this OOP learning resource!

## Adding or improving a topic
1. Topics live in `topics/NN_topic_name/`. Each folder contains:
   - `README.md` — concept guide, common mistakes, **interview questions**, **exercises with solutions**.
   - `<name>.py` — Python 3.10+ example with comments explaining each part.
   - `test_<name>.py` — `unittest` tests for the Python example.
   - `<name>.cpp` — C++20 example; `assert`s in `main()` act as its tests.
2. Keep examples small, runnable and commented line by line where it helps a learner.
3. Prefer standard library only (no third-party packages) so everything runs anywhere.
4. Link the new topic from the root `README.md` learning path.

## Style
- C++: C++20, standard headers only (no `<bits/stdc++.h>`), must compile with `-Wall -Wextra -Werror`
  on both GCC and Clang. Use smart pointers, `override`, `explicit`, `const` correctness.
- Python: type hints, PEP 8, `from __future__ import annotations`.

## Before opening a pull request
```bash
./scripts/run_all.sh        # builds all C++ examples and runs all Python tests
```
CI runs the same script on Ubuntu (GCC, Clang) and macOS.

## Commit messages
Use short imperative messages, e.g. `Add visitor pattern example`.

By contributing you agree that your contributions are licensed under the MIT License.
