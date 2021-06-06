package com.mojang.rubydung.level;

import org.lwjgl.opengl.GL11;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;

public class Tesselator {
    private FloatBuffer vertexBuffer;
    private FloatBuffer texCoordBuffer;
    private FloatBuffer colorBuffer;
    private int vertices;
    private float u;
    private float v;
    private float r;
    private float g;
    private float b;
    private boolean hasColor;
    private boolean hasTexture;
    
    public Tesselator() {
        this.vertexBuffer = BufferUtils.createFloatBuffer(300000);
        this.texCoordBuffer = BufferUtils.createFloatBuffer(200000);
        this.colorBuffer = BufferUtils.createFloatBuffer(300000);
        this.vertices = 0;
        this.hasColor = false;
        this.hasTexture = false;
    }
    
    public void flush() {
        this.vertexBuffer.flip();
        this.texCoordBuffer.flip();
        this.colorBuffer.flip();
        GL11.glVertexPointer(3, 0, this.vertexBuffer);
        if (this.hasTexture) {
            GL11.glTexCoordPointer(2, 0, this.texCoordBuffer);
        }
        if (this.hasColor) {
            GL11.glColorPointer(3, 0, this.colorBuffer);
        }
        GL11.glEnableClientState(32884);
        if (this.hasTexture) {
            GL11.glEnableClientState(32888);
        }
        if (this.hasColor) {
            GL11.glEnableClientState(32886);
        }
        GL11.glDrawArrays(7, 0, this.vertices);
        GL11.glDisableClientState(32884);
        if (this.hasTexture) {
            GL11.glDisableClientState(32888);
        }
        if (this.hasColor) {
            GL11.glDisableClientState(32886);
        }
        this.clear();
    }
    
    private void clear() {
        this.vertices = 0;
        this.vertexBuffer.clear();
        this.texCoordBuffer.clear();
        this.colorBuffer.clear();
    }
    
    public void init() {
        this.clear();
        this.hasColor = false;
        this.hasTexture = false;
    }
    
    public void tex(final float u, final float v) {
        this.hasTexture = true;
        this.u = u;
        this.v = v;
    }
    
    public void color(final float r, final float g, final float b) {
        this.hasColor = true;
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    public void vertex(final float x, final float y, final float z) {
        this.vertexBuffer.put(this.vertices * 3 + 0, x).put(this.vertices * 3 + 1, y).put(this.vertices * 3 + 2, z);
        if (this.hasTexture) {
            this.texCoordBuffer.put(this.vertices * 2 + 0, this.u).put(this.vertices * 2 + 1, this.v);
        }
        if (this.hasColor) {
            this.colorBuffer.put(this.vertices * 3 + 0, this.r).put(this.vertices * 3 + 1, this.g).put(this.vertices * 3 + 2, this.b);
        }
        ++this.vertices;
        if (this.vertices == 100000) {
            this.flush();
        }
    }
}
