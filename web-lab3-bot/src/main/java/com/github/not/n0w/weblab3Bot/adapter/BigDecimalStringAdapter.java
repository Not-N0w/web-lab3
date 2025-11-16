package com.github.not.n0w.weblab3Bot.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalStringAdapter extends TypeAdapter<BigDecimal> {

    @Override
    public void write(JsonWriter out, BigDecimal value) throws IOException {
        out.value(value.toPlainString());
    }

    @Override
    public BigDecimal read(JsonReader in) {
        return null;
    }
}