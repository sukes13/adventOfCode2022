package be.fgov.sfpd.kata.aoc22.day22

import be.fgov.sfpd.kata.aoc22.day22.FacingDirection.*


internal val cubeSidesChangesExample = listOf(
        CubeSideChange(1, 2, NORTH, SOUTH, true),
        CubeSideChange(1, 4, SOUTH, SOUTH, false),
        CubeSideChange(1, 6, EAST, WEST, true),
        CubeSideChange(1, 3, WEST, SOUTH, false),

        CubeSideChange(2, 1, NORTH, SOUTH, true),
        CubeSideChange(2, 5, SOUTH, NORTH, true),
        CubeSideChange(2, 6, WEST, NORTH, false),
        CubeSideChange(2, 3, EAST, EAST, false),

        CubeSideChange(3, 1, NORTH, EAST, false),
        CubeSideChange(3, 5, SOUTH, EAST, true),
        CubeSideChange(3, 2, WEST, WEST, false),
        CubeSideChange(3, 4, EAST, EAST, true),

        CubeSideChange(4, 1, NORTH, NORTH, false),
        CubeSideChange(4, 5, SOUTH, SOUTH, false),
        CubeSideChange(4, 3, WEST, WEST, false),
        CubeSideChange(4, 6, EAST, SOUTH, true),

        CubeSideChange(5, 4, NORTH, NORTH, false),
        CubeSideChange(5, 2, SOUTH, NORTH, true),
        CubeSideChange(5, 3, WEST, NORTH, true),
        CubeSideChange(5, 6, EAST, EAST, false),

        CubeSideChange(6, 4, NORTH, WEST, true),
        CubeSideChange(6, 2, SOUTH, EAST, true),
        CubeSideChange(6, 5, WEST, WEST, false),
        CubeSideChange(6, 1, EAST, WEST, false),
)

internal val cubeSidesChangesInput = listOf(
        CubeSideChange(1, 6, NORTH, EAST, false),
        CubeSideChange(1, 3, SOUTH, SOUTH, false),
        CubeSideChange(1, 2, EAST, EAST, false),
        CubeSideChange(1, 4, WEST, EAST, true),

        CubeSideChange(2, 6, NORTH, NORTH, false),
        CubeSideChange(2, 3, SOUTH, WEST, false),
        CubeSideChange(2, 1, WEST, WEST, false),
        CubeSideChange(2, 5, EAST, WEST, true),

        CubeSideChange(3, 1, NORTH, NORTH, false),
        CubeSideChange(3, 5, SOUTH, SOUTH, false),
        CubeSideChange(3, 4, WEST, SOUTH, false),
        CubeSideChange(3, 2, EAST, NORTH, false),

        CubeSideChange(4, 3, NORTH, EAST, false),
        CubeSideChange(4, 6, SOUTH, SOUTH, false),
        CubeSideChange(4, 1, WEST, EAST, true),
        CubeSideChange(4, 5, EAST, EAST, false),

        CubeSideChange(5, 3, NORTH, NORTH, false),
        CubeSideChange(5, 6, SOUTH, WEST, false),
        CubeSideChange(5, 4, WEST, WEST, false),
        CubeSideChange(5, 2, EAST, WEST, true),

        CubeSideChange(6, 4, NORTH, NORTH, false),
        CubeSideChange(6, 2, SOUTH, SOUTH, false),
        CubeSideChange(6, 1, WEST, SOUTH, false),
        CubeSideChange(6, 5, EAST, NORTH, false),
)