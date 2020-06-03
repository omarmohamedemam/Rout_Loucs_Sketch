package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.ejml.data.Complex64F;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Controller {
    @FXML
    Canvas canvas;
    @FXML
    Button viewButton;
    @FXML
    CheckBox rulesBox;
    @FXML
    CheckBox variKBox;
    @FXML
    Slider scaler;
    @FXML
    Label scaleLabel;
    @FXML
    private ScrollPane scrollPane;
   public static final int width=400;
   public static final int height=100;
    ArrayList<Point> poles = new ArrayList<>();
    ArrayList<Point> zeros = new ArrayList<>();
    ArrayList<Double> asymptotesAngles = new ArrayList<>();
    ArrayList<LineSegment> lineSegments = new ArrayList<>();
    double myScale = 1;
   int  kStart=1;
   int kStep=100000;
   int iterationNum=1000;




    double centroid=0;
    double sumOfPoles=0;
    double sumOfZeros=0;
    int numOfPoles;
    int numOfZeros;
    double scaleX=1;
    double scaleY=1;
    double intersectionWithJWAxis;
    double GS[] ={0,65000,5100,125,1} ;

    public void initialize(){

        scaler.setMin(1);
        scaler.setMax(5);
        canvas.getGraphicsContext2D().clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        drawAxis(canvas.getGraphicsContext2D());

    }
    public void setScale(){
        int scale= (int) scaler.getValue();
        scaleX=scaleY=scale;
        canvas.setScaleX(scale);
        canvas.setScaleY(scaleY);
        scaleLabel.setText(String.valueOf(scaleX));
        drawAxis(canvas.getGraphicsContext2D());
        View();
    }
    public void View(){
        initialize();
        if(rulesBox.isSelected())
            viewUsingRules();
        if (variKBox.isSelected())
            viewVaryOfK();
    }

    private void viewVaryOfK(){
        double coff[] ={1,65000,5100,125,1} ;
        int i=1;
        ArrayList<Color> colors=new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.YELLOW);
        colors.add(Color.GREEN);
        int colorsCounter=0;

        for(int k=kStart;i<=iterationNum;k+=kStep){
            i++;
            double[] kth={k,65000,5100,125,1};

            Complex64F[] c1 = Utility.findRoots(kth);
            for(Complex64F complex64F : c1){
                Point pole = new Pole(complex64F.real,complex64F.imaginary);
                double x=reX(pole.getX());
                double y = reY(pole.getY());

                canvas.getGraphicsContext2D().setStroke(colors.get(colorsCounter++));
                canvas.getGraphicsContext2D().strokeLine(x,y,x,y);
                canvas.getGraphicsContext2D().setLineWidth(2);
               // drawPole(pole,canvas.getGraphicsContext2D(),colors.get(colorsCounter++),false);
                colorsCounter %=4;

            }

        }

    }
    public void getPorpertiesOfK(int kStart,int kStep,int iterationNum){
        this.kStart=kStart;
        this.iterationNum=iterationNum;
        this.kStep=kStep;
    }
    public ArrayList<Integer> passCurrentValue(){
        ArrayList<Integer> val = new ArrayList<>();
        val.add(kStart);
        val.add(kStep);
        val.add(iterationNum);
        return val;
    }
    public void setKProperties() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("KWindow.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOpacity(1);
        stage.setTitle("K Properties");
        stage.setScene(new Scene(root, 320, 250));
        stage.showAndWait();
    }
    public void viewUsingRules(){

        zeros.clear();
        asymptotesAngles.clear();
        poles.clear();
        lineSegments.clear();
        centroid=0;
        sumOfZeros=0;
        sumOfPoles=0;
        numOfPoles=0;
        numOfZeros=0;
        GraphicsContext gc = canvas.getGraphicsContext2D();


        gc.lineTo(100,100);
        gc.setStroke(Color.BLACK);

        gc.setLineWidth(0.4);
        gc.setLineDashes(0);


        //Adding Poles
        Point pole1 = new Pole(0,0);
        Point pole2 = new Pole(-25,0);
        Point pole3 = new Pole(-50,10);
        Point pole4 = new Pole(-50,-10);
        poles.add(pole1);
        poles.add(pole2);
        poles.add(pole3);
        poles.add(pole4);
        numOfPoles = poles.size();
        numOfZeros = zeros.size();
        //Adding Zeros
        //...
        //...........
        int difference = numOfPoles-numOfZeros;

        //Getting sum of poles
        for(int i=0; i<numOfPoles;i++)
            sumOfPoles += poles.get(i).getX();

        //Getting sum of zeros
        for(int i=0; i<numOfZeros;i++)
            sumOfZeros += zeros.get(i).getX();
        //Calculating Centroid
        centroid = (sumOfPoles-sumOfZeros)/difference;

        //Calculating Asymptotes Angles
        for(int q=0;q<difference;q++){
            Double angle = (double) (((2 * q + 1) * 180) / difference);
            asymptotesAngles.add(angle);
        }
        //Find break point (can't do in this prog for now )
        double breakPoint = -9.15*myScale;
        //Find Critical Freq
        ArrayList<Point> onRealAxis = new ArrayList<>();
        for(Point pole : poles){
            if(pole.getY()==0){
                onRealAxis.add(pole);
            }
        }
        for(Point zero : zeros){
            if(zero.getY()==0){
                onRealAxis.add(zero);
            }
        }
        //Sort Poles from left to right of Axis
        Collections.sort(onRealAxis, new Comparator<Point>() {
            @Override
            public int compare(Point point, Point t1) {
                return point.getX()>t1.getX() ? 1 : -1;
            }
        });

        //Find All line Segments;
        for(int i=0;i<onRealAxis.size();i++){
            if(i%2==0){
                double start,end;
                start = onRealAxis.get(i).getX();
                if(i+1==onRealAxis.size())
                    end = 999999;
                else
                    end=onRealAxis.get(i+1).getX();
                LineSegment lineSegment = new LineSegment(start,end);
                lineSegments.add(lineSegment);

            }
        }
        //Getting Angles Of Departure

        for(Point pole : poles) {

            angleOfDeprture(pole, poles, zeros);

        }
        //Intersection with Jw Axis
        /*
        intersectionWithJwAxis = routhCriteria(GS).getIntersectionWithJwAxis();
         */
        intersectionWithJWAxis = 22.8 ;
    centroid *= myScale;
        //drawing poles
        for(Point pole : poles) {

            drawPole(pole, gc,Color.BLACK,true);

        }
        //drawing lines segments
        for(LineSegment lineSegment : lineSegments)
            drawLineSegment(lineSegment,gc);
        //drawing Asymptotes
        drawAsymptotes(asymptotesAngles,centroid,gc);

        double x1=reX(970);
        double y1=reY(1009.15 );
        double x2=reX(489-9.15);
        double y2=reY(-510);

        double xc=reX(centroid);
        double yc=reY(0);
        double x0 = reX(0);
        double reCentroid = reX(centroid);
        double reBreakPoint = reX(breakPoint);
        double reIntersectWithJwAxis= reY(intersectionWithJWAxis);
        double negReInterectwithJwAxis = reY(-intersectionWithJWAxis);
        double yC1= reY(intersectionWithJWAxis/2);
        double xC1 = reX(breakPoint);
        double yC2 =reY(-intersectionWithJWAxis/2);
        gc.setLineDashes(0);
        gc.setLineWidth(1);
        gc.setStroke(Color.BLACK);
        gc.beginPath();
        gc.moveTo(reBreakPoint,yc);
        gc.quadraticCurveTo(xC1,yC1,x0,reIntersectWithJwAxis);
        gc.lineTo(x1,y1);
        gc.moveTo(reBreakPoint,yc);
        gc.quadraticCurveTo(xC1,yC2,x0,negReInterectwithJwAxis);
        gc.lineTo(x2,y2);

        gc.moveTo(reX(poles.get(2).getX()),reY(poles.get(2).getY()));
        double x3= reX(-1000);
        double y3 =reY(0.97*1000-9.15);
        gc.quadraticCurveTo(reCentroid,yc,x3,y3);
        gc.moveTo(reX(poles.get(3).getX()),reY(poles.get(3).getY()));
        double x4= reX(-1000);
        double y4 =reY(0.97*-1000+9.15);
        gc.quadraticCurveTo(xc,yc,x4,y4);


       // gc.closePath();
        gc.stroke();
        gc.strokeLine(x0-3,reIntersectWithJwAxis,x0+3,reIntersectWithJwAxis);
        gc.strokeLine(x0-5,negReInterectwithJwAxis,x0+3,negReInterectwithJwAxis);

    }
    private void drawPole(Point pole,GraphicsContext gc,Color color,boolean drawAngle){
        double x= reX(pole.getX()*myScale);
        double y = reY(pole.getY()*myScale);
        int m=2;
        int a=10;
        gc.setStroke(color);
        gc.setLineWidth(1);
       /* gc.strokeLine(x-m,y-m,x+m,y+m);
        gc.strokeLine(x-m,y+m,x+m,y-m);

        */
        gc.strokeLine(x-m,y-m,x+m,y+m);
        gc.strokeLine(x-m,y+m,x+m,y-m);
        gc.setLineWidth(0.5);
        if(drawAngle) {
            gc.setStroke(Color.RED);
            gc.strokeLine(x, y, x + a, y);
            drawLineAtAngle(gc, x, y, 15, pole.getAngleOfDeparture());
            if (pole.getAngleOfDeparture() >= 0 && pole.getAngleOfDeparture() <= 180) {
                gc.strokeText(String.valueOf(pole.getAngleOfDeparture()), x, y, 10);
            } else {
                gc.strokeText(String.valueOf(pole.getAngleOfDeparture()), x, y + 20, 10);


            }

        }



    }
    private double reX(double X){
        return X+canvas.getWidth()/2;
    }
    private double reY(double Y){
        return (canvas.getHeight()/2)-Y;
    }
    private void drawAxis(GraphicsContext gc){
        canvas.setScaleX(1);
        canvas.setScaleY(1);
        gc.setLineWidth(1/scaleX);
        gc.setLineDashes(0);
        gc.setStroke(Color.gray(0.2,0.6));
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.strokeLine(0,canvas.getHeight()/2,canvas.getWidth(),canvas.getHeight()/2);
        gc.strokeLine(canvas.getWidth()/2,0,canvas.getWidth()/2,canvas.getHeight());
        double y = 0;
        y = reY(y);
        int step;
        switch ((int)scaleX){
            case 1:
                step=50;
                break;
            case 2:
                step =30;
                break;
            case 3:
                step = 25;break;
            case 4:
                step = 20; break;
            case 5:
                step = 20; break;
            default:
                step = 50;
                break;

        }//+X axis
        for(int i =(int)canvas.getWidth()/2;i<canvas.getWidth();i=i+step){
            String s =String.valueOf((i-canvas.getWidth()/2)/myScale);
            int indx = s.indexOf('.');
            s=s.substring(0,indx);
            gc.strokeText(s,i,y+10);
            drawAVerticalCut(i,gc);
            //-X axis
        }for(int i =(int)canvas.getWidth()/2;i>0;i=i-step){

            String s =String.valueOf((i-canvas.getWidth()/2)/myScale);
            int indx = s.indexOf('.');
            s=s.substring(0,indx);
            if(s.equalsIgnoreCase("0"))continue;

            gc.strokeText(s,i,y+10);
            drawAVerticalCut(i,gc);
        }
        //-Y axis

        double x=reX(0);
        for(int i =(int)canvas.getHeight()/2;i<canvas.getHeight();i=i+step){
            String s =String.valueOf((i-canvas.getHeight()/2)/myScale);
            int indx = s.indexOf('.');
            s=s.substring(0,indx);

            drawAHorintalCut(i,gc);
            if(s.equalsIgnoreCase("0"))continue;

            gc.strokeText(s,x-20,i);
        }
        //+Y axis
        for(int i =(int)canvas.getHeight()/2;i>0;i=i-step){
            String s =String.valueOf((i-canvas.getHeight()/2)/myScale);
            int indx = s.indexOf('.');
            s=s.substring(0,indx);
            if(s.equalsIgnoreCase("0"))continue;
            gc.strokeText(s,x-20,i);
            drawAHorintalCut(i,gc);
        }
        canvas.setScaleX(scaleX);
        canvas.setScaleY(scaleX);

    }
    private void drawLineSegment(LineSegment lineSegment,GraphicsContext gc){
        gc.setLineWidth(2);
        gc.setStroke(Color.BLACK);
        gc.setLineDashes(0);
        double x1 = reX(lineSegment.getStart()*myScale);
        double x2 = reX(lineSegment.getEnd()*myScale);
        gc.setLineWidth(2/scaleX);
        double y=0;
        y=reY(y);
        gc.strokeLine(x1,y,x2,y);


    }
    private void drawAsymptotes(ArrayList<Double> asymptotesAngles,double centroid,GraphicsContext gc){
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(0.8/scaleX);
        gc.setLineDashes(3);
        for(double Angle : asymptotesAngles)
            drawLineAtAngle(gc,reX(centroid),reY(0),1000,Angle);
    }
    private  void drawLineAtAngle(GraphicsContext gc, double x1,double y1, double length, double angle) {
        angle=-angle;
        gc.strokeLine(x1, y1,
                x1 + length * Math.cos(Math.toRadians(angle)), y1 + length
                        * Math.sin(Math.toRadians(angle)));
    }
    private double angleOfDeprture(Point polee,ArrayList<Point> poles,ArrayList<Point> zeros){
        double sumOfPolesAngles=0;
        double angOfDep=0;
        Pole pole = (Pole) polee;
        double sumOfZerosAngles=0;
        for(Point p : poles){
            double xDiff;
            double yDiff;
            if(p==pole)continue;
            yDiff=pole.getY()-p.getY();
            xDiff=pole.getX()-p.getX();
            double distance = distance(polee,p);
            double angle = Math.toDegrees(Math.atan(yDiff/xDiff));

            angle %= 360;
            sumOfPolesAngles += angle;
        }
        for(Point z : zeros){
            double xDiff;
            double yDiff;

            yDiff=pole.getY()-z.getY();
            xDiff=pole.getX()-z.getX();
            double angle = Math.toDegrees(Math.atan(yDiff/xDiff));
            sumOfZerosAngles += angle;
        }
        angOfDep=180-sumOfPolesAngles+sumOfZerosAngles;
        angOfDep=Math.round(angOfDep);
        angOfDep %= 360;
        poles.get(1).setAngleOfDeparture(0);

        pole.setAngleOfDeparture(angOfDep);
        return angOfDep;
    }
    private double distance(Point p1,Point p2){
        return Math.sqrt(Math.pow(p1.getX()-p2.getX(),2)+Math.pow(p2.getY()-p1.getY(),2));
    }
    private void drawAHorintalCut(double y,GraphicsContext gc){
        double x0=reX(0);
        gc.strokeLine(x0-3,y,x0+3,y);
    }
    private void drawAVerticalCut(double x,GraphicsContext gc){
        double y0=reY(0);
        gc.strokeLine(x,y0-3,x,y0+3);
    }

}
