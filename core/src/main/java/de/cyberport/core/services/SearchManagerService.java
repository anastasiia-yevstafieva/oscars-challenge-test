package de.cyberport.core.services;

import de.cyberport.core.dto.Film;
import de.cyberport.core.dto.SearchDTO;
import de.cyberport.core.utils.Constants;

import java.util.Comparator;

public interface SearchManagerService {

    boolean isFilmMatchedToSearchParams(Film film, SearchDTO searchDTO);

    Comparator<Film> getFilmComparator(Constants.SUPPORTED_SORT_BY sortBy);
}
