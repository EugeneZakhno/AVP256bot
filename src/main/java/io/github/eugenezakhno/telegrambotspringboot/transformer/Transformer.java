package io.github.eugenezakhno.telegrambotspringboot.transformer;

public interface Transformer<FROM, TO> {
    TO transform(FROM chat);
}
