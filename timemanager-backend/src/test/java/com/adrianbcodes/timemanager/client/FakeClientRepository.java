package com.adrianbcodes.timemanager.client;

import java.util.*;

public class FakeClientRepository implements ClientRepository{

    private final Map<Long, Client> clients = new HashMap<>();
    @Override
    public List<Client> getAllClients() {
        return clients.values().stream().toList();
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
