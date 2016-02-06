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
 * Servo test teleOP
 * Uses same commands from normal teleop
 * Only for servos!
 */
public class ServoTestTeleOp extends OpMode {

    // position of the arm servo.
    double leftServoPosition = 0.7;
    int tapeServoArrayCount = 5;

    Servo leftServo;
    Servo tapeServo;
    Servo gateServo;
    boolean gateOpen = false;

    long nextTick = System.currentTimeMillis();
    long yetAnotherTick = System.currentTimeMillis();

    /**
     * Constructor
     */
    public ServoTestTeleOp() {

    }

    /*
     * Code to run when the op mode is first enabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init() {


        // Lifts/lowers metal bar
        leftServo = hardwareMap.servo.get("lservo");

        // Lifts/lowers tape
        tapeServo = hardwareMap.servo.get("tapeServo");
        leftServo.setPosition(0.4);

        gateServo = hardwareMap.servo.get("gateServo");

        tapeServo.setPosition(1.0);


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
		 *
		 *  ## Gamepad 2 Controls ##
		 * A button: raises and lowers platform
		 * X button: stops platform motor for sure
		 * B button: opens and closes gate
		 * Up/down dpad: manual platform controls
		 */

        leftServo.setPosition(leftServoPosition);

        // ## METAL ARM ##
        if (gamepad1.y) {
            if (System.currentTimeMillis() > nextTick) {
                if (leftServoPosition == 0.8) {
                    leftServoPosition = 0.4;
                } else {
                    leftServoPosition = 0.8;
                }
                //leftServoPosition += 0.05;
                nextTick = System.currentTimeMillis() + 200;
            }
        }

        // Make sure that it is 0<x<1 (bounds checking)
        if (leftServoPosition > 1.00) {
            leftServoPosition = 1.00;
        }
        if (leftServoPosition < 0.00) {
            leftServoPosition = 0.00;
        }


        // ## GATE SERVO ##
        if (System.currentTimeMillis() > yetAnotherTick) {
            if (gamepad2.b) {
                if (gateOpen) {
                    yetAnotherTick += 300;
                    gateServo.setPosition(1.0);
                    gateOpen = false;
                } else if (!gateOpen) {
                    yetAnotherTick += 300;
                    gateServo.setPosition(0.4);
                    gateOpen = false;
                }
            }
        }

        // ## TAPE RAISE/LOWER ##
        double[] tapeServoArray = {0.5, 0.6, 0.7, 0.8, 0.9, 1.0};
        if (gamepad1.dpad_up) {
            if (System.currentTimeMillis() >= nextTick) {
                if (tapeServoArrayCount < tapeServoArray.length - 1) {
                    tapeServoArrayCount++;
                    tapeServo.setPosition(tapeServoArray[tapeServoArrayCount]);
                    nextTick = System.currentTimeMillis() + 150;
                }
            }
        } else if (gamepad1.dpad_down) {
            if (System.currentTimeMillis() >= nextTick) {
                if (tapeServoArrayCount > 0) {
                    tapeServoArrayCount--;
                    tapeServo.setPosition(tapeServoArray[tapeServoArrayCount]);
                    nextTick = System.currentTimeMillis() + 150;
                }
            }
        }

    }
    @Override
    public void stop() {

    }
}
