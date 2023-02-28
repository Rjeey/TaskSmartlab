package com.example.demo.controller;

import com.example.demo.service.BuildTimeTableService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/league")
@AllArgsConstructor
public class BuildTimetableController {

    private BuildTimeTableService service;

    @PostMapping("/timetable/console")
    public void makingTimetableAndOutputToConsole(MultipartFile leagueFile) throws Exception {
        service.consoleOutput(service.buildTimeTable(leagueFile));
    }
}