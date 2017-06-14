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

    //main driving motors
    DcMotor front;
    DcMotor back;
    DcMotor left;
    DcMotor right;

    // shooting and lifting
    DcMotor hopper;
    DcMotor shooter;
    DcMotor liftL;
    DcMotor liftR;

    //servos
    Servo gate;
    Servo extendL;
    Servo extendR;

    //data
    boolean freemode;
    boolean moving;
    double time;
    int target;
    int ticks;

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

        telemetry.addData("Initialized", null);

        // main driving motors
        front = hardwareMap.dcMotor.get("front");
        back = hardwareMap.dcMotor.get("back");
        left = hardwareMap.dcMotor.get("left");
        right = hardwareMap.dcMotor.get("right");
        left.setDirection(DcMotorSimple.Direction.REVERSE);
        back.setDirection(DcMotorSimple.Direction.REVERSE);

        // shooting and lifting
        hopper = hardwareMap.dcMotor.get("hopper");
        shooter = hardwareMap.dcMotor.get("shooter");
        liftL = hardwareMap.dcMotor.get("liftL");
        liftR = hardwareMap.dcMotor.get("liftR");

        // servos
        gate = hardwareMap.servo.get("gate");
        extendL = hardwareMap.servo.get("extendL");
        extendR = hardwareMap.servo.get("extendR");
        gate.setPosition(0.4);
        extendL.setPosition(1.0);
        extendR.setPosition(0.0);

        // data
        moving = false;
        time = 0;
        target = 0;
        freemode = false;
        ticks = 0;

    }
    //endregion


    @Override
    public void loop() {
        // MAIN DRIVING MOTORS-------------------
        float turnR = gamepad1.right_trigger;
        float turnL = gamepad1.left_trigger;
        float northSouth = gamepad1.right_stick_y;
        float eastWest = gamepad1.left_stick_x;

        northSouth = Range.clip(northSouth, -1, 1);
        eastWest = Range.clip(eastWest, -1, 1);
        turnL = Range.clip(turnL, -1, 1);
        turnR = Range.clip(turnR, -1, 1);

        if(turnR!=0 && turnL==0) {
            left.setPower(-turnR);
            front.setPower(turnR);
            back.setPower(-turnR);
            right.setPower(turnR);
        }
        else if(turnR==0 && turnL!=0) {
            left.setPower(turnL);
            front.setPower(-turnL);
            back.setPower(turnL);
            right.setPower(-turnL);
        }
        else {
            left.setPower(northSouth);
            front.setPower(eastWest);
            back.setPower(eastWest);
            right.setPower(northSouth);
        }

        //EXTENDER--------------------------
        if(gamepad2.right_bumper) {
            liftR.setPower(0.8);
        }
        else if(gamepad2.right_trigger!=0) {
            liftR.setPower(-0.8);
        }
        else {
            liftR.setPower(0.0);
        }

        if(gamepad2.left_bumper) {
            liftL.setPower(-0.8);
        }
        else if(gamepad2.left_trigger!=0) {
            liftL.setPower(0.8);
        }
        else {
            liftL.setPower(0.0);
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




//        if(shooter.isBusy()) {
//
//        }
//        else {
//            if(gamepad2.b) {
//                shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//                target = shooter.getTargetPosition() + 3340;
//                shooter.setTargetPosition(target);
//                shooter.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//                shooter.setPower(0.8);
//            }
//            else {
//                shooter.setPower(0.0);
//            }
//        }


        if(gamepad2.y) {
            shooter.setPower(0.8);
        }
        else {
            shooter.setPower(0.0);
        }

//        if(gamepad2.start) {
//            ticks++;
//            if(ticks == 1) {
//                freemode = !freemode;
//                if(freemode) {
//                    shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//                }
//            }
//        }
//        else {
//            ticks = 0;
//        }

        if(gamepad1.a) {
            gate.setPosition(0.1);
            time = runtime.seconds();
        }

        if(gate.getPosition()==0.1 && time+0.35 <= runtime.seconds()) {
            gate.setPosition(0.4);
            time = 0;
        }

        if(gamepad2.x) {
            extendL.setPosition(0.0);
            extendR.setPosition(1.0);
        }

        if(gamepad2.a) {
            extendL.setPosition(0.5);
            extendR.setPosition(0.5);
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