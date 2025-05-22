package br.com.fiap.qmove.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Funcionario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "O campo 'nome' é obrigatório")
    private String nome;
        
    @NotBlank(message = "O campo 'email' é obrigatório")
    private String email;
    
    @NotBlank(message = "O campo 'senha' é obrigatório")
    @Size(min = 8, max = 10, message = "Deve ter entre 8 e 10 caracteres.")
    private String senha;
}
