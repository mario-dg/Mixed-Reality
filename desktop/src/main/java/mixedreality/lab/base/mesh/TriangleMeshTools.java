/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 *
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.base.mesh;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.texture.Texture;
import com.jme3.util.TangentBinormalGenerator;
import mixedreality.lab.base.ui.Logger;

import java.util.List;

/**
 * Tools for triangle meshes.
 */
public class TriangleMeshTools {
  /**
   * Adds all content of the otherMesh to the meshBase.
   */
  public static void unite(TriangleMesh baseMesh, TriangleMesh otherMesh) {
    int vertexOffset = baseMesh.getNumberOfVertices();
    int texCoordOffset = baseMesh.getNumberOfTextureCoordinates();

    // Vertices
    for (int i = 0; i < otherMesh.getNumberOfVertices(); i++) {
      baseMesh.addVertex(new Vertex(otherMesh.getVertex(i)));
    }
    for (int i = 0; i < otherMesh.getNumberOfTextureCoordinates(); i++) {
      baseMesh.addTextureCoordinate(
              new Vector2f(otherMesh.getTextureCoordinate(i)));
    }
    for (int i = 0; i < otherMesh.getNumberOfTriangles(); i++) {
      Triangle t = new Triangle(otherMesh.getTriangle(i));
      t.addVertexIndexOffset(vertexOffset);
      t.addTexCoordOffset(texCoordOffset);
      baseMesh.addTriangle(t);
    }

    vertexOffset += otherMesh.getNumberOfVertices();
    texCoordOffset += otherMesh.getNumberOfTextureCoordinates();
  }

  /**
   * Create a unified mesh from all meshes in the list. Not tested for meshes
   * using textures.
   */
  public static TriangleMesh unite(List<TriangleMesh> meshes) {

    if (meshes.size() == 0) {
      return null;
    }

    TriangleMesh mesh = meshes.get(0);

    int vertexOffset = mesh.getNumberOfVertices();
    int texCoordOffset = mesh.getNumberOfTextureCoordinates();
    for (int meshIndex = 1; meshIndex < meshes.size(); meshIndex++) {
      TriangleMesh m = meshes.get(meshIndex);
      // Vertices
      for (int i = 0; i < m.getNumberOfVertices(); i++) {
        mesh.addVertex(new Vertex(m.getVertex(i)));
      }
      for (int i = 0; i < m.getNumberOfTextureCoordinates(); i++) {
        mesh.addTextureCoordinate(new Vector2f(m.getTextureCoordinate(i)));
      }
      for (int i = 0; i < m.getNumberOfTriangles(); i++) {
        Triangle t = new Triangle(m.getTriangle(i));
        t.addVertexIndexOffset(vertexOffset);
        t.addTexCoordOffset(texCoordOffset);
        mesh.addTriangle(t);
      }

      vertexOffset += m.getNumberOfVertices();
      texCoordOffset += m.getNumberOfTextureCoordinates();
    }

    return mesh;
  }


  /**
   * Move all vertices with the offset vector (x, y, z)
   */
  public static void translate(TriangleMesh mesh, float x, float y, float z) {
    for (int i = 0; i < mesh.getNumberOfVertices(); i++) {
      Vertex v = mesh.getVertex(i);
      v.getPosition().set(v.getPosition().x + x, v.getPosition().y + y,
              v.getPosition().z + z);
    }
  }

  /**
   * Move all vertices with the offset vector t
   */
  public static void translate(TriangleMesh mesh, Vector3f t) {
    translate(mesh, t.x, t.y, t.z);
  }

  /**
   * Merge all vertices which are closer to one another than numerical accuracy.
   */
  public static void mergeVertices(TriangleMesh mesh) {
    int numRemoved = 0;
    for (int i = 0; i < mesh.getNumberOfVertices(); i++) {
      for (int j = i + 1; j < mesh.getNumberOfVertices(); j++) {
        if (mesh.getVertex(i).getPosition().subtract(
                mesh.getVertex(j).getPosition()).length() < 1e-5) {
          for (int t = 0; t < mesh.getNumberOfTriangles(); t++) {
            Triangle triangle = mesh.getTriangle(t);
            triangle.replaceVertexIndex(i, j);
          }
          // Deprecate vertex j
          mesh.getVertex(j).getPosition().set(new Vector3f(Float.NaN, Float.NaN,
                  Float.NaN));
          numRemoved++;
        }
      }
    }
    // Remove all degenerated triangles.
    for (int i = 0; i < mesh.getNumberOfTriangles(); i++) {
      if (mesh.getTriangle(i).isDegenerated()) {
        mesh.removeTriangle(i);
        i--;
      } else {
        Triangle t = mesh.getTriangle(i);
        Vector3f a = mesh.getVertex(t.getVertexIndex(0)).position;
        Vector3f b = mesh.getVertex(t.getVertexIndex(1)).position;
        Vector3f c = mesh.getVertex(t.getVertexIndex(2)).position;
        if (Triangle.getArea(a, b, c) < 1e-5) {
          mesh.removeTriangle(i);
          i--;
        }
      }
    }
    Logger.getInstance().debug("Removed " + numRemoved
            + " vertices with same position.");
  }

  /**
   * Create a geometry object for a triangle mesh.
   */
  public static Geometry createJMonkeyMesh(AssetManager assetManager, TriangleMesh triangleMesh) {
    return createJMonkeyMesh(assetManager, triangleMesh, null, null);
  }

  /**
   * Create a geometry object for a triangle mesh.
   */
  public static Geometry createJMonkeyMesh(AssetManager assetManager, TriangleMesh triangleMesh,
                                           String textureFilename, String normalMapFilename) {
    Mesh mesh = new Mesh();
    mesh.setMode(Mesh.Mode.Triangles);

    int size = triangleMesh.getNumberOfTriangles() * 3;

    float[] positionBuffer = new float[size * 3];
    float[] colorBuffer = new float[size * 4];
    float[] normalBuffer = new float[size * 3];
    float[] texCoordsBuffer = new float[size * 2];
    int[] indexBuffer = new int[size];

    // Fill vertex and color buffer
    for (int triangleIndex = 0; triangleIndex < triangleMesh.getNumberOfTriangles(); triangleIndex++) {

      Triangle t = triangleMesh.getTriangle(triangleIndex);

      for (int vertexInTriangleIndex = 0; vertexInTriangleIndex < 3; vertexInTriangleIndex++) {
        int vertexIndex = t.getVertexIndex(vertexInTriangleIndex);
        int texCoordIndex = t.getTextureCoordinate(vertexInTriangleIndex);
        Vertex vertex = triangleMesh.getVertex(vertexIndex);
        Vector3f pos = vertex.getPosition();
        Vector3f normal = t.getNormal();
        ColorRGBA color = t.getColor();
        Vector2f texCoord = texCoordIndex >= 0 ? triangleMesh.getTextureCoordinate(texCoordIndex) :
                new Vector2f(0, 0);

        // Position
        positionBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3] = (float) pos.get(0);
        positionBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3 + 1] = (float) pos.get(1);
        positionBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3 + 2] = (float) pos.get(2);

        // Normal
        normalBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3] = (float) normal.get(0);
        normalBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3 + 1] = (float) normal.get(1);
        normalBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3 + 2] = (float) normal.get(2);

        // Color
        colorBuffer[triangleIndex * 12 + vertexInTriangleIndex * 4] = color.r;
        colorBuffer[triangleIndex * 12 + vertexInTriangleIndex * 4 + 1] = color.g;
        colorBuffer[triangleIndex * 12 + vertexInTriangleIndex * 4 + 2] = color.b;
        colorBuffer[triangleIndex * 12 + vertexInTriangleIndex * 4 + 3] = color.a;

        // Texture coords
        texCoordsBuffer[triangleIndex * 6 + vertexInTriangleIndex * 2] = texCoord.x;
        texCoordsBuffer[triangleIndex * 6 + vertexInTriangleIndex * 2 + 1] = texCoord.y;
      }
    }
    for (int i = 0; i < size; i++) {
      indexBuffer[i] = i;
    }

    mesh.setBuffer(VertexBuffer.Type.Position, 3, positionBuffer);
    mesh.setBuffer(VertexBuffer.Type.Index, 1, indexBuffer);
    mesh.setBuffer(VertexBuffer.Type.Color, 4, colorBuffer);
    mesh.setBuffer(VertexBuffer.Type.Normal, 3, normalBuffer);
    mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, texCoordsBuffer);
    mesh.updateBound();

    Geometry geom = new Geometry("triangle mesh", mesh);

    Material mat = new Material(assetManager,
            "Common/MatDefs/Light/Lighting.j3md");
    mat.setColor("Diffuse", ColorRGBA.White);
    mat.setBoolean("UseVertexColor", true);

    // Texture
    if (textureFilename != null) {
      Texture texture = assetManager.loadTexture(textureFilename);
      mat.setTexture("DiffuseMap", texture);
      texture.setMagFilter(Texture.MagFilter.Bilinear);
      texture.setMinFilter(Texture.MinFilter.BilinearNearestMipMap);
    }

    // Normal map
    if (normalMapFilename != null) {
      TangentBinormalGenerator.generate(geom);
      Texture normalMap = assetManager.loadTexture(normalMapFilename);
      normalMap.setMagFilter(Texture.MagFilter.Bilinear);
      normalMap.setMinFilter(Texture.MinFilter.BilinearNearestMipMap);
      mat.setTexture("NormalMap", normalMap);
    }

    geom.setMaterial(mat);

    return geom;
  }
}
