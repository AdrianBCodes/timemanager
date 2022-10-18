package com.adrianbcodes.timemanager.client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {
    Page<Client> getAllClientsByNameLikeAndNoteLike(String name, String note, Pageable pageable);
    List<Client> getAllClients();
    List<Client> getAllClientsDsl();
    Optional<Client> getClientById(Long id);
    Client saveClient(Client client);
    void deleteClient(Client client);
}
