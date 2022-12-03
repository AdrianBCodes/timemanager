package com.adrianbcodes.timemanager.client;

import com.adrianbcodes.timemanager.common.SortMapper;
import com.adrianbcodes.timemanager.common.StatusEnum;
import com.adrianbcodes.timemanager.exceptions.AlreadyDeletedException;
import com.adrianbcodes.timemanager.exceptions.BlankParameterException;
import com.adrianbcodes.timemanager.exceptions.NotFoundException;
import com.adrianbcodes.timemanager.exceptions.NotUniqueException;
import org.springframework.data.domain.*;


import java.util.ArrayList;
import java.util.List;

public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    List<Client> getAllClients(){
        return clientRepository.getAllClients().stream().filter(client -> client.getStatus().equals(StatusEnum.ACTIVE)).toList();
    }

    Page<Client> getAllActiveClientsPagedAndFiltered(String name, String note, int page, int size, String sort) {
        List<Sort.Order> orders = new ArrayList<>();

        String[] _sort = sort.split(",");
        orders.add(new Sort.Order(SortMapper.getSortDirection(_sort[1]), _sort[0]));

        Pageable pageable = PageRequest.of(page,size, Sort.by(orders));
        return clientRepository.getAllClientsByNameLikeAndNoteLikeAndStatus(name, note, StatusEnum.ACTIVE, pageable);
    }
    public Client getClientById(Long id){
        return clientRepository.getClientById(id).orElseThrow(() -> new NotFoundException("Client with id: " + id + " not found"));
    }
    public Client saveClient(Client client){
        if(client.getName().isBlank()){
            throw new BlankParameterException("Client's name cannot be empty");
        }
        if(!isClientNameUnique(client.getName(), StatusEnum.ACTIVE)){
            throw new NotUniqueException("Client with name: " + client.getName() + " already exists");
        }
        return clientRepository.saveClient(client);
    }

    void deleteClientById(Long id){
        Client toDelete = getClientById(id);
        if(toDelete.getStatus().equals(StatusEnum.DELETED)){
            throw new AlreadyDeletedException("Client with id: " + id + " already has status DELETED");
        }
        clientRepository.deleteClient(toDelete);
    }

    private boolean isClientNameUnique(String name, StatusEnum status){
        List<Client> clients = clientRepository.getAllClientsByNameAndStatus(name, status);
        return clients.isEmpty();
    }
}
