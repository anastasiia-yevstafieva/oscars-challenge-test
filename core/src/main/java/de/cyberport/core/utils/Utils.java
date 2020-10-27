package de.cyberport.core.utils;

import de.cyberport.core.dto.Film;
import de.cyberport.core.dto.SearchDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utils {

    public static List<Film> parseRootResourcetoFilmsCollection(Resource rootResource) {
        if (rootResource == null) {
            return null;
        }

        List<Film> fullFilmList = new ArrayList<>();

        for (Resource child : rootResource.getChildren()) {
            Film film = child.adaptTo(Film.class);
            fullFilmList.add(film);
        }

        return fullFilmList;
    }

    public static SearchDTO createSearchDTO(SlingHttpServletRequest req) {
        SearchDTO searchDTO = new SearchDTO();

        Map<String, String[]> requestParametersMap = req.getParameterMap();
        for (String parameterName : requestParametersMap.keySet()) {
            String[] parameterValues = requestParametersMap.get(parameterName);
            String parameterValue = parameterValues[parameterValues.length - 1];

            setSearchParam(searchDTO, parameterName, parameterValue);
        }

        return searchDTO;
    }

    private static SearchDTO setSearchParam(SearchDTO searchDTO, String parameterName, String parameterValue) {
        if (StringUtils.isBlank(parameterName))
            throw new IllegalArgumentException("Parameter " + parameterName + " cannot be null or empty");

        switch (parameterName) {
            case Constants.SupportedRequestParams.TITLE:
                searchDTO.setTitle(parameterValue);
                break;
            case Constants.SupportedRequestParams.YEAR:
                searchDTO.setYear(parseStrToInteger(parameterValue, parameterName));
                break;
            case Constants.SupportedRequestParams.MIN_YEAR:
                searchDTO.setMinYear(parseStrToInteger(parameterValue, parameterName));
                break;
            case Constants.SupportedRequestParams.MAX_YEAR:
                searchDTO.setMaxYear(parseStrToInteger(parameterValue, parameterName));
                break;
            case Constants.SupportedRequestParams.MIN_AWARDS:
                searchDTO.setMinAwards(parseStrToInteger(parameterValue, parameterName));
                break;
            case Constants.SupportedRequestParams.MAX_AWARDS:
                searchDTO.setMaxAwards(parseStrToInteger(parameterValue, parameterName));
                break;
            case Constants.SupportedRequestParams.NOMINATIONS:
                searchDTO.setNominations(parseStrToInteger(parameterValue, parameterName));
                break;
            case Constants.SupportedRequestParams.IS_BEST_PICTURE:
                final Boolean isBestPicture = (parameterValue != null)
                        ? Boolean.parseBoolean(parameterValue) : null;
                searchDTO.setIsBestPicture(isBestPicture);
                break;
            case Constants.SupportedRequestParams.SORT_BY:
                final Constants.SUPPORTED_SORT_BY sortBy = (parameterValue != null)
                        ? Constants.SUPPORTED_SORT_BY.valueOf(parameterValue.toUpperCase())
                        : Constants.SUPPORTED_SORT_BY.TITLE;
                searchDTO.setSortBy(sortBy);
                break;
            case Constants.SupportedRequestParams.LIMIT:
                searchDTO.setLimit(parseStrToInteger(parameterValue, parameterName));
                break;
            default:
                return searchDTO;
        }

        return searchDTO;
    }

    private static Integer parseStrToInteger(String parameterValue, String parameterName) throws IllegalArgumentException {
        if (parameterValue != null) {
            try {
                return Integer.parseInt(parameterValue);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Error with parsing to number of parameter " + parameterName);
            }
        }
        return null;
    }
}
