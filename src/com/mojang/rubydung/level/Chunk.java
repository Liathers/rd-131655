package com.mojang.rubydung.level;

import org.lwjgl.opengl.GL11;
import com.mojang.rubydung.Textures;
import com.mojang.rubydung.phys.AABB;

public class Chunk {
    public AABB aabb;
    public final Level level;
    public final int x0;
    public final int y0;
    public final int z0;
    public final int x1;
    public final int y1;
    public final int z1;
    private boolean dirty;
    private int lists;
    private static int texture;
    private static Tesselator t;
    public static int rebuiltThisFrame;
    public static int updates;
    
    static {
        Chunk.texture = Textures.loadTexture("/terrain.png", 9728);
        Chunk.t = new Tesselator();
        Chunk.rebuiltThisFrame = 0;
        Chunk.updates = 0;
    }
    
    public Chunk(final Level level, final int x0, final int y0, final int z0, final int x1, final int y1, final int z1) {
        this.dirty = true;
        this.lists = -1;
        this.level = level;
        this.x0 = x0;
        this.y0 = y0;
        this.z0 = z0;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.aabb = new AABB((float)x0, (float)y0, (float)z0, (float)x1, (float)y1, (float)z1);
        this.lists = GL11.glGenLists(2);
    }
    
    private void rebuild(final int layer) {
        if (Chunk.rebuiltThisFrame == 2) {
            return;
        }
        this.dirty = false;
        ++Chunk.updates;
        ++Chunk.rebuiltThisFrame;
        GL11.glNewList(this.lists + layer, 4864);
        GL11.glEnable(3553);
        GL11.glBindTexture(3553, Chunk.texture);
        Chunk.t.init();
        for (int x = this.x0; x < this.x1; ++x) {
            for (int y = this.y0; y < this.y1; ++y) {
                for (int z = this.z0; z < this.z1; ++z) {
                    if (this.level.isTile(x, y, z)) {
                    	final int tex = (y != this.level.depth * 2 / 3) ? 1 : 0;
                        if (tex == 0) {
                            Tile.rock.render(Chunk.t, this.level, layer, x, y, z);
                        }
                        else if (y > this.level.depth * 2 / 3 - 5 && y <= this.level.depth * 2 / 3 && this.level.getBrightness(x, y, z) == 1.0) {
                        	Tile.rock.render(Chunk.t, this.level, layer, x, y, z);
                        }
                        else {
                            Tile.grass.render(Chunk.t, this.level, layer, x, y, z);
                        }
                    }
                }
            }
        }
        Chunk.t.flush();
        GL11.glDisable(3553);
        GL11.glEndList();
    }
    
    public void render(final int layer) {
        if (this.dirty) {
            this.rebuild(0);
            this.rebuild(1);
        }
        GL11.glCallList(this.lists + layer);
    }
    
    public void setDirty() {
        this.dirty = true;
    }
}
