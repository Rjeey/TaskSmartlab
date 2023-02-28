package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class TimeTableLeague {

    private Map<Integer, List<FootballPair>> matcherRound;
}
