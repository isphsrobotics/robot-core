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
 * <p>
 * Enables control of the robot via the gamepad
 */
public class K9TeleOp extends OpMode {
	
	// position of the arm servo.
	double leftServoPosition = 0.0;
	double arm1Position = 0.0;
	double arm2Position = 1.0;

	DcMotor motorRight;
	DcMotor motorLeft;
	DcMotor motorTurbo;
	DcMotor middleRelease;
	DcMotor armMotor;
	Servo arm1;
	Servo arm2;
	Servo leftServo;

	/**
	 * Constructor
	 */
	public K9TeleOp() {

	}

	/*
	 * Code to run when the op mode is first enabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
	 */
	@Override
	public void init() {



		motorRight = hardwareMap.dcMotor.get("mRight");
		motorLeft = hardwareMap.dcMotor.get("mLeft");
		motorLeft.setDirection(DcMotor.Direction.REVERSE);

		motorTurbo = hardwareMap.dcMotor.get("mMid");
		middleRelease = hardwareMap.dcMotor.get("mRelease");

		armMotor = hardwareMap.dcMotor.get("mArm");

		leftServo = hardwareMap.servo.get("lservo");

		arm1 = hardwareMap.servo.get("lowerServo");
		arm2 = hardwareMap.servo.get("upperServo");

	}


	@Override
	public void loop() {

		/*
		 * Gamepad 1
		 * 
		 * Gamepad 1 controls the motors via the left stick, and it controls the
		 * wrist/claw via the a,b, x, y buttons
		 */



		float right = gamepad1.right_stick_y;
		float left = gamepad1.left_stick_y;

		// clip the right/left values so that the values never exceed +/- 1
		right = Range.clip(right, -1, 1);
		left = Range.clip(left, -1, 1);

		// scale the joystick value to make it easier to control
		// the robot more precisely at slower speeds.
		right = (float)scaleInput(right);
		left =  (float)scaleInput(left);
		
		// write the values to the motors
		motorRight.setPower(right);
		motorLeft.setPower(left);




		// update the position of the arm.
		if (gamepad1.right_bumper) {
			motorTurbo.setPower(-1.0);
		}else{
			motorTurbo.setPower(0.0);
		}

		leftServo.setPosition(leftServoPosition);



	if (gamepad1.y) {
//			// if the Y button is pushed on gamepad1, decrease the position of
		// the arm servo.

		leftServoPosition +=0.05;
	}

	// update the position of the claw
	if (gamepad1.b) {
		leftServoPosition -= 0.05;
	}

        if(leftServoPosition > 1.00){
            leftServoPosition = 1.00;
        }

        if(leftServoPosition < 0.00){
            leftServoPosition = 0.00;
        }



		if(gamepad1.x){
			armMotor.setPower(0.8);
		}else{
			armMotor.setPower(0.0);
		}

//		if(gamepad1.a){
//			armMotor.setPower(-1.0);
//		}else{
//			armMotor.setPower(0.0);
//		}



		if(gamepad1.dpad_up){
			arm1Position += 0.0000005;
			arm2Position -= 0.0000005;
		}

		if(gamepad1.dpad_down){
			arm1Position -= 0.0000005;
			arm2Position += 0.0000005;
		}



		if(arm1Position>1.00){
			arm1Position = 1.00;
		}

		if(arm2Position>1.00){
			arm2Position = 1.00;
		}

		if(arm1Position<0.00){
			arm1Position = 0.00;
		}

		if(arm2Position<0.00){
			arm2Position = 0.00;
		}

		arm1.setPosition(arm1Position);
		arm2.setPosition(arm2Position);


//
////
////		if (gamepad1.b) {
////			clawPosition -= clawDelta;
////		}

        // clip the position values so that they never exceed their allowed range.
//        leftServoPosition = Range.clip(leftServoPosition, ARM_MIN_RANGE, ARM_MAX_RANGE);
//        clawPosition = Range.clip(clawPosition, CLAW_MIN_RANGE, CLAW_MAX_RANGE);
//
//		// write position values to the wrist and claw servo
//		arm.setPosition(leftServoPosition);
//		claw.setPosition(clawPosition);



		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
        telemetry.addData("Text", "*** Robot Data***");
//        telemetry.addData("arm", "arm:  " + String.format("%.2f", leftServoPosition));
//        telemetry.addData("claw", "claw:  " + String.format("%.2f", clawPosition));
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", left));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));

	}

	/*
	 * Code to run when the op mode is first disabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */
	@Override
	public void stop() {

	}

    	
	/*
	 * This method scales the joystick input so for low joystick values, the 
	 * scaled value is less than linear.  This is to make it easier to drive
	 * the robot more precisely at slower speeds.
	 */
	double scaleInput(double dVal)  {
		double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
				0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };
		
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

}
