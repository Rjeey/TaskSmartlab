package com.example.demo.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class FootballPair {

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime dateMatch;

    private Team firstPlayer;
    private Team secondPlayer;
}
