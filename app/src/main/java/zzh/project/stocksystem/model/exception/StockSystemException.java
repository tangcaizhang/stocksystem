package zzh.project.stocksystem.model.exception;

/**
 * 业务异常
 */
public class StockSystemException extends Exception {
    public StockSystemException() {
        super();
    }

    public StockSystemException(String msg) {
        super(msg);
    }

    @Override
    public String getMessage() {
        return super.getMessage() == null ? "" : super.getMessage();
    }
}
