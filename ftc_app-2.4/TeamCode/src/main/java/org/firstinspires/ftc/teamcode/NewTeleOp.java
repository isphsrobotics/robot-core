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

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
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

@TeleOp(name="Main Teleop", group="Iterative Opmode")  // @Autonomous(...) is the other common choice
//@Disabled
public class NewTeleOp extends OpMode {

    DcMotor front;
    DcMotor back;
    DcMotor left;
    DcMotor right;

    DcMotor hopper;

    Servo gate;
    boolean moving;
    double time;

    private ElapsedTime runtime = new ElapsedTime();

    //region init()
    /*
     * Code to run when the op mode is first enabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init() {
        // Main motors (wheels) -- reverse one of them

        telemetry.addData("test", null);

        front = hardwareMap.dcMotor.get("front");
        back = hardwareMap.dcMotor.get("back");
        left = hardwareMap.dcMotor.get("left");
        right = hardwareMap.dcMotor.get("right");
        left.setDirection(DcMotorSimple.Direction.REVERSE);
        back.setDirection(DcMotorSimple.Direction.REVERSE);

        hopper = hardwareMap.dcMotor.get("hopper");

        gate = hardwareMap.servo.get("gate");
        gate.setPosition(0.4);
        moving = false;
        time = 0;

    }
    //endregion


    @Override
    public void loop() {

        float turn = gamepad1.left_stick_x;
        float northSouth = gamepad1.right_stick_y;
        float eastWest = gamepad1.right_stick_x;

        turn = Range.clip(turn, -1, 1);
        northSouth = Range.clip(northSouth, -1, 1);
        eastWest = Range.clip(eastWest, -1, 1);

        if(turn!=0 && (northSouth!=0 || eastWest!=0)) {
            front.setPower(eastWest);
            back.setPower(eastWest);
            left.setPower(northSouth-turn);
            right.setPower(northSouth+turn);
        }
        else if(turn!=0) {
            left.setPower(turn);
            front.setPower(turn);
            back.setPower(-turn);
            right.setPower(-turn);
        }
        else {
            left.setPower(northSouth);
            front.setPower(eastWest);
            back.setPower(eastWest);
            right.setPower(northSouth);
        }

        if(gamepad1.right_bumper) {
            hopper.setPower(0.8);
        }
        else if(gamepad1.left_bumper) {
            hopper.setPower(-0.8);
        }
        else {
            hopper.setPower(0);
        }

        if(gamepad1.a) {
            gate.setPosition(0.1);
            time = runtime.seconds();

        }

        if(gate.getPosition()==0.1 && time+0.35 <= runtime.seconds()) {
            gate.setPosition(0.4);
            time = 0;
        }

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