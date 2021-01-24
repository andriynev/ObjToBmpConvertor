package intersectionsProcessing;

import geometry.Triangle;
import geometry.Vector3;


public class IntersectionChecker
{

    public static boolean ThereIsIntersectionBetweenRayAndTriangle(Vector3 rayOrigin, Vector3 rayVector, Triangle inTriangle)
    {
        Vector3 vertex0 = inTriangle.getA();
        Vector3 vertex1 = inTriangle.getB();
        Vector3 vertex2 = inTriangle.getC();
        Vector3 edge1 = Vector3.opSubtract(vertex1, vertex0);
        Vector3 edge2 = Vector3.opSubtract(vertex2, vertex0);
        Vector3 h = rayVector.CrossProduct(edge2);
        double a = edge1.DotProduct(h);
        double EPSILON = 1e-5f;
        if (a > -EPSILON && a < EPSILON)
        {
            return false;
        }
        double f = 1 / a;
        Vector3 s = Vector3.opSubtract(rayOrigin, vertex0);
        double u = f * s.DotProduct(h);
        if (u < 0.0 || u > 1.0)
        {
            return false;
        }
        Vector3 q = s.CrossProduct(edge1);
        double v = f * rayVector.DotProduct(q);
        if (v < 0.0 || u + v > 1.0)
        {
            return false;
        }

        // At this stage we can compute t to find out where the intersection point is on the line.
        double t = f * edge2.DotProduct(q);
        return t > EPSILON;
    }
}

