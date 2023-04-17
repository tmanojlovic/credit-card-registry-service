package hr.bank.creditcardregistryservice.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.bank.creditcardregistryservice.model.Client;
import hr.bank.creditcardregistryservice.repository.ClientRepository;
import hr.bank.creditcardregistryservice.utils.CreditCardInfoFileUtils;
import hr.bank.creditcardregistryservice.utils.CustomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles({"test"})
public class CreditCardRegistryServiceApplicationTest {

    static {
        //Since temp directory depends on OS on which the test is run, it should be able to run anywhere
        System.setProperty("CREDIT_CARD_DATA_DIR", System.getProperty("java.io.tmpdir"));
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    @SpyBean
    private CreditCardInfoFileUtils creditCardInfoFileUtils;

    private final String regularPersonalIdentificationNumber = "69435151530";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void init() throws Exception {
        Client client = new Client(
                regularPersonalIdentificationNumber,
                "Test",
                "Testović",
                Client.CreditCardStatus.ACTIVE
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/clients")
                        .content(objectMapper.writeValueAsString(client))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()
                );
    }

    @Test
    public void testAddNewClient() {
        //Regular behaviour of application
        Client savedClient = clientRepository
                .findById(
                        CustomUtils.personalIdentificationNumberToId(regularPersonalIdentificationNumber)
                )
                .get();
        assertNotNull(savedClient);
        assertEquals("Test", savedClient.getFirstName());
        assertEquals("Testović", savedClient.getLastName());
    }

    @Test
    public void testNonExistingPersonalIdentificationNumber() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/clients/" + "80221040001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGettingExistingUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/clients/" + regularPersonalIdentificationNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        File file = new File(System.getProperty("CREDIT_CARD_DATA_DIR") + regularPersonalIdentificationNumber + ".csv");
        assertTrue(file.exists());

    }

    @Test
    public void testDeletingExistingUser() throws Exception {
        /* Here the corrupted file writer is simulated
         transaction should be rolled back and no insert should be made */
        mockMvc.perform(MockMvcRequestBuilders.get("/clients/" + regularPersonalIdentificationNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()
                );

        mockMvc.perform(MockMvcRequestBuilders.delete("/clients/" + regularPersonalIdentificationNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()
                );

        File file = new File(System.getProperty("CREDIT_CARD_DATA_DIR") + regularPersonalIdentificationNumber + ".csv");
        assertFalse(file.exists());

    }

    @Test
    public void testCorruptedFileSystem() throws Exception {
        /* Here the corrupted file writer is simulated
         transaction should be rolled back and no deletion should be made */
        doThrow(new IOException("Corrupted filesystem"))
                .when(creditCardInfoFileUtils).replaceCreditCardFile(any());

        mockMvc.perform(MockMvcRequestBuilders.delete("/clients/" + regularPersonalIdentificationNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        assertThat(clientRepository.existsById(CustomUtils.personalIdentificationNumberToId(regularPersonalIdentificationNumber)))
                .isEqualTo(true);

    }

    @Test
    public void testWrongPersonalIdentificationNumber() throws Exception {
        Client client = new Client(
                "80221030001",
                "Test",
                "Testović",
                Client.CreditCardStatus.ACTIVE
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/clients")
                        .content(objectMapper.writeValueAsString(client))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()
                );
    }


}