
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


    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor motorTurboRight;
    DcMotor motorTurboLeft;
    Servo armServo;

    double armPosition;
    long nextTick = System.currentTimeMillis();
    int armServoArrayCount = 5;

    /**
     * Constructor
     */
    public TeleOp() {

    }

    //region init()
    /*
     * Code to run when the op mode is first enabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init() {

        // Main motors (wheels) -- reverse one of them
        motorLeft = hardwareMap.dcMotor.get("lMotor");
        motorRight = hardwareMap.dcMotor.get("rMotor");
        motorRight.setDirection(DcMotor.Direction.REVERSE);

        // Turbo motor -- the one in the middle
        // TODO: FINISH MAP FOR TURBO MOTORS
        motorTurboRight = hardwareMap.dcMotor.get("rTurbo");
        motorTurboLeft = hardwareMap.dcMotor.get("lTurbo");
        motorTurboRight.setDirection(DcMotor.Direction.REVERSE);

        // Lifts and lowers the middle turbo motor
        armServo = hardwareMap.servo.get("armServo");

        armPosition = 0.5;
        armServo.setPosition(armPosition);
    }
    //endregion


    @Override
    public void loop() {

		/*
         * ## Gamepad 1 Controls ##
		 * 
		 * TODO: Summarise controls
		 */

        //region WHEELS
        // ## WHEEL MOTORS ##
        // Gets values from joysticks
        float right1 = gamepad1.right_stick_y;
        float left1 = gamepad1.left_stick_y;

        // clip the right/left values so that the values never exceed +/- 1
        right1 = Range.clip(right1, -1, 1);
        left1 = Range.clip(left1, (float) -1.0, (float) 1.0);

        // scale the joystick value with custom method to make it easier to control
        // the robot more precisely at slower speeds.
        right1 = (float) scaleInput(right1);
        left1 = (float) scaleInput(left1);

        // write values from vars to the motors
        motorRight.setPower(right1);
        motorLeft.setPower(left1);
        //endregion

        //region TURBO
        // ## TURBO MOTOR ##
        // Gets values from joysticks
        float right2 = gamepad2.right_stick_y;
        float left2 = gamepad2.left_stick_y;

        // clip the right/left values so that the values never exceed +/- 1
        right2 = Range.clip(right2, -1, 1);
        left2 = Range.clip(left2, (float) -1.0, (float) 1.0);

        // scale the joystick value with custom method to make it easier to control
        // the robot more precisely at slower speeds.
        right2 = (float) scaleInput(right2);
        left2 = (float) scaleInput(left2);

        // write values from vars to the motors
        motorTurboRight.setPower(right2);
        motorTurboLeft.setPower(left2);
        //endregion

        //region ARM SERVO
        // ## TURBO RAISE/LOWER ##
        double[] armServoArray = {0.5, 0.6, 0.7, 0.8, 0.9, 1.0};
        if (gamepad1.dpad_up) {
            if (System.currentTimeMillis() >= nextTick) {
                if (armServoArrayCount < armServoArray.length - 1) {
                    armServoArrayCount++;
                    armServo.setPosition(armServoArray[armServoArrayCount]);
                    nextTick = System.currentTimeMillis() + 150;
                }
            }
        } else if (gamepad1.dpad_down) {
            if (System.currentTimeMillis() >= nextTick) {
                if (armServoArrayCount > 0) {
                    armServoArrayCount--;
                    armServo.setPosition(armServoArray[armServoArrayCount]);
                    nextTick = System.currentTimeMillis() + 150;
                }
            }
        }
        //endregion
    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {

    }
    //endregion


    //region ScaleInput()
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
    //endregion


}
