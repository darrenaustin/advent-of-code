// ignore_for_file: public_member_api_docs, sort_constructors_first
import 'dart:convert';
import 'dart:io';

import 'package:advent_of_code_dart/days.dart';
import 'package:aoc/aoc.dart';
import 'package:collection/collection.dart';
import 'package:path/path.dart' as path;

final String packageDir = path.join(Directory.current.path, 'packages');

Future<void> main(List<String> arguments) async {
  final Map<int, Year> years = {};
  final Directory cwd = Directory.current;

  for (final AdventDay day in allDays) {
    cdPackagePath(day.year);
    final year = years[day.year] ??
        Year(
            year: day.year,
            path: path.join('packages', 'aoc${day.year}', 'lib', 'src'),
            days: <Day>[]);
    years[day.year] = year;
    final stars = await day.stars();
    year.days.add(Day(
        day: day.day,
        path: 'day${day.day.toString().padLeft(2, '0')}.dart',
        stars: stars));
    print('${day.year}.${day.day}: $stars');
  }

  final stats = Stats(
      language: 'Dart', path: 'dart', years: List<Year>.from(years.values));

  Directory.current = cwd;
  final statsFilePath = path.join(Directory.current.path, 'stats.json');
  final statsFile = File(statsFilePath);
  statsFile.createSync(recursive: true);
  final out = statsFile.openWrite();
  out.writeln(JsonEncoder.withIndent('  ').convert(stats));
  await out.close();
}

void cdPackagePath(int year) =>
    Directory.current = path.join(packageDir, 'aoc$year');

class Day {
  int day;
  String path;
  int stars;

  Day({
    required this.day,
    required this.path,
    required this.stars,
  });

  Day copyWith({
    int? day,
    String? path,
    int? stars,
  }) {
    return Day(
      day: day ?? this.day,
      path: path ?? this.path,
      stars: stars ?? this.stars,
    );
  }

  Map<String, dynamic> toMap() {
    return <String, dynamic>{
      'day': day,
      'path': path,
      'stars': stars,
    };
  }

  factory Day.fromMap(Map<String, dynamic> map) {
    return Day(
      day: map['day'] as int,
      path: map['path'] as String,
      stars: map['stars'] as int,
    );
  }

  Map<String, dynamic> toJson() => toMap();

  factory Day.fromJson(String source) =>
      Day.fromMap(json.decode(source) as Map<String, dynamic>);

  @override
  String toString() => 'Day(day: $day, path: $path, stars: $stars)';

  @override
  bool operator ==(covariant Day other) {
    if (identical(this, other)) return true;

    return other.day == day && other.path == path && other.stars == stars;
  }

  @override
  int get hashCode => day.hashCode ^ path.hashCode ^ stars.hashCode;
}

class Year {
  int year;
  String path;
  List<Day> days;

  Year({
    required this.year,
    required this.path,
    required this.days,
  });

  Year copyWith({
    int? year,
    String? path,
    List<Day>? days,
  }) {
    return Year(
      year: year ?? this.year,
      path: path ?? this.path,
      days: days ?? this.days,
    );
  }

  Map<String, dynamic> toMap() {
    return <String, dynamic>{
      'year': year,
      'path': path,
      'days': days.map((x) => x.toMap()).toList(),
    };
  }

  factory Year.fromMap(Map<String, dynamic> map) {
    return Year(
      year: map['year'] as int,
      path: map['path'] as String,
      days: List<Day>.from(
        (map['days'] as List<int>).map<Day>(
          (x) => Day.fromMap(x as Map<String, dynamic>),
        ),
      ),
    );
  }

  Map<String, dynamic> toJson() => toMap();

  factory Year.fromJson(String source) =>
      Year.fromMap(json.decode(source) as Map<String, dynamic>);

  @override
  String toString() => 'Year(year: $year, path: $path, days: $days)';

  @override
  bool operator ==(covariant Year other) {
    if (identical(this, other)) return true;
    final listEquals = const DeepCollectionEquality().equals;

    return other.year == year &&
        other.path == path &&
        listEquals(other.days, days);
  }

  @override
  int get hashCode => year.hashCode ^ path.hashCode ^ days.hashCode;
}

class Stats {
  String language;
  String path;
  List<Year> years;
  Stats({
    required this.language,
    required this.path,
    required this.years,
  });

  Stats copyWith({
    String? language,
    String? path,
    List<Year>? years,
  }) {
    return Stats(
      language: language ?? this.language,
      path: path ?? this.path,
      years: years ?? this.years,
    );
  }

  Map<String, dynamic> toMap() {
    return <String, dynamic>{
      'language': language,
      'path': path,
      'years': years.map((x) => x.toMap()).toList(),
    };
  }

  factory Stats.fromMap(Map<String, dynamic> map) {
    return Stats(
      language: map['language'] as String,
      path: map['path'] as String,
      years: List<Year>.from(
        (map['years'] as List<int>).map<Year>(
          (x) => Year.fromMap(x as Map<String, dynamic>),
        ),
      ),
    );
  }

  Map<String, dynamic> toJson() => toMap();

  factory Stats.fromJson(String source) =>
      Stats.fromMap(json.decode(source) as Map<String, dynamic>);

  @override
  String toString() => 'Stats(language: $language, path: $path, years: $years)';

  @override
  bool operator ==(covariant Stats other) {
    if (identical(this, other)) return true;
    final listEquals = const DeepCollectionEquality().equals;

    return other.language == language &&
        other.path == path &&
        listEquals(other.years, years);
  }

  @override
  int get hashCode => language.hashCode ^ path.hashCode ^ years.hashCode;
}
