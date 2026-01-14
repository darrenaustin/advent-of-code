# 🎄 Darren's Advent of Code  🎄

My solutions to the [Advent of Code][1] puzzles written in various
programming languages. I love solving programming puzzles, and this
allows me to explore different languages and algorithms in a fun way.

Given that, these are just my solutions to the problems.
There are undoubtedly cleaner, faster or more idiomatic ways to
solve them in a given language. (if so, please drop me a line
and let me know how to improve them).

## Support the cause

The Advent of Code is an awesome event that we can all look forward to each
year. If you can, please support Eric's efforts at:

https://adventofcode.com/support

## Current languages

I have solutions in various forms for:

- **[clojure][2]** - Really love [Clojure][3], but it had been years since I used it for anything.
- **[dart][4]** - Used [Dart][5] a lot when I was on the [Flutter][6] team, and wanted to sharpen my skills with it.

## Solutions by year

| [2015](SCOREBOARD.md#2015) | [2016](SCOREBOARD.md#2016) | [2017](SCOREBOARD.md#2017) | [2018](SCOREBOARD.md#2018) | [2019](SCOREBOARD.md#2019) | [2020](SCOREBOARD.md#2020) | [2021](SCOREBOARD.md#2021) | [2022](SCOREBOARD.md#2022) | [2023](SCOREBOARD.md#2023) | [2024](SCOREBOARD.md#2024) | [2025](SCOREBOARD.md#2025) |
| :------------------------: | :------------------------: | :------------------------: | :------------------------: | :------------------------: | :------------------------: | :------------------------: | :------------------------: | :------------------------: | :------------------------: | :------------------------: |
|            50 ⭐️            |            50 ⭐️            |            50 ⭐️            |            50 ⭐️            |            42 ⭐️            |            50 ⭐️            |            42 ⭐️            |            50 ⭐️            |            50 ⭐️            |            50 ⭐️            |            24 ⭐️            |

## Input files

The input files needed for these solutions come from the
[Advent of Code][1] site and are customized for each user. They
have [requested][7] that these input files are not made publicly
available. Therefore I have encrypted them in the top level `input`
directory using [git-crypt][8] following [this guide][9]. However,
you can specify a different location if you want to run them against
your own input by using the `-DAOC_INPUT_DIR=<some path>` command line
option to any of the language runners.

### Answer files
In addition to the raw input files mentioned above, I also store a
`DD_answer.json` file for each day's problem that has the form of:

```json
{
  "name" : "Not Quite Lisp",
  "answer1" : 42,
  "answer2" : 4242
}
```

This gives the name of the problem and the solutions for my input.
These answer files allow me to write tests against them to make sure
I don't break anything when solving new problems.

[1]: https://adventofcode.com
[2]: clojure/README.md
[3]: https://clojure.org/
[4]: dart/README.md
[5]: https://dart.dev/
[6]: https://flutter.dev/
[7]: https://adventofcode.com/about#faq_copying
[8]: https://github.com/AGWA/git-crypt
[9]: https://github.com/tschady/advent-of-code/blob/main/ENCRYPT.adoc
