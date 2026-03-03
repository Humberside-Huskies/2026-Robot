package frc.robot.commands.shooter;

import frc.robot.OperatorInput;
import frc.robot.commands.LoggingCommand;
import frc.robot.subsystems.ShooterSubsystem;

public class DefaultShooterCommand extends LoggingCommand {

    private final OperatorInput    operatorInput;
    private final ShooterSubsystem shooterSubsystem;

    public DefaultShooterCommand(OperatorInput operatorInput, ShooterSubsystem shooterSubsystem) {
        this.operatorInput    = operatorInput;
        this.shooterSubsystem = shooterSubsystem;

        this.addRequirements(shooterSubsystem);
    }

    public void init() {
        logCommandStart();
        shooterSubsystem.stop();
    }

    @Override
    public void execute() {

        // Set the shooter speed based on the left trigger
        shooterSubsystem.setShooterSpeed(operatorInput.getShooterSpeed());

        if (operatorInput.kickerOn()) {
            shooterSubsystem.setKickerSpeed(1);
        }
        else {
            shooterSubsystem.setKickerSpeed(0);
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        logCommandEnd(interrupted);

        shooterSubsystem.stop();
    }
}
