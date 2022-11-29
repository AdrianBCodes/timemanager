package com.adrianbcodes.timemanager.client;

import com.adrianbcodes.timemanager.common.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SqlClientRepository extends ClientRepository, JpaRepository<Client, Long> {
    @Query("""
            select c from Client c
            where upper(c.name) like upper(concat('%', ?1, '%')) and upper(c.note) like upper(concat('%', ?2, '%')) and c.status = ?3""")
    Page<Client> findByNameContainsIgnoreCaseAndNoteContainsIgnoreCaseAndStatus(String name, String note, StatusEnum status, Pageable pageable);
    List<Client> findByNameAndStatus(String name, StatusEnum status);


    @Override
    default Page<Client> getAllClientsByNameLikeAndNoteLikeAndStatus(String name, String note, StatusEnum status, Pageable pageable){
        return this.findByNameContainsIgnoreCaseAndNoteContainsIgnoreCaseAndStatus(name, note, status ,pageable);
    }

    @Override
    default List<Client> getAllClients() {
        return this.findAll();
    }

    @Override
    default List<Client> getAllClientsByNameAndStatus(String name, StatusEnum status){
        return this.findByNameAndStatus(name, status);
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
