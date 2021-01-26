package octree;

import geometry.Triangle;
import objFormatParser.ObjParser;

import java.util.ArrayList;

public class CreateOctree {

    public OcTree<Triangle> create(ObjParser parser) {
        ArrayList<Triangle> triangles = parser.getTriangleList();

        int length = parser.getTriangleList().size();
        float[] min = new float[]{(float) parser.getMinX(), (float) parser.getMinY(), (float) parser.getMinZ()};
        float[] max = new float[]{(float) parser.getMaxX(), (float) parser.getMaxY(), (float) parser.getMaxZ()};

        OcTree<Triangle> tree = new OcTreeRoot<>(min, max, 1, 100);


        for (int i = 0; i < length; i++) {
            CollidableAABB bound = new CollidableAABB(triangles.get(i).min(), triangles.get(i).max());
            OcTreeNode<Triangle> triangle = new OcTreeNode<>(bound, triangles.get(i));

            boolean a = tree.add(triangle);
            //System.out.println(a);
            int t = tree.totalObjects();
            //System.out.println(t);

        }
        return tree;
    }
}
