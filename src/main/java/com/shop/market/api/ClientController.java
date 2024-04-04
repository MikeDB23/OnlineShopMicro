package com.shop.market.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shop.market.dto.client.ClientDto;
import com.shop.market.dto.client.ClientToSaveDto;
import com.shop.market.exceptions.NotAbleToDeleteException;
import com.shop.market.exceptions.NotFoundException;
import com.shop.market.service.client.ClientService;

@RestController
@RequestMapping("/api/v1/customers")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }
    
    @GetMapping()
    public ResponseEntity<List<ClientDto>> getClients(){
        List<ClientDto> clientsDto = clientService.getAllClients();
        return ResponseEntity.ok().body(clientsDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable("id") Long id){
        try{
            ClientDto clientDto = clientService.findClientById(id);
            return ResponseEntity.ok().body(clientDto);
        }catch (NotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ClientDto> getClientByEmail(@PathVariable("email") String email){
        try{
            ClientDto clientDto = clientService.findByEmail(email);
            return ResponseEntity.ok().body(clientDto);
        }catch (NotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/city")
    public ResponseEntity<List<ClientDto>> getClientByAddress(@RequestParam String address){
        try{
            List<ClientDto> clientsDto = clientService.findByAddress(address);
            return ResponseEntity.ok().body(clientsDto);
        }catch (NotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping()
    public ResponseEntity createNewClient(@RequestBody ClientToSaveDto clientToSaveDto){
        try{
            ClientDto clientDto = clientService.saveClient(clientToSaveDto);
            return ResponseEntity.ok().body(clientDto);
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateClient(  @PathVariable("id") long id,
                                                    @RequestBody ClientToSaveDto clientToSaveDto){
        try{
            ClientDto clientDto = clientService.updateClient(id,clientToSaveDto);
            return ResponseEntity.ok().body(clientDto);
        }catch (NotFoundException e){
            return ResponseEntity.notFound().build();
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable("id") Long id){
        try{
            clientService.deleteClient(id);
            return ResponseEntity.ok().build();
        }catch (NotAbleToDeleteException e){
            return ResponseEntity.notFound().build();
        }
    }
}