package objConverter;

import bmpCreator.BmpCreator;
import geometry.Triangle;
import geometry.Vector3;
import intersectionsProcessing.IntersectionChecker;
import objFormatParser.ObjParser;
import octree.CollidableAABB;
import octree.CreateOctree;
import octree.OcTree;
import octree.OcTreeNode;

import java.util.List;

public class Converter {

    public static void convert() {

        ObjParser parser = new ObjParser();
        parser.parseObj();

        CreateOctree creator = new CreateOctree();
        OcTree<Triangle> tree = creator.create(parser);


        double maxX, maxY, minX, minY, minZ;
        maxX = parser.getMaxX();
        maxY = parser.getMaxY();
        minX = parser.getMinX();
        minY = parser.getMinY();
        minZ = parser.getMinZ();


        double centerX = (maxX - minX) / 2;
        double centerY = (maxY - minY) / 2;
        double coef;


        // Point from where rays would be cast
        if (centerX > centerY) {
            coef = centerX *3;
        } else {
            coef = centerY *3;
        }

        Vector3 cameraPos = new Vector3(minX + centerX, minY + centerY, minZ - coef);

        // Look direction
        Vector3 cameraDir = new Vector3(0, 0, 1);

        // Projection plane position is at 1 unit away from camera
        Vector3 planeOrigin = Vector3.opAdd(cameraPos, cameraDir.Norm());

        // Field of view in degrees
        double fov = 60;

        // Screen size in pixels
        int screenWidth = 500, screenHeight = 500;
        double[][] screenBuffer = new double[screenWidth][screenHeight];
        List<Triangle> trianglesList = null;


            for (int x = 0; x < screenWidth; x++)
            {
                for (int y = 0; y < screenHeight; y++)
                {
                    screenBuffer[x][y] = 10;


                        // Normalized coordinates of pixel to [-1;1] interval;
                        // y is inverted because in console output it goes from 0 to screenHeight
                        // and in world coordinates y goes from realPlaneHeight/2 to -realPlaneHeight/2
                        double xNorm = (x - screenWidth / 2) / (double)screenWidth;
                        double yNorm = -(y - screenHeight / 2) / (double)screenHeight;

                        double distanceToPlaneFromCamera = Vector3.opSubtract(cameraPos, planeOrigin).getLength();
                        double fovInRad = fov / 180f * Math.PI;

                        // Height of plane in front of camera in our world units. Tangent function needs radians
                        double realPlaneHeight = (double)(distanceToPlaneFromCamera * Math.tan(fovInRad));

                        // Here we suppose that camera is looking always from point (0,0,-Z), so the math here is little simpler to understand
                        // We need to find point on plane that is xNorm * realPlaneHeight / 2 units away from center in x direction,
                        // and similarly in y-dir. If the camera is moved and/or plane is rotated, math is little harder here
                        // Also, we assume height == width
                        Vector3 positionOnPlane = Vector3.opAdd(planeOrigin, new Vector3(xNorm * realPlaneHeight / 2, yNorm * realPlaneHeight / 2, 0));

                        // Throw a ray from camera to the point we found and beyond
                        Vector3 rayDirection = Vector3.opSubtract(positionOnPlane, cameraPos);

                        // If we find an intersection of the ray with our triangle, we draw "pixel"


                    trianglesList = getListOfIntersectTriangles(tree, cameraPos, cameraDir);

                    for (Triangle tr : trianglesList) {
                        Triangle triangleToDraw = tr;
                        if (IntersectionChecker.ThereIsIntersectionBetweenRayAndTriangle(cameraPos, rayDirection, triangleToDraw))
                        {

                            if(FlatShading.calculate(triangleToDraw, cameraDir) <= 0 && FlatShading.calculate(triangleToDraw, cameraDir) >= -1 )
                                screenBuffer[x][y] = FlatShading.calculate(triangleToDraw, new Vector3(-1, -1, 1));

                        }
                    }

                }
            }



        // Output our buffer
        //DrawScreenBuffer(screenHeight, screenWidth, screenBuffer);
        BmpCreator.saveBmp(screenWidth, screenHeight, screenBuffer);
    }

    private static List<Triangle> getListOfIntersectTriangles(OcTree<Triangle> tree, Vector3 start, Vector3 direction) {
        Vector3 v1, v2;
        List<Triangle> trianglesList = null;
        List<OcTree<Triangle>> childrenList;
        CollidableAABB bound;

        while (!tree.isLeaf()) {
            childrenList = tree.getChildren();
            for (int i = 0; i < 8; i++){
                bound = childrenList.get(i).getBounds();
                v1 = new Vector3(bound.getMin()[0],bound.getMin()[1], bound.getMin()[2]);
                v2 = new Vector3(bound.getMax()[0],bound.getMax()[1], bound.getMax()[2]);
                boolean intersect = IntersectionChecker.ThereIsIntersectionBetweenRayAndBoundingBox(start, direction, v1, v2 );
                if (intersect) {
                    tree = childrenList.get(i);
                    break;
                }
            }
        }

        List<OcTreeNode<Triangle>> list = tree.getObjects();
        for (OcTreeNode<Triangle> tr : list) {
            trianglesList.add(tr.getData());
        }

        return trianglesList;
    }

    private static void DrawScreenBuffer(int screenHeight, int screenWidth, boolean[][] screenBuffer)
    {
        // Y first, output by rows
        for (int y = 0; y < screenHeight; y++)
        {
            for (int x = 0; x < screenWidth; x++)
            {
                System.out.print(screenBuffer[x][y] ? "X" : ".");
            }
            System.out.println();
        }
    }

}
