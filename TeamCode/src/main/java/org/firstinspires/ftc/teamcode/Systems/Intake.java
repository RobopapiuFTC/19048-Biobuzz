package org.firstinspires.ftc.teamcode.Systems;

import com.pedropathing.utils.Timer;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;








import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
public class Intake {

    DcMotorEx intake, rearIntake;
    //public DcMotorEx shooter;
    Servo ramp, latch, flowerIntake;
    double intakeA, rearIntakeA;
    Timer timer, shootingTimer;

    public double direction;
    public boolean Flower=false,dir=true,LatchOpen;
    double closedpos=0.7    , openpos=0.47, flowerClosed=0.2;

    public Intake(HardwareMap hwMap){
        intake= hwMap.get(DcMotorEx.class, "intake");
        rearIntake= hwMap.get(DcMotorEx.class, "rintake");
        ramp= hwMap.get(Servo.class, "ramp");
        latch=hwMap.get(Servo.class, "latch");
        //rearIntake.setDirection(DcMotorSimple.Direction.REVERSE);
        latch.setPosition(0.5);
        ramp.setPosition(0.3);
        flowerIntake=hwMap.get(Servo.class, "flower");
        flowerIntake.setPosition(flowerClosed);
        timer=new Timer();

    }

    public void FrontSetPowerDir() {
        intake.setPower(1*direction);
    }

    public void RearSetPowerDir() {
        rearIntake.setPower(-1*direction);
    }

    public double getFA(){
        return intake.getCurrent(CurrentUnit.AMPS);
    }
    public double getRA() {return rearIntake.getCurrent(CurrentUnit.AMPS);}
    /*public void Int(double power, double direction, boolean latch) {
        if (direction != lastdirection) {
            timer.reset();
            lastdirection = direction;

        }
        if (direction == 1) {
            ramp.setPosition(0.3);
            latch(direction, false);

        } else {
            ramp.setPosition(0.61);
            latch(direction, false);
        }

            FrontSetPowerDir(1);
            RearSetPowerDir(1);
    }*/
   public void Int()
       {
           if (dir) {
               FrontSetPowerDir();
               RearSetPowerDir();
               if(getFA()>2.5||getRA()>2.5){
                   dir=false;
               }
           }else {
                if(getFA()>2.5){
                direction=1;
                FrontSetPowerDir();
                RearSetPowerDir();
                ramp.setPosition(0.3);
                }else if(getRA()>2.5){
                    direction=-1;
                    FrontSetPowerDir();
                    RearSetPowerDir();
                    ramp.setPosition(0.61);
                }
                if(getFA()<2.5&&getRA()<2.5){
                    dir=true;
                }

           }
       }

    public void IntStop(){
        intake.setPower(0);
        rearIntake.setPower(0);
    }
    public void flower(){
       if(Flower){
           flowerIntake.setPosition(0.64);

       }
       else{
           flowerIntake.setPosition(flowerClosed);
       }
    }

    public void Shooting(){
        intake.setPower(1);
        rearIntake.setPower(1);
    }


    public void latch() {

        if (direction == 1) {
            if (LatchOpen) {
                latch.setPosition(openpos);
            } else {
                latch.setPosition(closedpos);
            }
        } else {
            if (LatchOpen) {
                latch.setPosition(closedpos);
            } else {
                latch.setPosition(openpos);
            }
        }
    }
    public double getRP(){
        return ramp.getPosition();
    }
    public double getLP(){
        return latch.getPosition();
    }

}




