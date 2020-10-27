package de.cyberport.core.dto;

import java.util.List;

public class SearchResult {

    private List<Film> result;

    public SearchResult(List<Film> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "result=" + result +
                '}';
    }
}
