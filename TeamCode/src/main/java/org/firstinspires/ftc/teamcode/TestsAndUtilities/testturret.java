package org.firstinspires.ftc.teamcode.TestsAndUtilities;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Systems.Turret;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp(name="turret test")
public class testturret extends OpMode {
    Turret t;
    Follower f;
    boolean auto;
    Pose targetPose= new Pose(84.1, 47.85,0);
    Pose StartPose= new Pose(81.9, 24.1, Math.toRadians(90));
@Override
    public void init(){
    t=new Turret(hardwareMap, auto);
    f= Constants.create(hardwareMap);
    f.setPose(StartPose);
    f.update();

}
@Override
    public void loop(){
    f.update();
    t.loop();
    if(gamepad1.aWasPressed()){

        t.face(targetPose, f.pose());
    }

}





}
