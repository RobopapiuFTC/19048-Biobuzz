package org.firstinspires.ftc.teamcode.Systems;

import com.pedropathing.utils.Timer;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;








import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
public class Intake {

    public DcMotorEx intake, rearIntake;
    public Servo ramp, latch,flower;
    double intakeA, rearIntakeA;
    public Timer timer;

    public double direction = 1,ramppos;
    public boolean rampMove=false, on=false,latchopen, moveRamp=true,intaking=true,moving = true, ramping = true,ok=false,transfering=false;
    public double rampposition, latchclosedpos,lastdirection,closedpos=0.8, openpos=0.40,rampFront=0.3,rampBack = 0.61,flowerUp = 0.6, flowerDown = 0.2;

    public Intake(HardwareMap hwMap){
        intake= hwMap.get(DcMotorEx.class, "intake");
        rearIntake= hwMap.get(DcMotorEx.class, "rintake");
        ramp= hwMap.get(Servo.class, "ramp");
        latch=hwMap.get(Servo.class, "latch");
        flower = hwMap.get(Servo.class, "flower");
        flower.setPosition(flowerUp);
        latch.setPosition(closedpos);
        ramp.setPosition(rampFront);
        timer=new Timer();

    }
    public void loop(){
        setRamp();
        setMoveRamp();
        intake();
    }
    public void setPowerF(double power){
        intake.setPower(power);
    }
    public void setPowerR(double power){
        rearIntake.setPower(power);
    }
    public void setPower(double power){
        intake.setPower(power);
        rearIntake.setPower(power);
    }
    public double getFA(){
        return intake.getCurrent(CurrentUnit.AMPS);
    }
    public double getRA() {return rearIntake.getCurrent(CurrentUnit.AMPS);}
    public void latchclose(){
        latch.setPosition(closedpos);
    }
    public void latchopen(){
        latch.setPosition(openpos);
    }
    public void setMoveRamp(){
        if(!moving) return;
        if(getFA() > 3){
            moveRamp = false;
            ramp.setPosition(rampFront);
        }
        else if(getRA() > 3){
            moveRamp = false;
            ramp.setPosition(rampBack);
        }
    }
    public void setRamp(){
        if(!moveRamp) return;
        if(direction == 1) ramp.setPosition(rampFront);
        else ramp.setPosition(rampBack);
    }
    public void rotateRamp(){
        if(getRP() == rampFront) ramp.setPosition(rampBack);
        else if(getRP() == rampBack) ramp.setPosition(rampFront);
    }
    public void intake(){
        if(intaking){
            moving = true;
            latchclose();
            if (direction == 1) {
                setPowerF(1);
                setPowerR(0);
            } else {
                setPowerF(0);
                setPowerR(1);
            }
        }

    }
    public void Transfer(){
        intaking = false;
        transfering = true;
        ok = true;
        latchopen();
        Shooting();
        timer.reset();
    }
    public void Transfering(){
        if(!transfering)return;
        if(timer.seconds()>0.3 && timer.seconds()<0.4){
            IntStop();
            if(ok){
                rotateRamp();
                ok=false;
            }
        }
        if(timer.seconds()>0.4){
            intake.setPower(1);
            rearIntake.setPower(1);
        }
    }
    public void IntStop(){
        intake.setPower(0);
        rearIntake.setPower(0);
    }
    public void Shooting(){
        moving = false;
        moveRamp = false;
        intake.setPower(1);
        rearIntake.setPower(1);
    }
    public double getRP(){
        return ramp.getPosition();
    }
    public double getLP(){
        return latch.getPosition();
    }

}




