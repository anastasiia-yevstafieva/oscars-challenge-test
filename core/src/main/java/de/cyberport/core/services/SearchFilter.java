package de.cyberport.core.services;

import de.cyberport.core.dto.Film;

public interface SearchFilter {

    boolean matches(Film film);
}
