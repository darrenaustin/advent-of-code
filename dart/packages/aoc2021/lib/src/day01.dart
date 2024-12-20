// https://adventofcode.com/2021/day/1

import 'package:aoc/aoc.dart';
import 'package:aoc/util/range.dart';
import 'package:aoc/util/string.dart';

main() => Day01().solve();

class Day01 extends AdventDay {
  Day01() : super(2021, 1);

  @override
  dynamic part1(String input) {
    return increases(parseMeasurements(input));
  }

  @override
  dynamic part2(String input) {
    final measurements = parseMeasurements(input);
    return increases(range(measurements.length - 2).map(
        (i) => measurements[i] + measurements[i + 1] + measurements[i + 2]));
  }

  List<int> parseMeasurements(String input) =>
      input.lines.map((s) => int.parse(s)).toList();

  int increases(Iterable<int> measurements) {
    int previous = measurements.first;
    int increases = 0;
    for (final int measurement in measurements.skip(1)) {
      if (measurement > previous) {
        increases++;
      }
      previous = measurement;
    }
    return increases;
  }
}
