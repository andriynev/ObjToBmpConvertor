package objFormatParser;

import geometry.Triangle;
import geometry.Vector3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ObjParser {

    private final static String OBJ_VERTEX_NORMAL = "vn";
    private final static String OBJ_VERTEX = "v";
    private final static String OBJ_FACE = "f";

    private ArrayList<Vector3> vertexList = new ArrayList<Vector3>();
    private ArrayList<Triangle> triangleList = new ArrayList<Triangle>();

    private final static String PATH = "test.obj";


    public void parseObj() throws FileNotFoundException {

        File file = new File(PATH);
        Scanner sc = new Scanner(file);
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
            } else if (line.startsWith(OBJ_VERTEX)) {
                vertexList.add(processVertex(line));
            } else if (line.startsWith(OBJ_VERTEX_NORMAL)) {
                continue;
                //processNormal(line);
            } else if (line.startsWith(OBJ_FACE)) {
                triangleList.add(processFace(line));
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
        x = vertexList.get(parseInt(elem[1]));
        y = vertexList.get(parseInt(elem[2]));
        z = vertexList.get(parseInt(elem[3]));

        return new Triangle(x, y, z);
    }

    private int parseInt(String str) {
        String[] splitStr;
        int value = 0;
        int length = str.length();
        switch (length) {
            case 5 :
                splitStr = str.split("/");
                value = Integer.parseInt(splitStr[0]);
            case 4 :
                splitStr = str.split("//");
                value = Integer.parseInt(splitStr[0]);
            case 3 :
                splitStr = str.split("/");
                value = Integer.parseInt(splitStr[0]);
            case 1 :
                value = Integer.parseInt(str);
        }

       return value;

    }

}
