package org.firstinspires.ftc.teamcode;

import com.pedropathing.utils.Timer;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;








import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
public class Intake {

    DcMotorEx intake, rearIntake;
    //public DcMotorEx shooter;
    Servo ramp, latch;
    double intakeA, rearIntakeA;
    Timer timer;

    double direction,ramppos;
    boolean rampMove=false, on=false,latchopen, move;
    double rampposition, latchclosedpos,lastdirection,closedpos=0.57, openpos=0.8;

    public Intake(HardwareMap hwMap){
        intake= hwMap.get(DcMotorEx.class, "intake");
        rearIntake= hwMap.get(DcMotorEx.class, "rintake");
        ramp= hwMap.get(Servo.class, "ramp");
        latch=hwMap.get(Servo.class, "latch");
        latch.setPosition(closedpos);
        ramp.setPosition(0.29);
        timer=new Timer();

    }

    public void FrontSetPowerDir(double power,double direction) {
        intake.setPower(power*direction);
    }

    public void RearSetPowerDir(double power,double direction) {
        rearIntake.setPower(power * direction * -1);
    }

    public double getFA(){
        return intake.getCurrent(CurrentUnit.AMPS);
    }
    public double getRA() {return rearIntake.getCurrent(CurrentUnit.AMPS);}
    public void Int( double power, double direction) {
        if (direction != lastdirection) {
            timer.reset();
            lastdirection = direction;

        }
        if (direction == 1) {
            ramp.setPosition(0.29);
        } else {
            ramp.setPosition(0.61);
        }
        if (timer.seconds() > 1) {
            FrontSetPowerDir(power,direction);
            RearSetPowerDir(power, direction);
        }
        else{
            FrontSetPowerDir(0,0);
            RearSetPowerDir(0, 0);
        }
    }
   /*public void Int( double power, double direction, boolean dir)
       {
           if (dir) {
               FrontSetPowerDir(power,direction);
               RearSetPowerDir(power, direction);
               if (direction > 0) {
                   ramp.setPosition(0.29);
               }else {
                   ramp.setPosition(0.61);
               }
           }else {
               rampposition=ramp.getPosition();
               if(rampposition==0.29){
                   intake.setPower(1);
                   rearIntake.setPower(-1);
               }
               else{
                   intake.setPower(-1);
                   rearIntake.setPower(1);
               }
           }
       }*/

    public void IntStop(){
        intake.setPower(0);
        rearIntake.setPower(0);
    }

    public void latch(double direction, boolean on) {


        if (direction == 1) {
            if (on) {
                latch.setPosition(openpos);
            } else {
                latch.setPosition(closedpos);
            }
        } else {
            if (on) {
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




