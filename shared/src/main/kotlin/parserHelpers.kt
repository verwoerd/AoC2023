fun Sequence<String>.extractLongSequence() = map { it.splitSpaceSeperatedLongs() }

fun String.splitSpaceSeperatedLongs() = split(" ").map { it.toLong() }
