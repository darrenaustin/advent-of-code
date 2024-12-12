# advent-of-code-clojure

My solutions to the [Advent of Code][1] puzzles written in the
[Clojure][2] programming language. I love solving programming
puzzles, and my clojure was getting a little rusty.

Given that, these are just my solutions to the problems.
There are likely cleaner, faster or more idiomatic ways to
do this in Clojure (if so, please drop me a line and let me know).

## Input files

The input files needed for these solutions come from the
[Advent of Code][1] site and are customized for each user. They
have [requested][3] that these input files are not made publicly
available. Therefore I have put my input files in a private repo
named `advent_of_code_input` and check it out as a sibling of
this repo. However, you can specify the location of this repo with
the `-DINPUT_REPO=<some path>` command line option to  below.


## Usage

To run the all of the solutions you can use the `aoc` program:

```shell
clj -M -m aoc.core
```

Which as noted above requires the input files to be in a sibling repo.

To run just a specific year's solutions:

```shell
clj -M -m aoc.core 2020
```

To run only specific days you can use any number YYYY.DD arguments:

```shell
clj -M -m aoc.core 2020.3 2024.1
```

## Tests

In addition to the solutions there are also unit tests for each of the problems
as well as the common code in `src/aoc`. To run the tests for a given package use:

```shell
clj -M:test
```

## Code layout

- The main program to run everything is `src/aoc/core.clj`.
- Each yearly event's code is located in its own directory `src/aoc20XX`. Each day's solution is in the year's directory with the format `src/aoc/20XX/dayXX.clj`.
- Useful supporting utilities live in `src/aoc/util`.

## Support the cause if you can

The Advent of Code is an awesome event that we can all look forward to each
year. If you can, please support Eric's efforts at:

https://adventofcode.com/support

[1]: https://adventofcode.com
[2]: https://clojure.org
[3]: https://www.reddit.com/r/adventofcode/wiki/faqs/copyright/inputs
