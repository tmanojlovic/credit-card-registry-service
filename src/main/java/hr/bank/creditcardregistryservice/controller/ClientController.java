package hr.bank.creditcardregistryservice.controller;

import hr.bank.creditcardregistryservice.model.Client;
import hr.bank.creditcardregistryservice.service.ClientService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    private ClientController(ClientService clientService){
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<Client> addNewClient(@Validated @RequestBody Client client) {
            return new ResponseEntity<>(clientService.addNewClient(client), HttpStatusCode.valueOf(201));
    }

    @GetMapping("/{personalIdentificationNumber}")
    public ResponseEntity<Client> getClientByIdentificationNumber(
            @PathVariable String personalIdentificationNumber
    ) throws IOException {
        return new ResponseEntity<>(
                clientService.getClientByIdentificationNumber(personalIdentificationNumber),
                HttpStatusCode.valueOf(200)
        );
    }

    @DeleteMapping("/{personalIdentificationNumber}")
    public ResponseEntity<Void> deleteClient(@PathVariable String personalIdentificationNumber
    ) throws IOException {
        clientService.deleteClient(personalIdentificationNumber);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

}
