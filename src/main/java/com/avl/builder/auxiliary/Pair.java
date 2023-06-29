package com.avl.builder.auxiliary;

public class Pair<T1, T2> {

    public T1 Key;

    public Pair(T1 b, T2 value2) {
        Key = b;
        Value = value2;

    }

    public T1 getKey() {
        return Key;
    }

    public void setKey(T1 key) {
        Key = key;
    }

    public T2 Value;

    public T2 getValue() {
        return Value;
    }

    public void setValue(T2 value) {
        Value = value;
    }
}
