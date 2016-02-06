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

    Servo pushServo;

    @Override
    public void runOpMode(){
        pushServo = hardwareMap.servo.get("pushServo");
        try {
            waitForStart();
        }catch(InterruptedException e){

        }

        pushServo.setPosition(1.0);

    }


}
