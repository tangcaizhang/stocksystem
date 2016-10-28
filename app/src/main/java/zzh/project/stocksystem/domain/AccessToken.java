package zzh.project.stocksystem.domain;

public class AccessToken {
    public String accessToken;
    public long expiresIn;

    @Override
    public String toString() {
        return "AccessToken{" +
                "accessToken='" + accessToken + '\'' +
                ", expiresIn=" + expiresIn +
                '}';
    }
}
