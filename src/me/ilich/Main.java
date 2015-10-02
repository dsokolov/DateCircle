package me.ilich;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Date Circle");
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        List<Date> dates = new ArrayList<>();
        if (args.length > 0) {
            if (args[0].equals("-f")) {
                String fileName = args[1];
                System.out.print(fileName);
                File f = new File(fileName);
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new FileReader(f));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        try {
                            Date date = simpleDateFormat.parse(line);
                            dates.add(date);
                        } catch (ParseException e) {
                            System.out.println(line + " is not a valid date");
                            e.printStackTrace();
                        }
                    }
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (args[0].equals("-d")) {
                for (int i = 1; i < args.length; i++) {
                    String arg = args[i];
                    try {
                        Date date = simpleDateFormat.parse(arg);
                        dates.add(date);
                    } catch (ParseException e) {
                        System.out.println(arg + " is not a valid date");
                        e.printStackTrace();
                    }
                }
            }
        }
        for (Date date : dates) {
            System.out.println(date.toString());
        }
        List<Double> angels = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (Date date : dates) {
            calendar.setTime(date);
            int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
            int totalDays = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
            final double angel = ((double) dayOfYear / (double) totalDays) * 360.0d;
            angels.add(angel);
            System.out.println(dayOfYear + " / " + totalDays + " = " + angel);
        }

        try {
            final int width = 800;
            final int height = 800;

            final double radius = Math.min(width, height) * 0.45d;
            final double centerX = width / 2d;
            final double centerY = height / 2d;
            final float circleStroke = Math.min(width, height) * 0.01f;
            final float pointSize = Math.min(width, height) * 0.05f;


            // TYPE_INT_ARGB specifies the image format: 8-bit RGBA packed
            // into integer pixels
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            Graphics2D ig2 = bi.createGraphics();
            ig2.setPaint(Color.WHITE);
            ig2.fill(new Rectangle2D.Double(0, 0, width, height));

            ig2.setPaint(Color.BLUE);
            ig2.setStroke(new BasicStroke(circleStroke));
            for (int i = 0; i < 360; i++) {
                double fi = Math.toRadians(i);
                double fi1 = Math.toRadians(i + 1);
                double x1 = radius * Math.cos(fi) + centerX;
                double y1 = radius * Math.sin(fi) + centerY;
                double x2 = radius * Math.cos(fi1) + centerX;
                double y2 = radius * Math.sin(fi1) + centerY;
                ig2.draw(new Line2D.Double(x1, y1, x2, y2));
            }
            List<Point2D> point2Ds = new ArrayList<>();
            for (Double angel : angels) {
                double fi = Math.toRadians(angel);
                double x = radius * Math.cos(fi) + centerX;
                double y = radius * Math.sin(fi) + centerY;
                Point2D point2D = new Point2D.Double(x, y);
                point2Ds.add(point2D);
            }
            ig2.setPaint(Color.RED);
            for (Point2D point2D : point2Ds) {
                double x = point2D.getX() - (pointSize / 2d);
                double y = point2D.getY() - (pointSize / 2d);
                ig2.fill(new Ellipse2D.Double(x, y, pointSize, pointSize));
            }
            ig2.setPaint(Color.GREEN);
            for (Point2D pointA : point2Ds) {
                for (Point2D pointB : point2Ds) {
                    ig2.draw(new Line2D.Double(pointA, pointB));
                }
            }

            ImageIO.write(bi, "PNG", new File("yourImageName.PNG"));

        } catch (IOException ie) {
            ie.printStackTrace();
        }

    }
}
