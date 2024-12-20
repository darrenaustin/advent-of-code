// https://adventofcode.com/2022/day/14

import 'package:aoc/aoc.dart';
import 'package:aoc/util/sparse_grid.dart';
import 'package:aoc/util/string.dart';
import 'package:aoc/util/vec.dart';

main() => Day14().solve();

class Day14 extends AdventDay {
  Day14() : super(2022, 14);

  @override
  dynamic part1(String input) =>
      (SandMap(input.lines)..dropAllSand()).numSand();

  @override
  dynamic part2(String input) =>
      (SandMap(input.lines, floor: true)..dropAllSand()).numSand();
}

class SandMap {
  static final sand = 'o';
  static final rock = '#';
  static final space = ' ';
  static final sandSource = Vec(500, 0);
  static final dropDirections = [Vec.down, Vec.downLeft, Vec.downRight];

  SandMap(List<String> rockData, {bool floor = false})
      : _grid = SparseGrid<String>(defaultValue: space) {
    for (final line in rockData) {
      final rockPath = line
          .split(' -> ')
          .map((p) => p.split(','))
          .map((v) => Vec(double.parse(v.first), double.parse(v.last)));
      _drawPath(rockPath, rock);
    }
    _floor = floor ? _grid.maxLocation.y + 2 : null;
  }

  final SparseGrid<String> _grid;
  late final double? _floor;

  void dropAllSand() {
    while (!dropSand(sandSource)) {}
  }

  bool dropSand(Vec from) {
    if (_grid.cell(from) != space) {
      // Source is covered, so we are done.
      return true;
    }
    final dropSpots = dropDirections.map((d) => d + from);
    for (final spot in dropSpots) {
      if (_floor == null && spot.y > _grid.maxLocation.y) {
        // Falls forever
        return true;
      }
      final spotContents =
          spot.y < (_floor ?? double.infinity) ? _grid.cell(spot) : rock;
      if (spotContents == space) {
        return dropSand(spot);
      }
    }
    // Can't go any further, so it lands here
    _grid.setCell(from, sand);
    return false;
  }

  int numSand() => _grid.numSetCellsWhere((p) => p == sand);

  void _drawPath(Iterable<Vec> path, String value) {
    Vec current = path.first;
    for (final dest in path.skip(1)) {
      _drawLine(current, dest, rock);
      current = dest;
    }
  }

  void _drawLine(Vec from, Vec to, String value) {
    final diff = to - from;
    final delta = Vec(diff.x.sign, diff.y.sign);
    for (Vec current = from; current != to; current += delta) {
      _grid.setCell(current, value);
    }
    _grid.setCell(to, value);
  }
}
