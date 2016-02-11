package utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import java.nio.IntBuffer;
import java.util.Random;

/**
 * Created by paolo on 18/11/2015.
 * The Test class represent a singular test-case (a single "shape shown->answer" moment),
 * so it's the singular element of the Session, that's the complete Test-set.
 */
public class Test {

    public int testnumber;
    public int disparity;
    public Shape shape;
    public boolean result;
    public Bitmap bitmap;

    /* tentativo di misurare i layouts fallito
    public static int padding = 5;
    private int spaceforlayouts; */

    /**
     * Constructor for the Test Class
     * @param testnumber number of the test (i-esim test of the session)
     * @param disparity disparity set for the test
     * @param shape the shape to show the test
     */
    public Test(int testnumber, int disparity, Shape shape) {
        this.disparity=disparity;
        this.shape=shape;
        this.testnumber=testnumber;
        this.result=false;
    }

    /**
     * You know.
     * @return a string that describe the Test.
     */
    public String toString() {
        return "Test number "+testnumber+" : "+"at disparity="+this.disparity+" is "+this.shape.toString()+", result= "+this.result;
    }

    /**
     * A static method that returns a random shape from the array of shapes (excluding first "No image")
     * @return a random shape from the given enum Shape (excluding first "No image")
     */
    public static Shape randomShape() {
        Shape[] shapes = Shape.values();
        int rand;
        do {
            rand = new Random().nextInt(shapes.length);
        } while (rand==0); //exclude 0=No image
        return shapes[rand];
    }

    /**
     * Build the bitmap with the shape of the test
     */
    public void buildBitmap(int displayWidth, int displayHeigth, int disparity) {
        //crea la bitmap
        Bitmap b = Bitmap.createBitmap(displayWidth, (int) (displayHeigth * 0.65f), Bitmap.Config.ARGB_8888);
        Log.v("TAG", "Created bitmap size "+b.getWidth()+"X"+b.getHeight()+" with disparity= "+disparity);
        int n = b.getByteCount();
        int[] arr = new int[n];
        //prendi i punti col getPoints
        PointType[][] matrixOfPoints = getPoints(displayWidth,displayHeigth,disparity);

        //costruisci la bitmap coi Points ottenuti (per ora fa solo un nero/bianco con left e rigth)
        int p = 0;
        for(int j=0;j<displayHeigth;j++) {
            for(int i=0;i<displayWidth;i++) {
                if(matrixOfPoints[i][j].equals(PointType.LEFT)) arr[p]= Color.BLACK;
                else arr[p]= Color.WHITE;
                p++;
            }
        }
        b.copyPixelsFromBuffer(IntBuffer.wrap(arr));
        this.bitmap= b;
    }

    /**
     * Check the answer given
     * @param chosenShape the Shape that has been chosen
     * @return true if chosenShape is equals to the hidden Shape of the Test
     */
    public boolean CheckAnswer(Shape chosenShape) {
        return chosenShape.equals(this.shape);
    }

    public PointType[][] getPoints(int displayWidth, int displayHeight, int disparity) {
        PointType[][] matrixOfPoints = new PointType[displayWidth][displayHeight];

        //TODO qui ci va il vero algoritmo che resituisce i punti giusti
        //adesso questo fa solo un random di destra e sinistra
        Random r = new Random();
        for(int i=0;i<displayWidth;i++) {
            for (int j = 0; j < displayHeight; j++) {
                if (r.nextInt(2) == 0) matrixOfPoints[i][j] = PointType.LEFT;
                else matrixOfPoints[i][j] = PointType.RIGHT;
            }
        }
        return matrixOfPoints;
    }



}
