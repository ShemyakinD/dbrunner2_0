package com.shemyakin.spring.dbrunner2_0.Services;

import org.apache.ibatis.jdbc.RuntimeSqlException;
import org.apache.ibatis.jdbc.ScriptRunner;
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
import java.util.List;
import java.util.logging.Level;

@Service
@Scope("singleton")
public class Engine {
   /* @Scheduled(fixedRate = 120000)
    private static void CheckRun() {
        List<Database> databasesList = XMLizer.getDBList();
        for (Database db: databasesList) {
            if (db.getFolder().listFiles((file) -> file.getName().endsWith(".sql")).length > 0) {
                if (!processingDB.contains(db) && db.getActive()){
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
                Loggator.execLog(Level.INFO, db.getName(), "Начата обработка файла " + sqlFile.getName());
                runSQL(conn, sqlFile, db);
            }
        } catch (SQLException sqle) {
            Loggator.execLog(Level.SEVERE, db.getName(), sqle.getMessage());
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
                Loggator.execLog(Level.INFO, db.getName(), "Файл " + runFile.getName() + " успешно обработан.");
                reader.close();
                runFile.renameTo(new File(db.getFolder() + "\\Success\\" + runFile.getName()));
                runFile.delete();
            }
            catch (IOException ioe){
                Loggator.commonLog(Level.WARNING,"Ошибка чтения файла " + runFile.getAbsolutePath());
            }
            catch (RuntimeSqlException rsqle){
                throw new SQLException(rsqle);
            }

        }
        catch (SQLException sqle) {
            if (sqle.getMessage().indexOf("ORA-04021") == -1) {
                Loggator.execLog(Level.WARNING, db.getName(), "Ошибка выполнения файла! " + runFile.getName() + "\n\t" + sqle.getMessage().substring(sqle.getMessage().lastIndexOf(" Cause: ")));
                runFile.renameTo(new File(db.getFolder() + "\\Fail\\" + runFile.getName()));
                runFile.delete();
            } else {
                Loggator.execLog(Level.WARNING, db.getName(), "Файл " + runFile.getName() + " содержит объект, который сейчас заблокирован.\n\t" + sqle.getMessage().substring(sqle.getMessage().lastIndexOf(" Cause: ")));
            }
        }
        finally {
            processingDB.remove(db);
        }
    }*/
}
