/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all iterative OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Main TeleOp", group="Iterative Opmode")  // @Autonomous(...) is the other common choice
//@Disabled
public class MainTeleOp extends OpMode {

    DcMotor motorRight;
    DcMotor motorLeft;

    DcMotor motorHopper;
    DcMotor motorLauncher;

    DcMotor motorExtender;

    boolean slow;
    int currentPosition;

    //Servo ballLoader;
    //double openPos;
    //double holdingPos;
    //double launchPos;
    private ElapsedTime runtime = new ElapsedTime();

    //region init()
    /*
     * Code to run when the op mode is first enabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init() {
        // Launcher encoder positions

        // Main motors (wheels) -- reverse one of them
        motorLeft = hardwareMap.dcMotor.get("lMotor");
        motorRight = hardwareMap.dcMotor.get("rMotor");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        motorHopper = hardwareMap.dcMotor.get("hMotor");

        motorLauncher = hardwareMap.dcMotor.get("launcher");
        //motorLauncher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //currentPosition = motorLauncher.getCurrentPosition();

        motorExtender = hardwareMap.dcMotor.get("extender");

        slow = false;

        //ballLoader = hardwareMap.servo.get("loader");
        //ballLoader.setPosition(openPos);
    }
    //endregion


    @Override
    public void loop() {

        currentPosition = motorLauncher.getCurrentPosition();

        //region WHEELS
        // ## WHEEL MOTORS ##
        // Gets values from joysticks
        float right1;
        float left1;

        if(slow) {
            right1 = gamepad1.right_stick_y * (float)0.5;
            left1 = gamepad1.left_stick_y * (float)0.5;
        }
        else{
            right1 = gamepad1.right_stick_y;
            left1 = gamepad1.left_stick_y;
        }


        // clip the right/left values so that the values never exceed +/- 1
        if(gamepad1.dpad_up) {
            slow = false;
        }
        else if(gamepad1.dpad_down) {
            slow = true;
        }

        right1 = Range.clip(right1, -1, 1);
        left1 = Range.clip(left1, (float) -1.0, (float) 1.0);

        // scale the joystick value with custom method to make it easier to control
        // the robot more precisely at slower speeds.
        right1 = (float) scaleInput(right1);
        left1 = (float) scaleInput(left1);

        // write values from vars to the motors
        motorRight.setPower(right1);
        motorLeft.setPower(left1);

        // activates hopper motors
        if(gamepad1.right_bumper) {
            motorHopper.setPower(0.6);
        }
        else if(gamepad1.left_bumper) {
            motorHopper.setPower(-0.6);
        }
        else {
            motorHopper.setPower(0.0);
        }

        // activates launcher motors
        if(gamepad2.y) {
            motorLauncher.setPower(0.7);
        }
        else {
            motorLauncher.setPower(0.0);
        }

        if(gamepad2.right_bumper) {
            motorExtender.setPower(0.5);
        }
        else if(gamepad2.left_bumper) {
            motorExtender.setPower(-0.5);
        }
        else {
            motorExtender.setPower(0.0);
        }

//        if(gamepad2.dpad_up) {
//            motorExtender.setPower(0.5);
//        }
//        else if(gamepad2.dpad_down) {
//            motorExtender.setPower(-0.5);
//        }
//        else {
//            motorExtender.setPower(0.0);
//        }

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