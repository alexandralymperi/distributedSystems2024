package gr.hua.dit.ds.ds2024Team77.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import gr.hua.dit.ds.ds2024Team77.entities.Messages;
import org.springframework.stereotype.Repository;

//Repository interface for accessing the data of the messages (Messages).
//It uses Spring Data JPA and extends JpaRepository, allowing basic CRUD (Create, Read, Update, Delete) operations on the database.
@Repository
public interface MessagesRepository extends JpaRepository<Messages, Long> {

}
