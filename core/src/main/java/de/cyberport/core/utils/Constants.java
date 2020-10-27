package de.cyberport.core.utils;

public final class Constants {

    public static final class SupportedRequestParams {
        public static final String TITLE = "title";
        public static final String YEAR = "year";
        public static final String MIN_YEAR = "minYear";
        public static final String MAX_YEAR = "maxYear";
        public static final String MIN_AWARDS = "minAwards";
        public static final String MAX_AWARDS = "maxAwards";
        public static final String NOMINATIONS = "nominations";
        public static final String IS_BEST_PICTURE = "isBestPicture";
        public static final String SORT_BY = "sortBy";
        public static final String LIMIT = "limit";
    }

    public enum SUPPORTED_SORT_BY {
        TITLE,
        YEAR,
        AWARDS,
        NOMINATIONS,
        UNKNOWN
    }

}
