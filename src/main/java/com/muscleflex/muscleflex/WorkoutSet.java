package com.muscleflex.muscleflex;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public  class WorkoutSet {
    private final StringProperty workoutName;
    private final IntegerProperty duration;
    private final IntegerProperty sets;
    private final IntegerProperty restPeriod;

    public WorkoutSet(String workoutName, int duration, int sets, int restPeriod) {
        this.workoutName = new SimpleStringProperty(workoutName);
        this.duration = new SimpleIntegerProperty(duration);
        this.sets = new SimpleIntegerProperty(sets);
        this.restPeriod = new SimpleIntegerProperty(restPeriod);
    }

    public StringProperty workoutNameProperty() {
        return workoutName;
    }

    public IntegerProperty durationProperty() {
        return duration;
    }

    public IntegerProperty setsProperty() {
        return sets;
    }

    public IntegerProperty restPeriodProperty() {
        return restPeriod;
    }
}
