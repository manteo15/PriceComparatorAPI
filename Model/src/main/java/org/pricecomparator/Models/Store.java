package org.pricecomparator.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.List;

@Entity
public class Store {
    private @Id @GeneratedValue(strategy = GenerationType.AUTO) int id;
    private String name;

    public Store(String name) {
        this.name = name;
    }

    public Store(){}

    public Store(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
