#!/usr/bin/env bash
# Build and run every C++ example, then build and test every Java module with Maven.
# Usage: ./scripts/run_all.sh          (CXX=g++ ./scripts/run_all.sh to choose a compiler)
set -euo pipefail
cd "$(dirname "$0")/.."

CXX="${CXX:-c++}"
BUILD="$(mktemp -d)"
trap 'rm -rf "$BUILD"' EXIT
fail=0

echo "== C++ (std=c++20) with $CXX =="
for src in basics/*.cpp topics/*/*.cpp; do
  out="$BUILD/$(basename "${src%.cpp}")"
  flags="-std=c++20 -Wall -Wextra"
  [[ $src == topics/* ]] && flags="$flags -Werror"      # new code must be warning-free
  if $CXX $flags "$src" -o "$out" && echo | "$out" > "$BUILD/log" 2>&1; then
    echo "  ok   $src"
  else
    echo "  FAIL $src"; cat "$BUILD/log" 2>/dev/null || true; fail=1
  fi
done

echo "== Java (JDK 25, Maven wrapper): compile + JUnit 5 tests =="
if ./mvnw -B -ntp verify; then echo "  ok   ./mvnw verify"; else echo "  FAIL ./mvnw verify"; fail=1; fi

exit $fail
