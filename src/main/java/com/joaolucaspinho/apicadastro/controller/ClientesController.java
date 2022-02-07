package com.joaolucaspinho.apicadastro.controller;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import com.joaolucaspinho.apicadastro.model.Clientes;
import com.joaolucaspinho.apicadastro.repository.ClientesRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping({"/clientes"})

public class ClientesController {

    CPFValidator validator = new CPFValidator();
    Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");


    private ClientesRepository repository;

    ClientesController(ClientesRepository clientRepository){
        this.repository = clientRepository;
    }

    //Metodos:


    // Retorna todos os cadastros no banco de dados.
//    @GetMapping
//    public List findAll(){
//        return repository.findAll();
//    }

    // Adiciona um novo cadastro ao banco de dados com base nas informações inseridas no front-end.
    @PostMapping
    public ResponseEntity create(@RequestBody Clientes clientes){

        // Verifica se o CPF inserido já está cadastrado, caso não esteja, cadastra o CPF no banco de dados.
        try{
            // Verifica se todos os campos foram preenchidos
            if(clientes.getNome().isEmpty() || clientes.getEmail().isEmpty() || clientes.getCpf().isEmpty()) return ResponseEntity.badRequest().body("Todos os campos precisam ser preenchidos");

            // Verifica a validade do nome
            if(!clientes.getNome().matches("[A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ ]+$")) return ResponseEntity.badRequest().body("O campo 'nome' não aceita números.");

            // Verifica a validade do CPF
            try{
                validator.assertValid(clientes.getCpf());
            } catch (InvalidStateException e){
                return ResponseEntity.badRequest().body("CPF inválido");
            }

            // Verifica a validade do E-mail
            Matcher mat = pattern.matcher(clientes.getEmail());
            if (!mat.matches()) return ResponseEntity.badRequest().body("E-mail inválido.");

            repository.save(clientes);
            return ResponseEntity.ok(clientes);
        } catch (DataIntegrityViolationException e){
            return new ResponseEntity("CPF já cadastrado", HttpStatus.CONFLICT);
        }
    }

    // Altera o cadastro no banco de dados com base no ID;
    @PutMapping(value="/{id}")
    public ResponseEntity update(@PathVariable("id") long id,
                                           @RequestBody Clientes clientes){
        // Verifica se o CPF inserido já está cadastrado, caso não esteja, altera o CPF no banco de dados;
        try{

            // Verifica a validade do nome
            if(!clientes.getNome().matches("[A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ ]+$")) return ResponseEntity.badRequest().body("O campo 'nome' não aceita números.");

            // Verifica a validade do CPF
            try{
                validator.assertValid(clientes.getCpf());
            } catch (InvalidStateException e){
                return ResponseEntity.badRequest().body("CPF inválido");
            }

            // Verifica a validade do E-mail
            Matcher mat = pattern.matcher(clientes.getEmail());
            if (!mat.matches()) return ResponseEntity.badRequest().body("E-mail inválido.");

            return repository.findById(id)
                    .map(record -> {
                        record.setNome(clientes.getNome());
                        record.setEmail(clientes.getEmail());
                        record.setCpf(clientes.getCpf());
                        Clientes updated = repository.save(record);
                        return ResponseEntity.ok().body(updated);
                    }).orElse(ResponseEntity.notFound().build());
        } catch (DataIntegrityViolationException e){
            return ResponseEntity.unprocessableEntity().body("CPF já cadastrado.");
        }


    }

    //Deleta o cadastro do banco de dados com base no ID;
    @DeleteMapping(path ={"/{id}"})
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        return repository.findById(id)
                .map(record -> {
                    repository.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }

}
