package vine.tilemap;

public class GridCalculator {

    float[] squadVertices = new float[12];

    public void calculateVertexGrid(int startX, int startY, int width, int height, float[] vertices) {
        for (int i = width - 1; i >= 0; i--) {
            for (int j = height - 1; j >= 0; j--) {
                final int index = i + j * width;
                squadVertices[0] = (i + startX) * 32.f;
                squadVertices[1] = (j + startY) * 32.f;
                squadVertices[3] = (i + startX) * 32.f;
                squadVertices[4] = (j + startY) * 32.f + 32;
                squadVertices[6] = (i + startX) * 32.f + 32;
                squadVertices[7] = (j + startY) * 32.f + 32;
                squadVertices[9] = (i + startX) * 32.f + 32;
                squadVertices[10] = (j + startY) * 32.f;
                System.arraycopy(squadVertices, 0, vertices, index * 12, 12);
            }
        }
    }
}