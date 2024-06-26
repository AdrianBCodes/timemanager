package com.adrianbcodes.timemanager.client;

import com.adrianbcodes.timemanager.client.infrastucture.ClientWriteModel;
import com.adrianbcodes.timemanager.dto.ClientDTO;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/clients")
public class ClientController {
    ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }


    @GetMapping()
    ResponseEntity<Page<ClientDTO>> getAllActiveClientsPagedAndFiltered(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String note,
            @RequestParam(defaultValue = "id,asc") String sort
    ) {
        Page<ClientDTO> foundClients = clientService.getAllActiveClientsPagedAndFiltered(name, note, page, size, sort).map(Client::convertToClientDTO);
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


}
