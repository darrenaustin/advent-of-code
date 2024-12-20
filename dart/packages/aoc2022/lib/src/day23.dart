// https://adventofcode.com/2022/day/23

import 'dart:math';

import 'package:aoc/aoc.dart';
import 'package:aoc/util/range.dart';
import 'package:aoc/util/string.dart';
import 'package:aoc/util/vec.dart';
import 'package:collection/collection.dart';

main() => Day23().solve();

class Day23 extends AdventDay {
  Day23() : super(2022, 23);

  @override
  dynamic part1(String input) {
    final field = Field(input.lines);
    for (final _ in range(10)) {
      field.nextRound();
    }
    return field.minimalGridEmptyCells();
  }

  @override
  dynamic part2(String input) {
    final field = Field(input.lines);
    while (field.nextRound()) {}
    return field.round;
  }
}

class Field {
  Field(List<String> data) {
    final gridData = data.map((l) => l.split('')).toList();
    for (int y = 0; y < gridData.length; y++) {
      for (int x = 0; x < gridData.first.length; x++) {
        final cell = gridData[y][x];
        if (cell == '#') {
          _elves.add(Vec(x, y));
        }
      }
    }
  }

  int round = 0;
  final Set<Vec> _elves = {};

  bool nextRound() {
    // Find all elves with at least another around them.
    final movingElves =
        _elves.where((e) => Vec.aroundDirs.any((d) => _elves.contains(d + e)));

    // Determine the plans for all moving elves.
    final plans = <Vec, Vec>{};
    final destinations = <Vec>{};
    final overbooked = <Vec>{};
    for (final elf in movingElves) {
      for (final dir in _testDirs) {
        if (dir.checkDirections
            .map((d) => d + elf)
            .none((p) => _elves.contains(p))) {
          final destination = elf + dir.direction;
          if (destinations.contains(destination)) {
            overbooked.add(destination);
          } else {
            plans[elf] = destination;
            destinations.add(destination);
          }
          break;
        }
      }
    }

    // Move the elves that aren't going to overbooked locations.
    for (final plan in plans.entries) {
      if (!overbooked.contains(plan.value)) {
        _elves.remove(plan.key);
        _elves.add(plan.value);
      }
    }

    // Rotate the test directions
    _testDirs.add(_testDirs.removeAt(0));
    round += 1;

    return plans.isNotEmpty;
  }

  int minimalGridEmptyCells() {
    // Determine minimal grid around elves.
    Vec minS = Vec.zero;
    Vec maxS = Vec.zero;
    for (final elf in _elves) {
      minS = Vec(min(minS.x, elf.x), min(minS.y, elf.y));
      maxS = Vec(max(maxS.x, elf.x), max(maxS.y, elf.y));
    }

    // Calculate the empty cells in the grid.
    final span = maxS - minS;
    final numCells = (span.xInt + 1) * (span.yInt + 1);
    return numCells - _elves.length;
  }

  // Direction to travel if there are no neighbors in the directions list.
  final List<CheckDirs> _testDirs = [
    CheckDirs(Vec.up, [Vec.upLeft, Vec.up, Vec.upRight]),
    CheckDirs(Vec.down, [Vec.downLeft, Vec.down, Vec.downRight]),
    CheckDirs(Vec.left, [Vec.upLeft, Vec.left, Vec.downLeft]),
    CheckDirs(Vec.right, [Vec.upRight, Vec.right, Vec.downRight]),
  ];
}

class CheckDirs {
  CheckDirs(this.direction, this.checkDirections);
  final Vec direction;
  final List<Vec> checkDirections;
}
