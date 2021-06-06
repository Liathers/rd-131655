package com.mojang.rubydung;

import java.util.List;
import org.lwjgl.input.Keyboard;
import com.mojang.rubydung.phys.AABB;
import com.mojang.rubydung.level.Level;

public class Player {
    private Level level;
    public float xo;
    public float yo;
    public float zo;
    public float x;
    public float y;
    public float z;
    public float xd;
    public float yd;
    public float zd;
    public float yRot;
    public float xRot;
    public AABB bb;
    public boolean onGround;
    
    public Player(final Level level) {
        this.onGround = false;
        this.level = level;
        this.resetPos();
    }
    
    private void resetPos() {
        final float x = (float)(this.level.width / 2);
        final float y = (float)(this.level.depth * 2 / 3 + 12);
        final float z = (float)(this.level.height / 2);
        this.setPos(x, y, z);
    }
    
    private void setPos(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        final float w = 0.3f;
        final float h = 0.9f;
        this.bb = new AABB(x - w, y - h, z - w, x + w, y + h, z + w);
    }
    
    public void turn(final float xo, final float yo) {
        this.yRot += (float)(xo * 0.15);
        this.xRot -= (float)(yo * 0.15);
        if (this.xRot < -90.0f) {
            this.xRot = -90.0f;
        }
        if (this.xRot > 90.0f) {
            this.xRot = 90.0f;
        }
    }
    
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        float xa = 0.0f;
        float ya = 0.0f;
        if (Keyboard.isKeyDown(19)) {
            this.resetPos();
        }
        if (Keyboard.isKeyDown(200) || Keyboard.isKeyDown(17)) {
            --ya;
        }
        if (Keyboard.isKeyDown(208) || Keyboard.isKeyDown(31)) {
            ++ya;
        }
        if (Keyboard.isKeyDown(203) || Keyboard.isKeyDown(30)) {
            --xa;
        }
        if (Keyboard.isKeyDown(205) || Keyboard.isKeyDown(32)) {
            ++xa;
        }
        if ((Keyboard.isKeyDown(57) || Keyboard.isKeyDown(219)) && this.onGround) {
            this.yd = 0.12f;
        }
        this.moveRelative(xa, ya, this.onGround ? 0.02f : 0.005f);
        this.yd -= (float)0.005;
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.91f;
        this.yd *= 0.98f;
        this.zd *= 0.91f;
        if (this.onGround) {
            this.xd *= 0.8f;
            this.zd *= 0.8f;
        }
    }
    
    public void move(float xa, float ya, float za) {
        final float xaOrg = xa;
        final float yaOrg = ya;
        final float zaOrg = za;
        final List<AABB> aABBs = (List<AABB>)this.level.getCubes(this.bb.expand(xa, ya, za));
        for (int i = 0; i < aABBs.size(); ++i) {
            ya = ((AABB)aABBs.get(i)).clipYCollide(this.bb, ya);
        }
        this.bb.move(0.0f, ya, 0.0f);
        for (int i = 0; i < aABBs.size(); ++i) {
            xa = ((AABB)aABBs.get(i)).clipXCollide(this.bb, xa);
        }
        this.bb.move(xa, 0.0f, 0.0f);
        for (int i = 0; i < aABBs.size(); ++i) {
            za = ((AABB)aABBs.get(i)).clipZCollide(this.bb, za);
        }
        this.bb.move(0.0f, 0.0f, za);
        this.onGround = (yaOrg != ya && yaOrg < 0.0f);
        if (xaOrg != xa) {
            this.xd = 0.0f;
        }
        if (yaOrg != ya) {
            this.yd = 0.0f;
        }
        if (zaOrg != za) {
            this.zd = 0.0f;
        }
        this.x = (this.bb.x0 + this.bb.x1) / 2.0f;
        this.y = this.bb.y0 + 1.62f;
        this.z = (this.bb.z0 + this.bb.z1) / 2.0f;
    }
    
    public void moveRelative(float xa, float za, final float speed) {
        float dist = xa * xa + za * za;
        if (dist < 0.01f) {
            return;
        }
        dist = speed / (float)Math.sqrt((double)dist);
        xa *= dist;
        za *= dist;
        final float sin = (float)Math.sin(this.yRot * 3.141592653589793 / 180.0);
        final float cos = (float)Math.cos(this.yRot * 3.141592653589793 / 180.0);
        this.xd += xa * cos - za * sin;
        this.zd += za * cos + xa * sin;
    }
}
