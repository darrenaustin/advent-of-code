// https://adventofcode.com/2015/day/3

import 'package:aoc/aoc.dart';
import 'package:aoc/util/string.dart';
import 'package:aoc/util/vec.dart';

main() => Day03().solve();

class Day03 extends AdventDay {
  Day03() : super(2015, 3);

  @override
  dynamic part1(String input) {
    Vec current = Vec.zero;
    final housesVisited = <Vec>{current};
    for (final dir in input.chars.map((c) => _directionOffset[c]!)) {
      current += dir;
      housesVisited.add(current);
    }
    return housesVisited.length;
  }

  @override
  dynamic part2(String input) {
    Vec santa = Vec.zero;
    Vec robotSanta = Vec.zero;
    bool santaTurn = true;
    final housesVisited = <Vec>{santa};

    for (final dir in input.chars.map((c) => _directionOffset[c]!)) {
      if (santaTurn) {
        santa += dir;
        housesVisited.add(santa);
      } else {
        robotSanta += dir;
        housesVisited.add(robotSanta);
      }
      santaTurn = !santaTurn;
    }
    return housesVisited.length;
  }

  static const _directionOffset = <String, Vec>{
    '^': Vec.up,
    '>': Vec.right,
    'v': Vec.down,
    '<': Vec.left,
  };
}
