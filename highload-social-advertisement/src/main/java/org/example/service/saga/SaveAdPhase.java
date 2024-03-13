package org.example.service.saga;

import lombok.RequiredArgsConstructor;
import org.example.entity.Advertisement;
import org.example.infra.Phase;
import org.example.infra.PhaseException;
import org.example.repository.AdvertisementRepository;

@RequiredArgsConstructor
class SaveAdPhase implements Phase {

    private final AdSagaContext context;

    private final AdvertisementRepository adRepo;

    private final Advertisement ad;

    @Override
    public void execute() throws PhaseException {
        try {
            adRepo.save(ad, context.getPostId());
        } catch (Exception e) {
            //this phase doesn't roll back the saga
        }
    }

    @Override
    public void cancel() {
    }
}
