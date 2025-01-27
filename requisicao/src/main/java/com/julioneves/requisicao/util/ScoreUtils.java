package com.julioneves.requisicao.util;

public class ScoreUtils {

    private ScoreUtils() {
    }

    public static Float calcularScore(Float saldo) {
        return saldo != null ? saldo * 0.1f : 0f;
    }
}
