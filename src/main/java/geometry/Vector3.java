package geometry;

public final class Vector3 {
    private double X;
    private double Y;
    private double Z;

    public Vector3() {
    }

    public Vector3(double x, double y, double z) {
        X = x;
        Y = y;
        Z = z;
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public double getZ() {
        return Z;
    }

    public double getLength() {
        return Math.sqrt(X * X + Y * Y + Z * Z);
    }

    public Vector3 Norm() {
        double length = getLength();
        return new Vector3(X / length, Y / length, Z / length);
    }

    public static Vector3 opAdd(Vector3 left, Vector3 right) {
        return new Vector3(left.X + right.X, left.Y + right.Y, left.Z + right.Z);
    }

    public static Vector3 opSubtract(Vector3 left, Vector3 right) {
        return new Vector3(left.X - right.X, left.Y - right.Y, left.Z - right.Z);
    }

    public Vector3 CrossProduct(Vector3 edge2) {
        Vector3 u = this;
        Vector3 v = edge2;
        return new Vector3(u.Y * v.Z - u.Z * v.Y, u.Z * v.X - u.X * v.Z, u.X * v.Y - u.Y * v.X);
    }

    public double DotProduct(Vector3 other) {
        return this.X * other.X + this.Y * other.Y + this.Z * other.Z;
    }

}
