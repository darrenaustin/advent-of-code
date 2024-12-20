// https://adventofcode.com/2021/day/2

import 'package:aoc/aoc.dart';
import 'package:aoc/util/string.dart';
import 'package:aoc/util/vec.dart';

main() => Day02().solve();

class Day02 extends AdventDay {
  Day02() : super(2021, 2);

  @override
  dynamic part1(String input) {
    final destination = parseDirections(input).reduce((a, b) => a + b);
    return destination.x * destination.y;
  }

  @override
  dynamic part2(String input) {
    final directions = parseDirections(input);
    Vec destination = Vec.zero;
    double aim = 0;
    for (final direction in directions) {
      destination += Vec(direction.x, aim * direction.x);
      aim += direction.y;
    }
    return destination.x * destination.y;
  }

  List<Vec> parseDirections(String input) {
    const dirVec = {
      'forward': Vec.right,
      'up': Vec.up,
      'down': Vec.down,
    };
    return input.lines.map((line) {
      final parts = line.split(' ');
      return dirVec[parts[0]]! * int.parse(parts[1]);
    }).toList();
  }
}
