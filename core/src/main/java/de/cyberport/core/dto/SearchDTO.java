package de.cyberport.core.dto;

import de.cyberport.core.services.SearchFilter;
import de.cyberport.core.utils.Constants;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SearchDTO {
    private String title;
    private Integer year;
    private Integer minYear;
    private Integer maxYear;
    private Integer minAwards;
    private Integer maxAwards;
    private Integer nominations;
    private Boolean isBestPicture;
    private Constants.SUPPORTED_SORT_BY sortBy = Constants.SUPPORTED_SORT_BY.TITLE;
    private Integer limit;

    private final Map<String, SearchFilter> searchFilters = new HashMap<>();

    private void updateSearchFilters(String key, SearchFilter filter) {
        if (filter != null) {
            searchFilters.put(key, filter);
        } else {
            searchFilters.remove(key);
        }
    }

    public Collection<SearchFilter> getSearchFilters() {
        return searchFilters.values();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;

        final SearchFilter filter = (title == null) ? null : (film) -> {
            return film.getTitle() != null && film.getTitle().equals(this.title);
        };

        updateSearchFilters(Constants.SupportedRequestParams.TITLE, filter);
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;

        final SearchFilter filter = (year == null) ? null : (film) -> {
            return film.getYear() == this.year;
        };

        updateSearchFilters(Constants.SupportedRequestParams.YEAR, filter);
    }

    public Integer getMinYear() {
        return minYear;
    }

    public void setMinYear(Integer minYear) {
        this.minYear = minYear;

        final SearchFilter filter = (minYear == null) ? null : (film) -> {
            return film.getYear() >= this.minYear;
        };

        updateSearchFilters(Constants.SupportedRequestParams.MIN_YEAR, filter);
    }

    public Integer getMaxYear() {
        return maxYear;
    }

    public void setMaxYear(Integer maxYear) {
        this.maxYear = maxYear;

        final SearchFilter filter = (maxYear == null) ? null : (film) -> {
            return film.getYear() <= this.maxYear;
        };

        updateSearchFilters(Constants.SupportedRequestParams.MAX_YEAR, filter);
    }

    public Integer getMinAwards() {
        return minAwards;
    }

    public void setMinAwards(Integer minAwards) throws IllegalArgumentException {
        if (minAwards != null && minAwards < 0) {
            throw new IllegalArgumentException("minAwards cannot be less than 0");
        }

        this.minAwards = minAwards;

        final SearchFilter filter = (minAwards == null) ? null : (film) -> {
            return film.getAwards() >= this.minAwards;
        };

        updateSearchFilters(Constants.SupportedRequestParams.MIN_AWARDS, filter);
    }

    public Integer getMaxAwards() {
        return maxAwards;
    }

    public void setMaxAwards(Integer maxAwards) throws IllegalArgumentException {
        if (maxAwards != null && maxAwards < 0) {
            throw new IllegalArgumentException("maxAwards cannot be less than 0");
        }

        this.maxAwards = maxAwards;

        final SearchFilter filter = (maxAwards == null) ? null : (film) -> {
            return film.getAwards() <= this.maxAwards;
        };

        updateSearchFilters(Constants.SupportedRequestParams.MAX_AWARDS, filter);
    }

    public Integer getNominations() {
        return nominations;
    }

    public void setNominations(Integer nominations) throws IllegalArgumentException {
        if (nominations != null && nominations < 0) {
            throw new IllegalArgumentException("nominations cannot be less than 0");
        }

        this.nominations = nominations;

        final SearchFilter filter = (nominations == null) ? null : (film) -> {
            return film.getNominations() == this.nominations;
        };

        updateSearchFilters(Constants.SupportedRequestParams.NOMINATIONS, filter);
    }

    public Boolean isBestPicture() {
        return isBestPicture;
    }

    public void setIsBestPicture(Boolean isBestPicture) throws IllegalArgumentException {
        this.isBestPicture = isBestPicture;

        final SearchFilter filter = (isBestPicture == null) ? null : (film) -> {
            return film.isBestPicture() == this.isBestPicture;
        };

        updateSearchFilters(Constants.SupportedRequestParams.IS_BEST_PICTURE, filter);
    }

    public Constants.SUPPORTED_SORT_BY getSortBy() {
        return sortBy;
    }

    public void setSortBy(Constants.SUPPORTED_SORT_BY sortBy) {
        this.sortBy = sortBy;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) throws IllegalArgumentException {
        if (limit != null && limit < 0) {
            throw new IllegalArgumentException("limit cannot be less than 1");
        }

        this.limit = limit;
    }
}
