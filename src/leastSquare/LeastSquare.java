package leastSquare;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.List;
import myLib.utils.Utils;

/**
 * 1次の最小二乗法
 *
 * @author tadaki
 */
public class LeastSquare {

    //直線フィットの種類
    static public enum Type {

        Normal, SemiLog, LogLog
    }

    private String filename;//データのあるファイル名

    /**
     *
     * @param filename データファイル名
     */
    public LeastSquare(String filename) {
        this.filename = filename;
    }

    /**
     * 最小二乗法によるフィッティングの実施
     *
     * @param type フィッティングのタイプ
     * @return 結果を文字列表示したもの
     * @throws IOException
     */
    public String fitAndGetString(Type type) throws IOException {
        Point2D.Double param = fit(type);
        //フィッティングのタイプに応じて結果を出力
        StringBuilder sb = new StringBuilder();
        sb.append("y = ");
        switch (type) {
            case SemiLog:
                sb.append(Math.exp(param.y)).append(" * ");
                sb.append("exp(").append(param.x).append(" x)");
                break;
            case LogLog:
                sb.append(Math.exp(param.y)).append(" * ");
                sb.append("x**(").append(param.x).append(")");
                break;
            default:
                sb.append(param.x).append(" x + ").append(param.y);
        }
        return sb.toString();
    }

    /**
     * 最小二乗法によるフィッティングの実施
     *
     * type=Normalならばax+bの(a,b)
     *
     * type=SemiLogならばbe^{ax}の(a,b)
     *
     * type=LogLogならばbx^aの(a,b)
     *
     * @param type フィッティングのタイプ
     * @return 二つの係数
     * @throws IOException
     */
    public Point2D.Double fit(Type type) throws IOException {
        List<Point2D.Double> data = FileRead.readData(filename);
        List<Point2D.Double> dum = Utils.createList();
        //フィッティングに対応して、データを変換
        switch (type) {
            case SemiLog://片対数
                data.stream().filter((p) -> (p.y > 0)).forEach((p) -> {
                    double x = p.x;
                    double y = Math.log(p.y);
                    dum.add(new Point2D.Double(x, y));
                });
                break;
            case LogLog://両対数
                data.stream().filter((p) -> (p.x > 0 && p.y > 0)).
                        forEach((p) -> {
                            double x = Math.log(p.x);
                            double y = Math.log(p.y);
                            dum.add(new Point2D.Double(x, y));
                        });
                break;
            default:
                data.stream().forEach((p) -> {
                    dum.add(new Point2D.Double(p.x, p.y));
                });
                break;
        }
        return fitSub(dum);
    }

    private Point2D.Double fitSub(List<Point2D.Double> d) {
        double ax = 0.;//x の平均
        double ax2 = 0;//x^2 の平均
        double ay = 0;//y の平均
        double axy = 0.;//x*y の平均
        int n = 0;//データ点の数
        for (Point2D.Double p : d) {//全てのデータに対して
            //x,x^2,y,xyの和を計算





        }
        ax /= n;
        ax2 /= n;
        ay /= n;
        axy /= n;
        double sigma2 = ax2 - ax * ax;
        double a = (axy - ax * ay) / sigma2;
        double b = (ay * ax2 - axy * ax) / sigma2;
        return new Point2D.Double(a, b);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String result
                = new LeastSquare("out-linear.txt").fitAndGetString(Type.Normal);
        System.out.println(result);
        result = new LeastSquare("out-exp.txt").fitAndGetString(Type.SemiLog);
        System.out.println(result);
        result = new LeastSquare("out-pow.txt").fitAndGetString(Type.LogLog);
        System.out.println(result);
    }

}
