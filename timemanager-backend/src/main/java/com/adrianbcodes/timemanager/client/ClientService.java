package com.adrianbcodes.timemanager.client;

import com.adrianbcodes.timemanager.common.StatusEnum;
import com.adrianbcodes.timemanager.exceptions.AlreadyDeletedException;
import com.adrianbcodes.timemanager.exceptions.NotFoundException;

import java.util.List;

public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    List<Client> getAllClients(){
        return clientRepository.getAllClients().stream().filter(client -> client.getStatus().equals(StatusEnum.ACTIVE)).toList();
    }
    public Client getClientById(Long id){
        return clientRepository.getClientById(id).orElseThrow(() -> new NotFoundException("Client with id: " + id + " not found"));
    }
    Client saveClient(Client client){
        return clientRepository.saveClient(client);
    }

    void deleteClientById(Long id){
        Client toDelete = getClientById(id);
        if(toDelete.getStatus().equals(StatusEnum.DELETED)){
            throw new AlreadyDeletedException("Client with id: " + id + " already has status DELETED");
        }
        clientRepository.deleteClient(toDelete);
    }
}
