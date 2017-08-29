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

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="Blue Shooting", group="Autonomous")
//@Disabled
public class BlueShooting extends LinearOpMode implements SensorEventListener{

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    DcMotor front;
    DcMotor back;
    DcMotor left;
    DcMotor right;

    DcMotor hopper;
    DcMotor shooter;

    Servo gate;
    boolean moving;
    double time;
    int target;

    ColorSensor cSensor;

    // gyro sensor stuff
    SensorManager sensorManager;
    Sensor sensor;
    SensorEvent rawData;
    double timer;
    double rotations;
    boolean turning;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("test", "Initialized");
        telemetry.update();
        front = hardwareMap.dcMotor.get("front");
        back = hardwareMap.dcMotor.get("back");
        left = hardwareMap.dcMotor.get("left");
        right = hardwareMap.dcMotor.get("right");
        right.setDirection(DcMotorSimple.Direction.REVERSE);
        back.setDirection(DcMotorSimple.Direction.REVERSE);

        hopper = hardwareMap.dcMotor.get("hopper");
        shooter = hardwareMap.dcMotor.get("shooter");

        gate = hardwareMap.servo.get("gate");
        gate.setPosition(0.4);
        moving = false;
        time = 0;
        target = 0;

        cSensor = hardwareMap.colorSensor.get("cSensor");

        // gyro sensor stuff
        sensorManager = (SensorManager) hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        double left = 1;
        double right = 1;

        if(opModeIsActive()) {
            shoot(2);
//            eastWest(.17f,0.8,2000, left, right, 1);
//            northSouth(1.1f, 0.75, 4000, left, right, 1);
//            turn(true, 360, 0.7, 4000);
//            eastWest(.96f,0.8,2000, left, right, 1);
//            northSouthBlue(0.22f, 0.5, 1700, left, right, 1, 2.5);
//            eastWest(0.1f,0.6,2000, left, right, 1);
//            eastWest(0.07f,0.6,2000, left, right, -1);
        }
    }

    void northSouthBlue(float meters, double power, int speed, double leftMultiplier, double rightMultiplier, int direction, double cutoff) {
        //direction 1 = right, direction -1 = left
        int blue = 2;

        int target = (int)(meters*4690.882533);
        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        double start = runtime.seconds();

        while(opModeIsActive() && (cSensor.blue() <= blue)) {
//            left.setMaxSpeed((int)(speed));
            left.setPower(power*direction);
//            right.setMaxSpeed((int)(speed));
            right.setPower(power*direction);
            telemetry.addData("Target", Math.abs(target));
            telemetry.addData("Left", Math.abs(left.getCurrentPosition()));
            telemetry.addData("Right", Math.abs(right.getCurrentPosition()));
            telemetry.addData("Blue", cSensor.blue());
            telemetry.update();

            if(runtime.seconds() > start + cutoff) {
                left.setPower(0.0);
                right.setPower(0.0);
            }

        }
        northSouth(.015f, 0.75, 4000, 1.1, 1, 1);
        left.setPower(0.0);
        right.setPower(0.0);
    }

    void northSouth(float meters, double power, int speed, double leftMultiplier, double rightMultiplier, int direction) {
        //direction 1 = right, direction -1 = left
        int target = (int)(meters*4690.882533);
        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        double speedL = (speed*leftMultiplier)/40;
        double speedR = (speed*rightMultiplier)/40;

        while(opModeIsActive() && ((Math.abs(left.getCurrentPosition())<target)||Math.abs(right.getCurrentPosition())<target)) {
            if(speedL < speed) {
                speedL += (speed*leftMultiplier)/400;
            }
            if(speedR < speed) {
                speedR += (speed*rightMultiplier)/400;
            }
//            left.setMaxSpeed((int)(speedL));
            left.setPower(power*direction);
//            right.setMaxSpeed((int)(speedR));
            right.setPower(power*direction);
        }
        left.setPower(0.0);
        right.setPower(0.0);
    }

    void eastWest(float meters, double power, int speed, double leftMultiplier, double rightMultiplier, int direction) {
        //direction 1 = right, direction -1 = left
        int target = (int)(meters*4690.882533);
        front.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        back.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        front.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        back.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        double speedL = (speed*leftMultiplier)/40;
        double speedR = (speed*rightMultiplier)/40;

        while(opModeIsActive() && ((Math.abs(front.getCurrentPosition())<target)||Math.abs(back.getCurrentPosition())<target)) {
            if(speedL < speed) {
                speedL += (speed*leftMultiplier)/400;
            }
            if(speedR < speed) {
                speedR += (speed*rightMultiplier)/400;
            }
//            front.setMaxSpeed((int)(speedL));
            front.setPower(power*direction);
//            back.setMaxSpeed((int)(speedR));
            back.setPower(power*direction);
        }
        front.setPower(0.0);
        back.setPower(0.0);
    }

    void shoot(int shots) {

        shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        target = shooter.getTargetPosition() + 3360;
        shooter.setTargetPosition(target);
        shooter.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        shooter.setPower(0.8);
        while(opModeIsActive() && shooter.isBusy());
        shooter.setPower(0.0);

        for(int i = 0; (i < shots-1) && opModeIsActive(); i++) {

            gate.setPosition(0.1);

            time = runtime.seconds();
            while((time +0.6 >= runtime.seconds())&&opModeIsActive());

            if(opModeIsActive()) {
                gate.setPosition(0.4);
                time = 0;
            }

            time = runtime.seconds();
            while((time +0.5 >= runtime.seconds())&&opModeIsActive());

            shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            target = shooter.getTargetPosition() + 3360;
            shooter.setTargetPosition(target);
            shooter.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            shooter.setPower(0.8);
            while(opModeIsActive() && shooter.isBusy());
            shooter.setPower(0.0);

        }
    }

    void turn(boolean turnLeft, int degrees, double power, int speed) {
        front.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        back.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        turning = true;
        double turningOffset = 1.8;

        while(turning && opModeIsActive()) {
            telemetry.addData("Rotations", rotations);
            telemetry.addData("Degrees", degrees);
            telemetry.update();
            if(runtime.milliseconds()-timer >=10 && opModeIsActive()) {
                rotations += (Math.abs(rawData.values[1])+0.015)/100*(57.2958+turningOffset);
                timer = runtime.milliseconds();
            }
            turning = rotations < degrees;
//            front.setMaxSpeed(speed);
//            back.setMaxSpeed(speed);
//            left.setMaxSpeed(speed);
//            right.setMaxSpeed(speed);
            if(turnLeft){
                front.setPower(-power);
                back.setPower(power);
                left.setPower(-power);
                right.setPower(power);
            }
            else {
                front.setPower(power);
                back.setPower(-power);
                left.setPower(power);
                right.setPower(-power);
            }

        }
        front.setPower(0.0);
        back.setPower(0.0);
        left.setPower(0.0);
        right.setPower(0.0);
//        front.setMaxSpeed(0);
//        back.setMaxSpeed(0);
//        left.setMaxSpeed(0);
//        right.setMaxSpeed(0);
    }

    public void wait(double seconds) {
        double start = runtime.seconds();
        while(runtime.seconds() < start+seconds) { }
    }

    @Override
    public void onSensorChanged(SensorEvent e) {
        this.rawData = e;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}