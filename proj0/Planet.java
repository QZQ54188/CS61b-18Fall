public class Planet {

    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    private static double G = 6.67 * Math.pow(10, -11);

    public Planet(double xP, double yP, double xV,
            double yV, double m, String img) {
        this.xxPos = xP;
        this.yyPos = yP;
        this.xxVel = xV;
        this.yyVel = yV;
        this.mass = m;
        this.imgFileName = img;
    }

    // public Planet(){
    // xxPos = 0;
    // yyPos = 0;
    // xxVel = 0;
    // yyVel = 0;
    // mass = 0;
    // imgFileName = "";
    // }

    public Planet(Planet p) {
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }

    public double calcDistance(Planet p) {
        double dx = xxPos - p.xxPos;
        double dy = yyPos - p.yyPos;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double calcForceExertedBy(Planet p) {
        return G * mass * p.mass / Math.pow(this.calcDistance(p), 2);
    }

    public double calcForceExertedByX(Planet p) {
        double dx = p.xxPos - this.xxPos;
        return this.calcForceExertedBy(p) * dx / this.calcDistance(p);
    }

    public double calcForceExertedByY(Planet p) {
        double dy = p.yyPos - this.yyPos;
        return this.calcForceExertedBy(p) * dy / this.calcDistance(p);
    }

    public double calcNetForceExertedByX(Planet[] pArr) {
        double xForce = 0;
        for (Planet p : pArr) {
            if (!this.equals(p)) {
                xForce += this.calcForceExertedByX(p);
            }
        }
        return xForce;
    }

    public double calcNetForceExertedByY(Planet[] pArr) {
        double yForce = 0;
        for (Planet p : pArr) {
            if (!this.equals(p)) {
                yForce += this.calcForceExertedByY(p);
            }
        }
        return yForce;
    }

    public void update(double dt, double fX, double fY) {
        double aX = fX / mass;
        double aY = fY / mass;
        this.xxVel += aX * dt;
        this.yyVel += aY * dt;
        this.xxPos += xxVel * dt;
        this.yyPos += yyVel * dt;
    }

    public void draw() {
        StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
    }
}
