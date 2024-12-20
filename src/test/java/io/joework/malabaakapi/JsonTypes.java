package io.joework.malabaakapi;

import lombok.Getter;

@Getter
public enum JsonTypes {

    DTO("dto-json"),
    ENTITY("entity-json"),
    PAYLOAD("payload-json");

    private final String fileName;

    JsonTypes(String fileName) {
        this.fileName = fileName;
    }

}
