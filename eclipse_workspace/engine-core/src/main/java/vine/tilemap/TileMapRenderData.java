package vine.tilemap;

import java.util.Arrays;

import vine.game.Game;
import vine.graphics.VertexBufferObject;

public class TileMapRenderData {

    private final VertexBufferObject vertexBuffer;
    private final TileMap tileMap;

    private int screenxTiles = 42;
    private int screenyTiles = 27;

    private final float[] vertices;
    private final float[] uvs;

    private int cachedCameraX;
    private int cachedCameraY;

    private final GridCalculator verticeGridCalc = new GridCalculator();
    private final float[] squadVertices = new float[12];

    public TileMapRenderData(final TileMap map) {
        tileMap = map;
        screenxTiles = Game.getGame().getScreen().getWidth() / 32 + 2;
        screenyTiles = Game.getGame().getScreen().getHeight() / 32 + 2;
        //
        final int[] indices = new int[6 * screenxTiles * screenyTiles];
        this.vertices = new float[12 * screenxTiles * screenyTiles];
        this.uvs = new float[8 * screenxTiles * screenyTiles];
        for (int i = screenxTiles - 1; i >= 0; i--) {
            for (int j = screenyTiles - 1; j >= 0; j--) {
                final int index = i + j * screenxTiles;
                System.arraycopy(
                        new int[] { index * 4, index * 4 + 1, index * 4 + 2, index * 4 + 2, index * 4 + 3, index * 4 },
                        0, indices, index * 6, 6);
            }
        }
        vertexBuffer = new VertexBufferObject(vertices, indices, uvs, Game.getGame().getGraphics());
    }

    /**
     * @return
     */
    public final VertexBufferObject getRenderData(final int positionX, final int positionY) {
        final int cameraX = positionX / 32 - screenxTiles / 2 + 1;
        final int cameraY = positionY / 32 - screenyTiles / 2 + 1;
        if (cameraY != cachedCameraY || cameraX != cachedCameraX) {
            verticeGridCalc.calculateVertexGrid(cameraX, cameraY, screenxTiles, screenyTiles, vertices);
            vertexBuffer.changeVertices(vertices);
        }
        cachedCameraX = cameraX;
        cachedCameraY = cameraY;

        Arrays.fill(uvs, 0);
        for (int i = screenxTiles - 1; i >= 0; i--) {
            for (int j = screenyTiles - 1; j >= 0; j--) {
                final int index = i + j * screenxTiles;
                final int tile = (i + cameraX) + (j + cameraY) * tileMap.xTiles;
                if (tile > 0 && tile < tileMap.yTiles * tileMap.xTiles) {
                    System.arraycopy(tileMap.tiles[tile].getUVCoordinates(), 0, uvs, index * 8, 8);
                }
            }
        }
        vertexBuffer.changeTexture(uvs);
        return vertexBuffer;
    }
}