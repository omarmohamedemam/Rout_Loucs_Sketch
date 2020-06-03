package sample;

public class Pole implements Point {
    double x;
    double y;
    double angleOfDeparture;

    public double getAngleOfDeparture() {
        return angleOfDeparture;
    }

    public void setAngleOfDeparture(double angleOfDeparture) {
        this.angleOfDeparture = angleOfDeparture;
    }

    public Pole (double x , double y){
        this.x=x;
        this.y=y;
    }
    @Override

    public void setX(double x) {
        this.x=x;
    }

    @Override
    public void setY(double y) {
        this.y=y;
    }

    @Override
    public double getX() {
        return this.x;
    }

    @Override
    public double getY() {
        return this.y;
    }
}
