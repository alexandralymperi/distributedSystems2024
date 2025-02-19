package gr.hua.dit.ds.ds2024Team77.entities;

import jakarta.persistence.*;

/*
The Role entity represents a user role in the system.
Roles are used to define user rights and permissions.
*/

@Entity
@Table(name = "roles")
public class Role {

    //Columns
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Ιd;

    @Column
    private String name;

    //Default constructor (required by JPA).
    public Role(){

    }
    //Constructor
    public Role(String name) {
        this.name = name;
    }


    //Setters & Getters
    public Integer getΙd() {
        return Ιd;
    }

    public void setΙd(Integer ιd) {
        Ιd = ιd;
    }

    public Integer getId() {
        return Ιd;
    }

    public void setId(Integer Ιd) {
        this.Ιd = Ιd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //toString method
    @Override
    public String toString() {
        return "role{" +
                "id=" + Ιd +
                ", name='" + name + '\'' +
                '}';
    }
}