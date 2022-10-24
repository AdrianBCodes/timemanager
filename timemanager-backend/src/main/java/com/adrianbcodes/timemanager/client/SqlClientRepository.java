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


    @Override
    default Page<Client> getAllClientsByNameLikeAndNoteLike(String name, String note, Pageable pageable){
        return this.findByNameContainsIgnoreCaseAndNoteContainsIgnoreCaseAndStatus(name, note, StatusEnum.ACTIVE ,pageable);
    }

    @Override
    default List<Client> getAllClients() {
        return this.findAll();
    }
    @Override
    default List<Client> getAllClientsDsl(){
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
