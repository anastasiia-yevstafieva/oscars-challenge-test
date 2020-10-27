package de.cyberport.core.dto;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

@Model(adaptables = Resource.class)
public class Film {

    @Inject
    private String title;

    @Inject
    private int year;

    @Inject
    private int awards;

    @Inject
    private int nominations;

    @Inject
    private boolean isBestPicture;

    @Inject
    private int numberOfReferences;

    public Film() {

    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public int getAwards() {
        return awards;
    }

    public int getNominations() {
        return nominations;
    }

    public boolean isBestPicture() {
        return isBestPicture;
    }

    public int getNumberOfReferences() {
        return numberOfReferences;
    }

    @Override
    public String toString() {
        return "Film{" +
                "title='" + title + '\'' +
                ", year=" + year +
                ", awards=" + awards +
                ", nominations=" + nominations +
                ", isBestPicture=" + isBestPicture +
                ", numberOfReferences=" + numberOfReferences +
                '}';
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setAwards(int awards) {
        this.awards = awards;
    }

    public void setNominations(int nominations) {
        this.nominations = nominations;
    }

    public void setBestPicture(boolean bestPicture) {
        isBestPicture = bestPicture;
    }

    public void setNumberOfReferences(int numberOfReferences) {
        this.numberOfReferences = numberOfReferences;
    }
}
