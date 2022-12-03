package com.adrianbcodes.timemanager.client;

import com.adrianbcodes.timemanager.common.StatusEnum;
import com.adrianbcodes.timemanager.exceptions.AlreadyDeletedException;
import com.adrianbcodes.timemanager.exceptions.BlankParameterException;
import com.adrianbcodes.timemanager.exceptions.NotFoundException;
import com.adrianbcodes.timemanager.exceptions.NotUniqueException;
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
        Client client = ClientExample.getClient1();
        Client client2 = ClientExample.getClient2();
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
        Client client = ClientExample.getClient1();
        Client client2 = ClientExample.getClient2();
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
        Client client = ClientExample.getClient1();
        Client client2 = ClientExample.getClient2();
        clientService.saveClient(client);
        clientService.saveClient(client2);
        clientService.deleteClientById(client2.getId());
        //when
        var result = clientService.getAllClients();
        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(client);
    }
    @Test
    void getAllActiveClientsPagedAndFiltered() {
        //given
        Client client = ClientExample.getClient1();
        Client client2 = ClientExample.getClient2();
        clientService.saveClient(client);
        clientService.saveClient(client2);
        //when
        var result = clientService.getAllActiveClientsPagedAndFiltered(client.getName(), client.getNote(), 0, 5, "id,asc");
        //then
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0)).isEqualTo(client);
    }

    @Test
    void getClientById() {
        //given
        Client client = ClientExample.getClient1();
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
        Client client = ClientExample.getClient1();
        //when
        clientService.saveClient(client);
        //then
        assertThat(clientService.getClientById(client.getId())).isEqualTo(client);
    }

    @Test
    void saveClient_withEmptyName_throwsBlankParameterException() {
        //given
        Client client = ClientExample.getClient3WithEmptyName();
        //when
        var exception = catchThrowable(() -> clientService.saveClient(client));
        //then
        assertThat(exception).isInstanceOf(BlankParameterException.class);
    }

    @Test
    void saveClient_activeClientAlreadyHasSameName_throwsNotUniqueException() {
        //given
        Client client = ClientExample.getClient1();
        Client client2 = ClientExample.getClient1();
        clientService.saveClient(client);
        //when
        var exception = catchThrowable(() -> clientService.saveClient(client2));
        //then
        assertThat(exception).isInstanceOf(NotUniqueException.class);
    }

    @Test
    void deleteClientById() {
        //given
        Client client = ClientExample.getClient1();
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
        Client client = ClientExample.getClient1();
        client.setStatus(StatusEnum.DELETED);
        clientService.saveClient(client);
        //when
        var exception = catchThrowable(() -> clientService.deleteClientById(client.getId()));
        //then
        assertThat(exception).isInstanceOf(AlreadyDeletedException.class);
    }
}