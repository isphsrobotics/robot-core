/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * TeleOp Mode
 * Enables control of the robot via the gamepad
 */
public class TeleOp extends OpMode {

    // position of the arm servo.
    double leftServoPosition = 0.7;
    double tapePosition = 0.0;
    boolean platUp = true;


    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor motorTurbo;
    DcMotor middleRelease;
    DcMotor tapeMotor;
    //TODO: DEFINE PLATFORM MOTOR ON PHONE
    DcMotor platformMotor;
    Servo leftServo;
    Servo tapeServo;


    long nextTick = System.currentTimeMillis();
    long anotherTick = System.currentTimeMillis();

    /**
     * Constructor
     */
    public TeleOp() {

    }

    /*
     * Code to run when the op mode is first enabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init() {

        // Main motors (wheels) -- reverse one of them
        motorLeft = hardwareMap.dcMotor.get("mRight");
        motorRight = hardwareMap.dcMotor.get("mLeft");
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
        leftServo.setPosition(0.4 );


    }


    @Override
    public void loop() {

		/*
         * ## Gamepad 1 Controls ##
		 * 
		 * Right joystick: right wheel
		 * Left joystick: left wheel
		 * Right bumper: turbo (full/none)
		 * Y button: increases leftServoPosition
		 * B button: decreases leftServoPosition
		 * A button: decreases release motor
		 * X button: increases release motor
		 * Left arrow: tapeMotor forward
		 * Right arrow: tapeMotor backward
		 * Up arrow: lift tape
		 * Down arrow: lower tape
		 */

        // Gets values from joysticks
        float right = gamepad1.right_stick_y;
        float left = gamepad1.left_stick_y;

        // clip the right/left values so that the values never exceed +/- 1
        right = Range.clip(right, -1, 1);
        left = Range.clip(left, (float) -1.0, (float) 1.0);

        // scale the joystick value with custom method to make it easier to control
        // the robot more precisely at slower speeds.
        right = (float) scaleInput(right);
        left = (float) scaleInput(left);

        // write values from vars to the motors
        motorRight.setPower(right);
        motorLeft.setPower(left);


        // update the position of the arm.
        if (gamepad1.right_bumper) {
            motorTurbo.setPower(-1.0);
        }else if (gamepad1.left_bumper) {
            motorTurbo.setPower(1.0);
        } else {
            motorTurbo.setPower(0.0);
        }

        leftServo.setPosition(leftServoPosition);


        if (gamepad1.y) {
            if(System.currentTimeMillis() > nextTick) {
                if(leftServoPosition==0.8){
                    leftServoPosition=0.4;
                } else {
                    leftServoPosition=0.8;
                }
                //leftServoPosition += 0.05;
                nextTick = System.currentTimeMillis()+200;
            }
        }
//platform movement
        if (gamepad2.a) {
            if(System.currentTimeMillis() >= anotherTick)
                if(!platUp) {
                    while (System.currentTimeMillis() > anotherTick) {
                        platformMotor.setPower(1.0);
                        anotherTick += 3000;
                    }
                    platUp = true;
                    platformMotor.setPower(0.0);
                } else if(platUp){
                    while(System.currentTimeMillis() >= anotherTick) {
                        platformMotor.setPower(-1.0);
                        anotherTick += 3000;
                    }
                    platUp = false;
                    platformMotor.setPower(0.0);
                }
        }


        // update the position of the claw
//        if (gamepad1.b) {
//            if(System.currentTimeMillis() > nextTick) {
//                leftServoPosition -= 0.05;
//                nextTick = System.currentTimeMillis()+10;
//            }
//        }



        if (leftServoPosition > 1.00) {
            leftServoPosition = 1.00;
        }

        if (leftServoPosition < 0.00) {
            leftServoPosition = 0.00;
        }
        if (gamepad1.a) {
            middleRelease.setPower(1.0);
        } else if (gamepad1.x) {
            middleRelease.setPower(-1.0);
        } else {
            middleRelease.setPower(0.0);
        }

        if (gamepad1.dpad_left) {
            tapeMotor.setPower(1.0);
        }else if (gamepad1.dpad_right) {

            tapeMotor.setPower(-1.0);
        } else {
            tapeMotor.setPower(0.0);
        }

//        if (gamepad1.dpad_up) {
//            if(System.currentTimeMillis() > nextTick) {
//                tapePosition += 0.05;
//                nextTick = System.currentTimeMillis()+10;
//            }
//
//        }
//        if (gamepad1.dpad_up) {
//            if(System.currentTimeMillis() > nextTick) {
//                tapePosition += 0.05;
//                nextTick = System.currentTimeMillis()+10;
//            }
//        }
//
//        if (tapePosition > 1.00) {
//            tapePosition = 1.00;
//        }
//        if (tapePosition < 0.00) {
//            tapePosition = 0.00;
//        }
//
//        tapeServo.setPosition(tapePosition);
//New system for tape position
        double[] tapeServoArray = {0.0, 0.1, 0.2, 0.3, 0.4, 0.5};
        int tapeServoArrayCount = 5;
        if (gamepad1.dpad_up) {
            if (System.currentTimeMillis() >= nextTick) {
                if (tapeServoArrayCount < tapeServoArray.length) {
                    tapeServoArrayCount ++;
                    tapePosition = tapeServoArray[tapeServoArrayCount];
                    nextTick = System.currentTimeMillis() + 150;
                }
            }
        }
        if (gamepad2.dpad_down) {
            nextTick = System.currentTimeMillis();
            if (System.currentTimeMillis() >= nextTick) {
                if (tapeServoArrayCount > 0) {
                    tapeServoArrayCount --;
                    tapePosition = tapeServoArray[tapeServoArrayCount];
                    nextTick += 150;
                }
            }
        }


        // TODO: Telemetry

		/*
         * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
//        telemetry.addData("Text", "*** Servos ***");
//        telemetry.addData("leftServo pwr",  "leftServo: " + String.format("%.2f", leftServoPosition));
//		telemetry.addData("tapePosition ",  "tapePosition: " + String.format("%.2f", tapePosition));
//		telemetry.addData("Motors","*** Motors ***");
//		telemetry.addData("leftMotor", "leftMotor: " + String.format("%.2f", motorLeft));
//		telemetry.addData("rightMotor", "rightMotor: " + String.format("%.2f",motorRight));
//		telemetry.addData("turbo", "turbo: " + String.format("%.2", motorTurbo));

    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {

    }


    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleInput(double dVal) {
        double[] scaleArray = {0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }



}
