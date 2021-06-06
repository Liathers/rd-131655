package com.mojang.rubydung.level;

public class Tile {
    public static Tile rock;
    public static Tile grass;
    private int tex;
    
    static {
        Tile.rock = new Tile(0);
        Tile.grass = new Tile(1);
    }
    
    private Tile(final int tex) {
        this.tex = 0;
        this.tex = tex;
    }
    
    public void render(final Tesselator t, final Level level, final int layer, final int x, final int y, final int z) {
        final float u0 = this.tex / 16.0f;
        final float u2 = u0 + 0.0624375f;
        final float v0 = 0.0f;
        final float v2 = v0 + 0.0624375f;
        final float c1 = 1.0f;
        final float c2 = 0.8f;
        final float c3 = 0.6f;
        final float x2 = x + 0.0f;
        final float x3 = x + 1.0f;
        final float y2 = y + 0.0f;
        final float y3 = y + 1.0f;
        final float z2 = z + 0.0f;
        final float z3 = z + 1.0f;
        if (!level.isSolidTile(x, y - 1, z)) {
            final float br = level.getBrightness(x, y - 1, z) * c1;
            if (br == c1 ^ layer == 1) {
                t.color(br, br, br);
                t.tex(u0, v2);
                t.vertex(x2, y2, z3);
                t.tex(u0, v0);
                t.vertex(x2, y2, z2);
                t.tex(u2, v0);
                t.vertex(x3, y2, z2);
                t.tex(u2, v2);
                t.vertex(x3, y2, z3);
            }
        }
        if (!level.isSolidTile(x, y + 1, z)) {
            final float br = level.getBrightness(x, y, z) * c1;
            if (br == c1 ^ layer == 1) {
                t.color(br, br, br);
                t.tex(u2, v2);
                t.vertex(x3, y3, z3);
                t.tex(u2, v0);
                t.vertex(x3, y3, z2);
                t.tex(u0, v0);
                t.vertex(x2, y3, z2);
                t.tex(u0, v2);
                t.vertex(x2, y3, z3);
            }
        }
        if (!level.isSolidTile(x, y, z - 1)) {
            final float br = level.getBrightness(x, y, z - 1) * c2;
            if (br == c2 ^ layer == 1) {
                t.color(br, br, br);
                t.tex(u2, v0);
                t.vertex(x2, y3, z2);
                t.tex(u0, v0);
                t.vertex(x3, y3, z2);
                t.tex(u0, v2);
                t.vertex(x3, y2, z2);
                t.tex(u2, v2);
                t.vertex(x2, y2, z2);
            }
        }
        if (!level.isSolidTile(x, y, z + 1)) {
            final float br = level.getBrightness(x, y, z + 1) * c2;
            if (br == c2 ^ layer == 1) {
                t.color(br, br, br);
                t.tex(u0, v0);
                t.vertex(x2, y3, z3);
                t.tex(u0, v2);
                t.vertex(x2, y2, z3);
                t.tex(u2, v2);
                t.vertex(x3, y2, z3);
                t.tex(u2, v0);
                t.vertex(x3, y3, z3);
            }
        }
        if (!level.isSolidTile(x - 1, y, z)) {
            final float br = level.getBrightness(x - 1, y, z) * c3;
            if (br == c3 ^ layer == 1) {
                t.color(br, br, br);
                t.tex(u2, v0);
                t.vertex(x2, y3, z3);
                t.tex(u0, v0);
                t.vertex(x2, y3, z2);
                t.tex(u0, v2);
                t.vertex(x2, y2, z2);
                t.tex(u2, v2);
                t.vertex(x2, y2, z3);
            }
        }
        if (!level.isSolidTile(x + 1, y, z)) {
            final float br = level.getBrightness(x + 1, y, z) * c3;
            if (br == c3 ^ layer == 1) {
                t.color(br, br, br);
                t.tex(u0, v2);
                t.vertex(x3, y2, z3);
                t.tex(u2, v2);
                t.vertex(x3, y2, z2);
                t.tex(u2, v0);
                t.vertex(x3, y3, z2);
                t.tex(u0, v0);
                t.vertex(x3, y3, z3);
            }
        }
    }
    
    public void renderFace(final Tesselator t, final int x, final int y, final int z, final int face) {
        final float x2 = x + 0.0f;
        final float x3 = x + 1.0f;
        final float y2 = y + 0.0f;
        final float y3 = y + 1.0f;
        final float z2 = z + 0.0f;
        final float z3 = z + 1.0f;
        if (face == 0) {
            t.vertex(x2, y2, z3);
            t.vertex(x2, y2, z2);
            t.vertex(x3, y2, z2);
            t.vertex(x3, y2, z3);
        }
        if (face == 1) {
            t.vertex(x3, y3, z3);
            t.vertex(x3, y3, z2);
            t.vertex(x2, y3, z2);
            t.vertex(x2, y3, z3);
        }
        if (face == 2) {
            t.vertex(x2, y3, z2);
            t.vertex(x3, y3, z2);
            t.vertex(x3, y2, z2);
            t.vertex(x2, y2, z2);
        }
        if (face == 3) {
            t.vertex(x2, y3, z3);
            t.vertex(x2, y2, z3);
            t.vertex(x3, y2, z3);
            t.vertex(x3, y3, z3);
        }
        if (face == 4) {
            t.vertex(x2, y3, z3);
            t.vertex(x2, y3, z2);
            t.vertex(x2, y2, z2);
            t.vertex(x2, y2, z3);
        }
        if (face == 5) {
            t.vertex(x3, y2, z3);
            t.vertex(x3, y2, z2);
            t.vertex(x3, y3, z2);
            t.vertex(x3, y3, z3);
        }
    }
}
