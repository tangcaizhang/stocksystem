package zzh.project.stocksystem.bean;

import java.io.Serializable;

public class AccessToken implements Serializable{
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
