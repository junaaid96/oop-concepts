#!/usr/bin/env bash
# Build and run every C++ example and every Python test in the repository.
# Usage: ./scripts/run_all.sh          (CXX=g++ ./scripts/run_all.sh to choose a compiler)
set -euo pipefail
cd "$(dirname "$0")/.."

CXX="${CXX:-c++}"
PY="${PYTHON:-python3}"
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

echo "== Python basics (smoke run) =="
for src in basics/*.py; do
  if echo 5 | $PY "$src" > /dev/null; then echo "  ok   $src"; else echo "  FAIL $src"; fail=1; fi
done

echo "== Python unit tests =="
for dir in topics/*/; do
  if (cd "$dir" && $PY -m unittest discover -q > "$BUILD/log" 2>&1); then
    echo "  ok   $dir"
  else
    echo "  FAIL $dir"; cat "$BUILD/log"; fail=1
  fi
done

exit $fail
