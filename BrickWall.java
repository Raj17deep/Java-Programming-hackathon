import java.util.*;

public class BrickWall {

    // Direction vectors for movement (up, down, left, right)
    private static final int[] RefRow = {-1, 1, 0, 0};
    private static final int[] RefCol = {0, 0, -1, 1};

    // Constants for brick types
    private static final char Red = 'R';
    private static final char Green = 'G';
    private static final char Source = 'S';
    private static final char Destination = 'D';

    // Position holder for source and destination
    static class Position {
        int row, col;

        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    // Grid + brick info
    static class GridInfo {
        int size;
        char[][] grid;        // cell type (R/G/S/D)
        int[][] brickId;      // cell -> brick id
        List<Character> brickTypes = new ArrayList<>(); // id -> type
        int sourceId = -1;
        int destinationId = -1;

        GridInfo(int size) {
            this.size = size;
            this.grid = new char[size][size];
            this.brickId = new int[size][size];
            for (int i = 0; i < size; i++) Arrays.fill(this.brickId[i], -1);
        }

        int addBrick(char type) {
            brickTypes.add(type);
            return brickTypes.size() - 1;
        }

        int brickCount() {
            return brickTypes.size();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int gridSize = readGridSize(sc);
        List<String> inputLines = readInputLines(sc, gridSize);

        GridInfo gridInfo = parseGrid(inputLines, gridSize);
        int result = MinBricksToBreak(gridInfo);

        System.out.println(result);
    }

    private static int readGridSize(Scanner sc) {
        return sc.nextInt();
    }

    private static List<String> readInputLines(Scanner sc, int n) {
        sc.nextLine(); // consume newline after number
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            lines.add(sc.nextLine().trim());
        }
        return lines;
    }

    private static GridInfo parseGrid(List<String> inputLines, int gridSize) {
        GridInfo gridInfo = new GridInfo(gridSize);

        for (int row = 0; row < gridSize; row++) {
            parseGridRow(inputLines.get(row), row, gridInfo);
        }

        return gridInfo;
    }

    private static void parseGridRow(String line, int row, GridInfo gridInfo) {
        int col = 0;
        int index = 0;

        while (index < line.length() && col < gridInfo.size) {
            int count = extractNumber(line, index);
            index = skipDigits(line, index);

            if (index < line.length()) {
                char brickType = line.charAt(index);
                index++;

                int id = gridInfo.addBrick(brickType);
                fillGrid(gridInfo, row, col, count, brickType, id);
                if (brickType == Source) gridInfo.sourceId = id;
                if (brickType == Destination) gridInfo.destinationId = id;

                col += count;
            }
        }
    }

    private static int extractNumber(String line, int startIndex) {
        int number = 0;
        int index = startIndex;

        while (index < line.length() && Character.isDigit(line.charAt(index))) {
            number = number * 10 + (line.charAt(index) - '0');
            index++;
        }

        return number;
    }

    private static int skipDigits(String line, int startIndex) {
        int index = startIndex;
        while (index < line.length() && Character.isDigit(line.charAt(index))) {
            index++;
        }
        return index;
    }

    private static void fillGrid(GridInfo gridInfo, int row, int startCol,
                                      int count, char brickType, int brickId) {
        for (int i = 0; i < count; i++) {
            int col = startCol + i;
            if (col < gridInfo.size) {
                gridInfo.grid[row][col] = brickType;
                gridInfo.brickId[row][col] = brickId;
            }
        }
    }

    // Build brick adjacency (ignore red bricks entirely)
    private static List<List<Integer>> BrickGraph(GridInfo gi) {
        int B = gi.brickCount();
        // Adjacency list for bricks
        List<Set<Integer>> tmp = new ArrayList<>(B);
        for (int i = 0; i < B; i++) tmp.add(new HashSet<>());

        for (int r = 0; r < gi.size; r++) {
            for (int c = 0; c < gi.size; c++) {
                int a = gi.brickId[r][c];
                if (a < 0) continue;
                char ta = gi.brickTypes.get(a);
                if (ta == Red) continue;

                for (int d = 0; d < 4; d++) {
                    int nr = r + RefRow[d];
                    int nc = c + RefCol[d];
                    if (nr < 0 || nr >= gi.size || nc < 0 || nc >= gi.size) continue;

                    int b = gi.brickId[nr][nc];
                    if (b < 0 || b == a) continue;
                    char tb = gi.brickTypes.get(b);
                    if (tb == Red) continue; // cannot enter red bricks

                    tmp.get(a).add(b);
                    tmp.get(b).add(a);
                }
            }
        }

        // Convert adjacency list to adjacency matrix for 0-1 BFS
        List<List<Integer>> adj = new ArrayList<>(B);
        for (int i = 0; i < B; i++) adj.add(new ArrayList<>(tmp.get(i)));
        return adj;
    }

    // 0-1 BFS over bricks 
    // cost = 1 when entering a Green brick, else 0
    private static int MinBricksToBreak(GridInfo gi) {
        if (gi.sourceId == -1 || gi.destinationId == -1) return -1;

        List<List<Integer>> adj = BrickGraph(gi);
        int B = gi.brickCount();
        int[] dist = new int[B];
        Arrays.fill(dist, Integer.MAX_VALUE);

        Deque<Integer> dq = new ArrayDeque<>();
        dist[gi.sourceId] = 0;
        dq.offerFirst(gi.sourceId);

        while (!dq.isEmpty()) {
            int u = dq.pollFirst();
            if (u == gi.destinationId) return dist[u];
            if (dist[u] == Integer.MAX_VALUE) continue;

            for (int v : adj.get(u)) {
                int add = gi.brickTypes.get(v) == Green ? 1 : 0; // count 1 for only green
                int nd = dist[u] + add;
                if (nd < dist[v]) {
                    dist[v] = nd;
                    if (add == 0) dq.offerFirst(v); // 0 cost edges to front
                    else dq.offerLast(v); // 1 cost edges to back
                }
            }
        }
        return -1; // unreachable
    }
}