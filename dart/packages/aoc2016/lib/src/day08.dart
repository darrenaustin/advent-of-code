// https://adventofcode.com/2016/day/8

import 'package:aoc/aoc.dart';
import 'package:aoc/util/grid.dart';
import 'package:aoc/util/string.dart';
import 'package:aoc/util/vec.dart';

main() => Day08().solve();

class Day08 extends AdventDay {
  Day08() : super(2016, 8);

  @override
  dynamic part1(String input) {
    final Screen screen = Screen();
    for (final instruction in input.lines) {
      runInstruction(screen, instruction);
    }
    return screen.numLit();
  }

  @override
  dynamic part2(String input) {
    final Screen screen = Screen();
    for (final instruction in input.lines) {
      runInstruction(screen, instruction);
    }
    print('\n');
    print(screen);
    print('\n');
    // don't have an OCR for this, so manual inspection of the screen output is:
    return 'UPOJFLBCEZ';
  }

  final _rectPattern = RegExp(r'rect (\d+)x(\d+)');
  final _colPattern = RegExp(r'rotate column x=(\d+) by (\d+)');
  final _rowPattern = RegExp(r'rotate row y=(\d+) by (\d+)');

  void runInstruction(Screen screen, String instruction) {
    var match = _rectPattern.firstMatch(instruction);
    if (match != null) {
      screen.rect(int.parse(match.group(1)!), int.parse(match.group(2)!));
      return;
    }
    match = _colPattern.firstMatch(instruction);
    if (match != null) {
      screen.rotateColumn(
          int.parse(match.group(1)!), int.parse(match.group(2)!));
      return;
    }
    match = _rowPattern.firstMatch(instruction);
    if (match != null) {
      screen.rotateRow(int.parse(match.group(1)!), int.parse(match.group(2)!));
      return;
    }
    throw 'Unknown instruction: $instruction';
  }
}

class Screen {
  Screen() : _grid = Grid<String>(50, 6, ' ');

  final Grid<String> _grid;

  void rect(int width, int height) {
    for (int h = 0; h < height; h++) {
      for (int w = 0; w < width; w++) {
        _grid.setValue(Vec(w, h), '#');
      }
    }
  }

  void rotateColumn(int col, int amount) {
    for (int times = 0; times < amount; times++) {
      final bottom = _grid.value(Vec(col, _grid.height - 1));
      for (int h = _grid.height - 1; h > 0; h--) {
        _grid.setValue(Vec(col, h), _grid.value(Vec(col, h - 1)));
      }
      _grid.setValue(Vec(col, 0), bottom);
    }
  }

  void rotateRow(int row, int amount) {
    for (int times = 0; times < amount; times++) {
      final right = _grid.value(Vec(_grid.width - 1, row));
      for (int w = _grid.width - 1; w > 0; w--) {
        _grid.setValue(Vec(w, row), _grid.value(Vec(w - 1, row)));
      }
      _grid.setValue(Vec(0, row), right);
    }
  }

  int numLit() => _grid.values().where((String s) => s == '#').length;

  @override
  String toString() => _grid.toString();
}
