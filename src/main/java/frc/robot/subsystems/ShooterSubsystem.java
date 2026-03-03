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

public class ShooterSubsystem extends SubsystemBase {

    // The motors on the left side of the drive.
    private final SparkMax        shooterMotor   = new SparkMax(42,
        MotorType.kBrushless);
    private final SparkMax        kickerMotor    = new SparkMax(34,
        MotorType.kBrushless);

    // Encoders
    private final RelativeEncoder shooterEncoder = shooterMotor.getEncoder();
    private final RelativeEncoder kickerEncoder  = kickerMotor.getEncoder();

    /** Creates a new DriveSubsystem. */
    public ShooterSubsystem() {

        /*
         * Configure Motors
         */
        SparkMaxConfig config = new SparkMaxConfig();
        config.inverted(false)
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
    public void setShooterSpeed(double shooterSpeed) {
        shooterMotor.set(shooterSpeed);
    }

    public double getShooterSpeed() {
        return Math.round(shooterEncoder.getVelocity());
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