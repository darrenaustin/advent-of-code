// https://adventofcode.com/2016/day/17

import 'dart:convert';
import 'dart:math';

import 'package:aoc/aoc.dart';
import 'package:aoc/util/pathfinding.dart';
import 'package:aoc/util/vec.dart';
import 'package:collection/collection.dart';
import 'package:crypto/crypto.dart';

main() => Day17().solve();

class Day17 extends AdventDay {
  Day17() : super(2016, 17);

  static List<Vec> directions = [Vec.up, Vec.down, Vec.left, Vec.right];

  static Map<Vec, String> directionCode = {
    Vec.up: 'U',
    Vec.down: 'D',
    Vec.left: 'L',
    Vec.right: 'R',
  };

  @override
  dynamic part1(String input) {
    final Vec goal = Vec(3, 3);
    return aStarPath<Position>(
      start: Position(input, Vec.zero),
      isGoal: (pos) => pos.pos == goal,
      estimatedDistance: (pos) => goal.manhattanDistanceTo(pos.pos),
      costTo: (pos1, pos2) => 1,
      neighborsOf: nextPositions,
    )!
        .last
        .path
        .substring(input.length);
  }

  @override
  dynamic part2(String input) {
    // Longest path is NP-hard, so just try brute here.
    final Vec goal = Vec(3, 3);
    int maxPath = 0;
    final List<Position> openPositions = [Position(input, Vec.zero)];
    while (openPositions.isNotEmpty) {
      final pos = openPositions.removeLast();
      if (pos.pos == goal) {
        final pathLength = pos.path.length - input.length;
        maxPath = max(maxPath, pathLength);
      } else {
        openPositions.addAll(nextPositions(pos));
      }
    }
    return maxPath;
  }

  String textMD5(String text) => md5.convert(utf8.encode(text)).toString();

  bool validMove(Position pos, Vec dir) {
    final newPos = pos.pos + dir;
    return 0 <= newPos.x && newPos.x < 4 && 0 <= newPos.y && newPos.y < 4;
  }

  Iterable<Vec> openDirections(Position pos) {
    final dirHashes = textMD5(pos.path).substring(0, 4);
    return directions
        .whereIndexed(
            (index, d) => dirHashes[index].codeUnitAt(0) > 'a'.codeUnitAt(0))
        .where((d) => validMove(pos, d));
  }

  Position move(Position pos, Vec dir) =>
      Position(pos.path + directionCode[dir]!, pos.pos + dir);

  Iterable<Position> nextPositions(Position pos) =>
      openDirections(pos).map((d) => move(pos, d));
}

class Position {
  const Position(this.path, this.pos);

  final String path;
  final Vec pos;

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) {
      return true;
    }
    if (other.runtimeType != runtimeType) {
      return false;
    }
    return other is Position && other.path == path && other.pos == pos;
  }

  @override
  int get hashCode => path.hashCode ^ pos.hashCode;

  @override
  String toString() => 'Pos($path, $pos)';
}
