

package com.qualcomm.ftcrobotcontroller.opmodes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.io.IOException;


/**
 * Autonomous Mode
 * <p>
 *Enables control of the robot via the gamepad
 *
 * THIS IS A TEMPLATE. COPY AND PASTE THIS TO CREATE THE MULTIPLE VERSIONS WHICH WE NEED
 *
 */
public class AutonomousOpRichard extends LinearOpMode {

    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor motorTurbo;
    DcMotor middleRelease;
    DcMotor tapeMotor;
    Servo leftServo;
    Servo tapeServo;

    Bitmap image;


    /*
       * Code to run when the op mode is first enabled goes here
       * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
       */
    @Override
    public void runOpMode() {

        motorRight = hardwareMap.dcMotor.get("mRight");
        motorLeft = hardwareMap.dcMotor.get("mLeft");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        // Turbo motor -- the one in the middle
        motorTurbo = hardwareMap.dcMotor.get("mMid");

        // Lifts and lowers the middle turbo motor
        middleRelease = hardwareMap.dcMotor.get("mRelease");

        // For big pull ups -- measureable muscle
        tapeMotor = hardwareMap.dcMotor.get("tapeRelease");
        tapeMotor.setDirection(DcMotor.Direction.REVERSE);

        // Lifts/lowers metal bar
        leftServo = hardwareMap.servo.get("lservo");

        // Lifts/lowers tape
        tapeServo = hardwareMap.servo.get("tapeServo");
        leftServo.setPosition(0.8 );

        double t = System.currentTimeMillis() + 10000;

        // ALL TIMINGS ARE BS, WE HAVEN'T TESTED THEM YET
        // TODO: Fix timing


        // Move forward

        motorLeft.setPower(1.0);
        motorRight.setPower(1.0);

        while(t>System.currentTimeMillis()){
          // wait for 10 seconds, run sensor stuff here possibly
        }

        motorLeft.setPower(0.0);
        motorRight.setPower(0.0);

        // wait a bit

        t = System.currentTimeMillis() + 1000;
        while (t > System.currentTimeMillis()){

        }

        // turn left

        t = System.currentTimeMillis() + 1000;
        motorLeft.setPower(-1.0);
        motorRight.setPower(1.0);

        while (t > System.currentTimeMillis()){

        }

        motorLeft.setPower(0.0);
        motorRight.setPower(0.0);

        // move forward again

        t = System.currentTimeMillis() + 10000;
        motorLeft.setPower(1.0);
        motorRight.setPower(1.0);

        while (t > System.currentTimeMillis()){

        }

        motorLeft.setPower(0.0);
        motorRight.setPower(0.0);

        // turn left again

        t = System.currentTimeMillis() + 1000;
        motorLeft.setPower(-1.0);
        motorRight.setPower(1.0);

        while (t > System.currentTimeMillis()){

        }

        motorLeft.setPower(0.0);
        motorRight.setPower(0.0);

        // move forward again

        t = System.currentTimeMillis() + 1000;
        motorLeft.setPower(1.0);
        motorRight.setPower(1.0);

        while (t > System.currentTimeMillis()){

        }

        motorLeft.setPower(0.0);
        motorRight.setPower(0.0);
    }





    public int getCameraColor(SurfaceHolder s){

        /*
        METHOD TO FIND WHAT BEACON COLOR CAMERA IS LOOKING AT CURRENTLY
        
        This is done by taking a photo and analyzing the average color of the central 30% of the bitmap.

        You have to pass a separate surface holder you have to make in the main activity.
        Still haven't figured out how I'm going to do it.

        This method returns the following values:
        -1 - Something went wrong
        0 - Neither color was decisively detected
        1 - Red was detected
        2 - Blue was detected

    */

        // Attempt at using android camera libraries as sensors.

        // Open camera
        Camera c = Camera.open();


        // Get height and width of camera
        int height = c.getParameters().getPictureSize().height;
        int width = c.getParameters().getPictureSize().width;

        try {
            // Have to set a preview display somewhere, still figuring this out
            c.setPreviewDisplay(s);
            c.startPreview();

            // Taking a picture: Using a callback, which turns this into a BufferedImage, which is a global var
            c.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    image = BitmapFactory.decodeByteArray(data,0,data.length);
                }
            });

            // If for some reason camera fails to fire, return -1
            // c.release() allows camera to be used by other apps after this (Important!)
            if(image == null){
                c.release();
                return -1;
            }


            // Taking the middle 30% of the image (can be changed by changing the denominator)

            int h1 = height/3;
            int h2 = height - h1;

            int w1 = width/3;
            int w2 = width - w1;

            // Getting the average of the pixel colours in this central region
            // Note: There may be some loss of precision, mainly for the blue value (due to int division)

            int val = 0;
            int count = 0;

            for(int i = w1; i<w2; i++){
                for (int j =h1;j<h2;j++){
                    val += image.getPixel(i,j);
                    count++;
                }
            }

            int p = val/count;

            // Converting the color value into three separate RGB values
          
            int R = (p & 0xff0000) >> 16;
            int G = (p & 0xff00) >> 8;
            int B = p & 0xff;

            // If the Red value is sufficiently high, and other values relatively low (i.e. it's red, and not white)
            // TODO: Test these thresholds in a realistic environment
            if(R > 180 && (B < 50 && G < 50)){
                c.release();
                return 1;
                // RETURN 1 means it's red
            } else if(B > 180 && (R < 50 && G < 50)){
            // Same for Blue now
            // TODO: Test threshold
                c.release();
                return 2;
                // RETURN 2 means it's blue
            } else {
                c.release();
                return 0;
            }


        }catch(IOException e){
            c.release();
            return -1;
        }

    }

}
