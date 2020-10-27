package de.cyberport.core.servlets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.junit.jupiter.api.Assertions.*;

import de.cyberport.core.dto.Film;
import de.cyberport.core.dto.SearchDTO;
import de.cyberport.core.services.impl.SearchManagerServiceImpl;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author Vitalii Afonin
 */
@ExtendWith(AemContextExtension.class)
class OscarFilmContainerServletTest {

    private String FILM_CLASS_PACKAGE_NAME = "de.cyberport.core.dto";

    @Mock
    private SearchManagerServiceImpl searchManagerService;

    @InjectMocks
    private OscarFilmContainerServlet underTest = new OscarFilmContainerServlet();

    @BeforeEach
    void setUp(AemContext context) {
        MockSlingHttpServletRequest request = context.request();

        MockitoAnnotations.initMocks(this);
        context.addModelsForPackage(FILM_CLASS_PACKAGE_NAME);

        context.load().json("/oscars.json", "/content/oscars");
        context.currentResource("/content/oscars");

        request.setResource(context.currentResource());

        doCallRealMethod().when(searchManagerService).getFilmComparator(any());
        doCallRealMethod().when(searchManagerService).isFilmMatchedToSearchParams(any(Film.class), any(SearchDTO.class));
    }

    @Test
    void shouldReturnEmptyArrayWhenNoResultsBasedOnProvidedFilter(AemContext context) throws IOException {
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        final Map<String, Object> params = new HashMap<>();
        params.put("year", "2020");

        request.setParameterMap(params);

        underTest.doGet(request, response);

        assertEquals("application/json", response.getContentType());
        assertTrue(response.getOutputAsString().equals("{\"result\":[]}"));
    }

    @Test
    void shouldReturnAllFilmsWhenNotSupportedFilterIsProvided(AemContext context) throws IOException {
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        final Map<String, Object> params = new HashMap<>();
        params.put("lang", "en");

        request.setParameterMap(params);

        underTest.doGet(request, response);

        assertEquals("application/json", response.getContentType());
        assertTrue(response.getOutputAsString().contains("Zorba the Greek"));
        assertTrue(response.getOutputAsString().contains("Balance"));
        assertTrue(response.getOutputAsString().contains("Parasite"));
    }

    @Test
    void shouldReturnFilmsWhenTitleFilterIsProvided(AemContext context) throws IOException {
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        final Map<String, Object> params = new HashMap<>();
        params.put("title", "Ford v Ferrari");

        request.setParameterMap(params);

        underTest.doGet(request, response);

        assertEquals("application/json", response.getContentType());
        assertTrue(response.getOutputAsString().contains("{\"result\":[{\"title\":\"Ford v Ferrari\",\"year\":2019," +
                "\"awards\":2,\"nominations\":4,\"isBestPicture\":false,\"numberOfReferences\":1929}]}"));
    }

    @Test
    void shouldReturnFilmsWhenYearFilterIsProvided(AemContext context) throws IOException {
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        final Map<String, Object> params = new HashMap<>();
        params.put("year", "2019");

        request.setParameterMap(params);

        underTest.doGet(request, response);

        assertEquals("application/json", response.getContentType());
        assertTrue(response.getOutputAsString().contains("Parasite"));
        assertTrue(response.getOutputAsString().contains("Ford v Ferrari"));
        assertTrue(response.getOutputAsString().contains("1917"));
        assertFalse(response.getOutputAsString().contains("Black Panther"));
    }

    @Test
    void shouldReturnFilmsWhenMinYearFilterIsProvided(AemContext context) throws IOException {
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        final Map<String, Object> params = new HashMap<>();
        params.put("minYear", "2018");

        request.setParameterMap(params);

        underTest.doGet(request, response);

        assertEquals("application/json", response.getContentType());
        assertTrue(response.getOutputAsString().contains("Parasite"));
        assertTrue(response.getOutputAsString().contains("Spider-Man: Into the Spider-Verse"));
        assertFalse(response.getOutputAsString().contains("Call Me by Your Name"));
    }

    @Test
    void shouldReturnFilmsWhenMaxYearFilterIsProvided(AemContext context) throws IOException {
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        final Map<String, Object> params = new HashMap<>();
        params.put("maxYear", "1929");

        request.setParameterMap(params);

        underTest.doGet(request, response);

        assertEquals("application/json", response.getContentType());
        assertTrue(response.getOutputAsString().contains("The Divorcee"));
        assertTrue(response.getOutputAsString().contains("In Old Arizona"));
        assertTrue(response.getOutputAsString().contains("The Dove"));
        assertFalse(response.getOutputAsString().contains("Sentinels of Silence"));

    }

    @Test
    void shouldReturnFilmsWhenMinAwardsFilterIsProvided(AemContext context) throws IOException {
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        final Map<String, Object> params = new HashMap<>();
        params.put("minAwards", "9");

        request.setParameterMap(params);

        underTest.doGet(request, response);

        assertEquals("application/json", response.getContentType());
        assertTrue(response.getOutputAsString().contains("Gigi"));
        assertTrue(response.getOutputAsString().contains("West Side Story"));
        assertTrue(response.getOutputAsString().contains("Ben-Hur"));
        assertFalse(response.getOutputAsString().contains("Battleground"));

    }

    @Test
    void shouldReturnFilmsWhenMaxAwardsFilterIsProvided(AemContext context) throws IOException {
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        final Map<String, Object> params = new HashMap<>();
        params.put("maxAwards", "3");

        request.setParameterMap(params);

        underTest.doGet(request, response);

        assertEquals("application/json", response.getContentType());
        assertTrue(response.getOutputAsString().contains("All That Money Can Buy"));
        assertTrue(response.getOutputAsString().contains("All Quiet on the Western Front"));
        assertTrue(response.getOutputAsString().contains("The Adventures of Robin Hood"));
        assertFalse(response.getOutputAsString().contains("Butch Cassidy and the Sundance Kid"));
    }

    @Test
    void shouldReturnFilmsWhenNominationsFilterIsProvided(AemContext context) throws IOException {
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        final Map<String, Object> params = new HashMap<>();
        params.put("nominations", "14");

        request.setParameterMap(params);

        underTest.doGet(request, response);

        assertEquals("application/json", response.getContentType());
        assertTrue(response.getOutputAsString().contains("Titanic"));
        assertTrue(response.getOutputAsString().contains("La La Land"));
        assertTrue(response.getOutputAsString().contains("All About Eve"));
        assertFalse(response.getOutputAsString().contains("Butch Cassidy and the Sundance Kid"));
    }

    @Test
    void shouldReturnFilmsWhenIsBestPictureFilterIsTrue(AemContext context) throws IOException {
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        final Map<String, Object> params = new HashMap<>();
        params.put("isBestPicture", "true");

        request.setParameterMap(params);

        underTest.doGet(request, response);

        assertEquals("application/json", response.getContentType());
        assertTrue(response.getOutputAsString().contains("Midnight Cowboy"));
        assertTrue(response.getOutputAsString().contains("Patton"));
        assertTrue(response.getOutputAsString().contains("The French Connection"));
        assertFalse(response.getOutputAsString().contains("This Tiny World"));
    }

    @Test
    void shouldReturnFilmsWhenIsBestPictureFilterIsFalse(AemContext context) throws IOException {
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        final Map<String, Object> params = new HashMap<>();
        params.put("isBestPicture", "false");

        request.setParameterMap(params);

        underTest.doGet(request, response);

        assertEquals("application/json", response.getContentType());
        assertTrue(response.getOutputAsString().contains("This Tiny World"));
        assertTrue(response.getOutputAsString().contains("Marjoe"));
        assertTrue(response.getOutputAsString().contains("Limelight"));
        assertFalse(response.getOutputAsString().contains("Midnight Cowboy"));
    }

    @Test
    void shouldReturnFilmsWhenSortByFilterIsTitle(AemContext context) throws IOException {
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        final Map<String, Object> params = new HashMap<>();
        params.put("sortBy", "title");
        params.put("year", "1972");

        request.setParameterMap(params);

        underTest.doGet(request, response);

        assertEquals("application/json", response.getContentType());
        assertTrue(response.getOutputAsString().contains("{\"title\":\"A Christmas Carol\",\"year\":1972," +
                "\"awards\":1,\"nominations\":1,\"isBestPicture\":false,\"numberOfReferences\":1767}," +
                "{\"title\":\"Butterflies Are Free\",\"year\":1972,\"awards\":1,\"nominations\":3," +
                "\"isBestPicture\":false,\"numberOfReferences\":902},{\"title\":\"Cabaret\",\"year\":1972," +
                "\"awards\":8"));
    }

    @Test
    void shouldReturnFilmsWhenSortByFilterIsYear(AemContext context) throws IOException {
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        final Map<String, Object> params = new HashMap<>();
        params.put("sortBy", "year");
        params.put("minAwards", "11");

        request.setParameterMap(params);

        underTest.doGet(request, response);

        assertEquals("application/json", response.getContentType());
        assertTrue(response.getOutputAsString().contains("\"title\":\"Ben-Hur\",\"year\":1959,\"awards\":11," +
                "\"nominations\":12,\"isBestPicture\":true,\"numberOfReferences\":1385},{\"title\":\"Titanic\"," +
                "\"year\":1997,\"awards\":11,\"nominations\":14,\"isBestPicture\":true,\"numberOfReferences\":6335}," +
                "{\"title\":\"The Lord of the Rings: The Return of the King\",\"year\":2003,\"awards\":11"));
    }

    @Test
    void shouldReturnFilmsWhenSortByFilterIsAwards(AemContext context) throws IOException {
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        final Map<String, Object> params = new HashMap<>();
        params.put("sortBy", "awards");
        params.put("year", "1998");

        request.setParameterMap(params);

        underTest.doGet(request, response);

        assertEquals("application/json", response.getContentType());
        assertTrue(response.getOutputAsString().contains("{\"title\":\"Elizabeth\",\"year\":1998,\"awards\":1," +
                "\"nominations\":7,\"isBestPicture\":false,\"numberOfReferences\":192}," +
                "{\"title\":\"Life Is Beautiful\",\"year\":1998,\"awards\":3"));
    }

    @Test
    void shouldReturnFilmsWhenSortByFilterIsNominations(AemContext context) throws IOException {
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        final Map<String, Object> params = new HashMap<>();
        params.put("sortBy", "nominations");
        params.put("year", "1998");

        request.setParameterMap(params);

        underTest.doGet(request, response);

        assertEquals("application/json", response.getContentType());
        assertTrue(response.getOutputAsString().contains("\"title\":\"The Personals\",\"year\":1998,\"awards\":1," +
                "\"nominations\":1,\"isBestPicture\":false,\"numberOfReferences\":958}," +
                "{\"title\":\"What Dreams May Come\",\"year\":1998,\"awards\":1,\"nominations\":2"));
    }

    @Test
    void shouldReturn3FilmsWhenLimitFilterIsThree(AemContext context) throws IOException {
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        final Map<String, Object> params = new HashMap<>();
        params.put("limit", "3");
        params.put("year", "1998");

        request.setParameterMap(params);

        underTest.doGet(request, response);

        assertEquals("application/json", response.getContentType());
        assertTrue(response.getOutputAsString().equals("{\"result\":[{\"title\":\"Affliction\",\"year\":1998," +
                "\"awards\":1,\"nominations\":2,\"isBestPicture\":false,\"numberOfReferences\":195}," +
                "{\"title\":\"Bunny\",\"year\":1998,\"awards\":1,\"nominations\":1,\"isBestPicture\":false," +
                "\"numberOfReferences\":1374},{\"title\":\"Election Night\",\"year\":1998,\"awards\":1," +
                "\"nominations\":1,\"isBestPicture\":false,\"numberOfReferences\":1920}]}"));
    }

    @Test
    void shouldReturnAllFilmsWhenLimitFilterIsNotSent(AemContext context) throws IOException {
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        final Map<String, Object> params = new HashMap<>();
        params.put("minAwards", "11");

        request.setParameterMap(params);

        underTest.doGet(request, response);

        assertEquals("application/json", response.getContentType());
        assertTrue(response.getOutputAsString().equals("{\"result\":[{\"title\":\"Ben-Hur\",\"year\":1959," +
                "\"awards\":11,\"nominations\":12,\"isBestPicture\":true,\"numberOfReferences\":1385}," +
                "{\"title\":\"The Lord of the Rings: The Return of the King\",\"year\":2003,\"awards\":11," +
                "\"nominations\":11,\"isBestPicture\":true,\"numberOfReferences\":1877}," +
                "{\"title\":\"Titanic\",\"year\":1997,\"awards\":11,\"nominations\":14,\"isBestPicture\":true," +
                "\"numberOfReferences\":6335}]}"));
    }

    @Test
    void shouldReturnFilteredFilmsWhenSeveralFiltersSent(AemContext context) throws IOException {
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        final Map<String, Object> params = new HashMap<>();
        params.put("minAwards", "11");
        params.put("year", "1959");

        request.setParameterMap(params);

        underTest.doGet(request, response);

        assertEquals("application/json", response.getContentType());
        assertTrue(response.getOutputAsString().equals("{\"result\":[{\"title\":\"Ben-Hur\",\"year\":1959," +
                "\"awards\":11,\"nominations\":12,\"isBestPicture\":true,\"numberOfReferences\":1385}]}"));
    }

    @Test
    void shouldReturnFilmsWithNoPrimaryTypeAndResourceType(AemContext context) throws IOException {
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        final Map<String, Object> params = new HashMap<>();
        params.put("minAwards", "11");

        request.setParameterMap(params);

        underTest.doGet(request, response);

        assertEquals("application/json", response.getContentType());
        assertFalse(response.getOutputAsString().contains("jcr:primaryType"));
        assertFalse(response.getOutputAsString().contains("sling:resourceType"));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenFilterParameterValueIsInvalid(AemContext context) {
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        final Map<String, Object> params = new HashMap<>();
        params.put("year", "abcd");
        request.setParameterMap(params);

        assertThrows(IllegalArgumentException.class, () -> underTest.doGet(request, response));
    }
}
