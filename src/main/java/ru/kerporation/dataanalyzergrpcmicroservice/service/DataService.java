package ru.kerporation.dataanalyzergrpcmicroservice.service;

import ru.kerporation.dataanalyzergrpcmicroservice.model.Data;

import java.util.List;

public interface DataService {
    void handle(final Data data);
    List<Data> getWithBatch(final long batchSize);
}