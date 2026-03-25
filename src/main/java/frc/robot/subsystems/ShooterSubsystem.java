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
    private final SparkMax        shooterMotor    = new SparkMax(ShooterConstants.SHOOTER_MOTOR_CAN_ID,
        MotorType.kBrushless);
    private final SparkMax        shooterMotor2   = new SparkMax(ShooterConstants.SHOOTER_MOTOR_CAN_ID_B,
        MotorType.kBrushless);
    private final SparkMax        kickerMotor     = new SparkMax(ShooterConstants.KICKER_MOTOR_CAN_ID,
        MotorType.kBrushless);

    // Encoders
    private final RelativeEncoder shooterEncoder  = shooterMotor.getEncoder();
    private final RelativeEncoder shooterEncoder2 = shooterMotor2.getEncoder();
    private final RelativeEncoder kickerEncoder   = kickerMotor.getEncoder();

    // target rpm initialized as 0
    private double                targetRPM       = 0;

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

        config.inverted(true)
            .idleMode(IdleMode.kBrake)
            .disableFollowerMode();
        shooterMotor2.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

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

        double error  = targetRPM - getShooterSpeed();
        double error2 = targetRPM - getShooterSpeed2();
        SmartDashboard.putNumber("error", error);
        SmartDashboard.putNumber("error2", error2);

        if (Math.abs(error) < 500 && Math.abs(error2) < 500) {
            shooterMotor.set(targetRPM / ShooterConstants.MAX_SPEED + .0003 * error);
            shooterMotor2.set(targetRPM / ShooterConstants.MAX_SPEED + .0003 * error2);
        }

        else {
            shooterMotor.set(targetRPM / ShooterConstants.MAX_SPEED);
            shooterMotor2.set(targetRPM / ShooterConstants.MAX_SPEED);
        }
    }

    public double getShooterSpeed() {
        return Math.round(shooterEncoder.getVelocity());
    }


    public double getShooterSpeed2() {
        return Math.round(shooterEncoder2.getVelocity());
    }

    /** Check if shooter motors are at speed. Returns true if error is below threshold. */
    public boolean atSpeed() {
        return Math.abs(getShooterSpeed() - targetRPM) < 180 && Math.abs(getShooterSpeed2() - targetRPM) < 180;
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
        SmartDashboard.putNumber("Shooter 2 Speed", getShooterSpeed2());
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