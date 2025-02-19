package gr.hua.dit.ds.ds2024Team77.repository;

import gr.hua.dit.ds.ds2024Team77.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//The RoleRepository is an interface that provides methods for accessing and managing roles in the database.
//It inherits from JpaRepository to take advantage of out-of-the-box CRUD functions.
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    //updates or adds a new role. If the role already exists, the existing one is returned.
    //If not present, the new role is saved and returned. @param role The role to insert or update.
    //@return The existing or new role.
    Optional<Role> findByName(String roleName);

    default Role updateOrInsert(Role role){
        //Checks if the role already exists in the database
        Role existingRole = findByName(role.getName()).orElse(null);

        //If present, the existing role is returned
        if(existingRole != null ){
            return existingRole;
        }else{
            return save(role);
        }
    }

}
