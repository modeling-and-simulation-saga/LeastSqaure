package leastSquare;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import myLib.utils.Utils;

/**
 *
 * @author tadaki
 */
public class FileRead {

    private FileRead() {
    }

    static public List<Point2D.Double> readData(String filename) throws
            IOException, NumberFormatException {
        List<Point2D.Double> data;
        try (BufferedReader in = openReader(filename)) {
            data = Utils.createList();
            String line;
            while ((line = in.readLine()) != null) {
                String s[] = line.split(" ");
                double x = Double.valueOf(s[0]);
                double y = Double.valueOf(s[1]);
                data.add(new Point2D.Double(x, y));
            }
        }
        return data;
    }

    public static BufferedReader openReader(String filename) throws IOException {
        return openReader(filename, "UTF-8");
    }

    public static BufferedReader openReader(String filename, String encoding)
            throws IOException {
        File f = new File(filename);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(new FileInputStream(f), encoding));
        return in;
    }

}
