package com.adrianbcodes.timemanager.client;

import com.adrianbcodes.timemanager.common.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SqlClientRepository extends ClientRepository, JpaRepository<Client, Long> {

    @Override
    default List<Client> getAllClients() {
        return this.findAll();
    }

    @Override
    default Optional<Client> getClientById(Long id) {
        return this.findById(id);
    }

    @Override
    default Client saveClient(Client client) {
        return this.save(client);
    }

    @Override
    default void deleteClient(Client client){
        client.setDeletedAt(new Date());
        this.save(client);
    }
}
