package gr.hua.dit.ds.ds2024Team77.repository;

import gr.hua.dit.ds.ds2024Team77.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(String roleName);

    default Role updateOrInsert(Role role){
        Role existingRole = findByName(role.getName()).orElse(null);

        if(existingRole != null ){
            return existingRole;
        }else{
            return save(role);
        }
    }

}
