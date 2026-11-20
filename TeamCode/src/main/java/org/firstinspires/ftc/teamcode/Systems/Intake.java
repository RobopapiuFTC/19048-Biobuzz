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
    Servo ramp, latch;
    double intakeA, rearIntakeA;
    Timer timer, shootingTimer;

    double direction,ramppos;
    boolean rampMove=false, on=false,latchopen, move;
    double rampposition, latchclosedpos,lastdirection,closedpos=0.87    , openpos=0.47;

    public Intake(HardwareMap hwMap){
        intake= hwMap.get(DcMotorEx.class, "intake");
        rearIntake= hwMap.get(DcMotorEx.class, "rintake");
        ramp= hwMap.get(Servo.class, "ramp");
        latch=hwMap.get(Servo.class, "latch");
        //rearIntake.setDirection(DcMotorSimple.Direction.REVERSE);
        latch.setPosition(closedpos);
        ramp.setPosition(0.3);
        timer=new Timer();

    }

    public void FrontSetPowerDir(double power) {
        intake.setPower(power);
    }

    public void RearSetPowerDir(double power) {
        rearIntake.setPower(-power);
    }

    public double getFA(){
        return intake.getCurrent(CurrentUnit.AMPS);
    }
    public double getRA() {return rearIntake.getCurrent(CurrentUnit.AMPS);}
    public void Int(double power, double direction) {
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


    public void Shooting(){
        intake.setPower(1);
        rearIntake.setPower(1);
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




