package data;

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import myLib.utils.FileIO;
import myLib.utils.Utils;

/**
 *
 * @author tadaki
 */
public class DataGenerator {

    static public enum Type {

        Linear, Exponential, Power
    }
    final private int n;
    final private Type type;
    final private double max;

    public DataGenerator(int n, double max, Type type) {
        this.n = n;
        this.max = max;
        this.type = type;
    }

    public List<Point2D.Double> exec(double a, double b) {
        List<Point2D.Double> list;
        switch (type) {
            case Exponential:
                list = execExponential(a, b);
                break;
            case Power:
                list = execPower(a, b);
                break;
            default:
                list = execLinear(a, b);
                break;
        }
        return list;
    }

    private List<Point2D.Double> execLinear(double a, double b) {
        List<Point2D.Double> list = Utils.createList();
        double dx = max / n;
        for (int i = 0; i < n; i++) {
            double x = dx * (i + 1);
            double y = a * x + b;
            double r = 0.1 * (Math.random() - 0.5) * a * x;
            list.add(new Point2D.Double(x, y + r));
        }
        return list;
    }

    private List<Point2D.Double> execExponential(double a, double b) {
        List<Point2D.Double> list = Utils.createList();
        double dx = max / n;
        for (int i = 0; i < n; i++) {
            double x = dx * (i + 1);
            double y = a * x + Math.log(b);
            double r = 0.1 * (Math.random() - 0.5) * a * x;
            list.add(new Point2D.Double(x, Math.exp(y + r)));
        }
        return list;
    }

    private List<Point2D.Double> execPower(double a, double b) {
        List<Point2D.Double> list = Utils.createList();
        double dx = Math.log(max) / n;
        for (int i = 0; i < n; i++) {
            double x = Math.exp(dx * (i + 1));
            double y = a * Math.log(x) + Math.log(b);
            double r = 0.1 * (Math.random() - 0.5) * a * Math.log(x);
            list.add(new Point2D.Double(x, Math.exp(y + r)));
        }
        return list;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        int n = 1000;
        double max = 100;
        double a = -2.;
        double b = 10.;
        {
            DataGenerator sys = new DataGenerator(n, max, DataGenerator.Type.Linear);
            List<Point2D.Double> points = sys.exec(a, b);
            BufferedWriter out = FileIO.openWriter("out-linear.txt");
            for (Point2D.Double p : points) {
                FileIO.writeSSV(out, p.x, p.y);
            }
            out.close();
        }
        {
            DataGenerator sys = new DataGenerator(n, max, DataGenerator.Type.Exponential);
            List<Point2D.Double> points = sys.exec(a, b);
            BufferedWriter out = FileIO.openWriter("out-exp.txt");
            for (Point2D.Double p : points) {
                FileIO.writeSSV(out, p.x, p.y);
            }
            out.close();
        }
        {
            max = 10000;
            DataGenerator sys = new DataGenerator(n, max, DataGenerator.Type.Power);
            List<Point2D.Double> points = sys.exec(a, b);
            BufferedWriter out = FileIO.openWriter("out-pow.txt");
            for (Point2D.Double p : points) {
                FileIO.writeSSV(out, p.x, p.y);
            }
            out.close();
        }
    }

}
