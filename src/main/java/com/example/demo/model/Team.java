package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Year;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@AllArgsConstructor
public class Team {

    private String name;
    @JsonAlias("founding_date")
    private Year year;
}
