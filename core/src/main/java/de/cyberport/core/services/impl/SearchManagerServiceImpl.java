package de.cyberport.core.services.impl;

import de.cyberport.core.dto.Film;
import de.cyberport.core.dto.SearchDTO;
import de.cyberport.core.services.SearchFilter;
import de.cyberport.core.services.SearchManagerService;
import de.cyberport.core.utils.Constants;
import org.osgi.service.component.annotations.Component;

import java.util.Collection;
import java.util.Comparator;

@Component(service = { SearchManagerService.class }, immediate = true)
public class SearchManagerServiceImpl implements SearchManagerService {

    public boolean isFilmMatchedToSearchParams(Film film, SearchDTO searchDTO) {
        Collection<SearchFilter> searchFilterList = searchDTO.getSearchFilters();
        for (SearchFilter filter : searchFilterList) {
            if (!filter.matches(film))
                return false;
        }

        return true;
    }

    public Comparator<Film> getFilmComparator(Constants.SUPPORTED_SORT_BY sortBy) {
        switch (sortBy) {
            case AWARDS:
                return Comparator.comparingLong(Film::getAwards);
            case NOMINATIONS:
                return Comparator.comparingLong(Film::getNominations);
            case YEAR:
                return Comparator.comparingLong(Film::getYear);
            default:
                return Comparator.comparing(Film::getTitle);
        }
    }
}