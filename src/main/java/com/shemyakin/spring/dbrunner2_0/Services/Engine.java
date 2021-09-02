package com.shemyakin.spring.dbrunner2_0.Services;

import com.shemyakin.spring.dbrunner2_0.Entities.Database;
import org.apache.ibatis.jdbc.RuntimeSqlException;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

@Service
@Scope("singleton")
public class Engine {
    private static final Logger logger = LoggerFactory.getLogger(Engine.class);

    @Autowired
    private DatabaseService databaseService;

    private static Set<Database> processingDB = new HashSet<>();
    private static ExecutorService dbService = Executors.newCachedThreadPool();

    @Scheduled(fixedRate = 60000)
    private void CheckRun() {
        for (Database db: databaseService.getDBAvailableList()) {
            if (db.getFolder().listFiles((file) -> file.getName().endsWith(".sql")).length > 0) {
                if (!processingDB.contains(db) && db.getIsActive()){
                    //runEngine(db);
                    processingDB.add(db);
                    dbService.submit(() -> runEngine(db));
                }
            }
        }
    }

    public static boolean checkConnection(Database db){
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@" + db.getConnection(), db.getUsername(), db.getPassword())) {
            return true;
        }
        catch (SQLException sqle){
            return false;
        }
    }

    private static void runEngine(Database db) {
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@" + db.getConnection(), db.getUsername(), db.getPassword())) {
            File[] sqlFiles = db.getFolder().listFiles((file) ->
                    file.isFile() && file.getName().endsWith(".sql")
            );
            for (File sqlFile : sqlFiles) {
                logger.info("База данных <" + db.getName() + ">. Начата обработка файла " + sqlFile.getName());
                runSQL(conn, sqlFile, db);
            }
        } catch (SQLException sqle) {
            logger.error("База данных <" + db.getName() + ">. Ошибка запуска: " + sqle.getMessage());
            sqle.printStackTrace();
            processingDB.remove(db);
        }
    }

    private static void runSQL(Connection connection, File runFile, Database db) {
        try {
            ScriptRunner sr = new ScriptRunner(connection);
            sr.setStopOnError(true);
            sr.setSendFullScript(true);
            try (BufferedReader reader = new BufferedReader(new FileReader(runFile))) {
                sr.runScript(reader);
                logger.info("База данных <" + db.getName() + ">. Файл " + runFile.getName() + " успешно обработан");
                reader.close();
                runFile.renameTo(new File(db.getFolder() + "\\Success\\" + runFile.getName()));
                runFile.delete();
            }
            catch (IOException ioe){
                logger.warn("База данных <" + db.getName() + ">. Ошибка чтения файла " + runFile.getAbsolutePath());
            }
            catch (RuntimeSqlException rsqle){
                throw new SQLException(rsqle);
            }

        }
        catch (SQLException sqle) {
            if (sqle.getMessage().indexOf("ORA-04021") == -1) {
                logger.warn("База данных <" + db.getName() + ">. Ошибка выполнения файла! " + runFile.getName() + "\n\t" + sqle.getMessage().substring(sqle.getMessage().lastIndexOf(" Cause: ")));
                runFile.renameTo(new File(db.getFolder() + "\\Fail\\" + runFile.getName()));
                runFile.delete();
            } else {
                logger.warn("База данных <" + db.getName() + ">. Файл " + runFile.getName() + " содержит объект, который сейчас заблокирован.\n\t" + sqle.getMessage().substring(sqle.getMessage().lastIndexOf(" Cause: ")));
            }
        }
        finally {
            processingDB.remove(db);
        }
    }
}
