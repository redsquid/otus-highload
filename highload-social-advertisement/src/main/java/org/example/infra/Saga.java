package org.example.infra;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Saga {

    private final List<Phase> phases = new ArrayList<>();

    public void add(Phase phase) {
        phases.add(phase);
    }

    public void execute() throws PhaseException {
        int i = 0;
        try {
            for (; i < phases.size(); i++) {
                phases.get(i).execute();
            }
        } catch (PhaseException e) {
            log.error("Phase exception. Rollback", e);
            for (i = i - 1; i >= 0; i--) {
                phases.get(i).cancel();
            }
            throw e;
        }
    }
}
