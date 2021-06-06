package com.mojang.rubydung.level;

import com.mojang.rubydung.Player;

public class LevelRenderer {
    private Chunk[] chunks;
    private int xChunks;
    private int yChunks;
    private int zChunks;
    Tesselator t;
    
    public LevelRenderer(final Level level) {
        this.t = new Tesselator();
        this.xChunks = level.width / 16;
        this.yChunks = level.depth / 16;
        this.zChunks = level.height / 16;
        this.chunks = new Chunk[this.xChunks * this.yChunks * this.zChunks];
        for (int x = 0; x < this.xChunks; ++x) {
            for (int y = 0; y < this.yChunks; ++y) {
                for (int z = 0; z < this.zChunks; ++z) {
                    final int x2 = x * 16;
                    final int y2 = y * 16;
                    final int z2 = z * 16;
                    int x3 = (x + 1) * 16;
                    int y3 = (y + 1) * 16;
                    int z3 = (z + 1) * 16;
                    if (x3 > level.width) {
                        x3 = level.width;
                    }
                    if (y3 > level.depth) {
                        y3 = level.depth;
                    }
                    if (z3 > level.height) {
                        z3 = level.height;
                    }
                    this.chunks[(x + y * this.xChunks) * this.zChunks + z] = new Chunk(level, x2, y2, z2, x3, y3, z3);
                }
            }
        }
    }
    
    public void render(final Player player, final int layer) {
        Chunk.rebuiltThisFrame = 0;
        final Frustum frustum = Frustum.getFrustum();
        for (int i = 0; i < this.chunks.length; ++i) {
            if (frustum.cubeInFrustum(this.chunks[i].aabb)) {
                this.chunks[i].render(layer);
            }
        }
    }
}
