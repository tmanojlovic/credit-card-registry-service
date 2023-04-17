package hr.bank.creditcardregistryservice.service;

import hr.bank.creditcardregistryservice.exception.ClientAlreadyExistsException;
import hr.bank.creditcardregistryservice.model.Client;
import hr.bank.creditcardregistryservice.repository.ClientRepository;
import hr.bank.creditcardregistryservice.utils.CreditCardInfoFileUtils;
import hr.bank.creditcardregistryservice.utils.CustomUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.NoSuchElementException;


@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final CreditCardInfoFileUtils creditCardInfoFileUtils;


    public ClientService(ClientRepository clientRepository, CreditCardInfoFileUtils creditCardInfoFileUtils) {
        this.clientRepository = clientRepository;
        this.creditCardInfoFileUtils = creditCardInfoFileUtils;
    }

    public Client getClientByIdentificationNumber(String identificationNumber) throws IOException {

        Client client =  clientRepository.findById(CustomUtils.personalIdentificationNumberToId(identificationNumber)).orElseThrow(
                () -> new NoSuchElementException("Client with personal identification number " +
                        identificationNumber +
                        " doesnt exists."
                )
        );
        creditCardInfoFileUtils.replaceCreditCardFile(identificationNumber);
        writeCreditCardInfoToFile(client);
        return client;
    }
    @Transactional(rollbackFor = {RuntimeException.class, IOException.class})
    public void deleteClient(String identificationNumber) throws IOException {
        clientRepository.deleteById(CustomUtils.personalIdentificationNumberToId(identificationNumber));
        creditCardInfoFileUtils.replaceCreditCardFile(identificationNumber);

    }

    public Client addNewClient(Client client) {
        client.setId(CustomUtils.personalIdentificationNumberToId(client.getPersonalIdentificationNumber()));
        return saveClientToDatabase(client);
    }

    private Client saveClientToDatabase(Client client) {
        if (clientRepository.existsById(client.getId())) {
            throw new ClientAlreadyExistsException(
                    "Client with the personal identification number " +
                            client.getPersonalIdentificationNumber().substring(0, 4) + "**** already exists");
        }
        return clientRepository.save(client);
    }

    private void writeCreditCardInfoToFile(Client client) throws IOException {
        creditCardInfoFileUtils.writeClientToFile(client);
    }

}
