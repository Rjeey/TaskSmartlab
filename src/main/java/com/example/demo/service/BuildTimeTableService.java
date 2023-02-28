package com.example.demo.service;

import com.example.demo.mapper.Mapper;
import com.example.demo.model.FootballPair;
import com.example.demo.model.LeagueData;
import com.example.demo.model.Team;
import com.example.demo.model.TimeTableLeague;
import com.google.common.collect.Sets;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Log4j2
public class BuildTimeTableService {


    @Value("${smartlab.start-date}")
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime startDate;

    @Value("${smartlab.pause-between-round}")
    private Integer pauseBetweenRounds;
    @Value("${smartlab.pause-between-game}")
    private Integer pauseBetweenGames;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final Mapper mapper;

    public BuildTimeTableService(Mapper mapper) {
        this.mapper = mapper;
    }

    public TimeTableLeague buildTimeTable(MultipartFile file) throws Exception {
        LeagueData league = mapper.mapToLeagueData(file);
        List<FootballPair> firstRound = buildRound(league.getTeams());
        List<FootballPair> secondRound = buildSecondRound(firstRound);

        TimeTableLeague timeTable = new TimeTableLeague();
        timeTable.setMatcherRound(Map.of(1, firstRound, 2, secondRound));

        return timeTable;
    }


    private List<FootballPair> buildRound(Set<Team> teams) {
        Set<Set<Team>> teamPairing = Sets.combinations(teams, 2);
        return collectionMatchesFromPairings(teamPairing);
    }


    private List<FootballPair> collectionMatchesFromPairings(Set<Set<Team>> teamPairing) {

        LocalDateTime matchDate = startDate;

        List<FootballPair> matches = new ArrayList<>();
        List<Team> teams = new ArrayList<>();

        Queue<Set<Team>> pairsQueue  = new ArrayDeque<>(teamPairing);
        int i = 0;
        while (!pairsQueue.isEmpty()) {
            Set<Team> pairing = pairsQueue.poll();
            if(teams.stream().anyMatch(pairing::contains)){
                pairsQueue.add(pairing);
                i++;
            }else{
                Iterator<Team> pairingIterator = pairing.iterator();
                FootballPair match = new FootballPair()
                        .setDateMatch(matchDate)
                        .setFirstPlayer(pairingIterator.next())
                        .setSecondPlayer(pairingIterator.next());
                matches.add(match);
                teams.addAll(pairing);
                i=0;
            }
            if(i > pairsQueue.size()) {
                teams.clear();
                matchDate = matchDate.plusWeeks(pauseBetweenGames);
            }
        }
        return matches;
    }

    private List<FootballPair> buildSecondRound(List<FootballPair> firstRound) {
        long totalWeeks = pauseBetweenRounds + firstRound.size();
        return firstRound.stream()
                .map(pair -> new FootballPair()
                        .setDateMatch(pair.getDateMatch().plusWeeks(totalWeeks))
                        .setFirstPlayer(pair.getSecondPlayer())
                        .setSecondPlayer(pair.getFirstPlayer()))
                .collect(Collectors.toList());
    }

    public void consoleOutput(TimeTableLeague leagueTimeTable) {

        List<Integer> keys = new ArrayList<>(leagueTimeTable.getMatcherRound().keySet());
        Collections.sort(keys);
        for (Integer key : keys) {
            log.info("Round " + key);
            for (FootballPair pairMatch : leagueTimeTable.getMatcherRound().get(key)) {
                log.info(formatter.format(pairMatch.getDateMatch()) + ";" +
                        pairMatch.getFirstPlayer() + ";" +
                        pairMatch.getSecondPlayer());
            }

        }
    }
}
