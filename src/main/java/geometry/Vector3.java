package geometry;

public final class Vector3 {
    private float X;
    private float Y;
    private float Z;

    public Vector3() {
    }

    public Vector3(float x, float y, float z) {
        X = x;
        Y = y;
        Z = z;
    }

    public float getX() {
        return X;
    }

    public float getY() {
        return Y;
    }

    public float getZ() {
        return Z;
    }

    public float getLength() {
        return (float) Math.sqrt(X * X + Y * Y + Z * Z);
    }

    public Vector3 Norm() {
        float length = getLength();
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

    public float DotProduct(Vector3 other) {
        return this.X * other.X + this.Y * other.Y + this.Z * other.Z;
    }

}
