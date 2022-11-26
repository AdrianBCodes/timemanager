package com.adrianbcodes.timemanager.client;

import com.adrianbcodes.timemanager.common.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

public class FakeClientRepository implements ClientRepository{

    private final Map<Long, Client> clients = new HashMap<>();


    // TODO
    @Override
    public Page<Client> getAllClientsByNameLikeAndNoteLike(String name, String note, Pageable pageable) {
        return null;
    }

    @Override
    public List<Client> getAllClients() {
        return clients.values().stream().toList();
    }

    @Override
    public List<Client> getAllClientsByNameAndStatus(String name, StatusEnum status) {
        return clients.values().stream().filter(client -> client.getName().equals(name) && client.getStatus().equals(StatusEnum.ACTIVE)).toList();
    }

    @Override
    public Optional<Client> getClientById(Long id) {
        return Optional.ofNullable(clients.get(id));
    }

    @Override
    public Client saveClient(Client client) {
        // TODO Generate ID
        clients.put(client.getId(), client);
        return clients.get(client.getId());
    }

    @Override
    public void deleteClient(Client client) {
        client.setDeletedAt(new Date());
        clients.put(client.getId(), client);
    }
}
