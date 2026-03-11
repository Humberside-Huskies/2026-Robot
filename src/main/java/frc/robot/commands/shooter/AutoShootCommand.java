package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants.ShooterConstants;
import frc.robot.commands.LoggingCommand;
import frc.robot.subsystems.ShooterSubsystem;

public class AutoShootCommand extends LoggingCommand {

    private final ShooterSubsystem shooterSubsystem;
    private final Timer            timer = new Timer();

    public AutoShootCommand(ShooterSubsystem shooterSubsystem) {
        this.shooterSubsystem = shooterSubsystem;
        addRequirements(shooterSubsystem);
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();
    }

    @Override
    public void execute() {
        // begin shooting after 0.75s
        if (timer.get() > .75) {

            // run shooter motor
            shooterSubsystem.setShooterSpeed(ShooterConstants.MIN_SPEED);

            // Only kick when at speed
            if (shooterSubsystem.atSpeed()) {
                shooterSubsystem.setKickerSpeed(1);
            }
            else {
                shooterSubsystem.setKickerSpeed(0);
            }
        }
    }

    @Override
    public boolean isFinished() {
        // finished after 5 seconds.
        return timer.get() > 5;
    }

    @Override
    public void end(boolean interrupted) {
        shooterSubsystem.stop();
    }
}
