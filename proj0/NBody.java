public class NBody {
    public static double readRadius(String FileName) {
        In in = new In(FileName);
        in.readInt();
        return in.readDouble();
    }

    public static Planet[] readPlanets(String FileName) {
        In in = new In(FileName);
        int size = in.readInt();
        in.readDouble();
        double xxPos;
        double yyPos;
        double xxVel;
        double yyVel;
        double mass;
        String img;
        Planet p;
        Planet[] res = new Planet[size];
        for (int i = 0; i < size; i++) {
            xxPos = in.readDouble();
            yyPos = in.readDouble();
            xxVel = in.readDouble();
            yyVel = in.readDouble();
            mass = in.readDouble();
            img = in.readString();
            p = new Planet(xxPos, yyPos, xxVel, yyVel, mass, img);
            res[i] = p;
        }
        return res;
    }

    public static void main(String[] args) {
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];

        // draw the window
        double radius = NBody.readRadius(filename);
        StdDraw.setScale(-radius, radius);
        StdDraw.clear();

        StdDraw.picture(0, 0, "images/starfield.jpg");

        Planet[] pArr = NBody.readPlanets(filename);

        for (Planet p : pArr) {
            // System.out.println(p.imgFileName);
            p.draw();
        }

        StdDraw.enableDoubleBuffering();

        for (double time = 0; time != T; time += dt) {
            double[] xForces = new double[pArr.length];
            double[] yForces = new double[pArr.length];
            for (int i = 0; i < pArr.length; i++) {
                xForces[i] = pArr[i].calcNetForceExertedByX(pArr);
                yForces[i] = pArr[i].calcNetForceExertedByY(pArr);
            }
            for (int i = 0; i < pArr.length; i++) {
                pArr[i].update(dt, xForces[i], yForces[i]);
            }
            StdDraw.picture(0, 0, "images/starfield.jpg");
            for (Planet p : pArr) {
                p.draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
        }
        StdOut.printf("%d\n", pArr.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < pArr.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    pArr[i].xxPos, pArr[i].yyPos, pArr[i].xxVel,
                    pArr[i].yyVel, pArr[i].mass, pArr[i].imgFileName);
        }
    }
}