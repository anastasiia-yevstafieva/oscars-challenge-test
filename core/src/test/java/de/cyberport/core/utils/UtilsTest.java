package de.cyberport.core.utils;

import static org.junit.jupiter.api.Assertions.*;

import de.cyberport.core.dto.Film;
import de.cyberport.core.dto.SearchDTO;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(AemContextExtension.class)
class UtilsTest {

    private static final String SOURCE_FILE_WITH_3_FILMS = "/oscars_3_films.json";
    private static final String SOURCE_FILE_WITH_NO_FILMS = "/oscars_empty.json";
    private static final String DESTINATION_PATH = "/content/oscars";
    private static final String RESOURCE_PATH = "/content/oscars";
    private static final Resource NULL_RESOURCE = null;
    private static final String PARAM_VALUE_NOT_INTEGER = "Title";
    private static final String EMPTY_STRING = "";
    private static final int EXPECTED_RESULT_3_FILMS = 3;
    private static final int EXPECTED_RESULT_NO_FILMS = 0;
    private static final String EXPECTED_FILM_TITLE1 = "Film title";
    private static final String EXPECTED_FILM_TITLE2 = "Film title2";
    private static final String EXPECTED_FILM_TITLE3 = "Film title3";
    private static final int EXPECTED_YEAR = 1960;
    private static final int EXPECTED_MIN_YEAR = 1956;
    private static final int EXPECTED_MAX_YEAR = 1999;
    private static final int EXPECTED_MIN_AWARDS = 2;
    private static final int EXPECTED_MAX_AWARDS = 11;
    private static final int EXPECTED_NOMINATIONS = 5;
    private static final int EXPECTED_LIMIT = 10;



    /** Tests for Utils.parseRootResourcetoFilmsCollection method*/

    @Test
    public void shouldReturnNullWhenResourceIsNull() {
        assertNull(Utils.parseRootResourcetoFilmsCollection(NULL_RESOURCE));
    }

    @Test
    public void shouldReturn3FilmsWhenSourceContains3Films(AemContext context) {
        context.load().json(SOURCE_FILE_WITH_3_FILMS, DESTINATION_PATH);
        context.currentResource(RESOURCE_PATH);

        List<Film> parsedFilmList = Utils.parseRootResourcetoFilmsCollection(context.currentResource());

        assertEquals(EXPECTED_RESULT_3_FILMS, parsedFilmList.size());
    }

    @Test
    public void shouldReturnEmptyCollectionWhenSourceIsEmpty(AemContext context) {
        context.load().json(SOURCE_FILE_WITH_NO_FILMS, DESTINATION_PATH);
        context.currentResource(RESOURCE_PATH);

        List<Film> parsedFilmList = Utils.parseRootResourcetoFilmsCollection(context.currentResource());

        assertEquals(EXPECTED_RESULT_NO_FILMS, parsedFilmList.size());
    }

    /** Tests for Utils.createSearchDTO method*/

    @Test
    public void shouldReturnEmptySearchDTO(AemContext context) {
        MockSlingHttpServletRequest request = context.request();

        final Map<String, Object> params = new HashMap<>();
        request.setParameterMap(params);

        SearchDTO searchDTO = Utils.createSearchDTO(request);

        assertEquals(0, searchDTO.getSearchFilters().size());
        assertNull(searchDTO.getTitle());
        assertNull(searchDTO.getYear());
        assertNull(searchDTO.getMinYear());
        assertNull(searchDTO.getMaxYear());
        assertNull(searchDTO.getMinAwards());
        assertNull(searchDTO.getMaxAwards());
        assertNull(searchDTO.getNominations());
        assertNull(searchDTO.isBestPicture());
        assertEquals(Constants.SUPPORTED_SORT_BY.TITLE, searchDTO.getSortBy());
        assertNull(searchDTO.getLimit());
    }

    @Test
    public void shouldReturnSearchDTOWithSentParameters(AemContext context) {
        MockSlingHttpServletRequest request = context.request();

        final Map<String, Object> params = new HashMap<>();
        params.put("title", EXPECTED_FILM_TITLE1);
        params.put("year", "1960");
        params.put("minYear", "1956");
        params.put("maxYear", "1999");
        params.put("minAwards", "2");
        params.put("maxAwards", "11");
        params.put("nominations", "5");
        params.put("isBestPicture", "True");
        params.put("sortBy", "year");
        params.put("limit", "10");
        request.setParameterMap(params);

        SearchDTO searchDTO = Utils.createSearchDTO(request);

        assertEquals(8, searchDTO.getSearchFilters().size());
        assertEquals(EXPECTED_FILM_TITLE1, searchDTO.getTitle());
        assertEquals(EXPECTED_YEAR, searchDTO.getYear());
        assertEquals(EXPECTED_MIN_YEAR, searchDTO.getMinYear());
        assertEquals(EXPECTED_MAX_YEAR, searchDTO.getMaxYear());
        assertEquals(EXPECTED_MIN_AWARDS, searchDTO.getMinAwards());
        assertEquals(EXPECTED_MAX_AWARDS, searchDTO.getMaxAwards());
        assertEquals(EXPECTED_NOMINATIONS, searchDTO.getNominations());
        assertTrue(searchDTO.isBestPicture());
        assertEquals(Constants.SUPPORTED_SORT_BY.YEAR, searchDTO.getSortBy());
        assertEquals(EXPECTED_LIMIT, searchDTO.getLimit());
    }

    @Test
    public void shouldSetSortByToTitleByDefault(AemContext context) {
        MockSlingHttpServletRequest request = context.request();

        final Map<String, Object> params = new HashMap<>();
        request.setParameterMap(params);

        SearchDTO searchDTO = Utils.createSearchDTO(request);

        assertEquals(Constants.SUPPORTED_SORT_BY.TITLE, searchDTO.getSortBy());
    }

    @Test
    public void shouldSetLastRequestParameterValue(AemContext context) {
        MockSlingHttpServletRequest request = context.request();

        final Map<String, Object> params = new HashMap<>();
        params.put("title", EXPECTED_FILM_TITLE1);
        params.put("title", EXPECTED_FILM_TITLE2);
        params.put("title", EXPECTED_FILM_TITLE3);
        request.setParameterMap(params);

        SearchDTO searchDTO = Utils.createSearchDTO(request);

        assertEquals(1, searchDTO.getSearchFilters().size());
        assertEquals(EXPECTED_FILM_TITLE3, searchDTO.getTitle());
    }

    @Test
    public void shouldReturnEmptySearchDTOWhenRequestHasUnsupportedFilters(AemContext context) {
        MockSlingHttpServletRequest request = context.request();

        final Map<String, Object> params = new HashMap<>();
        params.put("count", "3");
        params.put("lang", "en");
        request.setParameterMap(params);

        SearchDTO searchDTO = Utils.createSearchDTO(request);

        assertEquals(0, searchDTO.getSearchFilters().size());
        assertNull(searchDTO.getTitle());
        assertNull(searchDTO.getYear());
        assertNull(searchDTO.getMinYear());
        assertNull(searchDTO.getMaxYear());
        assertNull(searchDTO.getMinAwards());
        assertNull(searchDTO.getMaxAwards());
        assertNull(searchDTO.getNominations());
        assertNull(searchDTO.isBestPicture());
        assertEquals(Constants.SUPPORTED_SORT_BY.TITLE, searchDTO.getSortBy());
        assertNull(searchDTO.getLimit());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenParameterNameIsEmpty(AemContext context) {
        MockSlingHttpServletRequest request = context.request();

        final Map<String, Object> params = new HashMap<>();
        params.put(EMPTY_STRING, EXPECTED_FILM_TITLE1);
        request.setParameterMap(params);

        assertThrows(IllegalArgumentException.class, () -> Utils.createSearchDTO(request));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenValueIsNotInteger(AemContext context) {
        MockSlingHttpServletRequest request = context.request();

        final Map<String, Object> params = new HashMap<>();
        params.put("year", PARAM_VALUE_NOT_INTEGER);
        request.setParameterMap(params);

        assertThrows(IllegalArgumentException.class, () -> Utils.createSearchDTO(request));
    }

}