// https://adventofcode.com/2022/day/8

import 'package:aoc/aoc.dart';
import 'package:aoc/util/collection.dart';
import 'package:aoc/util/grid.dart';
import 'package:aoc/util/string.dart';
import 'package:aoc/util/vec.dart';
import 'package:collection/collection.dart';

main() => Day08().solve();

class Day08 extends AdventDay {
  Day08() : super(2022, 8);

  @override
  dynamic part1(String input) {
    final grid = parseGrid(input);

    final Set<Vec> visible = {};
    for (int col = 0; col < grid.width; col++) {
      visible.addAll(seenInDirection(grid, Vec(col, 0), Vec.down));
      visible.addAll(seenInDirection(grid, Vec(col, grid.height - 1), Vec.up));
    }
    for (int row = 0; row < grid.height; row++) {
      visible.addAll(seenInDirection(grid, Vec(0, row), Vec.right));
      visible.addAll(seenInDirection(grid, Vec(grid.width - 1, row), Vec.left));
    }
    return visible.length;
  }

  @override
  dynamic part2(String input) {
    final grid = parseGrid(input);
    int score(Vec tree) =>
        Vec.orthogonalDirs.map((d) => numSeenFromTree(grid, tree, d)).product;
    return grid.locations().map(score).max;
  }

  Iterable<Vec> seenInDirection(Grid<int> grid, Vec starting, Vec dir) {
    final List<Vec> seen = [starting];
    Vec max = starting;
    Vec current = starting + dir;
    while (grid.validLocation(current)) {
      if (grid.value(max) < grid.value(current)) {
        seen.add(current);
        max = current;
      }
      current = current + dir;
    }
    return seen;
  }

  int numSeenFromTree(Grid<int> grid, Vec tree, Vec dir) {
    int seen = 0;
    int treeHeight = grid.value(tree);
    Vec current = tree + dir;
    while (grid.validLocation(current)) {
      seen += 1;
      if (grid.value(current) >= treeHeight) {
        return seen;
      }
      current = current + dir;
    }
    return seen;
  }

  Grid<int> parseGrid(String input) {
    final data =
        input.lines.map((l) => l.chars.map(int.parse).toList()).toList();
    return Grid<int>.from(data, 0);
  }
}
