/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.exercise3;

import com.jme3.math.*;
import mixedreality.base.math.Utils;
import mixedreality.base.mesh.ObjReader;
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
public class MyRendererScene extends Scene2D {

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

    public MyRendererScene(int width, int height) {
        super(width, height);
        camera = new Camera(new Vector3f(0, 0, -2), new Vector3f(0, 0, 0),
                new Vector3f(0, 1, 0), 90.0f / 180.0f * FastMath.PI, 1,
                width, height);
        backfaceCulling = true;
        lastMousePosition = null;

        modelRotation = new Vector3f(0f, 0f, 0f);
        modelTranslation = new Vector3f(0f, 0f, 0f);
        modelScale = new Vector3f(1f, 1f, 1f);

        ObjReader reader = new ObjReader();
        mesh = reader.read("models/cube.obj");
        //mesh = reader.read("Models/deer.obj");

        setupListeners();
    }

    Vector3f modelRotation;
    Vector3f modelScale;
    Vector3f modelTranslation;

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g.clearRect(0, 0, getWidth(), getHeight());

        if (mesh != null) {
            // Create model matrix from all 3 transformations (translation, scale and rotation)
            var m = Utils.modelMatrix(modelScale, modelTranslation, modelRotation);
            // Get view matrix from the camera
            var v = camera.makeCameraMatrix().invert();
            // Calculate projection matrix
            var p = Utils.projectionMatrix(camera, 0.1f, 100f);
            // Calculate screen mapping matrix
            var k = Utils.screenMapping(camera);
            // Order of multiplication very important
            var mvp = p.mult(v).mult(m);
            var color = new Color(255, 0, 0);

            for (int i = 0; i < mesh.getNumberOfTriangles(); i++) {
                var currTri = mesh.getTriangle(i);
//                color = new Color(currTri.getColor().getRed(), currTri.getColor().getGreen(), currTri.getColor().getBlue());
                // get vertex of all 3 points from their specified index
                var a = Utils.vec4(mesh.getVertex(currTri.getA()).getPosition());
                var b = Utils.vec4(mesh.getVertex(currTri.getB()).getPosition());
                var c = Utils.vec4(mesh.getVertex(currTri.getC()).getPosition());

                // multiply with mvp matrix to convert point from model to world to camera to homogenous space
                var trans_a = mvp.mult(a);
                var trans_b = mvp.mult(b);
                var trans_c = mvp.mult(c);

                // normalize by diving by w
                trans_a = trans_a.divide(trans_a.w);
                trans_b = trans_b.divide(trans_b.w);
                trans_c = trans_c.divide(trans_c.w);

                // calculate potentially rotated normal
                var triNormal = Utils.calcTriNormal(trans_a, trans_b, trans_c);
                var dot_product = triNormal.dot(new Vector3f(0f, 0f, 1f));

                // see if triangle normal is facing in direction of camera view
                if (dot_product < 0 || !backfaceCulling) {
                    // convert to screen space
                    var screen_a = k.mult(trans_a);
                    var screen_b = k.mult(trans_b);
                    var screen_c = k.mult(trans_c);

                    // draw final triangle
                    drawLine(g2, new Vector2f(screen_a.x, screen_a.y), new Vector2f(screen_b.x, screen_b.y), color);
                    drawLine(g2, new Vector2f(screen_b.x, screen_b.y), new Vector2f(screen_c.x, screen_c.y), color);
                    drawLine(g2, new Vector2f(screen_a.x, screen_a.y), new Vector2f(screen_c.x, screen_c.y), color);
                }
            }
        }
    }

    @Override
    public String getTitle() {
        return "2D Renderer";
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


        panel.add(new JLabel("Rotate x"));
        JSlider cbRotateX = new JSlider(0, (int)(2 * Math.PI * 100), 0);
        cbRotateX.addChangeListener(e -> {
                    modelRotation.setX(cbRotateX.getValue() / 100f);
                    repaint();
                }
        );
        panel.add(cbRotateX);

        panel.add(new JLabel("Rotate y"));
        JSlider cbRotateY = new JSlider(0, (int)(2 * Math.PI * 100), 0);
        cbRotateY.addChangeListener(e -> {
                    modelRotation.setY(cbRotateY.getValue() / 100f);
                    repaint();
                }
        );
        panel.add(cbRotateY);

        panel.add(new JLabel("Rotate z"));
        JSlider cbRotateZ = new JSlider(0, (int)(2 * Math.PI * 100), 0);
        cbRotateZ.addChangeListener(e -> {
                    modelRotation.setZ(cbRotateZ.getValue() / 100f);
                    repaint();
                }
        );
        panel.add(cbRotateZ);

        panel.add(new JLabel("scale x"));
        JSlider cbScaleX = new JSlider(1, 20, 1);
        cbScaleX.addChangeListener(e -> {
                    modelScale.setX(1f + cbScaleX.getValue() / 10f);
                    repaint();
                }
        );
        panel.add(cbScaleX);

        panel.add(new JLabel("scale y"));
        JSlider cbScaleY = new JSlider(1, 20, 1);
        cbScaleY.addChangeListener(e -> {
                    modelScale.setY(1f + cbScaleY.getValue() / 10f);
                    repaint();
                }
        );
        panel.add(cbScaleY);

        panel.add(new JLabel("scale z"));
        JSlider cbScaleZ = new JSlider(1, 20, 1);
        cbScaleZ.addChangeListener(e -> {
                    modelScale.setZ(1f + cbScaleZ.getValue() / 10f);
                    repaint();
                }
        );
        panel.add(cbScaleZ);


        panel.add(new JLabel("translate x"));
        JSlider cbTranslateX = new JSlider(-40, 20, 0);
        cbTranslateX.addChangeListener(e -> {
                    modelTranslation.setX(cbTranslateX.getValue() / 10f);
                    repaint();
                }
        );
        panel.add(cbTranslateX);

        panel.add(new JLabel("translate y"));
        JSlider cbTranslateY = new JSlider(-40, 20, 0);
        cbTranslateY.addChangeListener(e -> {
                    modelTranslation.setY(cbTranslateY.getValue() / 10f);
                    repaint();
                }
        );
        panel.add(cbTranslateY);

        panel.add(new JLabel("translate z"));
        JSlider cbTranslateZ = new JSlider(-40, 20, 0);
        cbTranslateZ.addChangeListener(e -> {
                    modelTranslation.setZ(cbTranslateZ.getValue() / 10f);
                    repaint();
                }
        );
        panel.add(cbTranslateZ);

        JButton cbReset = new JButton("Reset");
        cbReset.addActionListener(e -> {
            modelRotation = new Vector3f(0f, 0f, 0f);
            modelTranslation = new Vector3f(0f, 0f, 0f);
            modelScale = new Vector3f(1f, 1f, 1f);
            repaint();
        });
        panel.add(cbReset);

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
