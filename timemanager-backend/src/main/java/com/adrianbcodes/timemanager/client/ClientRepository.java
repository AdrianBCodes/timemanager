package com.adrianbcodes.timemanager.client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {
    List<Client> getAllClients();
    Optional<Client> getClientById(Long id);
    Client saveClient(Client client);
    void deleteClient(Client client);
}
