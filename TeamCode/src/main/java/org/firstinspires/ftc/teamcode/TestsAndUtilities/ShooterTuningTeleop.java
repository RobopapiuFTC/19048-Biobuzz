
package org.firstinspires.ftc.teamcode.TestsAndUtilities;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Systems.Turret;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp(name = "Shooter Calibration", group = "Testing")
public class ShooterTuningTeleop extends OpMode {

    private DcMotorEx shooter1;
    DcMotorEx rIntake;
    private DcMotorEx shooter2,intake;
    private Servo hood, ramp;
    Turret t;
    Follower f;
    boolean intakeIsOn;
    private double motorPower = 0.7;
    private double hoodPosition = 0.4;

    private final double POWER_STEP = 0.05;
    private final double HOOD_STEP = 0.01;

    private boolean prevUp = false;
    private boolean prevDown = false;
    private boolean prevRight = false;
    private boolean prevLeft = false;
    public static Pose startPose = new Pose(93.7, 9.9, Math.toRadians(90));

    @Override
    public void init() {
        shooter1 = hardwareMap.get(DcMotorEx.class, "SS");
        shooter2 = hardwareMap.get(DcMotorEx.class, "SD");
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        rIntake = hardwareMap.get(DcMotorEx.class, "rintake");
        hood = hardwareMap.get(Servo.class, "SVD");

        t=new Turret(hardwareMap, true);
        f= Constants.create(hardwareMap);
        ramp=hardwareMap.get(Servo.class, "ramp");
        ramp.setPosition(0.3);

        shooter1.setDirection(DcMotorSimple.Direction.FORWARD);
        shooter2.setDirection(DcMotorSimple.Direction.REVERSE);
        hood.setDirection(Servo.Direction.REVERSE);

        shooter1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        shooter1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooter2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        hood.setPosition(hoodPosition);

        telemetry.addLine("Shooter Calibration Ready");
        telemetry.update();
        f.setPose(startPose);
        f.update();
    }

    @Override
    public void loop() {
        Pose Hive1B= new Pose(83.42,48.7,Math.toRadians(90));

        Pose RobotPose=f.pose();
        f.update();
        t.face(Hive1B, RobotPose);
        t.loop();

        if(gamepad1.leftBumperWasPressed()){
            intakeIsOn=!intakeIsOn;
            if(intakeIsOn){
                intake.setPower(1);
                rIntake.setPower(1);

            }else{
                intake.setPower(0);
                rIntake.setPower(0);
            }
        }


        // Motor power controls
        if (gamepad1.dpad_up && !prevUp) {
            motorPower += POWER_STEP;
        }

        if (gamepad1.dpad_down && !prevDown) {
            motorPower -= POWER_STEP;
        }

        // Hood position controls
        if (gamepad1.dpad_right && !prevRight) {
            hoodPosition += HOOD_STEP;
        }

        if (gamepad1.dpad_left && !prevLeft) {
            hoodPosition -= HOOD_STEP;
        }

        motorPower = Range.clip(motorPower, 0.0, 1.0);
        hoodPosition = Range.clip(hoodPosition, 0.0, 1.0);

        // Stop/reset controls
        if (gamepad1.a) {
            motorPower = 0.0;
        }

        if (gamepad1.b) {
            motorPower = 0.7;
            hoodPosition = 0.4;
        }

        // Apply values
        shooter1.setPower(motorPower);
        shooter2.setPower(motorPower);
        hood.setPosition(hoodPosition);

        // Read velocity
        double velocity1 = shooter1.getVelocity();
        double velocity2 = shooter2.getVelocity();

        double averageVelocity =
                (Math.abs(velocity1) + Math.abs(velocity2)) / 2.0;

        // Telemetry
        telemetry.addData("Motor Power", motorPower);
        telemetry.addData("Shooter 1 Velocity", velocity1);
        telemetry.addData("Shooter 2 Velocity", velocity2);
        telemetry.addData("Average Velocity", averageVelocity);
        telemetry.addData("Hood Position", hoodPosition);
        telemetry.addData("Distance from hive ", Math.hypot(Hive1B.x() - RobotPose.x(), Hive1B.y() - RobotPose.y()));

        telemetry.update();

        // Save button states
        prevUp = gamepad1.dpad_up;
        prevDown = gamepad1.dpad_down;
        prevRight = gamepad1.dpad_right;
        prevLeft = gamepad1.dpad_left;
    }

    @Override
    public void stop() {
        shooter1.setPower(0);
        shooter2.setPower(0);
    }
}
