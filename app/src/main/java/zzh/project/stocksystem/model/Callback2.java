package zzh.project.stocksystem.model;

public interface Callback2<Data, Error> {
    void onSuccess(Data data);

    void onError(Error error);
}