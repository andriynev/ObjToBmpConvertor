package objConverter;

import geometry.Triangle;
import geometry.Vector3;

public  class FlatShading {

    public static double calculate(Triangle triangle, Vector3 lightDirection) {
        Vector3 norm;
        double vx1, vy1, vz1, vx2, vy2, vz2;
        Vector3 A = triangle.getA();
        Vector3 B = triangle.getB();
        Vector3 C = triangle.getC();

        vx1 = A.getX() - B.getX();
        vy1 = A.getY() - B.getY();
        vz1 = A.getZ() - B.getZ();

        vx2 = B.getX() - C.getX();
        vy2 = B.getY() - C.getY();
        vz2 = B.getZ() - C.getZ();

        Vector3 v1 = new Vector3(vx1, vy1, vz1);
        Vector3 v2 = new Vector3(vx2, vy2, vz2);

        norm = v1.CrossProduct(v2);

        double cos = (norm.DotProduct(lightDirection)) / (norm.getLength() * lightDirection.getLength());

        return cos;
    }


}
