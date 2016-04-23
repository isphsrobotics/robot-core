package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Alex's single component class
 * Used to troubleshoot & bug test
 */
public class LinearComponentTest extends LinearOpMode {

    Servo holderServo;
    Servo climberArmRight;

    @Override
    public void runOpMode(){
        holderServo = hardwareMap.servo.get("holder");
        climberArmRight = hardwareMap.servo.get("armRight");
        try {
            waitForStart();
        }catch(InterruptedException e){

        }
        if (gamepad1.y) {
            holderServo.setPosition(1.0);
        }
        if (gamepad1.b) {
            climberArmRight.setPosition(0.5);
        }
    }


}
