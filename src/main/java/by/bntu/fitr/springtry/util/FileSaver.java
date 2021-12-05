package by.bntu.fitr.springtry.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileSaver {
    private static final Logger logger = LogManager.getLogger();
    private static final String FINAL_PATH = "/img/uploads/";
    private static final String SAVE_PATH = "./src/main/webapp/img/uploads/";


    public String saveFile(MultipartFile file) throws IOException {
        String currentDir = Paths.get(".").toAbsolutePath().normalize().toString();
        String name = file.getOriginalFilename();
        String suffix = name.substring(name.lastIndexOf("."));
        String randFilename = UUID.randomUUID() + suffix;
        File fileToSave = new File(currentDir + SAVE_PATH + randFilename);
        file.transferTo(fileToSave);
        return FINAL_PATH + randFilename;
    }
}
