package util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.mgsx.gltf.scene3d.lights.SpotLightEx;
import app.Salient;

public class DebugDrawer {
    public ShapeRenderer renderer = Salient.INSTANCE.getShapeRenderer();
    public BitmapFont font = Salient.INSTANCE.getFont();
    public SpriteBatch batch = Salient.INSTANCE.getSpriteBatch();
    private static Camera camera;
    private static Color color = Color.BLACK;

    public static void setColor(Color color) {
        DebugDrawer.color = color;
    }

    public  void drawWireDisc(Vector3 position, Vector3 axis, float radius) {
        begin();
        drawWireDiscInternal(position, axis, radius);
        end();
    }

    public  void drawWireSphere(Vector3 position, float radius) {
        begin();
        drawWireSphereInternal(position, radius);
        end();
    }

    public  void drawWireCube(Vector3 position, Vector3 size) {
        begin();
        drawWireCubeInternal(position, size);
        end();
    }

    public  void drawWireFrustum(Frustum frustum) {
        begin();
        drawWireFrustumInternal(frustum);
        end();
    }

    public  void drawWireCone(Vector3 position, Vector3 axis, float radius, float height) {
        begin();
        drawWireConeInternal(position, axis, radius, height);
        end();
    }

    public  void drawSpotLight(SpotLightEx spotLightEx){
        begin();
        drawSpotLightInternal(spotLightEx);
        end();
    }

    private  void begin() {
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glLineWidth(1f);

        camera = Salient.INSTANCE.getCamera();

        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(color);
    }

    private  void end() {
        renderer.end();

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
    }

    private  void drawWireDiscInternal(Vector3 position, Vector3 axis, float radius) {
        Vector3 o = new Vector3(position);
        Vector3 d = new Vector3(axis).nor();
        Vector3 r = new Vector3(0, 0, 1);

        if (axis.epsilonEquals(Vector3.Z)) {
            r.set(1, 0, 0);
        }

        r.crs(d).nor();
        Vector3 u = new Vector3(d).crs(r).nor();

        float tau = (float)Math.PI * 2;
        int segments = 48;
        float step = tau / segments;

        Vector3 current = new Vector3();
        Vector3 next = new Vector3();

        Vector3 a = new Vector3();
        Vector3 b = new Vector3();

        for(int i = 0; i < segments; i++) {
            float sin = (float)Math.sin(i * step) * radius;
            float cos = (float)Math.cos(i * step) * radius;

            a.set(u).scl(cos);
            b.set(r).scl(sin);
            current.set(a).add(b).add(o);

            float nextsin = (float)Math.sin((i + 1) * step) * radius;
            float nextcos = (float)Math.cos((i + 1) * step) * radius;

            a.set(u).scl(nextcos);
            b.set(r).scl(nextsin);
            next.set(a).add(b).add(o);

            renderer.line(current, next);
        }
    }

    private  void drawWireSphereInternal(Vector3 position, float radius) {
        // Draw axially aligned discs
        //drawWireDisc(origin, Vector3.X, radius);
        //drawWireDisc(origin, Vector3.Y, radius);
        //drawWireDisc(origin, Vector3.Z, radius);

        // Draw disc that encompasses sphere according to camera perspective.
        Vector3 normal = new Vector3(position).sub(camera.position);
        float sqrMag = normal.len2();
        float n0 = radius * radius;
        float n1 = n0 * n0 / sqrMag;
        float n2 = (float) Math.sqrt(n0 - n1);

        Vector3 a = new Vector3(normal).scl(n0 / sqrMag);
        Vector3 b = new Vector3(position).sub(a);

        drawWireDiscInternal(b, normal, n2);
    }

    private  void drawWireCubeInternal(Vector3 position, Vector3 size) {
        float px = position.x;
        float py = position.y;
        float pz = position.z;
        float sx = size.x;
        float sy = size.y;
        float sz = size.z;

        Vector3 p0 = new Vector3(px - sx, py - sy, pz - sz);
        Vector3 p1 = new Vector3(px + sx, py - sy, pz - sz);
        Vector3 p2 = new Vector3(px + sx, py + sy, pz - sz);
        Vector3 p3 = new Vector3(px - sx, py + sy, pz - sz);
        Vector3 p4 = new Vector3(px - sx, py - sy, pz + sz);
        Vector3 p5 = new Vector3(px + sx, py - sy, pz + sz);
        Vector3 p6 = new Vector3(px + sx, py + sy, pz + sz);
        Vector3 p7 = new Vector3(px - sx, py + sy, pz + sz);

        // Bottom
        renderer.line(p0, p1);
        renderer.line(p1, p2);
        renderer.line(p2, p3);
        renderer.line(p3, p0);

        // Top
        renderer.line(p4, p5);
        renderer.line(p5, p6);
        renderer.line(p6, p7);
        renderer.line(p7, p4);

        // Sides
        renderer.line(p0, p4);
        renderer.line(p1, p5);
        renderer.line(p2, p6);
        renderer.line(p3, p7);
    }

    private  void drawWireFrustumInternal(Frustum frustum) {
        for(int i = 0; i < 4; i++) {
            Vector3 startPoint = frustum.planePoints[i];
            Vector3 endPoint = i != 3 ? frustum.planePoints[i + 1] : frustum.planePoints[0];

            renderer.line(startPoint.x, startPoint.y, startPoint.z, endPoint.x, endPoint.y, endPoint.z);
        }

        for(int i = 0; i < 4; i++) {
            Vector3 startPoint = frustum.planePoints[i];
            Vector3 endPoint = frustum.planePoints[i + 4];

            renderer.line(startPoint.x, startPoint.y, startPoint.z, endPoint.x, endPoint.y, endPoint.z);
        }

        for(int i = 4; i < 8; i++) {
            Vector3 startPoint = frustum.planePoints[i];
            Vector3 endPoint = i != 7 ? frustum.planePoints[i + 1] : frustum.planePoints[4];

            renderer.line(startPoint.x, startPoint.y, startPoint.z, endPoint.x, endPoint.y, endPoint.z);
        }
    }

    private  void drawWireConeInternal(Vector3 position, Vector3 axis, float radius, float height) {
        drawWireDiscInternal(position, axis, radius);

        Vector3 o = new Vector3(axis).nor().scl(height).add(position);
        Vector3 r = new Vector3(Vector3.Z);
        r.crs(axis).nor().scl(radius);
        Vector3 s = new Vector3(r);
        s.add(position);

        renderer.line(o.x, o.y, o.z, s.x, s.y, s.z);

        s.set(r).scl(-1);
        s.add(position);

        renderer.line(o.x, o.y, o.z, s.x, s.y, s.z);

        r.nor().crs(axis).scl(radius);
        s.set(r).add(position);

        renderer.line(o.x, o.y, o.z, s.x, s.y, s.z);

        s.set(r).scl(-1);
        s.add(position);

        renderer.line(o.x, o.y, o.z, s.x, s.y, s.z);
    }

    private  void drawSpotLightInternal(SpotLightEx spotLightEx){
        Vector3 position = spotLightEx.position;
        Vector3 direction = spotLightEx.direction;
        float radius = spotLightEx.intensity;
        float height = spotLightEx.range;
        float angle = spotLightEx.cutoffAngle;
        float cosAngle = (float) Math.cos(angle);
        float sinAngle = (float) Math.sin(angle);
        float coneRadius = radius * sinAngle;
        float coneHeight = radius * cosAngle;

        //line from position to direction scaled by height
        Vector3 discCenter = new Vector3(direction).nor().scl(height).add(position);
    }

    public void keyValue(Vector2 pos, String key, String value,BitmapFont font){
        batch.begin();
        font.draw(batch, key + ":"+value, pos.x, pos.y);
        batch.end();
    }
}
