package Year2023.Day8;

import java.util.function.Function;
import java.util.stream.IntStream;

public record Map(int width, int height, int[][] map) {
    static Map of(String input) {
        int[][] map = input
            .lines()
            .map(line -> line
                .chars()
                .map(c -> c - '0')
            .toArray())
            .toArray(int[][]::new);
        return new Map(map[0].length, map.length, map);
    }

    int countVisibleTrees() {
        int count = 0;
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                if (x == 0 || y == 0 || x == height - 1 || y == width - 1) {
                    count++;
                } else if (isTreeVisible(x, y)) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean isTreeVisible(int x, int y) {
        return isVisibleFromLeft(x, y) || isVisibleFromRight(x, y) || isVisibleFromTop(x, y) || isVisibleFromBottom(x, y);
    }
    
    private boolean isVisibleFromLeft(int x, int y) {
        int treeHeight = map[x][y];
        for (int k = y - 1; k >= 0; k--) {
            if (map[x][k] >= treeHeight) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isVisibleFromRight(int x, int y) {
        int treeHeight = map[x][y];
        for (int k = y + 1; k < width; k++) {
            if (map[x][k] >= treeHeight) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isVisibleFromTop(int x, int y) {
        int treeHeight = map[x][y];
        for (int k = x - 1; k >= 0; k--) {
            if (map[k][y] >= treeHeight) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isVisibleFromBottom(int x, int y) {
        int treeHeight = map[x][y];
        for (int k = x + 1; k < height; k++) {
            if (map[k][y] >= treeHeight) {
                return false;
            }
        }
        return true;
    }

    // PART 2

    public int maxScenicScore() {
        // Normal for-loop solution

        // int max = 0;
        // for (int x = 0; x < height; x++) {
        //     for (int y = 0; y < width; y++) {
        //         max = Math.max(max, scenicScore(x, y));
        //     }
        // }
        // return max;

        // Fancy stream solution
        return IntStream
            .range(0, width)
            .flatMap(y -> IntStream.range(0, height).map(x -> scenicScore(new Point(x, y))))
            .max()
            .getAsInt();
    }

    public int scenicScore(Point start) {
        int up = countTreesOnPath(start, p -> p.move(0, -1));
        int down = countTreesOnPath(start, p -> p.move(0, 1));
        int left = countTreesOnPath(start, p -> p.move(-1, 0));
        int right = countTreesOnPath(start, p -> p.move(1, 0));

        return up * down * left * right;
    }

    private boolean insideMap(Point point) {
        return point.x >= 0 && point.x < height && point.y >= 0 && point.y < width;
    }

    private int countTreesOnPath(Point start, Function<Point, Point> move) {
        int count = 0;
        Point current = move.apply(start);
        while (insideMap(current)) {
            count++;
            if (map[current.x][current.y] >= map[start.x][start.y]) {
                return count;
            }
            current = move.apply(current);
        }
        return count;
    }

    private record Point(int x, int y) {
        Point move (int dx, int dy) {
            return new Point(x + dx, y + dy);
        }
    }
}
