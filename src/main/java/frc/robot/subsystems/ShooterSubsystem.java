package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;

public class ShooterSubsystem extends SubsystemBase {

    // The motors on the left side of the drive.
    private final SparkMax        shooterMotor   = new SparkMax(ShooterConstants.SHOOTER_MOTOR_CAN_ID,
        MotorType.kBrushless);
    private final SparkMax        kickerMotor    = new SparkMax(ShooterConstants.KICKER_MOTOR_CAN_ID,
        MotorType.kBrushless);

    // Encoders
    private final RelativeEncoder shooterEncoder = shooterMotor.getEncoder();
    private final RelativeEncoder kickerEncoder  = kickerMotor.getEncoder();

    // target rpm initialized as 0
    private double                targetRPM      = 0;

    /** Creates a new DriveSubsystem. */
    public ShooterSubsystem() {

        /*
         * Configure Motors
         */
        SparkMaxConfig config = new SparkMaxConfig();
        config.inverted(true)
            .idleMode(IdleMode.kBrake)
            .disableFollowerMode();
        shooterMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        /*
         * Configure Right Side Motors
         */
        config = new SparkMaxConfig();
        config.inverted(false)
            .idleMode(IdleMode.kBrake)
            .disableFollowerMode();
        kickerMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    /*
     * Shooter Motor routines
     */
    public void setShooterSpeed(double targetRPM) {
        this.targetRPM = targetRPM;
        double error = targetRPM - getShooterSpeed();
        SmartDashboard.putNumber("error", error);
        if (Math.abs(error) < 500) {
            shooterMotor.set(targetRPM / ShooterConstants.MAX_SPEED + .0003 * error);
        }
        else {
            shooterMotor.set(targetRPM / ShooterConstants.MAX_SPEED);
        }
    }

    public double getShooterSpeed() {
        return Math.round(shooterEncoder.getVelocity());
    }


    /** Check if shooter motors are at speed. Returns true if error is below threshold. */
    public boolean atSpeed() {
        return Math.abs(getShooterSpeed() - targetRPM) < 200;
    }


    /*
     * Kicker Motor routines
     */
    public void setKickerSpeed(double kickerSpeed) {
        kickerMotor.set(kickerSpeed);
    }

    public double getKickerSpeed() {
        return Math.round(kickerEncoder.getVelocity());
    }


    @Override
    public void periodic() {

        SmartDashboard.putNumber("Shooter Speed", getShooterSpeed());
        SmartDashboard.putNumber("Kicker Speed", getKickerSpeed());
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append(this.getClass().getSimpleName()).append(" : ")
            .append("Shooter Speed ").append(getShooterSpeed())
            .append(", Kicker Speed ").append(getKickerSpeed());

        return sb.toString();
    }

    public void stop() {
        shooterMotor.set(0);
        kickerMotor.set(0);
    }

}