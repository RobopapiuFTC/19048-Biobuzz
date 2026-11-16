package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion;

@TeleOp(name="testint")
public class testintakeramp extends OpMode
{
    Intake i;
    boolean l=false,tr=false, ltr=false,s=false;
    DcMotorEx shooter;

    @Override
    public void init(){
        i= new Intake(hardwareMap);
        i.latch.setPosition(0.57);
        shooter=hardwareMap.get(DcMotorEx.class,"shooter");

    }
    @Override
    public void  loop() {
        // Toggle ramp servo position with B button
        if (gamepad1.bWasPressed()) {
            ltr = !ltr;
            if (ltr) {
                i.ramp.setPosition(0.3); // Open
            } else {
                i.ramp.setPosition(0.6); // Close
            }
        }

        // Toggle intake motor with A button
        if (gamepad1.aWasPressed()) {
            tr = !tr;
            if (tr) {
                i.intake.setPower(1); // ON
            } else {
                i.intake.setPower(0); // OFF
            }
        }
        if(gamepad1.xWasPressed()){
            l=!l;
            if(l){
                i.latch.setPosition(0.8);
            }else{
                i.latch.setPosition(0.57);
            }

        }
        if(gamepad1.yWasPressed()){
            s=!s;
            if(s){
                shooter.setPower(1);
                i.intake.setPower(1);
            }else{
                shooter.setPower(0);
                i.intake.setPower(0);
            }

        }

        telemetry.addData("Motor ON (tr)", tr);
        telemetry.addData("Ramp Open (ltr)", ltr);
        telemetry.update();
    }
}