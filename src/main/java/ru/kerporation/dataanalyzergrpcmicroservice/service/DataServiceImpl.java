package ru.kerporation.dataanalyzergrpcmicroservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kerporation.dataanalyzergrpcmicroservice.model.Data;
import ru.kerporation.dataanalyzergrpcmicroservice.repository.DataRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {

    private final DataRepository dataRepository;

    @Override
    public void handle(final Data data) {
        log.info("Data object {} was saved", data);
        dataRepository.save(data);
    }

    @Override
    @Transactional
    public List<Data> getWithBatch(final long batchSize) {
        final List<Data> data = dataRepository.findAllWithOffset(batchSize);
        if (!data.isEmpty()) {
            dataRepository.incrementOffset(Long.min(batchSize, data.size()));
        }
        return data;
    }
}
