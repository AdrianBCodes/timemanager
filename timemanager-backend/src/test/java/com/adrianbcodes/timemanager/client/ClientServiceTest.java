package com.adrianbcodes.timemanager.client;

import com.adrianbcodes.timemanager.common.StatusEnum;
import com.adrianbcodes.timemanager.exceptions.AlreadyDeletedException;
import com.adrianbcodes.timemanager.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.assertThat;

class ClientServiceTest {

    private ClientService clientService;

    @BeforeEach
    void init() {
        clientService = new ClientService(new FakeClientRepository());
    }

    @Test
    void getAllClients() {
        //given
        Client client = ClientBuilder
                .builder()
                .withId(1L)
                .withName("Client1")
                .withNote("Note1")
                .buildWithId();
        Client client2 = ClientBuilder
                .builder()
                .withId(2L)
                .withName("Client2")
                .withNote("Note2")
                .buildWithId();
        clientService.saveClient(client);
        clientService.saveClient(client2);
        //when
        var result = clientService.getAllClients();
        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).isEqualTo(client);
        assertThat(result.get(1)).isEqualTo(client2);
    }

    @Test
    void getAllClients_noClientsInDatabase() {
        //given
        //when
        var result = clientService.getAllClients();
        //then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void getAllClients_onlyClientsWithStatusDeletedInDatabase_returnsEmptyList() {
        //given
        Client client = ClientBuilder
                .builder()
                .withId(1L)
                .withName("Client1")
                .withNote("Note1")
                .buildWithId();
        Client client2 = ClientBuilder
                .builder()
                .withId(2L)
                .withName("Client2")
                .withNote("Note2")
                .buildWithId();
        clientService.saveClient(client);
        clientService.saveClient(client2);
        clientService.deleteClientById(client.getId());
        clientService.deleteClientById(client2.getId());
        //when
        var result = clientService.getAllClients();
        //then
        assertThat(result.size()).isEqualTo(0);

    }

    @Test
    void getAllClients_ignoresClientsWithStatusDeleted() {
        //given
        Client client = ClientBuilder
                .builder()
                .withId(1L)
                .withName("Client1")
                .withNote("Note1")
                .buildWithId();
        Client client2 = ClientBuilder
                .builder()
                .withId(2L)
                .withName("Client2")
                .withNote("Note2")
                .buildWithId();
        Client client3 = ClientBuilder
                .builder()
                .withId(3L)
                .withName("Client3")
                .withNote("Note3")
                .buildWithId();
        clientService.saveClient(client);
        clientService.saveClient(client2);
        clientService.saveClient(client3);
        clientService.deleteClientById(client3.getId());
        //when
        var result = clientService.getAllClients();
        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).isEqualTo(client);
        assertThat(result.get(1)).isEqualTo(client2);
    }

    @Test
    void getClientById() {
        //given
        Client client = ClientBuilder
                .builder()
                .withId(1L)
                .withName("Client1")
                .withNote("Note1")
                .buildWithId();
        clientService.saveClient(client);
        //when
        var result = clientService.getClientById(client.getId());
        //then
        assertThat(result).isEqualTo(client);
    }

    @Test
    void getClientById_clientDoesNotExist_throwsNotFoundException() {
        //given
        //when
        var exception = catchThrowable(() -> clientService.getClientById(1L));
        //then
        assertThat(exception).isInstanceOf(NotFoundException.class);
    }

    @Test
    void saveClient() {
        //given
        Client client = ClientBuilder
                .builder()
                .withId(1L)
                .withName("Client1")
                .withNote("Note1")
                .buildWithId();
        //when
        clientService.saveClient(client);
        //then
        assertThat(clientService.getClientById(client.getId())).isEqualTo(client);
    }

    @Test
    void deleteClientById() {
        //given
        Client client = ClientBuilder
                .builder()
                .withId(1L)
                .withName("Client1")
                .withNote("Note1")
                .buildWithId();
        client.setStatus(StatusEnum.ACTIVE);
        clientService.saveClient(client);
        //when
        clientService.deleteClientById(client.getId());
        //then
        assertThat(clientService.getClientById(client.getId()).getStatus()).isEqualTo(StatusEnum.DELETED);
    }

    @Test
    void deleteClientById_clientHasStatusDeleted_throwsAlreadyDeletedException() {
        //given
        Client client = ClientBuilder
                .builder()
                .withId(1L)
                .withName("Client1")
                .withNote("Note1")
                .buildWithId();
        client.setStatus(StatusEnum.DELETED);
        clientService.saveClient(client);
        //when
        var exception = catchThrowable(() -> clientService.deleteClientById(client.getId()));
        //then
        assertThat(exception).isInstanceOf(AlreadyDeletedException.class);
    }
}