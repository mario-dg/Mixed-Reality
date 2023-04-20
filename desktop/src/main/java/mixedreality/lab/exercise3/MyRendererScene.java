/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.exercise3;

import com.jme3.math.*;
import math.MathF;
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

    /**
     * This flag enables/disables backface culling
     */
    protected boolean rotateModel;

    public MyRendererScene(int width, int height) {
        super(width, height);
        camera = new Camera(new Vector3f(0, 0, -2), new Vector3f(0, 0, 0),
                new Vector3f(0, 1, 0), 90.0f / 180.0f * FastMath.PI, 1,
                width, height);
        backfaceCulling = true;
        lastMousePosition = null;
        rotateModel = false;

        ObjReader reader = new ObjReader();
//        mesh = reader.read("models/cube.obj");
        mesh = reader.read("Models/deer.obj");

        setupListeners();
    }

    float angleX = 0;
    float angleY = 0;
    float angleZ = 0;
    float scaleX = 1f;
    float scaleY = 1f;
    float scaleZ = 1f;
    float translateX = 0f;
    float translateY = 0f;
    float translateZ = 0f;

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g.clearRect(0, 0, getWidth(), getHeight());

        if (mesh != null) {
            if (rotateModel) {
                angleX += MathF.random(0.0001f, 0.01f);
                angleY += MathF.random(0.0001f, 0.01f);
                angleZ += MathF.random(0.0001f, 0.01f);
            }
            // Create model matrix from all 3 transformations (translation, scale and rotation)
            var m = Utils.modelMatrix(new Vector3f(scaleX, scaleY, scaleZ), new Vector3f(translateX, translateY, translateZ), new Vector3f(angleX, angleY, angleZ));
            // Get view matrix from the camera
            var v = camera.makeCameraMatrix();
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
                if (dot_product > 0 || !backfaceCulling) {
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

        JCheckBox cbRotateModelOnMouseMove = new JCheckBox("rotate model on mouse move");
        cbRotateModelOnMouseMove.setSelected(rotateModel);
        cbRotateModelOnMouseMove.addActionListener(e -> {
            rotateModel = cbRotateModelOnMouseMove.isSelected();
            repaint();
        });
        panel.add(cbRotateModelOnMouseMove);

        panel.add(new JLabel("scale x"));
        JSlider cbScaleX = new JSlider(1, 20, 1);
        cbScaleX.addChangeListener(e -> {
                    scaleX = 1f + cbScaleX.getValue() / 10f;
                    repaint();
                }
        );
        panel.add(cbScaleX);

        panel.add(new JLabel("scale y"));
        JSlider cbScaleY = new JSlider(1, 20, 1);
        cbScaleY.addChangeListener(e -> {
                    scaleY = 1f + cbScaleY.getValue() / 10f;
                    repaint();
                }
        );
        panel.add(cbScaleY);

        panel.add(new JLabel("scale z"));
        JSlider cbScaleZ = new JSlider(1, 20, 1);
        cbScaleZ.addChangeListener(e -> {
                    scaleZ = 1f + cbScaleZ.getValue() / 10f;
                    repaint();
                }
        );
        panel.add(cbScaleZ);


        panel.add(new JLabel("translate x"));
        JSlider cbtranslateX = new JSlider(-40, 20, 0);
        cbtranslateX.addChangeListener(e -> {
                    translateX = cbtranslateX.getValue() / 10f;
                    repaint();
                }
        );
        panel.add(cbtranslateX);

        panel.add(new JLabel("translate y"));
        JSlider cbtranslateY = new JSlider(-40, 20, 0);
        cbtranslateY.addChangeListener(e -> {
                    translateY = cbtranslateY.getValue() / 10f;
                    repaint();
                }
        );
        panel.add(cbtranslateY);

        panel.add(new JLabel("translate z"));
        JSlider cbtranslateZ = new JSlider(-40, 20, 0);
        cbtranslateZ.addChangeListener(e -> {
                    translateZ = cbtranslateZ.getValue() / 10f;
                    repaint();
                }
        );
        panel.add(cbtranslateZ);
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
                if (rotateModel)
                    repaint();
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
