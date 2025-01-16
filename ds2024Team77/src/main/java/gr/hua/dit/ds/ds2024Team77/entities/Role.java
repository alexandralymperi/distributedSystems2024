package gr.hua.dit.ds.ds2024Team77.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    //Columns
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Ιd;

    @Column
    private String name;

    //Constructor
    public Role(){

    }

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