package com.example.demo.mapper;

import com.example.demo.mapper.exception.FileParseException;
import com.example.demo.model.LeagueData;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Component
public class Mapper {
    private final ObjectMapper objectMapper;

    public Mapper(ObjectMapper objectMapper){
        this.objectMapper = objectMapper
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new JavaTimeModule());
    }

    public LeagueData mapToLeagueData(MultipartFile from) throws Exception {

        LeagueData mappedLeague;
        try(AutoCloseableFileWrapper wrapper = new AutoCloseableFileWrapper()){
            from.transferTo(wrapper.file);
            mappedLeague = objectMapper.readValue(wrapper.file, LeagueData.class);
        }catch (IOException e){
            e.printStackTrace();
            throw new FileParseException("Something went wrong with input file: " + from.getName());
        }

        return mappedLeague;
    }

    private static class AutoCloseableFileWrapper implements AutoCloseable{
        private final File file;

        public AutoCloseableFileWrapper() throws IOException{
            file = Files.createTempFile("temp" + Thread.currentThread().getId(), "").toFile();
        }

        @Override
        public void close() throws Exception {
            Files.delete(file.toPath());

        }
    }
}
