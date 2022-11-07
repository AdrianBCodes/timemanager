package com.adrianbcodes.timemanager.client;

import com.adrianbcodes.timemanager.client.infrastucture.ClientWriteModel;
import com.adrianbcodes.timemanager.dto.ClientDTO;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/clients")
public class ClientController {
    ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }


    @GetMapping()
    ResponseEntity<Page<ClientDTO>> getAllClientsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String note,
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        List<Order> orders = new ArrayList<>();

        if (sort[0].contains(",")) {
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
            }
        } else {
            orders.add(new Order(getSortDirection(sort[1]), sort[0]));
        }
        Pageable pageable = PageRequest.of(page,size, Sort.by(orders));

        Page<ClientDTO> foundClients = clientService.getAllClientsPaged(name, note, pageable).map(Client::convertToClientDTO);
        return ResponseEntity.ok(foundClients);
    }

    @GetMapping("{id}")
    ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        ClientDTO foundClient = clientService.getClientById(id).convertToClientDTO();
        return ResponseEntity.ok(foundClient);
    }

    @PostMapping
    ResponseEntity<ClientDTO> addClient(@RequestBody ClientWriteModel clientWM) {
        Client toAdd = ClientBuilder.builder()
                .withName(clientWM.getName())
                .withNote(clientWM.getNote())
                .build();
        ClientDTO addedClient = clientService.saveClient(toAdd).convertToClientDTO();
        return ResponseEntity.ok(addedClient);
    }

    @PutMapping("{id}")
    ResponseEntity<ClientDTO> editClient(@PathVariable Long id, @RequestBody ClientWriteModel clientWM) {
        Client previous = clientService.getClientById(id);
        Client edited = ClientBuilder.builder()
                .withId(previous.getId())
                .withName(clientWM.getName())
                .withNote(clientWM.getNote())
                .buildWithId();
        ClientDTO editedClient = clientService.saveClient(edited).convertToClientDTO();
        return ResponseEntity.ok(editedClient);
    }

    @DeleteMapping("{id}")
    ResponseEntity<?> deleteClientById(@PathVariable Long id) {
        clientService.deleteClientById(id);
        return ResponseEntity.ok(id);
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }
}
