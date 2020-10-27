package de.cyberport.core.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import de.cyberport.core.dto.Film;
import de.cyberport.core.dto.SearchDTO;
import de.cyberport.core.services.SearchManagerService;
import de.cyberport.core.utils.Constants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SearchManagerServiceImplTest {

    private String FILM_TITLE_1 = "Film title1";
    private String FILM_TITLE_2 = "Film title2";
    private String FILM_TITLE_3 = "Film title3";
    private int YEAR_1 = 1995;
    private int YEAR_2 = 2010;
    private int YEAR_3 = 2005;
    private int NOMINATIONS_1 = 23;
    private int NOMINATIONS_2 = 5;
    private int NOMINATIONS_3 = 19;
    public int AWARDS_1 = 5;
    public int AWARDS_2 = 2;
    public int AWARDS_3 = 4;


    private SearchManagerService serviceUnderTest = new SearchManagerServiceImpl();

    @Mock
    private Film film1;

    @Mock
    private Film film2;

    @Mock
    private Film film3;

    @Mock
    private SearchDTO searchDTO;

    private List<Film> filmsList = new ArrayList<>();

    @BeforeAll
    void setUp() {
        MockitoAnnotations.initMocks(this);
        prepareFilmsCollectionForTest();
    }

    /** Tests for SearchManagerService.isFilmMatchedToSearchParams method */

    @Test
    public void shouldReturnTrueWhenFilmTitleMatchesSearchParams() {
        searchDTO = new SearchDTO();
        searchDTO.setTitle(FILM_TITLE_2);

        assertTrue(serviceUnderTest.isFilmMatchedToSearchParams(film1, searchDTO));
    }

    @Test
    public void shouldReturnFalseWhenFilmTitleNotMatchesSearchParams() {
        searchDTO = new SearchDTO();
        searchDTO.setTitle(FILM_TITLE_3);

        assertFalse(serviceUnderTest.isFilmMatchedToSearchParams(film1, searchDTO));
    }

    /** Tests for SearchManagerService.getFilmComparator method */

    @Test
    public void shouldReturnFilmComparatorByAwards() {
        Comparator<Film> comparator = serviceUnderTest.getFilmComparator(Constants.SUPPORTED_SORT_BY.AWARDS);

        List<Film> sortedList = filmsList.stream().sorted(comparator).collect(Collectors.toList());
        assertEquals(film2, sortedList.get(0));
        assertEquals(film3, sortedList.get(1));
        assertEquals(film1, sortedList.get(2));
    }

    @Test
    public void shouldReturnFilmComparatorByNominations() {
        Comparator<Film> comparator = serviceUnderTest.getFilmComparator(Constants.SUPPORTED_SORT_BY.NOMINATIONS);

        List<Film> sortedList = filmsList.stream().sorted(comparator).collect(Collectors.toList());
        assertEquals(film2, sortedList.get(0));
        assertEquals(film3, sortedList.get(1));
        assertEquals(film1, sortedList.get(2));
    }

    @Test
    public void shouldReturnFilmComparatorByYear() {
        Comparator<Film> comparator = serviceUnderTest.getFilmComparator(Constants.SUPPORTED_SORT_BY.YEAR);

        List<Film> sortedList = filmsList.stream().sorted(comparator).collect(Collectors.toList());
        assertEquals(film1, sortedList.get(0));
        assertEquals(film3, sortedList.get(1));
        assertEquals(film2, sortedList.get(2));
    }

    @Test
    public void shouldReturnFilmComparatorByTitleByDefault() {
        Comparator<Film> comparator = serviceUnderTest.getFilmComparator(Constants.SUPPORTED_SORT_BY.UNKNOWN);

        List<Film> sortedList = filmsList.stream().sorted(comparator).collect(Collectors.toList());
        assertEquals(film3, sortedList.get(0));
        assertEquals(film1, sortedList.get(1));
        assertEquals(film2, sortedList.get(2));
    }

    private void prepareFilmsCollectionForTest() {
        when(film1.getTitle()).thenReturn(FILM_TITLE_2);
        when(film2.getTitle()).thenReturn(FILM_TITLE_3);
        when(film3.getTitle()).thenReturn(FILM_TITLE_1);

        when(film1.getYear()).thenReturn(YEAR_1);
        when(film2.getYear()).thenReturn(YEAR_2);
        when(film3.getYear()).thenReturn(YEAR_3);

        when(film1.getNominations()).thenReturn(NOMINATIONS_1);
        when(film2.getNominations()).thenReturn(NOMINATIONS_2);
        when(film3.getNominations()).thenReturn(NOMINATIONS_3);

        when(film1.getAwards()).thenReturn(AWARDS_1);
        when(film2.getAwards()).thenReturn(AWARDS_2);
        when(film3.getAwards()).thenReturn(AWARDS_3);

        filmsList.add(film1);
        filmsList.add(film2);
        filmsList.add(film3);
    }
}