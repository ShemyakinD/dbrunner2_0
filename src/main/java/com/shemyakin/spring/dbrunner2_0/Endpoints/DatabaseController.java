package com.shemyakin.spring.dbrunner2_0.Endpoints;

import com.shemyakin.spring.dbrunner2_0.DTO.DatabaseDTO;
import com.shemyakin.spring.dbrunner2_0.Entities.Database;
import com.shemyakin.spring.dbrunner2_0.Services.DatabaseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping(value = "/db")
public class DatabaseController {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private DatabaseService databaseService;

    @GetMapping(path = "/{db_name}",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public DatabaseDTO getDatabaseInfo(@PathVariable("db_name") String dbName) {
        return convertToDto(databaseService.getRunnableDBInfoByName(dbName));
    }

    private DatabaseDTO convertToDto(Database database) {
        DatabaseDTO databaseDTO = modelMapper.map(database, DatabaseDTO.class);
        try {
            databaseDTO.getNameFromFolder(database.getFolder());
        }
        catch (ParseException pe){
            return databaseDTO;
        }

        return databaseDTO;
    }

    private Database convertToEntity(DatabaseDTO postDto) throws ParseException {
        Database post = modelMapper.map(postDto, Database.class);

        if (postDto.getName() != null) {
            /*Database oldPost = postService.getPostById(postDto.getId());
            post.setRedditID(oldPost.getRedditID());
            post.setSent(oldPost.isSent());*/
        }
        return post;
    }
}
