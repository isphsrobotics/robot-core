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

    Servo servo;
    DcMotor motor;

    @Override
    public void runOpMode(){
        motor = hardwareMap.dcMotor.get("platformMotor");
        motor.setPower(0.0);

        try {
            waitForStart();
        }catch(InterruptedException e){

        }

        // ## MANUAL PLATFORM CONTROLS & FAILSAFE STOP ##
        if (gamepad1.x) {
            motor.setPower(0.0);
        } else if (gamepad2.dpad_down) {
            motor.setPower(-1.0);
        } else if (gamepad2.dpad_up) {
            motor.setPower(1.0);
        }

    }


}
