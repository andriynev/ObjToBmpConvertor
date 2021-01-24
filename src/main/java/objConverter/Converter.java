package objConverter;

import bmpCreator.BmpCreator;
import geometry.Triangle;
import geometry.Vector3;
import intersectionsProcessing.IntersectionChecker;
import objFormatParser.ObjParser;

import java.util.ArrayList;

public class Converter {

    public static void convert() {

        ObjParser parser = new ObjParser();
        parser.parseObj();

        ArrayList<Vector3> vertexList = parser.getVertexList();
//        for (int i = 0; i < vertexList.size(); i++) {
//            System.out.println(vertexList.get(i).getZ());
//        }
        ArrayList<Triangle> triangleList = parser.getTriangleList();
        double maxX, maxY, minX, minY, minZ;
        maxX = parser.getMaxX();
        maxY = parser.getMaxY();
        minX = parser.getMinX();
        minY = parser.getMinY();
        minZ = parser.getMinZ();


        double centerX = (Math.abs(maxX) + Math.abs(minX)) / 2;
        double centerY = (Math.abs(maxY) + Math.abs(minY)) / 2;

        // Point from where rays would be cast
        Vector3 cameraPos = new Vector3(minX + centerX, minY + centerY, minZ - 5);

        // Look direction
        Vector3 cameraDir = new Vector3(0, 0, 1);

        // Projection plane position is at 1 unit away from camera
        Vector3 planeOrigin = Vector3.opAdd(cameraPos, cameraDir.Norm());

        // Field of view in degrees
        double fov = 60;

        // Screen size in pixels
        int screenWidth = 100, screenHeight = 100;
        boolean[][] screenBuffer = new boolean[screenWidth][screenHeight];

        /*  b(-2,1,0) *--------* c(1,1,0)
         *              \     /
         *                \  /
         *                  *
         *             a(0,0,0)
         */
        //int size = 1;
        //Triangle[] trianglesToDraw = new Triangle[size];
        //trianglesToDraw[0] = new Triangle(new Vector3(0, 0, 0), new Vector3(-2, 1, 0), new Vector3(1, 1, 0));
        //trianglesToDraw[1] = new Triangle(new Vector3(0, 0, 0), new Vector3(2, 0, 0), new Vector3(1, 1, 0));
        //trianglesToDraw[2] = new Triangle(new Vector3(0, 0, 0), new Vector3(-2, 1, 0), new Vector3(-1, -1, 3));

        for (int i = 0; i < triangleList.size(); i++)
        {
            Triangle triangleToDraw = triangleList.get(i);
            //System.out.println(triangleToDraw.getC().getZ());
            for (int x = 0; x < screenWidth; x++)
            {
                for (int y = 0; y < screenHeight; y++)
                {
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
                    if (IntersectionChecker.ThereIsIntersectionBetweenRayAndTriangle(cameraPos, rayDirection, triangleToDraw))
                    {
                        screenBuffer[x][y] = true;
                    }
                }
            }
        }


        // Output our buffer
        //DrawScreenBuffer(screenHeight, screenWidth, screenBuffer);
        BmpCreator.saveBmp(screenWidth, screenHeight, screenBuffer);
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
