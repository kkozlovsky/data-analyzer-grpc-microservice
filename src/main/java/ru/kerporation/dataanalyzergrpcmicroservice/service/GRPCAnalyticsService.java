package ru.kerporation.dataanalyzergrpcmicroservice.service;

import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.kerporation.dataanalyzergrpcmicroservice.model.Data;
import ru.kerporation.grpccommon.AnalyticsServerGrpc;
import ru.kerporation.grpccommon.GRPCAnalyticsRequest;
import ru.kerporation.grpccommon.GRPCData;
import ru.kerporation.grpccommon.MeasurementType;

import java.time.ZoneOffset;
import java.util.List;

@GrpcService
@Slf4j
@RequiredArgsConstructor
public class GRPCAnalyticsService extends AnalyticsServerGrpc.AnalyticsServerImplBase {

    private final DataService dataService;

    @Override
    public void askForData(final GRPCAnalyticsRequest request,
                           final StreamObserver<GRPCData> responseObserver) {
        final List<Data> data = dataService.getWithBatch(request.getBatchSize());
        for (Data d : data) {
            final GRPCData dataRequest = GRPCData.newBuilder()
                    .setSensorId(d.getSensorId())
                    .setTimestamp(Timestamp.newBuilder().setSeconds(d.getTimestamp().toEpochSecond(ZoneOffset.UTC)).build())
                    .setMeasurementType(MeasurementType.valueOf(d.getMeasurementType().name())).setMeasurement(d.getMeasurement())
                    .build();
            responseObserver.onNext(dataRequest);
        }
        log.info("Batch was sent.");
        responseObserver.onCompleted();
    }

}
