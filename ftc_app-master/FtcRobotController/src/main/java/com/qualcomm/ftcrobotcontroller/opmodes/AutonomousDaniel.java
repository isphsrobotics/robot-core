

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


/**
 * Autonomous Mode
 * <p>
 *Enables control of the robot via the gamepad
 *
 * THIS IS A TEMPLATE. COPY AND PASTE THIS TO CREATE THE MULTIPLE VERSIONS WHICH WE NEED
 *
 */
public class AutonomousDaniel extends LinearOpMode {

    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor motorTurbo;
    DcMotor middleRelease;
    DcMotor tapeMotor;
    Servo leftServo;
    Servo tapeServo;
    ColorSensor sensor;

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
        leftServo.setPosition(0.8);

        sensor = hardwareMap.colorSensor.get("colorSensor");


//        motorRight.setPower(1.0);
//        motorLeft.setPower(1.0);
//        double secondTime = System.currentTimeMillis();
//        while(true){
//            if(secondTime + 550) {
//                motorRight.setPower(0.0);
//                motorLeft.setPower(0.0);
//                break;
//            }
//        }
//        motorRight.setPower(1.0);
//        motorLeft.setPower(-1.0);
//        double secondTime = System.currentTimeMillis();
//        while(true){
//            if(secondTime + 530){
//                motorRight.setPower(0.0);
//                motorLeft.setPower(0.0);
//                break;
//            }
//        }
        
        long someTime = System.currentTimeMillis();
        if(sensor.red()>= 180 && sensor.blue() <= 60 && sensor.green() <= 60){
            leftServo.setPosition(1.0);
        }
        else{
            someTime += 2000;
            motorRight.setPower(1.0);
            motorLeft.setPower(1.0);
            if(System.currentTimeMillis()==someTime) {
                motorRight.setPower(0.0);
                motorLeft.setPower(0.0);
                }
            }
            leftServo.setPosition(1.0);
        }
    }
}