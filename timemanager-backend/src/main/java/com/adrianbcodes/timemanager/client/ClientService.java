package com.adrianbcodes.timemanager.client;

import com.adrianbcodes.timemanager.common.StatusEnum;
import com.adrianbcodes.timemanager.exceptions.AlreadyDeletedException;
import com.adrianbcodes.timemanager.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


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

    Page<Client> getAllClientsPaged(String name, String note, Pageable pageable) {
        return clientRepository.getAllClientsByNameLikeAndNoteLike(name, note, pageable);
    }
    public Client getClientById(Long id){
        return clientRepository.getClientById(id).orElseThrow(() -> new NotFoundException("Client with id: " + id + " not found"));
    }
    public Client saveClient(Client client){
        return clientRepository.saveClient(client);
    }

    void deleteClientById(Long id){
        Client toDelete = getClientById(id);
        if(toDelete.getStatus().equals(StatusEnum.DELETED)){
            throw new AlreadyDeletedException("Client with id: " + id + " already has status DELETED");
        }
        clientRepository.deleteClient(toDelete);
    }

    private List<Sort.Order> createSortOrder(List<String> sortList, String sortDirection) {
        List<Sort.Order> sorts = new ArrayList<>();
        Sort.Direction direction;
        for (String sort : sortList) {
            if (sortDirection != null) {
                direction = Sort.Direction.fromString(sortDirection);
            } else {
                direction = Sort.Direction.DESC;
            }
            sorts.add(new Sort.Order(direction, sort));
        }
        return sorts;
    }
}
