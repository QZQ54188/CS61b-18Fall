package byog.lab5;

import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class HexWorld {

    // size表示每个六边形的边长为7
    public static int size = 7;
    // Width和Height分别表示窗口的横向方块数量和纵向方块数量
    private static final int WIDTH = 11 * size - 6;
    private static final int HEIGHT = 10 * size;

    private static final long SEED = 54188;
    private static final Random RANDOM = new Random(SEED);

    public static void drawSmallHexagon(int Row, int Col, TETile[][] hexaTile, int size) {
        int len = size;
        TETile tileStyle = randomTile();
        for (int i = 0; i < size; i++) {
            drawOneLine(Row, Col, len, hexaTile, tileStyle);
            Row++;
            Col--;
            len += 2;
        }
        for (int i = 0; i < size; i++) {
            Col++;
            len -= 2;
            drawOneLine(Row, Col, len, hexaTile, tileStyle);
            Row++;
        }
    }

    public static void drawOneLine(
            int Row,
            int Col,
            int num,
            TETile[][] hexaTiles,
            TETile tileType) {
        for (int i = 0; i < num; i++) {
            hexaTiles[i + Col][Row] = tileType;
        }
    }

    private static void pileTheHexagons(int Row, int Col, TETile[][] hexaTile, int size, int num) {
        for (int i = 0; i < num; i++) {
            drawSmallHexagon(Row, Col, hexaTile, size);
            Row += 2 * size;
        }
    }

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(9);
        switch (tileNum) {
            case 0:
                return Tileset.WALL;
            case 1:
                return Tileset.FLOWER;
            case 2:
                return Tileset.MOUNTAIN;
            case 3:
                return Tileset.UNLOCKED_DOOR;
            case 4:
                return Tileset.LOCKED_DOOR;
            case 5:
                return Tileset.SAND;
            case 6:
                return Tileset.TREE;
            case 7:
                return Tileset.GRASS;
            case 8:
                return Tileset.WATER;
            default:
                return Tileset.WALL;
        }
    }

    public static void addHexagon(TETile[][] hexaTile, int size) {
        int Row = 2 * size + 1;
        int leftCol = size + 1;
        int rightCol = WIDTH + 1 - 2 * (size - 1);
        int disCol = 2 * (size - 1) + 1;
        for (int num = 3; num < 5; num++) {
            pileTheHexagons(Row, leftCol, hexaTile, size, num);
            pileTheHexagons(Row, rightCol, hexaTile, size, num);
            Row -= size;
            leftCol += disCol;
            rightCol -= disCol;
        }
        pileTheHexagons(Row, leftCol, hexaTile, size, 5);
    }

    public static void main(String[] args) {

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH + 4, HEIGHT + 4);

        TETile[][] hexagonTiles = new TETile[WIDTH + 4][HEIGHT + 4];
        for (int x = 0; x < WIDTH + 4; x += 1) {
            for (int y = 0; y < HEIGHT + 4; y += 1) {
                hexagonTiles[x][y] = Tileset.FLOOR;
            }
        }

        addHexagon(hexagonTiles, size);

        ter.renderFrame(hexagonTiles);
    }
}
