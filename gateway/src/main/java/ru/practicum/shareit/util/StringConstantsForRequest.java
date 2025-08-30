package ru.practicum.shareit.util;

public final class StringConstantsForRequest {
    private StringConstantsForRequest() {
    }

    public static final String NAME_HEADER_USER_ID = "X-Sharer-User-Id";
    public static final String PATH_VARIABLE_ID = "id";
    public static final String PATH_VARIABLE_ITEM_ID = "itemId";
    public static final String REQUEST_PARAM_TEXT = "text";
    public static final String PATH_VARIABLE_BOOKING_ID = "bookingId";
    public static final String PATH_VARIABLE_REQUEST_ID = "requestId";
    public static final String PATH_VARIABLE_SEARCH = "search";
    public static final String PATH_VARIABLE_COMMENT = "comment";
    public static final String REQUEST_PARAM_STATE = "state";
    public static final String REQUEST_PARAM_PAGE = "page";
    public static final String REQUEST_PARAM_COUNT = "count";
    public static final String REQUEST_PARAM_APPROVED = "approved";
    public static final String DEFAULT_VALUE_0 = "0";
    public static final String DEFAULT_VALUE_REQUEST_PARAM_COUNT = "32";
    public static final String DEFAULT_VALUE_REQUEST_PARAM_STATE = "ALL";
    public static final String NAME_PARAMETER_SERVER_ADDRESS = "${shareit-server.url}";
    public static final String REQUEST_MAPPING_PATH_USER = "/users";
    public static final String API_PREFIX_USER = "/users";
    public static final String REQUEST_MAPPING_PATH_ITEM = "/items";
    public static final String API_PREFIX_ITEM = "/items";
    public static final String SEPARATOR = "/";
}
