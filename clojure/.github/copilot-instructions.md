# Project Instructions

This project is where I solve the yearly Advent of Code puzzles in clojure.

## Objectives

- I don't want help solving the puzzles, nor do I want you to give me solutions.
- I may ask you to review my solutions afterwards to see if you have any advice on how to improve the code quality.
- I will ask for occasional advice on code style or better ways to express something in Clojure.
- I will also ask for help developing my util libraries, their documentation and unit tests.
- I prefer readable, concise, idiomattic, performant and elegant code. In that order.

## Project Structure
This is a Clojure project. All code unless otherwise indicated should be in Clojure 1.12.0. Please
use any prefered "modern clojure" techniques.

The project has the following folder structure:

```
├── scripts/        - Utility bb scripts for the project
├── src/            - Main directory for all the source code
│   ├── aoc/        - Top-level definitions for running individual day's solutions
│   │   ├── util/   - Libraries of utilities useful for various kinds of puzzles
│   ├── aoc2015/    - Solutions for the given year. Each days is in dayXX.clj.
│   ├── ...
│   └── aoc2025/
├── templates/      - Used by the bb new-day task to create skeleton day files.
├── test/           - Main directory for all tests.
│   ├── aoc/        - Unit tests for everything under src/aoc
│   ├── aoc2015/    - Unit tests for each year. Each days tests are in dayXX_test.clj
│   ├── ...
│   └── aoc2025/
├── bb.edn          - Useful bb commands
├── deps.edn        - Project configuration, defining dependences.
└── tests.edn       - Configuration for the test runner.
```

### Input files
The input files used in the problems can be found in:

`../input/YYYY/DD_input.txt` for each problem, where
YYYY is the year and DD is the two digit day number.

## Running Tests
- To run all tests: `bb test`
- To focus on a specific namespace: `bb test --focus <namespace>`
- It can sometimes take time for `bb` to startup, so give it a couple of seconds before assuming it has failed.

## Writing unit tests
- The tests for a given function or macro should live in a file under the `test` directory.
- The folder structure under `test` should mirror the function's file path under `src`.
- The test filename should match the source filename with `_test` appended (e.g. `day01.clj` -> `day01_test.clj`).
- The test namespace should match the source namespace with `-test` appended.
- If the needed test file or directories don't exist, please create them.
- The order of the test functions should be exactly the same order as the functions in the original `src` file.

## Investigation testing
If you need to figure something out by running your own tests, feel free to create a test file at the top of the tree. You can then execute it in this project with the following command:

```
clj -M -m <test-file>.clj
```
