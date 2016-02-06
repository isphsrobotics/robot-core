package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Richard's servo testing class
 * Moves arm servo up and down
 */
public class ServoTestLinear extends LinearOpMode {

    Servo servo;

    @Override
    public void runOpMode(){
        servo = hardwareMap.servo.get("lservo");

        try {
            waitForStart();
        }catch(InterruptedException e){

        }

        double time = System.currentTimeMillis() + 1500;
        servo.setPosition(0.5);
        while(System.currentTimeMillis() < time){}
        servo.setPosition(0.4);

    }


}
