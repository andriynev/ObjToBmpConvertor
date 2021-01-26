package geometry;


public final class Triangle {
    private Vector3 a = new Vector3();
    private Vector3 b = new Vector3();
    private Vector3 c = new Vector3();

    public Triangle() {
    }

    public Triangle(Vector3 a, Vector3 b, Vector3 c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Vector3 getA() {
        return a;
    }

    public Vector3 getB() {
        return b;
    }

    public Vector3 getC() {
        return c;
    }

    public float[] min() {
        float[] min = new float[3];

        min[0] = (float) Math.min(Math.min(getA().getX(), getB().getX()), getC().getX());
        min[1] = (float) Math.min(Math.min(getA().getY(), getB().getY()), getC().getY());
        min[2] = (float) Math.min(Math.min(getA().getZ(), getB().getZ()), getC().getZ());

        return min;
    }

    public float[] max() {
        float[] max = new float[3];

        max[0] = (float) Math.max(Math.max(getA().getX(), getB().getX()), getC().getX());
        max[1] = (float) Math.max(Math.max(getA().getY(), getB().getY()), getC().getY());
        max[2] = (float) Math.max(Math.max(getA().getZ(), getB().getZ()), getC().getZ());

        return max;
    }

}
