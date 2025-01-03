// https://adventofcode.com/2016/day/2

import 'package:aoc/aoc.dart';
import 'package:aoc/util/string.dart';
import 'package:aoc/util/vec.dart';

main() => Day02().solve();

class Day02 extends AdventDay {
  Day02() : super(2016, 2);

  @override
  dynamic part1(String input) {
    const List<List<String>> touchPad = [
      ['1', '2', '3'],
      ['4', '5', '6'],
      ['7', '8', '9'],
    ];
    return keycode(touchPad, Vec(1, 1), directions(input));
  }

  @override
  dynamic part2(String input) {
    const List<List<String>> touchPad = [
      ['', '', '1', '', ''],
      ['', '2', '3', '4', ''],
      ['5', '6', '7', '8', '9'],
      ['', 'A', 'B', 'C', ''],
      ['', '', 'D', '', ''],
    ];
    return keycode(touchPad, Vec(0, 2), directions(input));
  }

  Iterable<Iterable<Vec>> directions(String input) {
    const Map<String, Vec> dirVec = {
      'U': Vec.up,
      'D': Vec.down,
      'L': Vec.left,
      'R': Vec.right,
    };
    return input.lines.map((s) => s.split('').map((d) => dirVec[d]!));
  }

  String keycode(List<List<String>> touchPad, Vec keyPosition,
      Iterable<Iterable<Vec>> directions) {
    bool validPos(Vec pos) =>
        0 <= pos.x &&
        pos.x < touchPad[0].length &&
        0 <= pos.y &&
        pos.y < touchPad.length &&
        touchPad[pos.y.toInt()][pos.x.toInt()].isNotEmpty;

    final pressed = [];
    for (final line in directions) {
      for (final dir in line) {
        final newPosition = keyPosition + dir;
        if (validPos(newPosition)) {
          keyPosition = newPosition;
        }
      }
      pressed.add(touchPad[keyPosition.y.toInt()][keyPosition.x.toInt()]);
    }
    return pressed.join();
  }
}
