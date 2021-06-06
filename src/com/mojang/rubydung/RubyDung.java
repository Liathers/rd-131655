package com.mojang.rubydung;

import org.lwjgl.util.glu.GLU;
import com.mojang.rubydung.level.Chunk;
import java.awt.Component;
import javax.swing.JOptionPane;
import java.io.IOException;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.BufferUtils;
import com.mojang.rubydung.level.LevelRenderer;
import com.mojang.rubydung.level.Level;
import java.nio.FloatBuffer;

public class RubyDung implements Runnable {
    private static boolean isIsometric = true;
    private int width;
    private int height;
    private FloatBuffer fogColor;
    private Timer timer;
    private Level level;
    private LevelRenderer levelRenderer;
    private Player player;
    
    public RubyDung() {
        this.fogColor = BufferUtils.createFloatBuffer(4);
        this.timer = new Timer(60.0f);
    }
    
    public void init() throws LWJGLException, IOException {
        final int col = 920330;
        final float fr = 0.5f;
        final float fg = 0.8f;
        final float fb = 1.0f;
        this.fogColor.put(new float[] { (col >> 16 & 0xFF) / 255.0f, (col >> 8 & 0xFF) / 255.0f, (col & 0xFF) / 255.0f, 1.0f });
        this.fogColor.flip();
        Display.setDisplayMode(new DisplayMode(1024, 768));
        //Display.setDisplayMode(new DisplayMode(854, 480));
        Display.create();
        Keyboard.create();
        Mouse.create();
        
        this.width = Display.getDisplayMode().getWidth();
        this.height = Display.getDisplayMode().getHeight();
        
        GL11.glEnable(3553);
        GL11.glShadeModel(7425);
        GL11.glClearColor(fr, fg, fb, 0.0f);
        GL11.glClearDepth(1.0);
        GL11.glEnable(2929);
        GL11.glDepthFunc(515);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(5888);
        this.level = new Level(256, 256, 64);
        this.levelRenderer = new LevelRenderer(this.level);
        this.player = new Player(this.level);
        Mouse.setGrabbed(true);
    }
    
    public void destroy() {
        Mouse.destroy();
        Keyboard.destroy();
        Display.destroy();
    }
    
    public void run() {
        try {
            this.init();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog((Component)null, e.toString(), "Failed to start RubyDung", 0);
            System.exit(0);
        }
        long lastTime = System.currentTimeMillis();
        int frames = 0;
        try {
            while (!Keyboard.isKeyDown(1)) {
                if (Display.isCloseRequested()) {
                    break;
                }
                this.timer.advanceTime();
                for (int i = 0; i < this.timer.ticks; ++i) {
                    this.tick();
                }
                this.render(this.timer.a);
                ++frames;
                while (System.currentTimeMillis() >= lastTime + 1000L) {
                    System.out.println(String.valueOf(frames) + " fps, " + Chunk.updates);
                    Chunk.updates = 0;
                    lastTime += 1000L;
                    frames = 0;
                }
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            return;
        }
        finally {
            this.destroy();
        }
        this.destroy();
    }
    
    public void tick() {
        this.player.tick();
    }
    
    private void moveCameraToPlayer(final float a) {
        GL11.glTranslatef(0.0f, 0.0f, -0.3f);
        GL11.glRotatef(this.player.xRot, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(this.player.yRot, 0.0f, 1.0f, 0.0f);
        final float x = this.player.xo + (this.player.x - this.player.xo) * a;
        final float y = this.player.yo + (this.player.y - this.player.yo) * a;
        final float z = this.player.zo + (this.player.z - this.player.zo) * a;
        GL11.glTranslatef(-x, -y, -z);
    }
    
    private void setupCamera(final float a) {
    	Mouse.setGrabbed(true);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GLU.gluPerspective(70.0f, this.width / (float)this.height, 0.05f, 1000.0f);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        this.moveCameraToPlayer(a);
    }
    
    private void isometricCamera(final float a) {
    	Mouse.setGrabbed(false);
    	
    	GL11.glViewport(0, 0, this.width, this.height);
    	GL11.glClear(16640);
        GL11.glEnable(2884);
    	
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, (double)this.width, 0.0, (double)this.height, 10.0, 10000.0);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        
        GL11.glRotatef(25, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(135, 0.0f, 1.0f, 0.0f);
        GL11.glTranslatef(-(int)(this.height / 2 - this.height / 4.75), (this.height / 8), (this.width / 2));
        GL11.glScalef(2, 2, 2);
    }
    
    public void render(final float a) {
        final float xo = (float)Mouse.getDX();
        final float yo = (float)Mouse.getDY();
        this.player.turn(xo, yo);
        GL11.glClear(16640);
        if (RubyDung.isIsometric) this.isometricCamera(a);
        else this.setupCamera(a);
        GL11.glEnable(2884);
        GL11.glEnable(2912);
        GL11.glFogi(2917, 2048);
        GL11.glFogf(2914, 0.2f);
        GL11.glFog(2918, this.fogColor);
        GL11.glDisable(2912);
        this.levelRenderer.render(this.player, 0);
        GL11.glEnable(2912);
        this.levelRenderer.render(this.player, 1);
        GL11.glDisable(3553);
        GL11.glDisable(2912);
        Display.update();
    }
    
    public static void checkError() {
        final int e = GL11.glGetError();
        if (e != 0) {
            throw new IllegalStateException(GLU.gluErrorString(e));
        }
    }
    
    public static void main(final String[] args) throws LWJGLException {
        new Thread((Runnable)new RubyDung()).start();
    }
}
