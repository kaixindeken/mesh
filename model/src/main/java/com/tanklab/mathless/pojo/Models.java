package com.tanklab.mathless.pojo;

import javax.persistence.*;

public class Models {

    @Id private Long id;

    private String name;

    private String description;

    /** @return id */
    public Long getId() {
        return id;
    }

    /** @param id */
    public void setId(Long id) {
        this.id = id;
    }

    /** @return name */
    public String getName() {
        return name;
    }

    /** @param name */
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
