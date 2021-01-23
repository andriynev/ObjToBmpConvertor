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

}
