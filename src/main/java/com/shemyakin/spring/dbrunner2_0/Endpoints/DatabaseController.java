package com.shemyakin.spring.dbrunner2_0.Endpoints;

import com.shemyakin.spring.dbrunner2_0.DTO.DatabaseDTO;
import com.shemyakin.spring.dbrunner2_0.Entities.Database;
import com.shemyakin.spring.dbrunner2_0.Exceptions.DatabaseServiceException;
import com.shemyakin.spring.dbrunner2_0.Services.DatabaseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/db")
public class DatabaseController {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private DatabaseService databaseService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<DatabaseDTO> getDatabaseList() {
        return convertToDto(databaseService.getDBAvailableList());
    }

    @GetMapping(path = "/{db_name}",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public DatabaseDTO getDatabaseInfo(@PathVariable("db_name") String dbName) throws DatabaseServiceException {
        return convertToDto(databaseService.getDBInfoByName(dbName));
    }

    @PutMapping(path = "/sched/{db_name}")
    @ResponseStatus(HttpStatus.OK)
    public void setIsActiveDbAttribute(@PathVariable("db_name") String dbName) throws DatabaseServiceException {
        databaseService.updateDBIsActiveAttribute(dbName,"true");
    }

    @DeleteMapping(path = "/sched/{db_name}")
    @ResponseStatus(HttpStatus.OK)
    public void setIsNotActiveDbAttribute(@PathVariable("db_name") String dbName) throws DatabaseServiceException {
        databaseService.updateDBIsActiveAttribute(dbName,"false");
    }

    private DatabaseDTO convertToDto(Database database) {
        DatabaseDTO databaseDTO = modelMapper.map(database, DatabaseDTO.class);
//        try {
            databaseDTO.getNameFromFolder(database.getFolder());
//        }
        /*catch (ParseException pe){
            return databaseDTO;
        }*/

        return databaseDTO;
    }

    private List<DatabaseDTO> convertToDto(List<Database> databases) {
        List<DatabaseDTO> databasesDTO = databases
                .stream()
                .map(database -> modelMapper.map(database, DatabaseDTO.class))
                .collect(Collectors.toList());
        return databasesDTO;
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
