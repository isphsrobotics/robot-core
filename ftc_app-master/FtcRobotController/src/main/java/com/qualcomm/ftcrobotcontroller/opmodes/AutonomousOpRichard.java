

package com.qualcomm.ftcrobotcontroller.opmodes;

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

        t = System.currentTimeMillis() + 1000;
        while (t > System.currentTimeMillis()){

        }


        t = System.currentTimeMillis() + 1000;
        motorLeft.setPower(-1.0);
        motorRight.setPower(1.0);

        while (t > System.currentTimeMillis()){

        }

        motorLeft.setPower(0.0);
        motorRight.setPower(0.0);

        t = System.currentTimeMillis() + 10000;
        motorLeft.setPower(1.0);
        motorRight.setPower(1.0);

        while (t > System.currentTimeMillis()){

        }

        motorLeft.setPower(0.0);
        motorRight.setPower(0.0);

        t = System.currentTimeMillis() + 1000;
        motorLeft.setPower(-1.0);
        motorRight.setPower(1.0);

        while (t > System.currentTimeMillis()){

        }

        motorLeft.setPower(0.0);
        motorRight.setPower(0.0);

        t = System.currentTimeMillis() + 1000;
        motorLeft.setPower(1.0);
        motorRight.setPower(1.0);

        while (t > System.currentTimeMillis()){

        }

        motorLeft.setPower(0.0);
        motorRight.setPower(0.0);
    }

    public int getCameraColor(SurfaceHolder s){

        // Attempt at using android camera libraries as sensors.

        Camera c = Camera.open();

        try {
            c.setPreviewDisplay(s);
            c.startPreview();
            c.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {

                }
            });
        }catch(IOException e){}

        return 0;

    }

}
