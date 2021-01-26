package octree;

public interface Collidable {
    int numDimensions();

    double realMin(int d);

    double realMax(int d);

    default boolean containsBoundingBox(Collidable other) {
        assert  (other.numDimensions() == numDimensions()) ;
        for (int d = 0; d < numDimensions(); d++) {
            if (realMin(d) > other.realMin(d) || realMax(d) < other.realMax(d)) {
                return false;
            }
        }
        return true;
    }

    default boolean intersectsBoundingBox(Collidable other) {
        assert  (other.numDimensions() == numDimensions()) ;
        for (int d = 0; d < numDimensions(); d++) {
            if (realMin(d) > other.realMax(d) || realMax(d) < other.realMin(d)) {
                return false;
            }
        }
        return true;
    }

    default boolean containsPoint(float[] coords) {
        return containsBoundingBox(new CollidableAABB(coords, coords));
    }

}