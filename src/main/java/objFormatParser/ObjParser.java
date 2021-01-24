package objFormatParser;

import geometry.Triangle;
import geometry.Vector3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ObjParser {

    private final static String OBJ_VERTEX_NORMAL = "vn";
    private final static String OBJ_VERTEX_TEXTURE = "vt";
    private final static String OBJ_VERTEX = "v";
    private final static String OBJ_FACE = "f";

    private ArrayList<Vector3> vertexList = new ArrayList<Vector3>();
    private ArrayList<Triangle> triangleList = new ArrayList<Triangle>();

    private final static String PATH = "test.obj";
    //private final static String PATH = "delphin.obj";

    public ObjParser() {
    }

    public ArrayList<Vector3> getVertexList() {
        return vertexList;
    }

    public ArrayList<Triangle> getTriangleList() {
        return triangleList;
    }

    public void parseObj() {

        File file = new File(PATH);
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line;
        int lineCount = 1;

        while (sc.hasNextLine()) {
            line = sc.nextLine();
            if (null == line) {
                break;
            }

            line = line.trim();

            if (line.length() == 0) {
                continue;
            }

            if (line.startsWith("#")) // comment
            {
                continue;
            } else if (line.startsWith(OBJ_VERTEX_TEXTURE)) {
                continue;
            } else if (line.startsWith(OBJ_VERTEX_NORMAL)) {
                continue;
            } else if (line.startsWith(OBJ_FACE)) {
                triangleList.add(processFace(line));
            } else if (line.startsWith(OBJ_VERTEX)) {
                vertexList.add(processVertex(line));
            } else {
                continue;
            }
            lineCount++;
        }

    }

    private Vector3 processVertex(String line) {
        double x, y, z;
        String[] elem = line.split(" ");
        x = Double.parseDouble(elem[1]);
        y = Double.parseDouble(elem[2]);
        z = Double.parseDouble(elem[3]);

        return new Vector3(x, y, z);
    }

    private Triangle processFace(String line) {
        String[] elem = line.split(" ");
        Vector3 x = null , y = null , z = null;

        x = vertexList.get(parseInt(elem[1]) - 1);
        y = vertexList.get(parseInt(elem[2]) - 1);
        z = vertexList.get(parseInt(elem[3]) - 1);

        return new Triangle(x, y, z);
    }

    private int parseInt(String str) {
        String[] splitStr;
        int value = 0;

        splitStr = str.split("/");
        value = Integer.parseInt(splitStr[0]);
//        int length = splitStr.length;
//        switch (length) {
//            case 3 :
//                //splitStr = str.split("/");
//
//                break;
////            case 4 :
////                //splitStr = str.split("//");
////                value = Integer.parseInt(splitStr[0]);
////                break;
////            case 2 :
////                //splitStr = str.split("/");
////                value = Integer.parseInt(splitStr[0]);
////                break;
////            case 1 :
////                value = Integer.parseInt(str);
//            default: break;
//        }

       return value;

    }

    public double getMaxX() {
        double maxX = vertexList.get(0).getX();

        for (int i = 1; i < vertexList.size(); i++) {
            if (vertexList.get(i).getX() > maxX) {
                maxX = vertexList.get(i).getX();
            }
        }
        return maxX;
    }

    public double getMaxY() {
        double maxY = vertexList.get(0).getY();

        for (int i = 1; i < vertexList.size(); i++) {
            if (vertexList.get(i).getY() > maxY) {
                maxY = vertexList.get(i).getY();
            }
        }
        return maxY;
    }

    public double getMinX() {
        double minX = vertexList.get(0).getX();

        for (int i = 1; i < vertexList.size(); i++) {
            if (vertexList.get(i).getX() < minX) {
                minX = vertexList.get(i).getX();
            }
        }
        return minX;
    }

    public double getMinY() {
        double minY = vertexList.get(0).getY();

        for (int i = 1; i < vertexList.size(); i++) {
            if (vertexList.get(i).getY() < minY) {
                minY = vertexList.get(i).getY();
            }
        }
        return minY;
    }

    public double getMinZ() {
        double minZ = vertexList.get(0).getZ();

        for (int i = 1; i < vertexList.size(); i++) {
            if (vertexList.get(i).getZ() < minZ) {
                minZ = vertexList.get(i).getZ();
            }
        }
        return minZ;
    }

}
