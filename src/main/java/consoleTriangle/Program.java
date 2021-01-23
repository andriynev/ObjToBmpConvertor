package consoleTriangle;

import bmpCreator.BmpCreator;

public class Program
{
    public static void main(String[] args)
    {
        // Point from where rays would be cast
        Vector3 cameraPos = new Vector3(-0.5f, 0.5f, -5);

        // Look direction
        Vector3 cameraDir = new Vector3(0, 0, 1);

        // Projection plane position is at 1 unit away from camera
        Vector3 planeOrigin = Vector3.opAdd(cameraPos, cameraDir.Norm());

        // Field of view in degrees
        float fov = 60;

        // Screen size in pixels
        int screenWidth = 50, screenHeight = 50;
        boolean[][] screenBuffer = new boolean[screenWidth][screenHeight];

        /*  b(-2,1,0) *--------* c(1,1,0)
         *              \     /
         *                \  /
         *                  *
         *             a(0,0,0)
         */
        int size = 1;
        Triangle[] trianglesToDraw = new Triangle[size];
        trianglesToDraw[0] = new Triangle(new Vector3(0, 0, 0), new Vector3(-2, 1, 0), new Vector3(1, 1, 0));
        //trianglesToDraw[1] = new Triangle(new Vector3(0, 0, 0), new Vector3(2, 0, 0), new Vector3(1, 1, 0));
        //trianglesToDraw[2] = new Triangle(new Vector3(0, 0, 0), new Vector3(-2, 1, 0), new Vector3(-1, -1, 3));

        for (int i = 0; i < size; i++)
        {
           Triangle triangleToDraw = trianglesToDraw[i];
        for (int x = 0; x < screenWidth; x++)
        {
            for (int y = 0; y < screenHeight; y++)
            {
                // Normalized coordinates of pixel to [-1;1] interval;
                // y is inverted because in console output it goes from 0 to screenHeight
                // and in world coordinates y goes from realPlaneHeight/2 to -realPlaneHeight/2
                float xNorm = (x - screenWidth / 2) / (float)screenWidth;
                float yNorm = -(y - screenHeight / 2) / (float)screenHeight;

                float distanceToPlaneFromCamera = Vector3.opSubtract(cameraPos, planeOrigin).getLength();
                double fovInRad = fov / 180f * Math.PI;

                // Height of plane in front of camera in our world units. Tangent function needs radians
                float realPlaneHeight = (float)(distanceToPlaneFromCamera * Math.tan(fovInRad));

                // Here we suppose that camera is looking always from point (0,0,-Z), so the math here is little simpler to understand
                // We need to find point on plane that is xNorm * realPlaneHeight / 2 units away from center in x direction,
                // and similarly in y-dir. If the camera is moved and/or plane is rotated, math is little harder here
                // Also, we assume height == width
                Vector3 positionOnPlane = Vector3.opAdd(planeOrigin, new Vector3(xNorm * realPlaneHeight / 2, yNorm * realPlaneHeight / 2, 0));

                // Throw a ray from camera to the point we found and beyond
                Vector3 rayDirection = Vector3.opSubtract(positionOnPlane, cameraPos);

                // If we find an intersection of the ray with our triangle, we draw "pixel"
                if (ThereIsIntersectionBetweenRayAndTriangle(cameraPos, rayDirection, triangleToDraw))
                {
                    screenBuffer[x][y] = true;
                }
            }
        }
        }


        // Output our buffer
        DrawScreenBuffer(screenHeight, screenWidth, screenBuffer);
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

    /**
     Copy paste from wiki
     https://en.wikipedia.org/wiki/M%C3%B6ller%E2%80%93Trumbore_intersection_algorithm

     @param rayOrigin
     @param rayVector
     @param inTriangle
     @return
     */
    private static boolean ThereIsIntersectionBetweenRayAndTriangle(Vector3 rayOrigin, Vector3 rayVector, Triangle inTriangle)
    {
        Vector3 vertex0 = inTriangle.getA();
        Vector3 vertex1 = inTriangle.getB();
        Vector3 vertex2 = inTriangle.getC();
        Vector3 edge1 = Vector3.opSubtract(vertex1, vertex0);
        Vector3 edge2 = Vector3.opSubtract(vertex2, vertex0);
        Vector3 h = rayVector.CrossProduct(edge2);
        float a = edge1.DotProduct(h);
        float EPSILON = 1e-5f;
        if (a > -EPSILON && a < EPSILON)
        {
            return false;
        }
        float f = 1 / a;
        Vector3 s = Vector3.opSubtract(rayOrigin, vertex0);
        float u = f * s.DotProduct(h);
        if (u < 0.0 || u > 1.0)
        {
            return false;
        }
        Vector3 q = s.CrossProduct(edge1);
        float v = f * rayVector.DotProduct(q);
        if (v < 0.0 || u + v > 1.0)
        {
            return false;
        }

        // At this stage we can compute t to find out where the intersection point is on the line.
        float t = f * edge2.DotProduct(q);
        return t > EPSILON;
    }
}

