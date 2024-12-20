// https://adventofcode.com/2019/day/13

import 'package:aoc/aoc.dart';
import 'package:aoc/util/vec.dart';
import 'package:collection/collection.dart';

import 'intcode.dart';

main() => Day13().solve();

const int empty = 0;
const int wall = 1;
const int block = 2;
const int paddle = 3;
const int ball = 4;

class Day13 extends AdventDay {
  Day13() : super(2019, 13);

  @override
  dynamic part1(String input) {
    final machine = Intcode.from(program: input);
    while (!machine.execute()) {}
    return machine.output.slices(3).where((p) => p.last == block).length;
  }

  @override
  dynamic part2(String input) {
    final machine = Intcode.from(program: input);
    int score = 0;
    Vec ballPos = Vec.zero;
    Vec paddlePos = Vec.zero;

    machine[0] = 2;
    while (!machine.execute() || machine.output.isNotEmpty) {
      machine.output.slices(3).map((p) => p.toList()).forEach((command) {
        if (command[0] == -1 && command[1] == 0) {
          score = command[2];
        } else {
          switch (command[2]) {
            case ball:
              ballPos = Vec(command[0], command[1]);
              break;
            case paddle:
              paddlePos = Vec(command[0], command[1]);
              break;
          }
        }
      });
      machine.output.clear();
      // Simplistic AI: just move the paddle in the direction of the ball.
      machine.input.add(ballPos.xInt.compareTo(paddlePos.xInt));
    }
    return score;
  }
}
