// https://adventofcode.com/2015/day/18

import 'package:aoc/aoc.dart';
import 'package:aoc/util/collection.dart';
import 'package:aoc/util/grid.dart';
import 'package:aoc/util/string.dart';
import 'package:aoc/util/vec.dart';

main() => Day18().solve();

class Day18 extends AdventDay {
  Day18() : super(2015, 18);

  @override
  dynamic part1(String input, [int steps = 100]) =>
      iterate(animate, lightGrid(input))
          .elementAt(steps)
          .values()
          .where(lightOn)
          .length;

  @override
  dynamic part2(String input, [int steps = 100]) =>
      iterate(cornersOnAnimate, turnCornersOn(lightGrid(input)))
          .elementAt(steps)
          .values()
          .where(lightOn)
          .length;

  static Grid<String> lightGrid(String input) =>
      Grid.from(input.lines.map((s) => s.chars).toList(), '.');

  static bool lightOn(String l) => l == '#';

  static Grid<String> animate(Grid<String> lights) {
    final nextLights = Grid.emptyFrom(lights);
    for (final loc in lights.locations()) {
      final bool on = lightOn(lights.value(loc));
      final int neighborsOn = lights.neighborValues(loc).where(lightOn).length;
      if (on && (neighborsOn == 2 || neighborsOn == 3)) {
        nextLights.setValue(loc, '#');
      } else if (!on && neighborsOn == 3) {
        nextLights.setValue(loc, '#');
      }
    }
    return nextLights;
  }

  static Grid<String> cornersOnAnimate(Grid<String> lights) =>
      turnCornersOn(animate(lights));

  static Grid<String> turnCornersOn(Grid<String> lights) {
    lights.setValue(Vec.zero, '#');
    lights.setValue(Vec(lights.width - 1, 0), '#');
    lights.setValue(Vec(0, lights.height - 1), '#');
    lights.setValue(Vec(lights.width - 1, lights.height - 1), '#');
    return lights;
  }
}
