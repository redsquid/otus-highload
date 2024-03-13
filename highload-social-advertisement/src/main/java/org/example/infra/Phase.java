package org.example.infra;

public interface Phase {

    void execute() throws PhaseException;

    void cancel();
}
