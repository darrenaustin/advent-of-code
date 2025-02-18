// https://adventofcode.com/2023/day/23

import 'package:aoc/aoc.dart';
import 'package:aoc/util/grid.dart';
import 'package:aoc/util/vec.dart';
import 'package:collection/collection.dart';

main() => Day23().solve();

typedef DistanceGraph = Map<Vec, List<(Vec, int)>>;

class Day23 extends AdventDay {
  Day23() : super(2023, 23);

  @override
  dynamic part1(String input) {
    final grid = Grid.parse(input);
    final (start, _) = grid.rowCells(0).firstWhere((c) => c.$2 == '.');
    final (end, _) =
        grid.rowCells(grid.height - 1).firstWhere((c) => c.$2 == '.');
    return maxPathSteepSlopes(grid, start, end);
  }

  @override
  dynamic part2(String input) {
    final grid = Grid.parse(input);
    final (start, _) = grid.rowCells(0).firstWhere((c) => c.$2 == '.');
    final (end, _) =
        grid.rowCells(grid.height - 1).firstWhere((c) => c.$2 == '.');
    return maxPath(grid, start, end);
  }

  int maxPathSteepSlopes(Grid<String> g, Vec start, Vec goal) {
    final maxPathsFrom = <Vec, int>{goal: 0};

    int maxPathFrom(Vec pos, Set<Vec> visited) {
      final maxSteps = maxPathsFrom[pos];
      if (maxSteps != null) {
        return maxSteps;
      }
      final next = adjacent(g, pos, true).where((p) => !visited.contains(p));
      if (next.isEmpty) {
        return 0;
      }
      final maxNextSteps =
          next.map((n) => maxPathFrom(n, {...visited, pos})).max + 1;
      maxPathsFrom[pos] = maxNextSteps;
      return maxNextSteps;
    }

    return maxPathFrom(start, {});
  }

  Iterable<Vec> adjacent(Grid<String> grid, Vec loc,
      [bool steepSlopes = false]) {
    final dirs = steepSlopes
        ? switch (grid.value(loc)) {
            '.' => Vec.orthogonalDirs,
            '>' => [Vec.right],
            '<' => [Vec.left],
            'v' => [Vec.down],
            '^' => [Vec.up],
            _ => throw Exception('Unknown floor type: ${grid.value(loc)}'),
          }
        : Vec.orthogonalDirs;
    return dirs
        .map((d) => loc + d)
        .where((p) => grid.validLocation(p) && grid.value(p) != '#');
  }

  int maxPath(Grid<String> g, Vec start, Vec goal) {
    // Originally I tried to do some kind of memoizing pass through the grid,
    // but the search space was just too big. Finally gave up and looked at
    // how others had solved it. This solution is based on Neil
    // Thistlethwaite's excellent comment:
    //
    //   https://www.reddit.com/r/adventofcode/comments/18oy4pc/comment/kekk8li/?utm_source=share&utm_medium=web2x&context=3
    //
    // Given that all the paths are 1-wide coorridors we can reduce the
    // grid to a graph of intersections, with the distances between connected
    // intersections as the weight. This will allow a brute force search to
    // determine the longest path. It is still pretty slow (~3-4s) on my
    // machine, so there is still some optimazation to be done.
    final graph = distanceGraph(g, start, goal);

    int maxSteps = 0;
    void search(Vec pos, Set<Vec> path, int totalDistance) {
      if (pos == goal && totalDistance > maxSteps) {
        maxSteps = totalDistance;
        return;
      }
      if (graph.containsKey(pos)) {
        for (final (node, distance) in graph[pos]!) {
          if (!path.contains(node)) {
            path.add(node);
            search(node, path, distance + totalDistance);
            path.remove(node);
          }
        }
      }
    }

    search(start, {}, 0);
    return maxSteps;
  }

  Set<Vec> findIntersections(Grid<String> g) => g
      .locationsWhereValue((c) => c != '#')
      .where((l) => g.validLocation(l) && adjacent(g, l).length > 2)
      .toSet();

  DistanceGraph distanceGraph(Grid<String> g, Vec start, Vec goal) {
    final intersections = findIntersections(g)
      ..add(start)
      ..add(goal);
    final graph = <Vec, List<(Vec, int)>>{};
    for (final node in intersections) {
      List<Vec> queue = [node];
      final seen = {node};
      int distance = 0;
      while (queue.isNotEmpty) {
        final nextOpen = <Vec>[];
        distance++;
        for (final pos in queue) {
          for (final neighbor in adjacent(g, pos)) {
            if (seen.add(neighbor)) {
              if (intersections.contains(neighbor)) {
                graph[node] = (graph[node] ?? [])..add((neighbor, distance));
              } else {
                nextOpen.add(neighbor);
              }
            }
          }
        }
        queue = nextOpen;
      }
    }
    return graph;
  }
}
