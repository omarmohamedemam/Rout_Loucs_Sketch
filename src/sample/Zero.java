package sample;

public class Zero implements Point {
    double x;
    double y;
    double angleOfArrival;
    public Zero (double x , double y){
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

    @Override
    public double getAngleOfDeparture() {
        return angleOfArrival;
    }

    @Override
    public void setAngleOfDeparture(double angle) {
            angleOfArrival=angle;
    }
}
