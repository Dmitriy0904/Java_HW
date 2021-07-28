package entity;

import java.time.Instant;

public class Operation {
    private Long id;
    private String name;
    private String description;
    private Category category;
    private Double value;
    private Instant date;

    //В MYSQL В ТАБЛИЦЕ ДОБАВИТЬ КОЛОНКУ С ДАТОЙ

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public Double getValue() {
        return value;
    }

    public Instant getDate() {
        return date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
