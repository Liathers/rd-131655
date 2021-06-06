package com.mojang.rubydung.level;

import com.mojang.rubydung.phys.AABB;
import java.util.ArrayList;
import java.util.Random;

public class Level {
    public final int width;
    public final int height;
    public final int depth;
    private Random random;
    private byte[] blocks;
    private int[] lightDepths;
    
    public Level(final int w, final int h, final int d) {
        this.width = w;
        this.height = h;
        this.depth = d;
        this.blocks = new byte[w * h * d];
        this.lightDepths = new int[w * h];
        this.random = new Random();
        for (int x = 0; x < w; ++x) {
            for (int y = 0; y < d; ++y) {
                for (int z = 0; z < h; ++z) {
                    final int i = (y * this.height + z) * this.width + x;
                    this.blocks[i] = (byte)((y <= d * 2 / 3) ? 1 : 0);
                }
            }
        }
        this.generateCaves(w, h, d);
        this.calcLightDepths(0, 0, w, h);
    }
    
    private void generateCaves(int w, int h, int d) {
        for (int count = w * h * d / 256 / 2, j = 0; j < count; ++j) {
            float float12 = this.random.nextFloat() * w;
            float float13 = this.random.nextFloat() * d;
            float float14 = this.random.nextFloat() * h;
            int integer15 = (int)(this.random.nextFloat() + this.random.nextFloat() * 150.0f);
            float float16 = (float)(this.random.nextFloat() * 3.141592653589793 * 2.0);
            float float17 = 0.0f;
            float float18 = (float)(this.random.nextFloat() * 3.141592653589793 * 2.0);
            float float19 = 0.0f;
            for (int l = 0; l < integer15; ++l) {
                float12 += (float)(Math.sin(float16) * Math.cos(float18));
                float14 += (float)(Math.cos(float16) * Math.cos(float18));
                float13 += (float)Math.sin(float18);
                float16 += float17 * 0.2f;
                float17 *= 0.9f;
                float17 += this.random.nextFloat() - this.random.nextFloat();
                float18 += float19 * 0.5f;
                float18 *= 0.5f;
                float19 *= 0.9f;
                float19 += this.random.nextFloat() - this.random.nextFloat();
                float float21 = (float)(Math.sin(l * 3.141592653589793 / integer15) * 2.5);
                for (int xx = (int)(float12 - float21); xx <= (int)(float12 + float21); ++xx) {
                    for (int yy = (int)(float13 - float21); yy <= (int)(float13 + float21); ++yy) {
                        for (int zz = (int)(float14 - float21); zz <= (int)(float14 + float21); ++zz) {
                            float float25 = xx - float12;
                            float float26 = yy - float13;
                            float float27 = zz - float14;
                            float float28 = float25 * float25 + float26 * float26 * 2.0f + float27 * float27;
                            if (float28 < float21 * float21 && xx >= 1 && yy >= 1 && zz >= 1 && xx < this.width - 1 && yy < this.depth - 1 && zz < this.height - 1) {
                                int integer29 = (yy * this.height + zz) * this.width + xx;
                                blocks[integer29] = 0;
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void calcLightDepths(final int x0, final int y0, final int x1, final int y1) {
        for (int x2 = x0; x2 < x0 + x1; ++x2) {
            for (int z = y0; z < y0 + y1; ++z) {
                final int oldDepth = this.lightDepths[x2 + z * this.width];
                int y2;
                for (y2 = this.depth - 1; y2 > 0 && !this.isLightBlocker(x2, y2, z); --y2) {}
                if (oldDepth != (this.lightDepths[x2 + z * this.width] = y2)) {
                }
            }
        }
    }
    
    public boolean isTile(final int x, final int y, final int z) {
        return x >= 0 && y >= 0 && z >= 0 && x < this.width && y < this.depth && z < this.height && this.blocks[(y * this.height + z) * this.width + x] == 1;
    }
    
    public boolean isSolidTile(final int x, final int y, final int z) {
        return this.isTile(x, y, z);
    }
    
    public boolean isLightBlocker(final int x, final int y, final int z) {
        return this.isSolidTile(x, y, z);
    }
    
    public ArrayList<AABB> getCubes(final AABB aABB) {
        @SuppressWarnings({ "unchecked", "rawtypes" })
		final ArrayList<AABB> aABBs = (ArrayList<AABB>)new ArrayList();
        int x0 = (int)aABB.x0;
        int x2 = (int)(aABB.x1 + 1.0f);
        int y0 = (int)aABB.y0;
        int y2 = (int)(aABB.y1 + 1.0f);
        int z0 = (int)aABB.z0;
        int z2 = (int)(aABB.z1 + 1.0f);
        if (x0 < 0) {
            x0 = 0;
        }
        if (y0 < 0) {
            y0 = 0;
        }
        if (z0 < 0) {
            z0 = 0;
        }
        if (x2 > this.width) {
            x2 = this.width;
        }
        if (y2 > this.depth) {
            y2 = this.depth;
        }
        if (z2 > this.height) {
            z2 = this.height;
        }
        for (int x3 = x0; x3 < x2; ++x3) {
            for (int y3 = y0; y3 < y2; ++y3) {
                for (int z3 = z0; z3 < z2; ++z3) {
                    if (this.isSolidTile(x3, y3, z3)) {
                        aABBs.add(new AABB((float)x3, (float)y3, (float)z3, (float)(x3 + 1), (float)(y3 + 1), (float)(z3 + 1)));
                    }
                }
            }
        }
        return aABBs;
    }
    
    public float getBrightness(final int x, final int y, final int z) {
        final float dark = 0.8f;
        final float light = 1.0f;
        if (x < 0 || y < 0 || z < 0 || x >= this.width || y >= this.depth || z >= this.height) {
            return light;
        }
        if (y < this.lightDepths[x + z * this.width]) {
            return dark;
        }
        return light;
    }
}
