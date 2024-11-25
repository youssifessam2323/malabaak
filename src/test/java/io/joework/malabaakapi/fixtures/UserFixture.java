package io.joework.malabaakapi.fixtures;

import io.joework.malabaakapi.JsonTypes;
import io.joework.malabaakapi.model.User;
import io.joework.malabaakapi.model.dto.SignupRequest;


import static io.joework.malabaakapi.TestUtils.jsonParserByFile;

public class UserFixture {

    public static User getUserEntityNewRecord() {
        return jsonParserByFile(JsonTypes.ENTITY, "USER_ENTITY_NEW_RECORD", User.class);
    }

    public static SignupRequest getValidSignupRequest() {
        return jsonParserByFile(JsonTypes.DTO, "SIGNUP_REQUEST_VALID_JSON", SignupRequest.class);
    }

}
