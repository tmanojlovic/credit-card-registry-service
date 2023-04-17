package hr.bank.creditcardregistryservice.utils;

import hr.bank.creditcardregistryservice.model.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class CreditCardInfoFileUtils {

    private final String dirToWrite;

    public CreditCardInfoFileUtils(@Value("${app.credit-card-data-dir}") String dirToWrite){
        this.dirToWrite = dirToWrite;
    }

    public void writeClientToFile(Client client) throws IOException {
        String filename = getFilename(client);
        try (FileWriter writer = new FileWriter(dirToWrite + filename)) {
            writer.write(
                    client.getFirstName() + "," + client.getLastName() + "," + client.getPersonalIdentificationNumber() + "," + client.getStatus()
            );
        }
    }

    private String getFilename(Client client) {
        return client.getPersonalIdentificationNumber() + ".csv";
    }

    public void replaceCreditCardFile(String personalIdentificationNumber) throws IOException {
        
        File oldFile = new File(dirToWrite + personalIdentificationNumber + ".csv");
        File newFile = new File(oldFile.getParent(), personalIdentificationNumber + "-" + System.currentTimeMillis() +"-INACTIVE.csv");

        if(!oldFile.exists())
            return;

        boolean success = oldFile.renameTo(newFile);
        if (!success){
            throw new IOException("Failed to rename file");
        }
    }
    
}