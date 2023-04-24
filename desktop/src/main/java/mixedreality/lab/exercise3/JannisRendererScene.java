/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.exercise3;

import com.jme3.math.*;
import math.MathF;
import mixedreality.base.mesh.ObjReader;
import mixedreality.base.mesh.Triangle;
import mixedreality.base.mesh.TriangleMesh;
import ui.Scene2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Drawing canvas for a 3D renderer.
 */
public class JannisRendererScene extends Scene2D {

    /**
     * This mesh is rendered
     */
    protected TriangleMesh mesh;

    /**
     * Virtual camera.
     */
    protected Camera camera;

    /**
     * This flag enables/disables backface culling
     */
    protected boolean backfaceCulling;

    public JannisRendererScene(int width, int height) {
        super(width, height);
        camera = new Camera(new Vector3f(0, 0, -2), new Vector3f(0, 0, 0),
                new Vector3f(0, 1, 0), 90.0f / 180.0f * FastMath.PI, 1,
                width, height);
        backfaceCulling = true;
        lastMousePosition = null;

        ObjReader reader = new ObjReader();
        //mesh = reader.read("models/cube.obj");
        mesh = reader.read("Models/deer.obj");

        setupListeners();
    }

    @Override
    public void paint(Graphics g) {
        if (mesh == null) return;
        Graphics2D g2 = (Graphics2D) g;
        g.clearRect(0, 0, getWidth(), getHeight());

        Matrix4f m = Matrix4f.IDENTITY;
        Matrix4f v = camera.makeCameraMatrix().invert();
        Matrix4f p = new Matrix4f(
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 1/camera.getZ0(), 0
        );
        float w = camera.getWidth();
        float h = camera.getHeight();
        float fx = w / (2 * MathF.tan(camera.getFovX() / 2));
        float fy = w / (2 * MathF.tan(camera.getFovY() / 2));
        Matrix4f k = new Matrix4f(
                fx, 0, 0, w/2,
                0, fy, 0, h/2,
                0, 0, 0, 0,
                0, 0, 0, 0
        );
        Matrix4f mvp = p.mult(v).mult(m);
        for (int triangleIndex = 0; triangleIndex < mesh.getNumberOfTriangles(); triangleIndex++) {
            Triangle triangle = mesh.getTriangle(triangleIndex);
            Vector3f p0V3 = mesh.getVertex(triangle.getVertexIndex(0)).getPosition();
            Vector3f p1V3 = mesh.getVertex(triangle.getVertexIndex(1)).getPosition();
            Vector3f p2V3 = mesh.getVertex(triangle.getVertexIndex(2)).getPosition();
            Vector4f p0V4 = new Vector4f(p0V3.x, p0V3.y, p0V3.z, 1);
            Vector4f p1V4 = new Vector4f(p1V3.x, p1V3.y, p1V3.z, 1);
            Vector4f p2V4 = new Vector4f(p2V3.x, p2V3.y, p2V3.z, 1);

            Vector4f p0MVP = mvp.mult(p0V4);
            Vector4f p1MVP = mvp.mult(p1V4);
            Vector4f p2MVP = mvp.mult(p2V4);
            p0MVP = p0MVP.divide(p0MVP.w);
            p1MVP = p1MVP.divide(p1MVP.w);
            p2MVP = p2MVP.divide(p2MVP.w);

            float triangleRotation = (p1MVP.x - p0MVP.x) * (p2MVP.y - p1MVP.y) - (p2MVP.x - p1MVP.x) * (p1MVP.y - p0MVP.y);

            if (triangleRotation < 0 || !backfaceCulling) {
                Vector4f p0MVPK = k.mult(p0MVP);
                Vector4f p1MVPK = k.mult(p1MVP);
                Vector4f p2MVPK = k.mult(p2MVP);

                drawLine(g2, new Vector2f(p0MVPK.x, p0MVPK.y), new Vector2f(p1MVPK.x, p1MVPK.y), Color.BLACK);
                drawLine(g2, new Vector2f(p1MVPK.x, p1MVPK.y), new Vector2f(p2MVPK.x, p2MVPK.y), Color.BLACK);
                drawLine(g2, new Vector2f(p2MVPK.x, p2MVPK.y), new Vector2f(p0MVPK.x, p0MVPK.y), Color.BLACK);
            }
        }
    }

    @Override
    public String getTitle() {
        return "Jannis 2D Renderer";
    }

    /**
     * Draw a line using the given coordinates (no further transformations).
     */
    public void drawLine(Graphics2D gc, Vector2f a, Vector2f b, Color color) {
        gc.setColor(color);
        gc.drawLine((int) a.x, (int) a.y, (int) b.x, (int) b.y);
    }

    @Override
    public JPanel getUserInterface() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JCheckBox cbBackfaceCulling = new JCheckBox("backfaceCulling");
        cbBackfaceCulling.setSelected(backfaceCulling);
        cbBackfaceCulling.addActionListener(e -> {
            backfaceCulling = cbBackfaceCulling.isSelected();
            repaint();
        });
        panel.add(cbBackfaceCulling);

        return panel;
    }

    /**
     * Setup listeners - used for user interaction.
     */
    public void setupListeners() {
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Vector2f mPos = new Vector2f(e.getX(), e.getY());
                if (lastMousePosition != null) {
                    float dx = mPos.x - lastMousePosition.x;
                    float dy = mPos.y - lastMousePosition.y;
                    camera.rotateHorizontal(dx);
                    camera.rotateVertical(dy);
                    repaint();
                }
                lastMousePosition = mPos;
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                lastMousePosition = null;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                lastMousePosition = null;
            }
        });
    }
}